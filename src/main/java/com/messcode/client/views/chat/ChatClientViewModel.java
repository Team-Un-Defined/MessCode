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
import java.util.Iterator;

/**
 * The ViewModel of the ChatClient (main) panel.
 * Filters & processes the information going to and from the Controller.
 *
 * @author Kamilla Kisová
 */
public class ChatClientViewModel implements Subject {

    private User currentUser;
    private PropertyChangeSupport support;
    private ObservableList<User> usersList;
    private ObservableList<Group> groups;
    private MainModel mainModel;
    private User receiver;
    private Group receiverGroup;


    /**
     * Constructor of the ChatClientViewModel
     *
     * @param mainModel the MainModel, which manages all the information in the background
     */
    public ChatClientViewModel(MainModel mainModel) {
        support = new PropertyChangeSupport(this);
        usersList = FXCollections.observableArrayList();
        groups = FXCollections.observableArrayList();
        this.mainModel = mainModel;
        mainModel.addListener("newGroupMessage", this::displayGroup);
        mainModel.addListener("RefresgGroups", this::refreshGroups);
        mainModel.addListener("MessageForEveryone", this::displayPublic);
        mainModel.addListener("newPM", this::displayPM);
        mainModel.addListener("SetUsernameInChat", this::setUsernameInChat);
        mainModel.addListener("ReloadUsers", this::addOfflineUsers);
        mainModel.addListener("LoginData",this::refresh);
      
    }

    
    private void refresh(PropertyChangeEvent propertyChangeEvent) {
        Platform.runLater(() -> {
            support.firePropertyChange("refresh", null, "false");
        });
    }

    /**
     * Refreshes the list of groups
     *
     * @param propertyChangeEvent PropertyChangeEvent triggered event
     */
    private void refreshGroups(PropertyChangeEvent propertyChangeEvent) {
        Platform.runLater(() -> {
            if((ArrayList<Group>)propertyChangeEvent.getNewValue()==null){}
            else {
            groups.clear();
            groups.addAll((ArrayList<Group>) propertyChangeEvent.getNewValue());}
        });
    }

    /**
     * Adds the users to the list
     *
     * @param propertyChangeEvent PropertyChangeEvent triggered event
     */
    private void addOfflineUsers(PropertyChangeEvent propertyChangeEvent) {
        ArrayList<User> users = (ArrayList<User>) propertyChangeEvent.getNewValue();
        Platform.runLater(() -> {
            usersList.clear();
            usersList.addAll(users);
            System.out.println("THIS IS IN THE MODEL"+usersList);
        });
    }


    /**
     * Sets the current user's name
     *
     * @param propertyChangeEvent PropertyChangeEvent triggered event
     */
    private void setUsernameInChat(PropertyChangeEvent propertyChangeEvent) {
        currentUser = (User) propertyChangeEvent.getNewValue();
    }

    /**
     * Initiates the displaying of the public message
     *
     * @param propertyChangeEvent PropertyChangeEvent triggered event
     */
    private void displayPublic(PropertyChangeEvent propertyChangeEvent) {
        PublicMessage publicMessage = (PublicMessage) propertyChangeEvent.getNewValue();
        // message.setValue(publicMessage.getTime() + " " + publicMessage.getUsername() + ": " + publicMessage.getMsg());
        support.firePropertyChange("MessageForEveryone", null, publicMessage.getTime() + " " + publicMessage.getUsername() + ": " + publicMessage.getMsg());
    }

    /**
     * Gets the list of users from the triggered event
     *
     * @param propertyChangeEvent PropertyChangeEvent triggered event
     */
    

    /**
     * Getter for the list of users
     *
     * @return ObservableList<User>
     */
    public ObservableList<User> getUsersList() {
        return usersList.filtered(p-> !p.getSalt().equals(" - deleted"));
    }

    /**
     * Getter for the list of groups
     *
     * @return ObservableList<Group>
     */
    public ObservableList<Group> getGroups() {
        return groups;
    }

    /**
     * Sends the public message
     *
     * @param mess PublicMessage
     */
    public void sendPublic(PublicMessage mess) {
        mainModel.sendPublic(mess);
    }

