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
import java.util.Hashtable;
import javafx.application.Platform;

/**
 *This is where the main bussiness logic happens.
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
    private Hashtable<String,PrivateMessage> lastMessages;

    /**
     * This constructor initializes all the Users,Groups,Messages and the current user and also adds listener for the client.
     * @param client
     */
    public MainModelManager(Client client) {
        support = new PropertyChangeSupport(this);
        allMessage = new ArrayList<>();
        allUsers = new ArrayList<>();
        this.client = client;
        lastMessages = new Hashtable<>();
        try {
            client.start();
            client.addListener("newGroupMessage", this::receiveGroup);
            client.addListener("RefresgGroups", this::refreshGroupList);
            client.addListener("AddNewUser", this::modifyAllUsersList);
            client.addListener("MessageForEveryone", this::receivePublic);
            client.addListener("newPM", this::receivePM);
            client.addListener("UserLeave", this::userLeft);
            client.addListener("LoginResponse", this::loginResponse);
            client.addListener("LoginData", this::loginData);
            client.addListener("createUserResponse", this::createAccount);
            client.addListener("passChangeResponse", this::passChangeResponse);
            client.addListener("userDeleted", this::modifyAllUsersList);
            client.addListener("AddAllGroupMessages", this::addAllGroupMessages);
            client.addListener("addOfflineUser", this::addOfflineUser);
            client.addListener("kickUser",this::kickUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void userLeft(PropertyChangeEvent propertyChangeEvent){
    User u = (User) propertyChangeEvent.getNewValue();
      u.setSalt("");
       

            for (int i = 0; i < allUsers.size(); i++) {
                if (allUsers.get(i).getEmail().equals(u.getEmail())) {
                    allUsers.set(i, u);
                    break;
                }
            }
            
       support.firePropertyChange("ReloadUsers",null,allUsers);
    }
    
    
    /**
     * This method is responsible for removing out a user from the allUsers list and sending out an event for the listeners with the deleted user.
     * @param propertyChangeEvent PropertyChangeEvent object
     */
    private void kickUser(PropertyChangeEvent propertyChangeEvent) {
        System.out.println("Step one goes");
        Container packet = (Container)propertyChangeEvent.getNewValue();
        if(((User)packet.getObject()).getEmail().equals(user.getEmail()))
        {System.exit(1);}
        else {
            System.out.println("Step two goes");
            User u = (User)packet.getObject();
           for(int i =0; i<allUsers.size();i++)
           {
               if(allUsers.get(i).getEmail().equals(u.getEmail()))
               {
                   allUsers.set(i,u);
                   break;
               }
           }
            support.firePropertyChange("ReloadUsers",null,allUsers);
        }
    }

    /**
     * This method is responsible for adding a new offline user to the list when the user is createad, and sending a event to the listeners with the user in it.
     * @param propertyChangeEvent
     */
    private void addOfflineUser(PropertyChangeEvent propertyChangeEvent) {
        User u = ((User) ((Container) propertyChangeEvent.getNewValue()).getObject());
        
        allUsers.add(u);
        support.firePropertyChange("ReloadUsers", null, allUsers);
    }



    /**
     * This method is responsible for getting the current user.
     * @return User object
     */
    @Override
    public User getCurrentUser() {
        return user;
    }

    /**
     * This method is responsible for calling the deleteUser on the client.
     * @param use User object
     */
    @Override
    public void deleteUser(User use) {
        client.deleteUser(use);
    }

    /**
     * This method is responsible for adding all the group messages for a new user and sending an event to the listeners with a boolean in it.
     * @param propertyChangeEvent PropertyChangeEvent object
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
     * This method is responsible for sending the user object that was deleted to the listeners to update the views
     * @param propertyChangeEvent PropertyChangeEvent object
     */
   

    /**
     * This method is responsible for sending a packet containing a boolean to the views
     * @param propertyChangeEvent PropertyChangeEvent object
     */
    private void passChangeResponse(PropertyChangeEvent propertyChangeEvent) {
        Container packet = ((Container) propertyChangeEvent.getNewValue());
        support.firePropertyChange("passChangeResponse", null, ((boolean) packet.getObject()));
    }

    /**
     * This method sends an event to the listener if the arguments is a null User object or 2 different events if its a user object and its not null.
     * In the first case it means the account creation failed. In the second case it means it worked, and the gui needs to be updated with the new user.
     * @param propertyChangeEvent PropertyChangeEvent object
     */
    private void createAccount(PropertyChangeEvent propertyChangeEvent) {
        if (((User) ((Container) propertyChangeEvent.getNewValue()).getObject()) == null) {
            support.firePropertyChange("createUserResponse", null, false);
        } else {
            User u = ((User) ((Container) propertyChangeEvent.getNewValue()).getObject());
            support.firePropertyChange("createUserResponse", null, true);
            allUsers.add(u);
            support.firePropertyChange("ReloadUsers", null, allUsers);
        }
    }

    /**
     * This method is responsible for distributing all the different data, such as User, UserList, all types of messages and last seen messages to the user by
     * sending events to the listeners
     * @param propertyChangeEvent PropertyChangeEvent object
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
         

        }

        support.firePropertyChange("ReloadUsers", null, allUsers);
        support.firePropertyChange("RefresgGroups", null, allGroups);
        System.out.println("Everything has been casted");

        //user.getLastSeen.add(lastSeen);

        this.allMessage.addAll(allPublicMessages);

        support.firePropertyChange("LoginData", null, allMessage);  // probably lot more stuff should happen here and vm, but rn this is okay.
        System.out.println(user.getEmail() + " " + user.getName());
        support.firePropertyChange("SetUsernameInChat", null, user);
    }

    /**
     * This method is responsible for sending a boolean to the gui which means whether the login was successful or not
     * @param propertyChangeEvent PropertyChangeEvent object
     */
    private void loginResponse(PropertyChangeEvent propertyChangeEvent) {
        boolean answer = (boolean) propertyChangeEvent.getNewValue();
        System.out.println("in model: " + answer);
        support.firePropertyChange("LoginResponseToVM", null, answer);
    }

    /**
     * This method is responsible for adding a new public message to the list of messages and firing an event to the listeners with the message in it.
     * @param propertyChangeEvent PropertyChangeEvent object
     */
    public void receivePublic(PropertyChangeEvent propertyChangeEvent) {
        PublicMessage publicMessage = (PublicMessage) propertyChangeEvent.getNewValue();
        this.allMessage.add(publicMessage);
        System.out.println("got to model");
        support.firePropertyChange("MessageForEveryone", null, publicMessage);
    }

    /**
     * This method is responsible for filtering the recieved private message to decide how the GUI should react to it, meanwhile firing events to the listeners
     * @param propertyChangeEvent PropertyChangeEvent object
     */
    public void receivePM(PropertyChangeEvent propertyChangeEvent) {
        PrivateMessage pm = (PrivateMessage) propertyChangeEvent.getNewValue();
       this.allMessage.add(pm);
       
       int saved =0;
       if(selectedUser!=null && pm.getSender().getEmail().equals(selectedUser.getEmail())){
       this.user.addUnreadMessages(pm);
       System.out.println("IT IS IN THE UNREAD " + pm.getMsg());
       saved =1;
       }
       if(pm.getSender().getEmail().equals(this.user.getEmail())){
       this.user.addUnreadMessages(pm);
        System.out.println("IT IS IN THE UNREAD " + pm.getMsg());
       saved = 2;
       }
       
        
     
       lastMessages.put(pm.getSender().getEmail(),pm);
        
        
        support.firePropertyChange("newPM", "false", pm);
    }

    /**
     * This method is responsible for filtering the recieved group message to decide how the GUI should react to it, meanwhile firing events to the listeners
     * @param propertyChangeEvent PropertyChangeEvent object
     */
    private void receiveGroup(PropertyChangeEvent propertyChangeEvent) {
        GroupMessages gm = (GroupMessages) propertyChangeEvent.getNewValue();
        this.allMessage.add(gm);
        int i=0;
        if ((selectedGroup != null && gm.getGroup().getName().equals(selectedGroup.getName())) || gm.getSender().getEmail().equals(user.getEmail())) {
            for (PublicMessage g : user.getUnreadMessages()) {
                if (g instanceof GroupMessages) {
                    if (((GroupMessages) g).getGroup().getName().equals(selectedGroup.getName())) {
                        user.getUnreadMessages().set(i, gm);


                        support.firePropertyChange("newGroupMessage", null, gm);
                        return;
                    }
                }
                i++;
            }
            
        }
        support.firePropertyChange("newGroupMessage", null, gm);
    }

    /**
     * This method is responsible for firing an event to the listeners containing the newly added user
     * @param propertyChangeEvent PropertyChangeEvent object
     */
    public void modifyAllUsersList(PropertyChangeEvent propertyChangeEvent) {
        User user = (User) propertyChangeEvent.getNewValue();
        if(user==null){
            System.out.println("OLI FUCKED UP THE RETURN");
            return;
        }
       
            if (user.getSalt().equals(" - deleted")) {
                for (int i = 0; i < allUsers.size(); i++) {
                    if (allUsers.get(i).getEmail().equals(user.getEmail())) {
                        allUsers.set(i, user);
                        break;
                    }
                }
            } else {
                user.setSalt(" - online");
                for (int i = 0; i < allUsers.size(); i++) {
                    if (allUsers.get(i).getEmail().equals(user.getEmail())) {
                
                            if ((!allUsers.get(i).getSalt().equals(" - online"))) {
                                User temp = allUsers.get(i);
                                allUsers.set(i, user);
                                if (!temp.getEmail().equals(user.getEmail())) {
                                    allUsers.add(temp);
                                }
                                break;
                        }
                        break;
                    }
                }
            }
             support.firePropertyChange("ReloadUsers", null, allUsers);
       
       
       
    }

    /**
     * This method calls the addUser method on the client
     * @param email String object
     * @param pwd String object
     */
    @Override
    public void addUser(String email, String pwd) {
        User user = new User(email, pwd);
        client.addUser(user);
    }

    /**
     * This method is responsible for adding the PropertyChangeListener
     * @param eventName
     * @param listener
     */
    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    /**
     * This method is responsible for removing the PropertyChangeListener
     * @param eventName
     * @param listener
     */
    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }

    /**
     * This method is responsible for calling the sendPublic method on the client
     * @param mess PublicMessage object
     */
    @Override
    public void sendPublic(PublicMessage mess) {
        client.sendPublic(mess);
    }

    /**
     * This method is responsible for calling the sendPM method on the client
     * @param message PrivateMessage object
     */
    @Override
    public void sendPM(PrivateMessage message) {
        client.sendPM(message);
    }

    /**
     * This method is responsible for calling the sendGroup method on the client
     * @param mess GroupMessage object
     */
    @Override
    public void sendGroup(GroupMessages mess) {
        client.sendGroup(mess);
    }




    /**
     * This method is responsible for calling the register method on the client
     * @param firstName String object
     * @param lastName  String object
     * @param email  String object
     * @param password  String object
     * @param type  String object
     */
    @Override
    public void register(String firstName, String lastName, String email, String password, String type) {
        User newUser = new User(firstName, lastName, email, password, type);
        client.register(newUser);
    }

    /**
     * This method is responsible for loading and selecting all the Private Messages associated with a user from the all messages list
     * @param receiver User  object
     * @return ArrayList<PrivateMessage></PrivateMessage>
     */
    @Override
    public ArrayList<PrivateMessage> loadPMs(User receiver) {

        ArrayList<PrivateMessage> pivi = new ArrayList<PrivateMessage>();
        for (PublicMessage p : this.allMessage) {
            
            if (p instanceof PrivateMessage && (((PrivateMessage) p).getReceiver().getEmail().equals(receiver.getEmail()) || ((PrivateMessage) p).getSender().getEmail().equals(receiver.getEmail()))) {
                pivi.add(((PrivateMessage) p));
                //System.out.println("KUKURIKUUUUUUUUU   "+p.getMsg());
            }
        }
        return pivi;
    }

    /**
     * This method is responsible for loading and selecting all the Public Messages from the all messages list
     * @return  ArrayList<PublicMessage>
     */
    @Override
    public ArrayList<PublicMessage> loadPublics() {
        ArrayList<PublicMessage> pubi = new ArrayList<>();
        PublicMessage puu = new PublicMessage(user, "dasd");
        for (PublicMessage p : this.allMessage) {
            if (p.getClass().equals(puu.getClass())) {
                pubi.add(p);
            }
        }
        return pubi;
    }

    /**
     * This method is responsible for loading and selecting all the Group Messages associated with the group from the all messages list
     * @param selectedGroup Group object
     * @return ArrayList<GroupMessages>
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
     * This method is responsible for calling the changePassword method on the client
     * @param current String object
     * @param password String object
     * @param passwordConfirmed String object
     */
    @Override
    public void changePassword(String current, String password, String passwordConfirmed) {
        User u = new User(user.getEmail(), current);
        u.setPassword(passwordConfirmed);
        System.out.println("salty: " + u.getSalt());
        client.changePassword(u);
    }

    /**
     * This method is responsible for calling the newGroup method on the client if its not already in the group list
     * @param g
     */
    @Override
    public boolean newGroup(Group g) {
      for(Group gr :this.allGroups){
      if(gr.getName().equals(g.getName())){
      return false;
      }
      
      }
        
        client.newGroup(g);
        return true;
    }

    /**
     * This method is responsible for refereshing the group lists and firing an event for the listeners.
     * @param propertyChangeEvent PropertyChangeEvent object
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
     * This method is responsible for calling the addMember method on the client
     * @param u ArrayList<User> object
     */
    @Override
    public void addMember(ArrayList<User> u) {
        Group updatedGroup = new Group(selectedGroup.getName(), selectedGroup.getDescription(), selectedGroup.getLeader());
        updatedGroup.setMembers(selectedGroup.getMembers());
        updatedGroup.addMembers(u);
        client.addMember(updatedGroup);
    }

    /**
     * This method is responsible for calling the removeMember method on the client
     * @param u ArrayList<User> object
     */
    @Override
    public void removeMember(ArrayList<User> u) {
        Group updatedGroup = new Group(selectedGroup.getName(), selectedGroup.getDescription(), selectedGroup.getLeader());
        updatedGroup.addMembers(u);
        client.removeMember(updatedGroup);
    }

    /**
     * This method is responsible for calling the deleteGroup method on the client
     * @param g Group object
     */
    @Override
    public void deleteGroup(Group g) {
        client.deleteGroup(g);
    }

    /**
     * This method is responsible for calling the resetPassword method on the client
     * @param use User pobject
     */
    @Override
    public void resetPassword(User use) {
        client.resetPassword(use);
    }

    /**
     * This method is responsible for calling the changeLeader method on the client
     * @param g Group object
     */
    @Override
    public void changeLeader(Group g) {
        client.changeLeader(g);
    }

    /**
     * This method is responsible for updating the last read Private Messages of the user
     * @param u User object
     * @return boolean true, if there is a new unread message, false if there is no new message.
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
                if (last.getTime().getNanos() < (pub.getTime().getNanos())) {
                    last = pub;
                }
            } else last = pub;

        }
        if(lastMessages.get(u.getEmail())!=null)
        last = lastMessages.get(u.getEmail());
        
      
    
       
        return this.user.addableUnreadMessages(last);
        
    }

    /**
     * This method is responsible for setting the current selected user for the mainModelManager.
     * @param us User object
     */
    @Override
    public void setSelectedUser(User us) {
        selectedUser = us;
        ArrayList<PrivateMessage> goog = loadPMs(us);
        PrivateMessage lastMessage = null;
        for (PrivateMessage pub : goog) {
                if (lastMessage != null) {
                    if (lastMessage.getTime().getNanos()<(pub.getTime()).getNanos()) {
                        lastMessage = pub;
                    }

                } else lastMessage = pub;
        }
        
        if(lastMessages.get(us.getEmail())!=null){
        lastMessage = lastMessages.get(us.getEmail());
        }
        
        if (lastMessage != null) {
        this.user.addUnreadMessages(lastMessage);
        }
    }

    /**
     * get method
     * @return User object
     */
    public User getSelectedUser() {
        return selectedUser;
    }

    /**
     * This method is responsible for updating the last read Group Messages of the user in a specific group
     * @param g Group object
     * @return boolean true, if there is a new unread message, false if there is no new message.
     */
    @Override
    public boolean unredGMs(Group g) {
        ArrayList<GroupMessages> grps = loadGroup(g);
        GroupMessages last = null;
        if (grps.isEmpty()) {
            return false;
        }

        for (GroupMessages gub : grps) {
            if (last != null) {
                if (last.getTime().before(gub.getTime())) {
                    last = gub;
                  
                }
            } else last = gub;
        }

        int m = 0;
        for (PublicMessage p : user.getUnreadMessages()) {

            if (p instanceof GroupMessages) {

                if (g.getName().equals(((GroupMessages) p).getGroup().getName())) m++;

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
     *This method is responsible for calling the saveDataOnExit method on the client
     */
    @Override
    public void saveDataOnExit() {
        client.saveDataOnExit(user);
    }

    /**
     * This method is responsible for setting the currently selected group from the gui into the MainModelManager
     * @param selectedGroup Group object
     */
    public void setSelectedGroup(Group selectedGroup) {

       
        if (!user.getUnreadGMs().isEmpty())
           

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
                        if ((u.getTime().before(lastMessage.getTime())) || (u.getTime().equals(lastMessage.getTime()))) {
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
