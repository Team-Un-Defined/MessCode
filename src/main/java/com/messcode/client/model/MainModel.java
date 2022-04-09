package com.messcode.client.model;

import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.util.Subject;
import com.messcode.transferobjects.messages.PublicMessage;

import java.beans.PropertyChangeEvent;

public interface MainModel extends Subject
{
  void sendPublic( PublicMessage mess);
  void addUser(User username);
  void receivePublic(PropertyChangeEvent propertyChangeEvent);
  void addToUsersList(PropertyChangeEvent propertyChangeEvent);
  void sendListOfPmRoomUsers(PrivateMessage PMmessage);
  void sendPM(PrivateMessage message);
  void receivePM(PropertyChangeEvent propertyChangeEvent);
}
