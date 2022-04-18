package com.messcode.server.networking;

import JDBC.ExportData;
import JDBC.ImportData;
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
                    case PRIVATE_MESSAGE: {
                        PrivateMessage pm = (PrivateMessage) packet.getObject();
                        System.out.println("calling the method on the PM !!!");
                        pool.sendMessageInPM(pm);
                        dbi.saveMessage(pm);
                        break;
                    }
                    case USER_JOIN: {
                        User usertemp = (User) packet.getObject();

                        boolean isItSame =pool.userCheck(usertemp);
                        if(isItSame) break;
                        Container packetToClient = null;



                        packetToClient = dbe.checkLogin(usertemp.getEmail(), usertemp.getStrPassword()); /// here the username, should be email, and email should be passowrd


                        if (packetToClient.getObject()!=null) {
                            packetToClient = dbe.acceptLogin( usertemp.getEmail(),(String)packetToClient.getObject());

                        } else {
                            outToClient.writeObject(packetToClient);
                            break;
                        }
                        pool.addHandler(this);
                        user = (User)((ArrayList<Object>)packetToClient.getObject()).get(2);


                        pool.userJoin(user);
                        outToClient.writeObject(packetToClient);
                        updateUsersList();

                        break;
                    }
                    case PUBLIC_MESSAGE: {
                        PublicMessage message = (PublicMessage) packet.getObject();
                        pool.broadcastMessage(message);
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
            System.out.println("Telling one user that new one added: "+ user.getName() + " email: "+ user.getEmail() );
            outToClient.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void sendMessageInPM(PrivateMessage pm) {
        try {
            System.out.println("HELLO THIS SHOULD BE BLALGLA");
            Container packet = new Container(pm,ClassName.PRIVATE_MESSAGE);
            outToClient.writeObject(packet);
            System.out.println("server has sent the stuff "+ pm.getMsg());
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
}

