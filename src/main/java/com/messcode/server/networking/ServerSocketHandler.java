package com.messcode.server.networking;

import JDBC.ExportData;
import JDBC.ImportData;
import com.messcode.transferobjects.*;
import com.messcode.transferobjects.messages.GroupMessages;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *This class is responsible for listening and reacting to packets that come from the client
 *
 */
public class ServerSocketHandler implements Runnable {

    private static final Logger log4j = LogManager.getLogger(ServerSocketHandler.class);
    private ConnectionPool pool;
    private Socket socket;
    private ImportData dbi;
    private ExportData dbe;

    private ObjectOutputStream outToClient;
    private ObjectInputStream inFromClient;

    private User user;

    /**
     * Constructor where the Socket, ConnectionPool, ImportData,ExportData, ObjectOutputStream,ObjectInputStream are initialized
     * It also contains the user.
     *
     * @param socket Socket object for connection
     * @param pool ConnectionPool object for keeping track of all clients
     * @param dbii ImportData object for JDBC
     * @param dbee ExportDate object for JDBC
     * @throws IOException an exception that provides information on errors.
     */
    public ServerSocketHandler(Socket socket, ConnectionPool pool, ImportData dbii, ExportData dbee) throws IOException {
        dbe = dbee;
        dbi = dbii;
        this.socket = socket;
        this.pool = pool;
        inFromClient = new ObjectInputStream(socket.getInputStream());
        outToClient = new ObjectOutputStream(socket.getOutputStream());

    }

