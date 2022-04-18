package com.messcode.client.networking;

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

public class ClientSocketHandler implements Runnable {

    private SocketClient socketClient;
    private Socket socket;
    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;

    public ClientSocketHandler(Socket socket, SocketClient socketClient) throws IOException {
        this.socket = socket;
        this.socketClient = socketClient;
        outToServer = new ObjectOutputStream(socket.getOutputStream());
        inFromServer = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                Container packet = (Container) inFromServer.readObject();
                System.out.println("Im the client and i got the PM finally pls wtf"+ packet.getClassName());
                switch (packet.getClassName()) {
                    case PRIVATE_MESSAGE: {
                        PrivateMessage pm = (PrivateMessage) packet.getObject();

                        receivePM(pm);
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
                        System.out.println("in client: " + answ);
                        loginResponse(answ);
                        break;
                    }
                    case LOGIN_DATA: {
                        System.out.println("i got the data " + packet);
                        loginData(packet);
                        break;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loginData(Container packet) {
        socketClient.loginData(packet);
    }

    private void userLeft(Object arg) {
        User user = (User) arg;
        socketClient.removeFromList(user);
    }

    //  BACK TO FXML
    private void loginResponse(boolean answ) {
        socketClient.loginResponse(answ);
    }

    private void addToUsersList(User user) {
        socketClient.addToList(user);
    }

    private void receivePublic(PublicMessage message) {
        socketClient.displayMessage(message);
        System.out.println("I GOT THIS: " + message.getUsername() + " " + message.getMsg());
    }

    private void receivePM(PrivateMessage message) {
        socketClient.displayPM(message);
        System.out.println("CLIENT GOT THE PM : "+message.getUsername() + " " + message.getMsg());
    }

    public void sendPM(PrivateMessage message) {
        try {
            Container packet = new Container(message, PRIVATE_MESSAGE);
            outToServer.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //  TO SERVER
    public void sendPublic(PublicMessage message) {
        try {
            Container packet = new Container(message, ClassName.PUBLIC_MESSAGE);
            outToServer.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addUser(User username) {
        try {
            System.out.println("wgatdup: "+username);
            Container packet = new Container(username, ClassName.USER_JOIN);

            System.out.println("WTF IS GOING ON: : "+ username.getEmail() + " pwd "+ username.getStrPassword());
            System.out.println("FASZOMAT A KURVA JAVAÁBA :? "+  packet.getObject());
            System.out.println("FASZOMAT A KURVA JAVAÁBA user  :? "+  packet.getObject());
            outToServer.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register(User newUser) {
        try {
            Container packet = new Container(newUser, ClassName.REGISTER_USER);

            outToServer.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
