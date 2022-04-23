package com.messcode.client.model;

import com.messcode.client.Start;
import com.messcode.client.networking.Client;
import com.messcode.transferobjects.AccountManager;
import com.messcode.transferobjects.Container;
import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.messages.GroupMessages;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;
import com.messcode.transferobjects.User;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

public class MainModelManager implements MainModel {

    private Client client;
    private PropertyChangeSupport support;
    private User user;
    private PrivateMessage usersPM;
    private ArrayList<PublicMessage> allMessage;
    private ArrayList<User> allUsers;
    private ArrayList<Group> allGroups;
    private Group selectedGroup;

    public Group getSelectedGroup() {
        return selectedGroup;
    }

    public void setSelectedGroup(Group selectedGroup) {
        support.firePropertyChange("changeSelectedGroup",null,selectedGroup);
        this.selectedGroup = selectedGroup;

    }

    @Override
    public User getCurrentUser() {
        return user;
    }

    @Override
    public void deleteUser(User use) {
        client.deleteUser(use);
    }

    public MainModelManager(Client client) {
        support = new PropertyChangeSupport(this);
        allMessage = new ArrayList<>();
        allUsers = new ArrayList<>();
        this.client = client;
        try {
            client.start();
            client.addListener("newGroupMessage", this::receiveGroup);
            client.addListener("RefresgGroups", this::refreshGroupList);
            client.addListener("AddNewUser", this::addToUsersList);
            client.addListener("MessageForEveryone", this::receivePublic);
            client.addListener("newPM", this::receivePM);
            client.addListener("RemoveUser", this::removeFromUsersList);
            client.addListener("LoginResponse", this::loginResponse);
            client.addListener("LoginData", this::loginData);
            client.addListener("createUserResponse", this::createAccount);
            client.addListener("passChangeResponse", this::passChangeResponse);
            client.addListener("userDeleted",this::userDeleted);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void userDeleted(PropertyChangeEvent propertyChangeEvent) {
        User u =(User )propertyChangeEvent.getNewValue();
        support.firePropertyChange("AddNewUser", null,u);
    }

    private void passChangeResponse(PropertyChangeEvent propertyChangeEvent) {
        Container packet = ((Container) propertyChangeEvent.getNewValue());
        support.firePropertyChange("passChangeResponse", null, ((boolean) packet.getObject()));
    }

    private void createAccount(PropertyChangeEvent propertyChangeEvent) {
        if(((User) ((Container) propertyChangeEvent.getNewValue()).getObject()) == null)
        {
            System.out.println("THE OBJECT IS NULL, NICCEEE");
            support.firePropertyChange("createUserResponse", null, false);
        }
        else {
            System.out.println("THE OBJECT IS NOT NULL, NICEE");
            User u =((User)((Container)propertyChangeEvent.getNewValue()).getObject());
            support.firePropertyChange("createUserResponse", null, true);
            allUsers.add(u);
            support.firePropertyChange("AddOfflineUsers", null, allUsers);
        }



    }

    private void loginData(PropertyChangeEvent propertyChangeEvent) {
        Container packet = ((Container) propertyChangeEvent.getNewValue());
        ArrayList<Object> objs = (ArrayList<Object>) packet.getObject();
        ArrayList<PublicMessage> allPublicMessages = (ArrayList<PublicMessage>) objs.get(0);
        ArrayList<PublicMessage> lastSeen = (ArrayList<PublicMessage>) objs.get(1);
        if (objs.size() > 4) {
            allGroups = (ArrayList<Group>) objs.get(4);
        }
        user = (User) objs.get(2);
        allUsers = (ArrayList<User>) objs.get(3); //ALL USERS ADDED TO THE ALLUSER LIST.
        for (User u : allUsers) {
            java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.FINE,
                    "///////////" + u.getEmail() + "////////////");
            System.out.println("///////////" + u.getEmail() + "////////////");
        }

        support.firePropertyChange("AddOfflineUsers", null, allUsers);
        support.firePropertyChange("RefresgGroups", null, allGroups);
        java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.FINE, "Everything has been casted");
        System.out.println("Everything has been casted");

        //user.getLastSeen.add(lastSeen);

        this.allMessage = allPublicMessages;

        support.firePropertyChange("LoginData", null, allMessage);  // probably lot more stuff should happen here and vm, but rn this is okay.
        java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.FINE,
                user.getEmail() + " " + user.getName());
        System.out.println(user.getEmail() + " " + user.getName());
        support.firePropertyChange("SetUsernameInChat", null, user);
    }

    private void loginResponse(PropertyChangeEvent propertyChangeEvent) {
        boolean answer = (boolean) propertyChangeEvent.getNewValue();
        java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.FINE, "in model: " + answer);
        System.out.println("in model: " + answer);
        support.firePropertyChange("LoginResponseToVM", null, answer);
    }

    private void removeFromUsersList(PropertyChangeEvent propertyChangeEvent) {
        support.firePropertyChange(propertyChangeEvent);
    }

    //  GLOBAL CHAT
    @Override
    public void sendListOfPmRoomUsers(PrivateMessage usersPM) {
        this.usersPM = usersPM;
        support.firePropertyChange("UsersOnlineInPM", null, usersPM);
    }

    public void receivePublic(PropertyChangeEvent propertyChangeEvent) {
        PublicMessage publicMessage = (PublicMessage) propertyChangeEvent.getNewValue();
        this.allMessage.add(publicMessage);
        java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.FINE, "got to model");
        System.out.println("got to model");
        support.firePropertyChange("MessageForEveryone", null, publicMessage);
    }

    public void receivePM(PropertyChangeEvent propertyChangeEvent) {
        PrivateMessage pm = (PrivateMessage) propertyChangeEvent.getNewValue();
        this.allMessage.add(pm);
        java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.FINE,
                "//////////////////////////PMPM//////////////////////////////");
        System.out.println("//////////////////////////PMPM//////////////////////////////");
        support.firePropertyChange("newPM", null, pm);
    }

    private void receiveGroup(PropertyChangeEvent propertyChangeEvent) {

        GroupMessages gm = (GroupMessages) propertyChangeEvent.getNewValue();
        this.allMessage.add(gm);
        support.firePropertyChange("newGroupMessage", null, gm);
    }

    public void addToUsersList(PropertyChangeEvent propertyChangeEvent) {
        User user = (User) propertyChangeEvent.getNewValue();
        support.firePropertyChange("AddNewUser", null, user);
    }

    @Override
    public void addUser(String email, String pwd) {
        User javaIsRetarded = new User(email, pwd);

        client.addUser(javaIsRetarded);
    }

    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }

    @Override
    public void sendPublic(PublicMessage mess) {
        client.sendPublic(mess);
    }

    @Override
    public void sendPM(PrivateMessage message) {
        client.sendPM(message);
    }

    @Override
    public void sendGroup(GroupMessages mess) {
        client.sendGroup(mess);
    }

    public ArrayList<PublicMessage> getAllMessage() {
        return allMessage;
    }

    public void setAllMessage(ArrayList<PublicMessage> allMessage) {
        this.allMessage = allMessage;
    }

    @Override
    public void register(String firstName, String lastName, String email, String password, String type) {
        User newUser = new User(firstName, lastName, email, password, type);
        client.register(newUser);
    }

    @Override
    public ArrayList<PrivateMessage> loadPMs(User receiver) {

        ArrayList<PrivateMessage> pivi = new ArrayList<>();
        for (PublicMessage p : this.allMessage) {
            if (p instanceof PrivateMessage && (((PrivateMessage) p).getReceiver().getEmail().equals(receiver.getEmail()) || ((PrivateMessage) p).getSender().getEmail().equals(receiver.getEmail()))) {
                pivi.add(((PrivateMessage) p));
            }
        }
        return pivi;
    }

    @Override
    public ArrayList<PublicMessage> loadPublics() {
        ArrayList<PublicMessage> pubi = new ArrayList<>();
        PublicMessage puu = new PublicMessage(user, "dasd");
        for (PublicMessage p : this.allMessage) {
            if (p.getClass().equals(puu.getClass())) {
                java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.FINE,
                        "messa: time : " + p.getTime() + "  mes: " + p.getMsg());
                System.out.println("messa: time : " + p.getTime() + "  mes: " + p.getMsg());
                pubi.add(p);
            }
        }
        return pubi;
    }

    @Override
    public ArrayList<GroupMessages> loadGroup(Group selectedGroup) {
        ArrayList<GroupMessages> grupi = new ArrayList<>();
        for (PublicMessage p : this.allMessage) {
            if (p instanceof GroupMessages && (selectedGroup.getName().equals(((GroupMessages) p).getGroup().getName()))) {
                grupi.add((GroupMessages) p);
            }
        }
        return grupi;
    }

    @Override
    public void changePassword(String current, String password, String passwordConfirmed) {
        User u = new User(user.getEmail(), current);
        u.setPassword(passwordConfirmed);
        System.out.println("salty: "+ u.getSalt());
        client.changePassword(u);
    }

    @Override
    public void newGroup(Group g) {
        client.newGroup(g);
    }

    public void refreshGroupList(PropertyChangeEvent propertyChangeEvent) {
        ArrayList<Group> g = (ArrayList<Group>) propertyChangeEvent.getNewValue();
        allGroups = g;
        support.firePropertyChange("RefresgGroups", null, g);
    }

    @Override
    public void addMember(ArrayList<User> u) {
       Group updatedGroup = new Group(selectedGroup.getName(), selectedGroup.getDescription(), selectedGroup.getLeader());
       updatedGroup.setMembers(selectedGroup.getMembers());
       updatedGroup.addMembers(u);
       client.addMember(selectedGroup);
    }

}
