package com.messcode.client.model;

import com.messcode.client.networking.Client;
import com.messcode.transferobjects.Container;
import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.GroupMessages;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 */
public class MainModelManager implements MainModel {

    private Client client;
    private PropertyChangeSupport support;
    private User user;
    private PrivateMessage usersPM;
    private ArrayList<PublicMessage> allMessage;
    private ArrayList<User> allUsers;
    private ArrayList<Group> allGroups;
    private Group selectedGroup;
    private User selectedUser;

    /**
     * @param client
     */
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
            client.addListener("userDeleted", this::userDeleted);
            client.addListener("AddAllGroupMessages", this::addAllGroupMessages);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return
     */
    public Group getSelectedGroup() {
        return selectedGroup;
    }

    /**
     * @return
     */
    @Override
    public User getCurrentUser() {
        return user;
    }

    /**
     * @param use
     */
    @Override
    public void deleteUser(User use) {
        client.deleteUser(use);
    }

    /**
     * @param propertyChangeEvent
     */
    private void addAllGroupMessages(PropertyChangeEvent propertyChangeEvent) {
        ArrayList<GroupMessages> msgs = (ArrayList<GroupMessages>) ((Container) propertyChangeEvent.getNewValue()).getObject();
        for (PublicMessage pu : allMessage) {
            if (pu instanceof GroupMessages) {
                msgs.removeIf(p -> p.getTime().equals(pu.getTime()) && p.getMsg().equals(pu.getMsg()) && p.getGroup().getName().equals(((GroupMessages) pu).getGroup().getName()) && p.getSender().getEmail().equals(pu.getSender().getEmail()));
            }
        }
        allMessage.addAll(msgs);
        support.firePropertyChange("newGroupMessagesAdded", null, true);
    }

    /**
     * @param propertyChangeEvent
     */
    private void userDeleted(PropertyChangeEvent propertyChangeEvent) {
        User u = (User) ((Container) propertyChangeEvent.getNewValue()).getObject();
        support.firePropertyChange("AddNewUser", null, u);
    }

    /**
     * @param propertyChangeEvent
     */
    private void passChangeResponse(PropertyChangeEvent propertyChangeEvent) {
        Container packet = ((Container) propertyChangeEvent.getNewValue());
        support.firePropertyChange("passChangeResponse", null, ((boolean) packet.getObject()));
    }

    /**
     * @param propertyChangeEvent
     */
    private void createAccount(PropertyChangeEvent propertyChangeEvent) {
        if (((User) ((Container) propertyChangeEvent.getNewValue()).getObject()) == null) {
            System.out.println("THE OBJECT IS NULL, NICCEEE");
            support.firePropertyChange("createUserResponse", null, false);
        } else {
            System.out.println("THE OBJECT IS NOT NULL, NICEE");
            User u = ((User) ((Container) propertyChangeEvent.getNewValue()).getObject());
            support.firePropertyChange("createUserResponse", null, true);
            allUsers.add(u);
            support.firePropertyChange("AddOfflineUsers", null, allUsers);
        }
    }

    /**
     * @param propertyChangeEvent
     */
    private void loginData(PropertyChangeEvent propertyChangeEvent) {
        Container packet = ((Container) propertyChangeEvent.getNewValue());
        ArrayList<Object> objs = (ArrayList<Object>) packet.getObject();
        ArrayList<PublicMessage> allPublicMessages = (ArrayList<PublicMessage>) objs.get(0);

        if (objs.size() > 3) {
            allGroups = (ArrayList<Group>) objs.get(3);
        }
        user = (User) objs.get(1);
        allUsers = (ArrayList<User>) objs.get(2); //ALL USERS ADDED TO THE ALLUSER LIST.
        for (User u : allUsers) {
            System.out.println("///////////" + u.getSalt() + "////////////");

        }

        support.firePropertyChange("AddOfflineUsers", null, allUsers);
        support.firePropertyChange("RefresgGroups", null, allGroups);
        System.out.println("Everything has been casted");

        //user.getLastSeen.add(lastSeen);

        this.allMessage = allPublicMessages;

        support.firePropertyChange("LoginData", null, allMessage);  // probably lot more stuff should happen here and vm, but rn this is okay.
        System.out.println(user.getEmail() + " " + user.getName());
        support.firePropertyChange("SetUsernameInChat", null, user);
    }

