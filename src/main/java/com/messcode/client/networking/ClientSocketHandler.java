package com.messcode.client.networking;

import com.messcode.transferobjects.User;
import com.messcode.transferobjects.UserList;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;
import com.messcode.transferobjects.util.Request;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientSocketHandler implements Runnable {
    private SocketClient socketClient;
    private Socket socket;

    private ObjectOutputStream outToServer;
    private ObjectInputStream inFromServer;

    public ClientSocketHandler(Socket socket, SocketClient socketClient)
            throws IOException {
        this.socket = socket;
        this.socketClient = socketClient;
        outToServer = new ObjectOutputStream(socket.getOutputStream());
        inFromServer = new ObjectInputStream(socket.getInputStream());
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object obj = inFromServer.readObject();
                if (obj instanceof PrivateMessage) {
                    PrivateMessage pm = (PrivateMessage) obj;
                    receivePM(pm);
                }
                else if (obj instanceof PublicMessage) {
                    PublicMessage message = ((PublicMessage) obj);
                    receivePublic(message);
                } else if (obj instanceof User) {
                    User user = ((User) obj);
                    addToUsersList(user);
                } else if (obj instanceof UserList) {
                    UserList users = (UserList) obj;

                    for (int i = 0; i < users.getSize(); i++) {
                        addToUsersList(users.get(i));
                    }
                }
                  else if (obj instanceof Request) {
                    Request request = (Request) obj;
                    if (request.getType().equals("UserLeft")) {
                        userLeft(request.getArg());
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void userLeft(Object arg) {
        User user = (User) arg;
        socketClient.removeFromList(user);
    }

    //  BACK TO FXML

    private void addToUsersList(User user) {
        socketClient.addToList(user);
    }

    private void receivePublic(PublicMessage message) {
        socketClient.displayMessage(message);
        System.out.println(message.getUsername() + " " + message.getMsg());
    }
    private void receivePM(PrivateMessage message) {
        socketClient.displayMessage(message);
        System.out.println(message.getUsername() + " " + message.getMsg());
    }
    
    public void sendPM(PrivateMessage message) {
        try {
            outToServer.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    //  TO SERVER
    public void sendPublic(PublicMessage message) {
        try {
            outToServer.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addUser(User username) {
        try {
            outToServer.writeObject(username);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    

    public void sendMessageInPM(PrivateMessage message) {
        try {
            outToServer.writeObject(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
