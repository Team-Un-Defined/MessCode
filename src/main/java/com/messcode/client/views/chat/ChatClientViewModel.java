package com.messcode.client.views.chat;

import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.AccountManager;
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
import javafx.event.EventHandler;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogEvent;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.SQLOutput;
import java.util.ArrayList;

/**
 *
 */
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


    /**
     * @param mainModel
     */
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

    /**
     * @param propertyChangeEvent
     */
    private void refreshGroups(PropertyChangeEvent propertyChangeEvent) {
        Platform.runLater(() -> {
            groups.clear();
            groups.addAll((ArrayList<Group>) propertyChangeEvent.getNewValue());
        });
    }

    /**
     * @param propertyChangeEvent
     */
    private void addOfflineUsers(PropertyChangeEvent propertyChangeEvent) {
        ArrayList<User> users = (ArrayList<User>) propertyChangeEvent.getNewValue();

        Platform.runLater(() -> {
            usersList.clear();
            usersList.addAll(users);
            System.out.println(usersList);
        });
    }

    /**
     * @param propertyChangeEvent
     */
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

    /**
     * @param propertyChangeEvent
     */
    private void setUsernameInChat(PropertyChangeEvent propertyChangeEvent) {
        currentUser = (User) propertyChangeEvent.getNewValue();
    }

    /**
     * @param propertyChangeEvent
     */
    private void displayPublic(PropertyChangeEvent propertyChangeEvent) {
        PublicMessage publicMessage = (PublicMessage) propertyChangeEvent.getNewValue();

        // message.setValue(publicMessage.getTime() + " " + publicMessage.getUsername() + ": " + publicMessage.getMsg());
        support.firePropertyChange("MessageForEveryone", null, publicMessage.getTime() + " " + publicMessage.getUsername() + ": " + publicMessage.getMsg());
    }

    /**
     * @param propertyChangeEvent
     */
    private void getUsersList(PropertyChangeEvent propertyChangeEvent) {
        User user = (User) propertyChangeEvent.getNewValue();
        System.out.println("USER SALT: " + user.getSalt());
        Platform.runLater(() -> {
            if (user.getSalt().equals(" - deleted")) {
                for (int i = 0; i < usersList.size(); i++) {
                    if (usersList.get(i).getEmail().equals(user.getEmail())) {
                        usersList.set(i, user);
                        break;
                    }
                }
            } else {
                user.setSalt(" - online");
                for (int i = 0; i < usersList.size(); i++) {
                    if (usersList.get(i).getEmail().equals(user.getEmail())) {
                        usersList.remove(i);
                        for (int j = 0; j < usersList.size(); j++) {
                            if ((!usersList.get(j).getSalt().equals(" - online"))) {
                                User temp = usersList.get(j);
                                usersList.set(j, user);
                                if (!temp.getEmail().equals(user.getEmail())) {
                                    usersList.add(temp);
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            System.out.println("NEW USER ADDED WHLEO");
            System.out.println(usersList);
        });
    }

    /**
     * @param mess
     */
    public void sendPublic(PublicMessage mess) {
        mainModel.sendPublic(mess);
    }

    /**
     * @return
     */
    public ObservableList<User> getUsersList() {
        return usersList;
    }

    /**
     * @return
     */
    public ObservableList<Group> getGroups() {
        return groups;
    }

    /**
     * @param mess
     */
    public void sendPM(PrivateMessage mess) {
        mainModel.sendPM(mess);
    }

    /**
     * @param mess
     */
    public void sendGroup(GroupMessages mess) {
        mainModel.sendGroup(mess);
    }

    /**
     * @return
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * @param usersPM
     */
    public void sendListOfPmRoomUsers(PrivateMessage usersPM) {
        mainModel.sendListOfPmRoomUsers(usersPM);
    }

    /**
     * @param eventName
     * @param listener
     */
    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    /**
     * @param eventName
     * @param listener
     */
    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }

    /**
     * @param propertyChangeEvent
     */
    private void displayPM(PropertyChangeEvent propertyChangeEvent) {
        PrivateMessage pm = (PrivateMessage) propertyChangeEvent.getNewValue();

        if (this.receiver == null) return;
        else if (pm.getReceiver().getEmail().equals(this.receiver.getEmail()) || pm.getSender().getEmail().equals(this.receiver.getEmail())) {
            support.firePropertyChange("newPM", null, pm.getTime() + " " + pm.getUsername() + ": " + pm.getMsg());
            System.out.println("got to PMPM :" + pm.getTime() + " " + pm.getUsername() + ": " + pm.getMsg());
        }
    }

    /**
     * @param propertyChangeEvent
     */
    private void displayGroup(PropertyChangeEvent propertyChangeEvent) {
        GroupMessages gm = (GroupMessages) propertyChangeEvent.getNewValue();

        if (this.receiverGroup == null) {
            System.out.println("IM IN DISPLAYSGROUP in vm");
            support.firePropertyChange("newGroupMessage", null, "true");
        } else if (gm.getGroup().getName().equals(this.receiverGroup.getName())) {
            System.out.println("Im in displaysgroup in vm, but i have selected the group");
            //GMmessage.set(gm.getTime() + " " + gm.getUsername() + ": " + gm.getMsg());
            String s = gm.getTime() + " " + gm.getUsername() + ": " + gm.getMsg();
            support.firePropertyChange("newGroupMessage", null, s);
        } else {
            support.firePropertyChange("newGroupMessage", null, "false");
        }

    }

    /**
     * @return
     */
    public ArrayList<PublicMessage> loadPublics() {
        return mainModel.loadPublics();
    }

    /**
     * @return
     */
    public ArrayList<PrivateMessage> loadPMs() {
        return mainModel.loadPMs(receiver);
    }

    /**
     * @return
     */
    public ArrayList<GroupMessages> loadGroup() {

        return mainModel.loadGroup(receiverGroup);
    }

    /**
     * @return
     */
    public User getReceiver() {
        return receiver;
    }

    /**
     * @param receiver
     */
    public void setReceiver(User receiver) {
        this.receiver = receiver;
        mainModel.setSelectedUser(receiver);
    }

    /**
     * @return
     */
    public Group getReceiverGroup() {
        return receiverGroup;
    }

    /**
     * @param receiverGroup
     */
    public void setReceiverGroup(Group receiverGroup) {
        this.receiverGroup = receiverGroup;
        mainModel.setSelectedGroup(receiverGroup);
    }

    /**
     * @param use
     */
    public void resetPassword(User use) {
        AccountManager m = new AccountManager();
        String password = m.generatePassword();
        User user = new User(use.getName(), use.getSurname(), use.getEmail(), password, use.getType());

        Platform.runLater(() -> {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Generated password");
            ButtonType buttonType = new ButtonType("Copy password", ButtonBar.ButtonData.OK_DONE);
            dialog.setContentText("Please make sure to forward the new user their\n" +
                    "generated password: " + password);
            System.out.println("ppass: " + password);
            dialog.getDialogPane().getButtonTypes().add(buttonType);
            dialog.setOnCloseRequest(actionEvent(password));

            dialog.showAndWait();
        });
        mainModel.resetPassword(user);
    }

    /**
     * @param password
     * @return
     */
    private EventHandler<DialogEvent> actionEvent(String password) {
        String ctc = password;
        StringSelection stringSelection = new StringSelection(ctc);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
        return null;
    }

    /**
     * @param u
     * @return
     */
    public boolean getUnredPMs(User u) {
        return mainModel.unredPMs(u);
    }

    /**
     * @param g
     * @return
     */
    public boolean getUnredGMs(Group g) {
        boolean lul = mainModel.unredGMs(g);
        System.out.println(" THIS IS MY LIFE " + lul);
        return lul;
    }

    /**
     *
     */
    public void saveDataOnExit() {
        mainModel.saveDataOnExit();
    }
}
