package com.messcode.server.networking;

import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;

import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
    private List<ServerSocketHandler> connections = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    public void broadcastMessage(PublicMessage publicMessage) {
        for (ServerSocketHandler handler : connections) {
            handler.sendMessage(publicMessage);
        }
    }

    public void userJoin(User user) {
        for (ServerSocketHandler handler : connections) {
            if (handler.getUser() != null && !handler.getUser().equals(user)) {
                handler.joinChat(user);
            }
        }
        users.add(user);
    }

    public void inviteToPM(PrivateMessage usersPM) {
        for (ServerSocketHandler handler : connections) {
            if (handler.getUser().equals(usersPM.getReceiver())) {
                handler.sendInvite(usersPM);
            }
        }
    }

    public void sendMessageInPM(PrivateMessage pm) {
        for (ServerSocketHandler handler : connections) {

            if (handler.getUser().equals(pm.getSender()) || handler.getUser().equals(pm.getReceiver()))
                handler.sendMessageInPM(pm);
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public synchronized void addHandler(ServerSocketHandler handler) {
        connections.add(handler);
    }

    public void removeHandler(ServerSocketHandler handler) {
        connections.remove(handler);
        userLeft(handler.getUser());
    }

    private void userLeft(User user) {
        users.remove(user);
        for (ServerSocketHandler handler : connections) {
            if (handler.getUser() != null && !handler.getUser().equals(user)) {
                handler.userLeft(user);
            }
        }

    }
}
