package com.messcode.server.networking;

import JDBC.ExportData;
import JDBC.ImportData;
import com.messcode.transferobjects.ClassName;
import com.messcode.transferobjects.Container;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.UserList;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;
import com.messcode.transferobjects.util.Request;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerSocketHandler implements Runnable {
    private ConnectionPool pool;
    private Socket socket;
private ExportData dbe;
private ImportData dbi;
    private ObjectOutputStream outToClient;
    private ObjectInputStream inFromClient;

    private User user;

    public ServerSocketHandler(Socket socket, ConnectionPool pool)
            throws IOException {
        dbe=new ExportData();
        dbi=new ImportData();
        this.socket = socket;
        this.pool = pool;
        inFromClient = new ObjectInputStream(socket.getInputStream());
        outToClient = new ObjectOutputStream(socket.getOutputStream());

    }

    @Override
    public void run() {
        try {
            while (true) {
                Container packet = (Container)(inFromClient.readObject());
                System.out.println("NEW PACKET : "+ packet.getClassName() + " object "+ packet.getObject() );
                switch(packet.getClassName())
                {
                    case PRIVATE_MESSAGE:
                    {
                        PrivateMessage pm = (PrivateMessage)packet.getObject();
                        pool.sendMessageInPM(pm);
                        break;
                    }
                    case USER_JOIN:
                    {
                        pool.addHandler(this);
                        user = (User) packet.getObject();
                        System.out
                                .println("[SERVER] " + user.getUsername() + " joined the chat");
                        pool.userJoin(user);
                        updateUsersList();
                        break;
                    }
                    case PUBLIC_MESSAGE:
                    {
                        PublicMessage message = (PublicMessage)packet.getObject();
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
        }
    }

    public void updateUsersList() {
        try {
            UserList users = new UserList();
            users.addList(pool.getUsers());
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
        try { Container packet = new Container(user, ClassName.USER_JOIN);
            outToClient.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInvite(PrivateMessage usersPM) {
        try {
            outToClient.writeObject(usersPM);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageInPM(PrivateMessage pm) {
        try {
            Container packet = new Container(pm, ClassName.PRIVATE_MESSAGE);
            outToClient.writeObject(packet);
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