    /**
     * @param propertyChangeEvent
     */
    private void loginResponse(PropertyChangeEvent propertyChangeEvent) {
        boolean answer = (boolean) propertyChangeEvent.getNewValue();
        System.out.println("in model: " + answer);
        support.firePropertyChange("LoginResponseToVM", null, answer);
    }

    /**
     * @param propertyChangeEvent
     */
    private void removeFromUsersList(PropertyChangeEvent propertyChangeEvent) {
        support.firePropertyChange(propertyChangeEvent);
    }

    /**
     * @param usersPM
     */
    //  GLOBAL CHAT
    @Override
    public void sendListOfPmRoomUsers(PrivateMessage usersPM) {
        this.usersPM = usersPM;
        support.firePropertyChange("UsersOnlineInPM", null, usersPM);
    }

    /**
     * @param propertyChangeEvent
     */
    public void receivePublic(PropertyChangeEvent propertyChangeEvent) {
        PublicMessage publicMessage = (PublicMessage) propertyChangeEvent.getNewValue();
        this.allMessage.add(publicMessage);
        System.out.println("got to model");
        support.firePropertyChange("MessageForEveryone", null, publicMessage);
    }

    /**
     * @param propertyChangeEvent
     */
    public void receivePM(PropertyChangeEvent propertyChangeEvent) {
        PrivateMessage pm = (PrivateMessage) propertyChangeEvent.getNewValue();
        this.allMessage.add(pm);

        if (selectedUser != null && (pm.getSender().getEmail().equals(selectedUser.getEmail()))) {
            for (PublicMessage u : user.getUnreadMessages()) {
                if (u instanceof PrivateMessage) {

                    if (((PrivateMessage) u).getReceiver().getEmail().equals(selectedUser.getEmail()) || u.getSender().getEmail().equals(selectedUser.getEmail())) {
                        user.getUnreadMessages().remove(u);
                        user.getUnreadMessages().add(pm);
                        support.firePropertyChange("newPM", null, pm);
                        return;
                    }
                }
            }
        } else if (selectedUser != null && (pm.getReceiver().getEmail().equals(selectedUser.getEmail()))) {

            for (int i = 0; i < user.getUnreadMessages().size(); i++) {

                if (user.getUnreadMessages().get(i) instanceof PrivateMessage) {
                    if (((PrivateMessage) user.getUnreadMessages().get(i)).getReceiver().getEmail().equals(pm.getReceiver().getEmail()) ||
                            user.getUnreadMessages().get(i).getSender().getEmail().equals(pm.getReceiver().getEmail())) {
                        user.getUnreadMessages().set(i, pm);

                        support.firePropertyChange("newPM", null, pm);
                        return;
                    }
                }
            }
        }
        System.out.println("//////////////////////////PMPM//////////////////////////////");
        support.firePropertyChange("newPM", null, pm);
    }

    /**
     * @param propertyChangeEvent
     */
    private void receiveGroup(PropertyChangeEvent propertyChangeEvent) {
        GroupMessages gm = (GroupMessages) propertyChangeEvent.getNewValue();
        this.allMessage.add(gm);
        if (selectedGroup != null) {
            if (gm.getGroup().getName().equals(selectedGroup.getName())) {
                for (PublicMessage g : user.getUnreadMessages()) {
                    if (g instanceof GroupMessages) {
                        if (selectedGroup != null) {
                            if (((GroupMessages) g).getGroup().getName().equals(gm.getGroup().getName())) {
                                user.getUnreadMessages().remove(g);
                                user.getUnreadMessages().add(gm);
                                support.firePropertyChange("newGroupMessage", null, gm);
                                return;
                            }
                        }
                    }
                }
            }
            support.firePropertyChange("newGroupMessage", null, gm);
        }
    }

    /**
     * @param propertyChangeEvent
     */
    public void addToUsersList(PropertyChangeEvent propertyChangeEvent) {
        User user = (User) propertyChangeEvent.getNewValue();
        support.firePropertyChange("AddNewUser", null, user);
    }

