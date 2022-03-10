package com.messcode.client.networking;

import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;
import com.messcode.transferobjects.util.Subject;
import com.messcode.transferobjects.UsersPM;

import java.io.IOException;

public interface Client extends Subject
{
  void start() throws IOException;
  void displayMessage(PublicMessage message);
  void addUser(User username);
  void sendMessage(PublicMessage um);
  void invitePmToServer(UsersPM usersPM);
  void sendMessageInPMToServer(PrivateMessage message);

}
