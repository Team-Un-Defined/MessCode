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
    private Hashtable<String,PrivateMessage> lastMessages;

    /**
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
            client.addListener("addOfflineUser", this::addOfflineUser);
            client.addListener("kickUser",this::kickUser);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void kickUser(PropertyChangeEvent propertyChangeEvent) {
        Container packet = (Container)propertyChangeEvent.getNewValue();
        if(((User)packet.getObject()).getEmail().equals(user.getEmail()))
        {System.exit(1);}
        else {
            User u = (User)packet.getObject();
           for(int i =0; i<allUsers.size();i++)
           {
               if(allUsers.get(i).getEmail().equals(u.getEmail()))
               {
                   allUsers.remove(i);
                   break;
               }
           }
            support.firePropertyChange("removeOfflineUser",null,allUsers);
        }
    }

    /**
     * @param propertyChangeEvent
     */
    private void addOfflineUser(PropertyChangeEvent propertyChangeEvent) {
        User u = ((User) ((Container) propertyChangeEvent.getNewValue()).getObject());

        allUsers.add(u);
        support.firePropertyChange("AddOfflineUsers", null, allUsers);
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
            support.firePropertyChange("createUserResponse", null, false);
        } else {
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

        this.allMessage.addAll(allPublicMessages);

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
       
       int saved =0;
       if(selectedUser!=null && pm.getSender().getEmail().equals(selectedUser.getEmail())){
       lastMessages.put(pm.getSender().getEmail(), pm);
       this.user.addUnreadMessages(pm);
       saved =1;
       }
       if(pm.getSender().getEmail().equals(this.user.getEmail())){
       this.user.addUnreadMessages(pm);
       lastMessages.put(pm.getReceiver().getEmail(),pm);
       saved = 2;
       }
       
        
       System.out.println("///////////////////////444444///////////////////////////////////");
       user.getUnreadPMs().forEach(h-> System.out.println("[RECEIVER] "+h.getReceiver().getEmail() + "[SENDER] "+ h.getSender().getEmail() ));
       System.out.println("//////////////////////////////////////////////////////////");
       
        if (saved!=0) System.out.println("/////////////////THE PM WAS SAVED IN THE UNREAD LIST//////////////////////");
        if(saved!=2) lastMessages.put(pm.getSender().getEmail(),pm);
        
        
        support.firePropertyChange("newPM", "false", pm);
    }

    /**
     * @param propertyChangeEvent
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
                if (last.getTime().getNanos() < (pub.getTime().getNanos())) {
                    last = pub;
                }
            } else last = pub;

        }
        if(lastMessages.get(u.getEmail())!=null)
        last = lastMessages.get(u.getEmail());
        
         System.out.println("///////////////////////LASTTT777777777777777777777777///////////////////////////////////");
                        this.user.getUnreadPMs().forEach(h-> System.out.println("[RECEIVER] "+h.getReceiver().getEmail() + "[SENDER] "+ h.getSender().getEmail() +"[MESSAGE]  "+h.getMsg()+"[TIME]: " + h.getTime()));
                        System.out.println("//////////////////////"+last.getMsg() + "[TIME] "+last.getTime() +"    "+last.getReceiver().getEmail()+"   "+ last.getSender().getEmail()   +"////////////////////////////////////");
                        
        System.out.println("THE LAST MESSAAGE IS : " + last.getMsg() + "[OTHER PARTY    ]" + u.getEmail()  );
        int m = 0;
        for (PrivateMessage p : this.user.getUnreadPMs()) {
          String pSender = p.getSender().getEmail();
          String pReceiver = p.getReceiver().getEmail();
          String lastSender = last.getSender().getEmail();
          String lastReceiver = last.getReceiver().getEmail();
                System.out.println("LAST SENDER: "+lastReceiver + "NOTED SENDER:  "+ pReceiver + " IS "+(pSender.equals(lastSender) && pReceiver.equals(lastReceiver)));
                System.out.println("LAST SENDER: " + lastSender +"NOTED RCEIVER: " + pReceiver + " IS  " +(pSender.equals(lastReceiver) && pReceiver.equals(lastSender)));
                if ((pSender.equals(lastSender) && pReceiver.equals(lastReceiver)) || (pSender.equals(lastReceiver) && pReceiver.equals(lastSender)) ){ m++;
                  
                    System.out.println(p.getTime() + "  "+ last.getTime() +"  IS  "+ (p.getTime().before(last.getTime())));
              
                    if (p.getTime().getNanos()< last.getTime().getNanos()) {

                       
                        
                        System.out.println("///////////////////////LASTTT11122222221///////////////////////////////////");
                        this.user.getUnreadPMs().forEach(h-> System.out.println("[RECEIVER] "+h.getReceiver().getEmail() + "[SENDER] "+ h.getSender().getEmail() +"[MESSAGE]  "+h.getMsg()+"[TIME]: " + h.getTime()));
                        System.out.println("//////////////////////"+last.getMsg() + "[TIME] "+last.getTime() +"    "+last.getReceiver().getEmail()+"   "+ last.getSender().getEmail()   +"////////////////////////////////////");
                        
                         return true;

                    }
                    }

                 
                
           
        }
        if (last != null && m == 0) {
              System.out.println("///////////////////////LASTTT11122222221///////////////////////////////////");
            user.getUnreadPMs().forEach(h-> System.out.println("[RECEIVER] "+h.getReceiver().getEmail() + "[SENDER] "+ h.getSender().getEmail() +"[MESSAGE]  "+h.getMsg()+"[TIME]: " + h.getTime()));
           System.out.println("//////////////////////"+last.getMsg() + "[TIME] "+last.getTime() +"    "+last.getReceiver().getEmail()+"   "+ last.getSender().getEmail()   +"////////////////////////////////////");

            return true;
        }
         System.out.println("///////////////////////THIS IS FALSE///////////////////////////////////");
        return false;
    }

    /**
     * @param us
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
//        System.out.println("**************************************"+lastMessage.getMsg());
        if (lastMessage != null) {
       this.user.addUnreadMessages(lastMessage);
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
     *
     */
    @Override
    public void saveDataOnExit() {
        client.saveDataOnExit(user);
    }

    /**
     * @param selectedGroup
     */
    public void setSelectedGroup(Group selectedGroup) {

        System.out.println("/////////////////////PPPPPPPPPPPPPPPPPPPP//////////////////////");
        if (!user.getUnreadGMs().isEmpty())
            user.getUnreadGMs().forEach(g -> System.out.println("[GROUP]  " + g.getGroup().getName() + "[MESSAGE]  " + g.getMsg() + "   TIME:" + g.getTime()));
        System.out.println("//////////////////////////////////////////////////////////////");

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
