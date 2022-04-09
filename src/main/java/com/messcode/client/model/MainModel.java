package com.messcode.client.model;

import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.util.Subject;
import com.messcode.transferobjects.UsersPM;
import com.messcode.transferobjects.messages.PublicMessage;

import java.beans.PropertyChangeEvent;

public interface MainModel extends Subject
{
  void sendMessage( PublicMessage mess);
  void addUser(User username);
  void receiveMessageInChat(PropertyChangeEvent propertyChangeEvent);
  void addToUsersList(PropertyChangeEvent propertyChangeEvent);
  void sendInviteToPM(User user);
  void sendListOfPmRoomUsers(UsersPM usersPM);
  void sendMessageInPmToServer(PrivateMessage message);
}
