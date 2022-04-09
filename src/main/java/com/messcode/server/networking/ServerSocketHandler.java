package com.messcode.server.networking;

import com.messcode.transferobjects.User;
import com.messcode.transferobjects.UserList;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;
import com.messcode.transferobjects.util.Request;
import com.messcode.transferobjects.UsersPM;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerSocketHandler implements Runnable {
    private ConnectionPool pool;
    private Socket socket;

    private ObjectOutputStream outToClient;
    private ObjectInputStream inFromClient;

    private User user;

    public ServerSocketHandler(Socket socket, ConnectionPool pool)
            throws IOException {
        this.socket = socket;
        this.pool = pool;
        inFromClient = new ObjectInputStream(socket.getInputStream());
        outToClient = new ObjectOutputStream(socket.getOutputStream());

    }

    @Override
    public void run() {
        try {
            while (true) {
                Object obj = inFromClient.readObject();
                if (obj instanceof User) {
                    pool.addHandler(this);
                    user = (User) obj;
                    System.out
                            .println("[SERVER] " + user.getUsername() + " joined the chat");
                    pool.userJoin(user);
                    updateUsersList();
                } else if (obj instanceof PublicMessage) {
                    PublicMessage message = (PublicMessage) obj;
                    pool.broadcastMessage(message);
                } else if (obj instanceof UsersPM) {
                    UsersPM usersPM = (UsersPM) obj;
                    pool.inviteToPM(usersPM);
                } else if (obj instanceof PrivateMessage) {
                    PrivateMessage pm = (PrivateMessage) obj;
                    pool.sendMessageInPM(pm);
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
            outToClient.writeObject(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(PublicMessage publicMessage) {
        try {
            System.out.println(
                    "[SERVER] " + "user: " + publicMessage.getUsername() + " sent: "
                            + publicMessage.getMsg());
            outToClient.writeObject(publicMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User getUser() {
        return user;
    }

    public void joinChat(User user) {
        try {
            outToClient.writeObject(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendInvite(UsersPM usersPM) {
        try {
            outToClient.writeObject(usersPM);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageInPM(PrivateMessage pm) {
        try {
            outToClient.writeObject(pm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void userLeft(User user) {
        Request request = new Request("UserLeft", user);
        try {
            outToClient.writeObject(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

