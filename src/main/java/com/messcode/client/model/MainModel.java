package com.messcode.client.model;

import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.GroupMessages;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.util.Subject;
import com.messcode.transferobjects.messages.PublicMessage;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;

public interface MainModel extends Subject {

    void sendPublic(PublicMessage mess);

    void addUser(String email, String pwd);

    void sendListOfPmRoomUsers(PrivateMessage PMmessage);

    void sendPM(PrivateMessage message);

    void sendGroup(GroupMessages mess);

    public void register(String firstName, String lastName, String email, String password,String type);
   
    public ArrayList<PrivateMessage> loadPMs(User receiver);
    
    public ArrayList<PublicMessage> loadPublics();
   
   public void newGroup(Group g);

    public ArrayList<GroupMessages> loadGroup(Group selectedGroup);

    void changePassword(String current,String password, String passwordConfirmed);
  
    public void setSelectedGroup(Group selectedGroup);

    User getCurrentUser();

    void deleteUser(User use);

    void storePrivateKeyPass(String privateKeyPass);
}
