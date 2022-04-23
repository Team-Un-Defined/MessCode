package com.messcode.client.views.chat;

import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.GroupMessages;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;
import com.messcode.transferobjects.util.Subject;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

public class ChatClientViewModel implements Subject {

    private User currentUser;
    private PropertyChangeSupport support;
    private ObservableList<User> usersList;
    private ObservableList<Group> groups;
    private MainModel mainModel;
    private StringProperty message;
    private StringProperty PMmessage;
    private StringProperty GMmessage;
    private User receiver;
    private Group receiverGroup;


    public ChatClientViewModel(MainModel mainModel) {
        support = new PropertyChangeSupport(this);
        message = new SimpleStringProperty();
        PMmessage = new SimpleStringProperty();
        GMmessage = new SimpleStringProperty();
        usersList = FXCollections.observableArrayList();
        groups = FXCollections.observableArrayList();
        this.mainModel = mainModel;
        mainModel.addListener("newGroupMessage", this::displayGroup);
        mainModel.addListener("RefresgGroups", this::refreshGroups);
        mainModel.addListener("AddNewUser", this::getUsersList);
        mainModel.addListener("MessageForEveryone", this::displayPublic);
        mainModel.addListener("newPM", this::displayPM);
        mainModel.addListener("SetUsernameInChat", this::setUsernameInChat);
        mainModel.addListener("RemoveUser", this::removeFromUsersList);
        mainModel.addListener("AddOfflineUsers", this::addOfflineUsers);

    }

    private void refreshGroups(PropertyChangeEvent propertyChangeEvent) {
        Platform.runLater(() -> {
            groups.clear();
            groups.addAll((ArrayList<Group>) propertyChangeEvent.getNewValue());
        });
    }

    private void addOfflineUsers(PropertyChangeEvent propertyChangeEvent) {
        ArrayList<User> users = (ArrayList<User>) propertyChangeEvent.getNewValue();

        Platform.runLater(() -> {
            usersList.addAll(users);
            System.out.println(usersList);
        });
    }

    private void removeFromUsersList(PropertyChangeEvent propertyChangeEvent) {
        User user = (User) propertyChangeEvent.getNewValue();
        Platform.runLater(() -> {
            user.setSalt("");
            for (int i = 0; i < usersList.size(); i++) {
                if (usersList.get(i).getEmail().equals(user.getEmail())) {
                    usersList.set(i, user);
                    break;
                }
            }
            System.out.println(usersList);
        });
    }

    private void setUsernameInChat(PropertyChangeEvent propertyChangeEvent) {
        currentUser = (User) propertyChangeEvent.getNewValue();
    }

    private void displayPublic(PropertyChangeEvent propertyChangeEvent) {
        PublicMessage publicMessage = (PublicMessage) propertyChangeEvent.getNewValue();

        // message.setValue(publicMessage.getTime() + " " + publicMessage.getUsername() + ": " + publicMessage.getMsg());
        support.firePropertyChange("MessageForEveryone", null, publicMessage.getTime() + " " + publicMessage.getUsername() + ": " + publicMessage.getMsg());
    }

    private void getUsersList(PropertyChangeEvent propertyChangeEvent) {
        User user = (User) propertyChangeEvent.getNewValue();
        System.out.println("USER SALT: "+ user.getSalt());
        Platform.runLater(() -> {
            if(user.getSalt().equals(" - deleted")){
                for (int i = 0; i < usersList.size(); i++) {
                    if (usersList.get(i).getEmail().equals(user.getEmail())) {
                        usersList.set(i, user);              // KAMI PUT THE ONLINE DOT HERE or in the controller i dont know man, i hate my life and i hate guis,how are you btw?
                                                            // heyo, im pretty okay rn, thanks for the question (and the note) wbu?
                                                            // im good, i just hate lings' merge conflicts, btw i added the - deleted user tag, it is added in the salt from server.
                        break;                              // fug dat bitch ass lings man
                    }
                }
            }else {
                user.setSalt(" - online");  //
                for (int i = 0; i < usersList.size(); i++) {
                    if (usersList.get(i).getEmail().equals(user.getEmail())) {
                        usersList.set(i, user);
                        break;
                    }
                }
            }
            System.out.println("NEW USER ADDED WHLEO");
            System.out.println(usersList);
        });
    }

    public void sendPublic(PublicMessage mess) {
        mainModel.sendPublic(mess);
    }

    public ObservableList<User> getUsersList() {
        return usersList;
    }

    public ObservableList<Group> getGroups() {
        return groups;
    }

    public StringProperty messageProperty() {
        return message;
    }

    public StringProperty GMProperty() {
        return GMmessage;
    }

    public StringProperty PMProperty() {
        return PMmessage;
    }

    public void sendPM(PrivateMessage mess) {
        mainModel.sendPM(mess);
    }

    public void sendGroup(GroupMessages mess) {
        mainModel.sendGroup(mess);
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void sendListOfPmRoomUsers(PrivateMessage usersPM) {
        mainModel.sendListOfPmRoomUsers(usersPM);
    }

    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }

    private void displayPM(PropertyChangeEvent propertyChangeEvent) {

        PrivateMessage pm = (PrivateMessage) propertyChangeEvent.getNewValue();

        if (this.receiver == null) return;
        else if (pm.getReceiver().getEmail().equals(this.receiver.getEmail()) || pm.getSender().getEmail().equals(this.receiver.getEmail())) {

            // PMmessage.setValue(pm.getTime() + " " + pm.getUsername() + ": " + pm.getMsg());
            support.firePropertyChange("newPM", null, pm.getTime() + " " + pm.getUsername() + ": " + pm.getMsg());
            System.out.println("got to PMPM :" + pm.getTime() + " " + pm.getUsername() + ": " + pm.getMsg());
        }
    }

    private void displayGroup(PropertyChangeEvent propertyChangeEvent) {
        GroupMessages gm = (GroupMessages) propertyChangeEvent.getNewValue();

        if (this.receiverGroup == null) return;
        else if (gm.getGroup().getName().equals(this.receiverGroup.getName())) {
            //GMmessage.set(gm.getTime() + " " + gm.getUsername() + ": " + gm.getMsg());
            String s = gm.getTime() + " " + gm.getUsername() + ": " + gm.getMsg();
            support.firePropertyChange("newGroupMessage", null, s);
        }

    }

    public ArrayList<PublicMessage> loadPublics() {
        return mainModel.loadPublics();
    }

    public ArrayList<PrivateMessage> loadPMs() {
        return mainModel.loadPMs(receiver);
    }

    public ArrayList<GroupMessages> loadGroup() {
        return mainModel.loadGroup(receiverGroup);
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public Group getReceiverGroup() {
        return receiverGroup;
    }

    public void setReceiverGroup(Group receiverGroup) {
        this.receiverGroup = receiverGroup;
        mainModel.setSelectedGroup(receiverGroup);
    }


}
