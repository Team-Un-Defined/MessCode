package com.messcode.server.networking;

import JDBC.ExportData;
import com.messcode.transferobjects.ClassName;
import com.messcode.transferobjects.Container;
import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.GroupMessages;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *This class contains all the clients that are connected to the server and also their corresponding user objects
 *
 */
public class ConnectionPool {

    private List<ServerSocketHandler> connections = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    /**
     * This method broadcasts all the public messages to all clients
     *
     * @param publicMessage PublicMessage object
     */
    public void broadcastMessage(PublicMessage publicMessage) {
        for (ServerSocketHandler handler : connections) {
            handler.sendMessage(publicMessage);
        }
    }

    /**
     * This method is responsible for updating clients when a user joins the chat
     *
     * @param user User object
     */
    public void userJoin(User user) {
        for (ServerSocketHandler handler : connections) {
            if (handler.getUser() != null && !handler.getUser().equals(user)) {
                handler.joinChat(user);
            }
        }
        users.add(user);
    }

    /**
     * This method is responisble for sending Private Messages to the connected clients
     *
     * @param pm PrivateMessage Object
     */
    public void sendMessageInPM(PrivateMessage pm) {
        for (ServerSocketHandler handler : connections) {

            if (handler.getUser().getEmail().equals(pm.getSender().getEmail()) || handler.getUser().getEmail().equals(pm.getReceiver().getEmail())) {
                handler.sendMessageInPM(pm);
                System.out.println("I got the user");
            }

        }
    }

    /**
     * This method gets the user list
     *
     * @return List<User></User> Object
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * This method is responsible for adding the clients correspondig server socket thread to the connections pool
     *
     * @param handler ServerSocketHandler object
     */
    public synchronized void addHandler(ServerSocketHandler handler) {
        connections.add(handler);
    }

    /**
     * This method is responsible for removing the server socket handler from the connections pool
     *
     * @param handler ServerSocketHandler Object
     */
    public void removeHandler(ServerSocketHandler handler) {
        connections.remove(handler);
        userLeft(handler.getUser());
    }

    /**
     * This method updates the clients when a user leaves the chat system
     *
     * @param user User object
     */
    private void userLeft(User user) {
        users.remove(user);
        for (ServerSocketHandler handler : connections) {
            if (handler.getUser() != null && !handler.getUser().equals(user)) {
                handler.userLeft(user);
            }
        }
    }

    /**
     * This method checks whether the user is already connected to the server
     *
     * @param us User object
     * @return boolean True if connected false if not.
     */
    public boolean userCheck(User us) {
        boolean ans = false;
        for (ServerSocketHandler handler : connections) {


            System.out.println(us);
            System.out.println(handler.getUser());
            System.out.println(handler.getUser().getEmail());
            if (handler.getUser().equals(us) || handler.getUser().getEmail().equals(us.getEmail())) {
                ans = true;
                break;
            }
        }
        return ans;
    }

    /**
     * This updates all the groups for all the users in the database
     *
     * @param dbe ExportDate JDBC object
     */
    public void updateGroup(ExportData dbe) {
        for (ServerSocketHandler handler : connections) {
            try {
                handler.sendGroups(new Container(dbe.updateGroups(handler.getUser()), ClassName.GROUP_UPDATE));

            } catch (SQLException ex) {
                Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * This method is responsible for sending the group messages to the corresponding users.
     *
     * @param message GroupMessage object
     */
    public void sendGroupMessages(GroupMessages message) {
        for (ServerSocketHandler handler : connections) {
            for (int i = 0; i < message.getGroup().getMembers().size(); i++) {
                if (message.getGroup().getMembers().get(i).getEmail().equals(handler.getUser().getEmail()) || handler.getUser().isSuperuser() || handler.getUser().isEmployer()) {
                    handler.sendGroupMessage(message);
                    break;
                }
            }


        }
    }

    /**
     * This method is responsible for kicking a specific user and notifying all the other users.
     *
     * @param u User object
     */
    public void kickUser(User u) {
        for (ServerSocketHandler handler : connections) {
            handler.removeUser(u);
            if (handler.getUser().getEmail().equals(u.getEmail())) {

                removeHandler(handler);
                break;
            }
        }
    }

    /**
     * This method is responsible for sending all the group messages of a user  for a specific group
     *
     * @param groupMessages Arraylist<PublicMessage></PublicMessage> object
     */
    public void sendAllGroupMessages(ArrayList<PublicMessage> groupMessages) {
        if (groupMessages != null) {
            Group g = ((GroupMessages) groupMessages.get(0)).getGroup();
            for (ServerSocketHandler handler : connections) {
                for (int i = 0; i < g.getMembers().size(); i++) {
                    if (g.getMembers().get(i).getEmail().equals(handler.getUser().getEmail())) {
                        handler.sendAllGroupMessage(groupMessages);
                        break;
                    }
                }
            }
        }
    }

    /**
     *
     * This method is responsible for adding a newly created users who is still offline and haven't logged in yet.
     *
     * @param u User Object the one being added
     * @param userr User object the one who added the user.
     */
    public void addNewOfflineUser(User u, User userr) {

        for (ServerSocketHandler handler : connections) {
            if (handler.getUser() != null && (!handler.getUser().getEmail().equals(userr.getEmail()))) {
                handler.updateOfflineList(u);
            }
        }

    }
}
