package com.messcode.server.networking;

import JDBC.ExportData;
import JDBC.ImportData;
import com.messcode.transferobjects.ClassName;
import com.messcode.transferobjects.Container;
import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.UserList;
import com.messcode.transferobjects.messages.GroupMessages;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;

public class ServerSocketHandler implements Runnable {

    private ConnectionPool pool;
    private Socket socket;
    private ImportData dbi;
    private ExportData dbe;

    private ObjectOutputStream outToClient;
    private ObjectInputStream inFromClient;

    private User user;

    public ServerSocketHandler(Socket socket, ConnectionPool pool, ImportData dbii, ExportData dbee) throws IOException {
        dbe = dbee;
        dbi = dbii;
        this.socket = socket;
        this.pool = pool;
        inFromClient = new ObjectInputStream(socket.getInputStream());
        outToClient = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                Container packet = (Container) (inFromClient.readObject());
                System.out.println("NEW PACKET : " + packet.getClassName() + " object " + packet.getObject());
                switch (packet.getClassName()) {
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
                        user = (User) ((ArrayList<Object>) packetToClient.getObject()).get(2);

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
                        outToClient.writeObject(dbi.createAccount(u));
                        // maybe add offline user so everyone gets it updated, but not that important
                        break;
                    }
                    case PASSWORD_CHANGE: {
                        User u = (User) packet.getObject();
                       boolean answ= dbi.changePassword(u);
                       Container pckt = new Container(answ,ClassName.PASSWORD_CHANGE);
                    outToClient.writeObject(pckt);
                        break;
                    }
                    case REMOVE_USER: {
                        User u = (User) packet.getObject();
                        u.setSalt("- deleted");
                      boolean result= dbi.deleteUser(u);
                     if(result)
                     {
                         Container pckt = new Container(u,ClassName.REMOVE_USER);

                     }
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateUsersList() {
        try {
            UserList users = new UserList();
            users.addList(pool.getUsers());
            System.out.println("server, user size: " + users.getSize());
            Container packet = new Container(users, ClassName.USER_LIST);
            outToClient.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(PublicMessage publicMessage) {
        try {
            System.out.println(
                    "[SERVER] " + "user: " + publicMessage.getUsername() + " sent: "
                            + publicMessage.getMsg());
            Container packet = new Container(publicMessage, ClassName.PUBLIC_MESSAGE);
            outToClient.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getUser() {
        return user;
    }

    public void joinChat(User user) {
        try {
            Container packet = new Container(user, ClassName.USER_JOIN);
            System.out.println("Telling one user that new one added: " + user.getName() + " email: " + user.getEmail());
            outToClient.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void sendMessageInPM(PrivateMessage pm) {
        try {
            System.out.println("HELLO THIS SHOULD BE BLALGLA");
            Container packet = new Container(pm, ClassName.PRIVATE_MESSAGE);
            outToClient.writeObject(packet);
            System.out.println("server has sent the stuff " + pm.getMsg());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void userLeft(User user) {

        Container packet = new Container(user, ClassName.USER_LEFT);
        try {
            outToClient.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void sendGroups(Container updateGroups) {
        try {
            outToClient.writeObject(updateGroups);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendGroupMessage(GroupMessages message) {
        try {
            System.out.println(
                    "[SERVER] " + "user: " + message.getUsername() + "in group : " + message.getGroup().getName() + " sent: "
                            + message.getMsg());
            Container packet = new Container(message, ClassName.GROUP_MESSAGE);
            outToClient.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

