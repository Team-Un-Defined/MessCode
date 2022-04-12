package com.messcode.client.model;

import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.util.Subject;
import com.messcode.transferobjects.messages.PublicMessage;

import java.beans.PropertyChangeEvent;

public interface MainModel extends Subject {

    void sendPublic(PublicMessage mess);

    void addUser(String email, String pwd);

    void sendListOfPmRoomUsers(PrivateMessage PMmessage);

    void sendPM(PrivateMessage message);

    public void register(String firstName, String lastName, String email, String password);

}
