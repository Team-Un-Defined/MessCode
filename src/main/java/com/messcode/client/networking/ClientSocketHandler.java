package com.messcode.client.networking;

import com.messcode.client.Start;
import com.messcode.transferobjects.ClassName;
import com.messcode.transferobjects.Container;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.UserList;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import static com.messcode.transferobjects.ClassName.PRIVATE_MESSAGE;

import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.messages.GroupMessages;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 *This class is responsible for sending  and listening to packets from the server
 *
 */
public class ClientSocketHandler implements Runnable {

    private SocketClient socketClient;
    private Socket socket;
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;

    /**
     * This constructor initialized the Socket, SocketClient and ObjectInput/OutputStreams
     *
     * @param socket Socket object
     * @param socketClient SocketClient object
     * @throws IOException connection errors
     */
    public ClientSocketHandler(Socket socket, SocketClient socketClient) throws IOException {
        this.socket = socket;
        this.socketClient = socketClient;
        outToServer = new ObjectOutputStream(socket.getOutputStream());
        inFromServer = new ObjectInputStream(socket.getInputStream());
    }

    /**
     *This method is where the client is listening to the packets coming from the server
     */
    @Override
    public void run() {
        try {
            while (true) {
                Container packet = (Container) inFromServer.readObject();
                java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.FINE,
                        "Im the client and i got the PM finally pls wtf" + packet.getClassName());
                System.out.println("Im the client and i got the PM finally pls wtf" + packet.getClassName());
                switch (packet.getClassName()) {
                    case GROUP_UPDATE: {
                        ArrayList<Group> groups = (ArrayList<Group>) packet.getObject();
                        receiveGroups(groups);
                        break;
                    }
                    case CREATE_ACCOUNT: {

                        userCreateResponse(packet);
                        break;
                    }
                    case PRIVATE_MESSAGE: {
                        PrivateMessage pm = (PrivateMessage) packet.getObject();
                        receivePM(pm);

                        break;
                    }
                    case GROUP_MESSAGE: {
                        GroupMessages gm = (GroupMessages) packet.getObject();
                        receiveGroup(gm);

                        break;
                    }
                    case PUBLIC_MESSAGE: {
                        PublicMessage pub = (PublicMessage) packet.getObject();
                        receivePublic(pub);

                        break;
                    }
                    case USER_JOIN: {
                        User us = (User) packet.getObject();
                        addToUsersList(us);
                        break;
                    }
                    case USER_LIST: {
                        UserList users = (UserList) packet.getObject();
                        java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.FINE,
                                "got this message from server: " + users.getSize()
                                        + " user: " + users.get(0).getEmail());
                        System.out.println("got this message from server: " + users.getSize()
                                + " user: " + users.get(0).getEmail());
                        for (int i = 0; i < users.getSize(); i++) {
                            addToUsersList(users.get(i));
                        }
                        break;
                    }
                    case USER_LEFT: {
                        User user = (User) packet.getObject();
                        userLeft(user);
                        break;
                    }
                    case LOGIN_RESPONSE: {
                        boolean answ = (boolean) packet.getObject();
                        java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.FINE,
                                "in client: " + answ);
                        System.out.println("in client: " + answ);
                        loginResponse(answ);
                        break;
                    }
                    case LOGIN_DATA: {
                        java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.FINE,
                                "i got the data " + packet);
                        System.out.println("i got the data " + packet);

                        loginData(packet);
                        break;
                    }
                    case PASSWORD_CHANGE: {
                        java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.FINE,
                                "i got the  pass change data " + packet);
                        System.out.println("i got the  pass change data " + packet);
                        passChangeResponse(packet);
                        break;
                    }
                    case REMOVE_USER: {
                        java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.FINE,
                                "i got the  remove user data " + packet);
                        System.out.println("i got the  remove user data " + packet);
                        removedUser(packet);
                        break;
                    }
                    case KICK_USER: {
                        java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.FINE,
                                "i got banned " + packet);
                        System.out.println("user got banned " + packet);
                        userKick(packet);
                        break;
                    }
                    case ALL_GROUP_MESSAGES: {
                        java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.FINE,
                                "i got ALL THE group messages " + packet);
                        getALlGroupMessages(packet);

                        break;
                    }
                    case OFFLINE_USER: {
                        java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.FINE,
                                "i A NEW OFFLINE USER " + packet);
                        addNewOfflineUser(packet);

                        break;
                    }

                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is responsible for calling the kickUser method on the socketClient
     * @param packet Container object
     *
     */
    private void userKick(Container packet) {
        socketClient.kickUser(packet);
    }

    /**
     *  This method is responsible for calling the addNewOfflineUser method on the socketClient
     * @param packet Container packet
     */
    private void addNewOfflineUser(Container packet) {
        socketClient.addOfflineUser(packet);
    }

    /**
     *  This method is responsible for calling the getAllGroupMessages method on the socketClient
     * @param pckt Cotainer object
     */
    private void getALlGroupMessages(Container pckt) {
        socketClient.getAllGroupMessages(pckt);
    }

    /**
     *  This method is responsible for calling the removedUser method on the socketClient
     * @param packet Container object
     */
    private void removedUser(Container packet) {
        socketClient.removeUser(packet);
    }

    /**
     *  This method is responsible for calling the passChangeResponse method on the socketClient
     *
     * @param packet Container packet
     */
    private void passChangeResponse(Container packet) {
        socketClient.passChangeResponse(packet);
    }

    /**
     *  This method is responsible for calling the userCreateResponse method on the socketClient
     * @param cont Container object
     */
    private void userCreateResponse(Container cont) {
        socketClient.userCreateResponse(cont);
    }

    /**
     *
     *  This method is responsible for calling the loginDate method on the socketClient
     * @param packet Container packet
     */
    private void loginData(Container packet) {
        socketClient.loginData(packet);
    }

    /**
     *  This method is responsible for calling the userLeft method on the socketClient
     * @param arg Object
     */
    private void userLeft(Object arg) {
        User user = (User) arg;
        socketClient.removeFromList(user);
    }

    /**
     *  This method is responsible for calling the loginResponse method on the socketClient
     *
     * @param answ boolean object
     */

    private void loginResponse(boolean answ) {
        socketClient.loginResponse(answ);
    }

    /**
     *  This method is responsible for calling the addToUsersList method on the socketClient
     * @param user User object
     */
    private void addToUsersList(User user) {
        socketClient.addToList(user);
    }

    /**
     *  This method is responsible for calling the displayMessage method on the socketClient
     * @param message PublicMessage object
     */
    private void receivePublic(PublicMessage message) {
        socketClient.displayMessage(message);
        java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.FINE,
                "I GOT THIS: " + message.getUsername() + " " + message.getMsg());
        System.out.println("I GOT THIS: " + message.getUsername() + " " + message.getMsg());
    }

    /**
     *  This method is responsible for calling the recievePM method on the socketClient
     * @param message PrivateMessage object
     */
    private void receivePM(PrivateMessage message) {
        socketClient.displayPM(message);
        java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.FINE,
                "CLIENT GOT THE PM : " + message.getUsername() + " " + message.getMsg());
        System.out.println("CLIENT GOT THE PM : " + message.getUsername() + " " + message.getMsg());
    }

    /**
     *  This method is responsible for calling the receiveGroup method on the socketClient
     * @param gm GroupMessages object
     */
    private void receiveGroup(GroupMessages gm) {
        socketClient.displayGroup(gm);
        java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.FINE,
                "CLIENT GOT THE Group message : " + gm.getUsername() + " " + gm.getMsg());
        System.out.println("CLIENT GOT THE Group message : " + gm.getUsername() + " " + gm.getMsg());
    }

    /**
     *  This method is responsible for sending a packet to the server
     * @param message PrivateMessage object
     */
    public void sendPM(PrivateMessage message) {
        try {
            Container packet = new Container(message, PRIVATE_MESSAGE);
            outToServer.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is responsible for sending a packet to the server
     * @param message PublicMessage object
     */
    //  TO SERVER
    public void sendPublic(PublicMessage message) {
        try {
            Container packet = new Container(message, ClassName.PUBLIC_MESSAGE);
            outToServer.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is responsible for sending a packet to the server
     * @param username User object
     */
    public void addUser(User username) {
        try {
            java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.FINE, "wgatdup: " + username);
            System.out.println("wgatdup: " + username);
            Container packet = new Container(username, ClassName.USER_JOIN);

            java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.FINE,
                    "WTF IS GOING ON: : " + username.getEmail() + " pwd " + username.getStrPassword());
            System.out.println("WTF IS GOING ON: : " + username.getEmail() + " pwd " + username.getStrPassword());
            java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.FINE,
                    "FASZOMAT A KURVA JAVAÁBA :? " + packet.getObject());
            System.out.println("FASZOMAT A KURVA JAVAÁBA :? " + packet.getObject());
            java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.FINE,
                    "FASZOMAT A KURVA JAVAÁBA user  :? " + packet.getObject());
            System.out.println("FASZOMAT A KURVA JAVAÁBA user  :? " + packet.getObject());
            outToServer.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is responsible for sending a packet to the server
     * @param newUser User object
     */
    public void register(User newUser) {
        try {
            Container packet = new Container(newUser, ClassName.CREATE_ACCOUNT);

            outToServer.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is responsible for sending a packet to the server
     * @param g Group object
     */
    public void addGroup(Group g) {
        try {
            Container packet = new Container(g, ClassName.CREATING_GROUP);
            outToServer.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is responsible for sending a packet to the server
     * @param groups ArrayList<Group></Group> object
     */
    private void receiveGroups(ArrayList<Group> groups) {
        socketClient.refreshGroupList(groups);
    }

    /**
     * This method is responsible for sending a packet to the server
     * @param mess GroupMessages object
     */
    void sendGroup(GroupMessages mess) {
        try {
            Container packet = new Container(mess, ClassName.GROUP_MESSAGE);
            outToServer.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is responsible for sending a packet to the server
     * @param u User object
     */
    public void changePassword(User u) {
        try {
            Container packet = new Container(u, ClassName.PASSWORD_CHANGE);
            outToServer.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is responsible for sending a packet to the server
     * @param use User object
     */
    public void deleteUser(User use) {
        try {
            Container packet = new Container(use, ClassName.REMOVE_USER);
            outToServer.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is responsible for sending a packet to the server
     * @param selectedGroup Group object
     */
    void addMember(Group selectedGroup) {
        try{
        Container packet = new Container(selectedGroup,ClassName.ADD_GROUPMEMBER);
        outToServer.writeObject(packet);
        }catch(IOException e){
          e.printStackTrace();
        }
    }

    /**
     * This method is responsible for sending a packet to the server
     * @param selectedGroup Group object
     */
    void removeMember(Group selectedGroup) {
        try{
        Container packet = new Container(selectedGroup,ClassName.REMOVE_GROUPMEMBER);
        outToServer.writeObject(packet);
        }catch(IOException e){
          e.printStackTrace();
        }
    }

    /**
     * This method is responsible for sending a packet to the server
     * @param g Group object
     */
    void deleteGroup(Group g) {
         try{
        Container packet = new Container(g,ClassName.DELETE_GROUP);
        outToServer.writeObject(packet);
        }catch(IOException e){
          e.printStackTrace();
        }
    }

    /**
     * This method is responsible for sending a packet to the server
     * @param use User object
     */
    public void resetPassword(User use) {
        try{
            Container packet = new Container(use,ClassName.RESET_PASSWORD);
            outToServer.writeObject(packet);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * This method is responsible for sending a packet to the server
     * @param g Group object
     */
    void changeLeader(Group g) {
        try{
        Container packet = new Container(g,ClassName.CHANGE_GROUP_LEADER);
        outToServer.writeObject(packet);
        }catch(IOException e){
          e.printStackTrace();
        }
    }

    /**
     * This method is responsible for sending a packet to the server
     * @param user User object
     */
    public void saveDataOnExit(User user) {
        try{
            Container packet = new Container(user,ClassName.USER_LEFT);
            outToServer.writeObject(packet);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
