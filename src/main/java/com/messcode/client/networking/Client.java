package com.messcode.client.networking;

import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.GroupMessages;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;
import com.messcode.transferobjects.util.Subject;

import java.io.IOException;
import java.util.ArrayList;

public interface Client extends Subject {

    void start() throws IOException;

    void displayMessage(PublicMessage message);

    void addUser(User username);

    void sendPublic(PublicMessage um);

    void sendPM(PrivateMessage pm);

    public void register(User newUser);
    
    public void newGroup(Group g);
    
    public void refreshGroupList(ArrayList<Group> g);
    
    public void sendGroup(GroupMessages mess);

    void changePassword(User u);

    void deleteUser(User use);

    public void addMember(Group selectedGroup);

    public void removeMember(Group selectedGroup);

    public void deleteGroup(Group g);

    public void changeLeader(Group g);

    void resetPassword(User use);
}