    /**
     * @param email
     * @param pwd
     */
    @Override
    public void addUser(String email, String pwd) {
        User user = new User(email, pwd);
        client.addUser(user);
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
     * @param mess
     */
    @Override
    public void sendPublic(PublicMessage mess) {
        client.sendPublic(mess);
    }

    /**
     * @param message
     */
    @Override
    public void sendPM(PrivateMessage message) {
        client.sendPM(message);
    }

    /**
     * @param mess
     */
    @Override
    public void sendGroup(GroupMessages mess) {
        client.sendGroup(mess);
    }

    /**
     * @return
     */
    public ArrayList<PublicMessage> getAllMessage() {
        return allMessage;
    }

    /**
     * @param allMessage
     */
    public void setAllMessage(ArrayList<PublicMessage> allMessage) {
        this.allMessage = allMessage;
    }

    /**
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     * @param type
     */
    @Override
    public void register(String firstName, String lastName, String email, String password, String type) {
        User newUser = new User(firstName, lastName, email, password, type);
        client.register(newUser);
    }

    /**
     * @param receiver
     * @return
     */
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

    /**
     * @return
     */
    @Override
    public ArrayList<PublicMessage> loadPublics() {
        ArrayList<PublicMessage> pubi = new ArrayList<>();
        PublicMessage puu = new PublicMessage(user, "dasd");
        for (PublicMessage p : this.allMessage) {
            if (p.getClass().equals(puu.getClass())) {
                System.out.println("messa: time : " + p.getTime() + "  mes: " + p.getMsg());
                pubi.add(p);
            }
        }
        return pubi;
    }

    /**
     * @param selectedGroup
     * @return
     */
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

    /**
     * @param current
     * @param password
     * @param passwordConfirmed
     */
    @Override
    public void changePassword(String current, String password, String passwordConfirmed) {
        User u = new User(user.getEmail(), current);
        u.setPassword(passwordConfirmed);
        System.out.println("salty: " + u.getSalt());
        client.changePassword(u);
    }

    /**
     * @param g
     */
    @Override
    public void newGroup(Group g) {
        client.newGroup(g);
    }

    /**
     * @param propertyChangeEvent
     */
    public void refreshGroupList(PropertyChangeEvent propertyChangeEvent) {
        ArrayList<Group> g = (ArrayList<Group>) propertyChangeEvent.getNewValue();
        if (selectedGroup != null) {
            for (Group grup : g) {
                System.out.println("group : " + grup.getName());
                System.out.println(" actual group: " + selectedGroup.getName());
                if (grup.getName().equals(selectedGroup.getName())) {
                    setSelectedGroup(grup);
                }
            }
        }
        allGroups = g;
        support.firePropertyChange("RefresgGroups", null, g);
    }

    /**
     * @param u
     */
    @Override
    public void addMember(ArrayList<User> u) {
        Group updatedGroup = new Group(selectedGroup.getName(), selectedGroup.getDescription(), selectedGroup.getLeader());
        updatedGroup.setMembers(selectedGroup.getMembers());
        updatedGroup.addMembers(u);
        client.addMember(updatedGroup);
    }

    /**
     * @param u
     */
    @Override
    public void removeMember(ArrayList<User> u) {
        Group updatedGroup = new Group(selectedGroup.getName(), selectedGroup.getDescription(), selectedGroup.getLeader());
        updatedGroup.addMembers(u);
        client.removeMember(updatedGroup);
    }

    /**
     * @param g
     */
    @Override
    public void deleteGroup(Group g) {
        client.deleteGroup(g);
    }

    /**
     * @param use
     */
    @Override
    public void resetPassword(User use) {
        client.resetPassword(use);
    }

    /**
     * @param g
     */
    @Override
    public void changeLeader(Group g) {
        client.changeLeader(g);
    }

    /**
     * @param u
     * @return
     */
    @Override
    public boolean unredPMs(User u) {
        ArrayList<PrivateMessage> pivi = loadPMs(u);
        PrivateMessage last = null;
        if (pivi.isEmpty()) {
            return false;
        }

        for (PrivateMessage pub : pivi) {
            if (last != null) {
                if (last.getTime().before(pub.getTime())) {
                    last = pub;
                }

            } else last = pub;
        }

        for (PublicMessage p : user.getUnreadMessages()) {
            if (p instanceof PrivateMessage) {

                if (((PrivateMessage) p).getReceiver().getEmail().equals(u.getEmail())) {
                    if ((p.getTime().before(last.getTime()))) {


                        System.out.println(last.getMsg());

                        return true;
                    }
                } else if (((PrivateMessage) p).getSender().getEmail().equals(u.getEmail())) {

                    if ((p.getTime().before(last.getTime()))) {

                        System.out.println(last.getMsg());

                        return true;

                    }
                }
            }
        }
        return false;
    }

    /**
     * @param us
     */
    @Override
    public void setSelectedUser(User us) {
        selectedUser = us;
        PrivateMessage lastMessage = null;
        for (PublicMessage pub : allMessage) {
            if (pub instanceof PrivateMessage && (pub.getSender().getEmail().equals(us.getEmail()) || ((PrivateMessage) pub).getReceiver().getEmail().equals(us.getEmail()))) {
                if (lastMessage != null) {
                    if (lastMessage.getTime().before(pub.getTime())) {
                        lastMessage = (PrivateMessage) pub;

                    }

                } else lastMessage = (PrivateMessage) pub;
            }
        }

        if (lastMessage != null) {
            for (PublicMessage u : user.getUnreadMessages()) {
                if (u instanceof PrivateMessage) {
                    if (lastMessage.getSender().getEmail().equals(us.getEmail())) {
                        if ((u.getTime().before(lastMessage.getTime()) || u.getTime().equals(lastMessage.getTime())) &&
                                (u.getSender().getEmail().equals(lastMessage.getSender().getEmail()) || ((PrivateMessage) u).getReceiver().getEmail().equals(lastMessage.getSender().getEmail())))
                            user.getUnreadMessages().remove(u);
                        user.getUnreadMessages().add(lastMessage);

                        return;
                    } else {
                        if ((u.getTime().before(lastMessage.getTime()) || u.getTime().equals(lastMessage.getTime())) &&
                                (u.getSender().getEmail().equals(lastMessage.getReceiver().getEmail()) || ((PrivateMessage) u).getReceiver().getEmail().equals(lastMessage.getReceiver().getEmail()))) {

                            user.getUnreadMessages().remove(u);
                            user.getUnreadMessages().add(lastMessage);

                            return;


                        }
                    }
                }
            }
        }
    }

    /**
     * @return
     */
    public User getSelectedUser() {
        return selectedUser;
    }

    /**
     * @param g
     * @return
     */
    @Override
    public boolean unredGMs(Group g) {
        ArrayList<GroupMessages> grps = loadGroup(g);
        GroupMessages last = null;
        if (grps.isEmpty()) {
            System.out.println("DOES THIS RETURN always false?");
            return false;
        }

        for (GroupMessages gub : grps) {
            if (last != null) {
                if (last.getTime().before(gub.getTime())) {
                    last = gub;
                    System.out.println("is this the last? " + last.getMsg());
                }

            } else last = gub;
        }

        if (user.getUnreadMessages().isEmpty()) {
            return true;
        }
        int m = 0;
        for (PublicMessage p : user.getUnreadMessages()) {

            if (p instanceof GroupMessages) {
                m++;
                if (((GroupMessages) p).getGroup().getName().equals(g.getName())) {
                    if ((p.getTime().before(last.getTime()))) {
                        System.out.println("this will never run " + last.getMsg());

                        return true;
                    }
                }
            }
        }
        if (last != null && m == 0) {
            return true;
        }
        System.out.println("WE FOUND NOTHINGUUU ");
        return false;
    }

    /**
     * @param selectedGroup
     */
    public void setSelectedGroup(Group selectedGroup) {
        support.firePropertyChange("changeSelectedGroup", null, selectedGroup);
        this.selectedGroup = selectedGroup;

        GroupMessages lastMessage = null;
        for (PublicMessage pub : allMessage) {
            if (pub instanceof GroupMessages && ((GroupMessages) pub).getGroup().getName().equals(selectedGroup.getName())) {
                if (lastMessage != null) {
                    if (lastMessage.getTime().before(pub.getTime())) {
                        lastMessage = (GroupMessages) pub;

                    }

                } else lastMessage = (GroupMessages) pub;
            }

        }

        if (lastMessage != null) {
            for (PublicMessage u : user.getUnreadMessages()) {
                if (u instanceof GroupMessages) {
                    if (((GroupMessages) u).getGroup().getName().equals(selectedGroup.getName())) {
                        if ((u.getTime().before(lastMessage.getTime()))) {
                            user.getUnreadMessages().remove(u);
                            user.getUnreadMessages().add(lastMessage);

                            return;
                        }
                    }
                }
            }
            user.getUnreadMessages().add(lastMessage);
        }
    }
}