    /**
     * Sends the private message
     *
     * @param mess PrivateMessage
     */
    public void sendPM(PrivateMessage mess) {
        mainModel.sendPM(mess);
    }

    /**
     * Sends the group message
     *
     * @param mess GroupMessages
     */
    public void sendGroup(GroupMessages mess) {
        mainModel.sendGroup(mess);
    }

    /**
     * Getter for the current user
     *
     * @return User
     */
    public User getCurrentUser() {
        return currentUser;
    }



    /**
     * Method for adding a listener. Inherited from Subject
     *
     * @param eventName String name of the event
     * @param listener  PropertyChangeListener listener of the event
     */
    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    /**
     * Method for removing a listener. Inherited from Subject
     *
     * @param eventName String name of the event
     * @param listener  PropertyChangeListener listener of the event
     */
    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }

    /**
     * Displays the incoming private message
     *
     * @param propertyChangeEvent PropertyChangeEvent triggered event
     */
    private void displayPM(PropertyChangeEvent propertyChangeEvent) {
        PrivateMessage pm = (PrivateMessage) propertyChangeEvent.getNewValue();

        if (this.receiver == null) {
            support.firePropertyChange("newPM", null, "true");
            return;
        }
        else if (pm.getReceiver().getEmail().equals(this.receiver.getEmail()) || pm.getSender().getEmail().equals(this.receiver.getEmail())) {
            support.firePropertyChange("newPM", null, pm.getTime() + " " + pm.getUsername() + ": " + pm.getMsg());
            System.out.println("got to PMPM :" + pm.getTime() + " " + pm.getUsername() + ": " + pm.getMsg());
        }
        else 
        {
        support.firePropertyChange("newPM", null, "true");
        }
    }

    /**
     * Displays the selected group and its options
     *
     * @param propertyChangeEvent PropertyChangeEvent triggered event
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
     * Loads the public messages
     *
     * @return ArrayList<PublicMessage>
     */
    public ArrayList<PublicMessage> loadPublics() {
        return mainModel.loadPublics();
    }

    /**
     * Loads the private messages
     *
     * @return ArrayList<PrivateMessage>
     */
    public ArrayList<PrivateMessage> loadPMs() {
        return mainModel.loadPMs(receiver);
    }

    /**
     * Loads the groups
     *
     * @return ArrayList<GroupMessages>
     */
    public ArrayList<GroupMessages> loadGroup() {
        return mainModel.loadGroup(receiverGroup);
    }

    /**
     * Getter for the receiver user
     *
     * @return User
     */
    public User getReceiver() {
        return receiver;
    }

    /**
     * Setter for the receiver user
     *
     * @param receiver User
     */
    public void setReceiver(User receiver) {
        this.receiver = receiver;
        mainModel.setSelectedUser(receiver);
    }

    /**
     * Getter for the receiver gorup
     *
     * @return Group
     */
    public Group getReceiverGroup() {
        return receiverGroup;
    }

    /**
     * Setter for the receiver group
     *
     * @param receiverGroup Group that receives the message
     */
    public void setReceiverGroup(Group receiverGroup) {
        this.receiverGroup = receiverGroup;
        mainModel.setSelectedGroup(receiverGroup);
    }

    /**
     * Initiates the password reset of the User
     *
     * @param use User
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
     * Copies the password upon dialog close
     *
     * @param password String
     * @return EventHandler<DialogEvent>
     */
    private EventHandler<DialogEvent> actionEvent(String password) {
        String ctc = password;
        StringSelection stringSelection = new StringSelection(ctc);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
        return null;
    }

    /**
     * Retrieves the unread PMs from the MainModel
     *
     * @param u User
     * @return boolean
     */
    public boolean getUnredPMs(User u) {
        return mainModel.unredPMs(u);
    }

    /**
     * Retrieves the unread GMs from the MainModel
     *
     * @param g Group
     * @return boolean
     */
    public boolean getUnredGMs(Group g) {
        boolean lul = mainModel.unredGMs(g);
        return lul;
    }

    /**
     * Initiates the data save on exit
     */
    public void saveDataOnExit() {
        mainModel.saveDataOnExit();
    }
}
