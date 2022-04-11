package com.messcode.client.networking;

import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;
import com.messcode.transferobjects.util.Subject;

import java.io.IOException;

public interface Client extends Subject
{
  void start() throws IOException;
  void displayMessage(PublicMessage message);
  void addUser(User username);
  void sendPublic(PublicMessage um);
  void sendPM(PrivateMessage pm);


}