    /**
     *This run method is responsible for listening to the packets from clients.
     */
    @Override
    public void run() {
        try {
            while (true) {
                Container packet = (Container) (inFromClient.readObject());
                System.out.println("NEW PACKET : " + packet.getClassName() + " object " + packet.getObject());
                switch (packet.getClassName()) {
                    case CHANGE_GROUP_LEADER:{
                     Group g = (Group) packet.getObject();
                     dbi.updateLeader(g);
                      pool.updateGroup(dbe);
                    break;
                    }
                    case DELETE_GROUP:{
                    Group g = (Group) packet.getObject();
                    dbi.removeGroupMembers(g);
                    g.setLeader(null);
                    dbi.updateLeader(g);
                    pool.updateGroup(dbe);

                    break;
                    }
                    case REMOVE_GROUPMEMBER:{
                          Group g = (Group) packet.getObject();
                        dbi.removeGroupMembers(g);
                        pool.updateGroup(dbe);
                            
                        break;
                    }
                    case ADD_GROUPMEMBER: {
                        Group g = (Group) packet.getObject();
                        dbi.addGroupMembers(g);
                        pool.updateGroup(dbe);
                        pool.sendAllGroupMessages(dbe.getGroupMessages(g));
                        break;
                    }
                    case CREATING_GROUP: {
                        System.out.println("com.messcode.server.networking.ServerSocketHandler.run()");
                        Group g = (Group) packet.getObject();
                        dbi.createGroup(g);
                        pool.updateGroup(dbe);
                        break;
                    }
                    case PRIVATE_MESSAGE: {
                        PrivateMessage pm = (PrivateMessage) packet.getObject();
                        System.out.println("calling the method on the PM !!!");
                        pool.sendMessageInPM(pm);
                        dbi.saveMessage(pm);
                        break;
                    }
                    case USER_JOIN: {

                        User usertemp = (User) packet.getObject();

                        boolean isItSame = pool.userCheck(usertemp);
                        if (isItSame) break;
                        Container packetToClient = null;


                        packetToClient = dbe.checkLogin(usertemp.getEmail(), usertemp.getStrPassword()); /// here the username, should be email, and email should be passowrd


                        if (packetToClient.getObject() != null) {
                            packetToClient = dbe.acceptLogin(usertemp.getEmail(), (String) packetToClient.getObject());

                        } else {
                            packetToClient = new Container(false, ClassName.LOGIN_RESPONSE);
                            outToClient.writeObject(packetToClient);
                            break;
                        }
                        pool.addHandler(this);
                        user = (User) ((ArrayList<Object>) packetToClient.getObject()).get(1);

                        pool.userJoin(user);
                        outToClient.writeObject(packetToClient);
                        updateUsersList();

                        break;
                    }
                    case PUBLIC_MESSAGE: {
                        PublicMessage message = (PublicMessage) packet.getObject();
                        pool.broadcastMessage(message);
                        dbi.saveMessage(message);
                        break;
                    }
                    case GROUP_MESSAGE: {
                        GroupMessages message = (GroupMessages) packet.getObject();
                        pool.sendGroupMessages(message);
                        dbi.saveMessage(message);
                        break;
                    }
                    case USER_LEFT: {
                        User use = (User) packet.getObject();
                        dbi.saveDataOnExit(use);
                        pool.removeHandler(this);
                        break;
                    }
                    case CREATE_ACCOUNT: {
                        User u = (User) packet.getObject();
                        Container c = dbi.createAccount(u);
                        if((User)c.getObject()!=null) {
                            pool.addNewOfflineUser(u,user);

                        }
                        outToClient.writeObject(c);
                        break;
                    }
                    case PASSWORD_CHANGE: {
                        User u = (User) packet.getObject();
                        boolean answ = dbi.changePassword(u);
                        Container pckt = new Container(answ, ClassName.PASSWORD_CHANGE);
                        outToClient.writeObject(pckt);
                        break;
                    }
                    case REMOVE_USER: {
                        User u = (User) packet.getObject();
                        u.setSalt(" - deleted");
                        boolean result = dbi.deleteUser(u);
                        if (result) {
                            Container pckt = new Container(u, ClassName.REMOVE_USER);

                            outToClient.writeObject(pckt);
                            pool.kickUser(u);
                        }
                        break;
                    }
                    case RESET_PASSWORD: {
                        User u = (User) packet.getObject();

                         dbi.resetPassword(u);

                        break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            try {
                pool.removeHandler(this);
                socket.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                log4j.error(ex.getMessage(), ex);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log4j.error(e.getMessage(), e);
        }
    }

    /**
     *This function updates the user list  for the client
     *
     */
    public void updateUsersList() {
        try {
            UserList users = new UserList();
            users.addList(pool.getUsers());
            System.out.println("server, user size: " + users.getSize());
            Container packet = new Container(users, ClassName.USER_LIST);
            outToClient.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
            log4j.error(e.getMessage(), e);
        }
    }

    /**
     * This method is responsible for sending a public message to the clients
     *
     * @param publicMessage PublicMessage object containing a message for clients
     */
    public void sendMessage(PublicMessage publicMessage) {
        try {
            System.out.println(
                    "[SERVER] " + "user: " + publicMessage.getUsername() + " sent: "
                            + publicMessage.getMsg());
            Container packet = new Container(publicMessage, ClassName.PUBLIC_MESSAGE);
            outToClient.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
            log4j.error(e.getMessage(), e);
        }
    }

    /**
     * Returns the current user of this instance.
     *
     * @return User User object
     */
    public User getUser() {
        return user;
    }

    /**
     * This method is called when a client joins the chat system to update the userlist for other users.
     *
     * @param user User object
     */
    public void joinChat(User user) {
        try {
            Container packet = new Container(user, ClassName.USER_JOIN);
            System.out.println("Telling one user that new one added: " + user.getName() + " email: " + user.getEmail());
            outToClient.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
            log4j.error(e.getMessage(), e);
        }
    }

    /**
     * This method is responsible for sending private messages to both parties
     *
     * @param pm PrivateMessage object
     */
    public void sendMessageInPM(PrivateMessage pm) {
        try {
            System.out.println("HELLO THIS SHOULD BE BLALGLA");
            Container packet = new Container(pm, ClassName.PRIVATE_MESSAGE);
            outToClient.writeObject(packet);
            System.out.println("server has sent the stuff " + pm.getMsg());
        } catch (IOException e) {
            e.printStackTrace();
            log4j.error(e.getMessage(), e);
        }
    }

    /**
     * This method notifies the clients that a user left the chat system.
     *
     * @param user User object
     */
    public void userLeft(User user) {
        Container packet = new Container(user, ClassName.USER_LEFT);
        try {
            outToClient.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
            log4j.error(e.getMessage(), e);
        }
    }

    /**
     * This method sends all the groups to the clients.
     *
     * @param updateGroups Group object
     */
    void sendGroups(Container updateGroups) {
        try {
            outToClient.writeObject(updateGroups);
        } catch (IOException e) {
            e.printStackTrace();
            log4j.error(e.getMessage(), e);
        }
    }

    /**
     * This method sends the group message to all the clients connected to that group
     *
     * @param message Group object
     */
    public void sendGroupMessage(GroupMessages message) {
        try {
            System.out.println(
                    "[SERVER] " + "user: " + message.getUsername() + "in group : " + message.getGroup().getName() + " sent: "
                            + message.getMsg());
            Container packet = new Container(message, ClassName.GROUP_MESSAGE);
            outToClient.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
            log4j.error(e.getMessage(), e);
        }
    }

    /**
     *This method is responsible for sending a ban notification to client, and other clients.
     *
     * @param u User object
     */
    public void removeUser(User u) {
        try {
            Container b = new Container(u, ClassName.KICK_USER);
            outToClient.writeObject(b);
        } catch (IOException e) {
            e.printStackTrace();
            log4j.error(e.getMessage(), e);
        }

    }

    /**
     * This method is responsible for sending all the group messages to a client
     *
     * @param groupMessages ArrayList<PublicMessage></PublicMessage> Object
     */
    public void sendAllGroupMessage(ArrayList<PublicMessage> groupMessages) {
        try {
            Container b = new Container(groupMessages, ClassName.ALL_GROUP_MESSAGES);
            outToClient.writeObject(b);
        } catch (IOException e) {
            e.printStackTrace();
            log4j.error(e.getMessage(), e);
        }
    }

    /**
     * This method is responsible for sending a new user to the offline list for clients
     *
     * @param u User object
     */
    public void updateOfflineList(User u) {
        try {

            Container packet = new Container(u, ClassName.OFFLINE_USER);
            outToClient.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
            log4j.error(e.getMessage(), e);
        }
    }
}

