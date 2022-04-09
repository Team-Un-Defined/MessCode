package com.messcode.transferobjects.messages;

import com.messcode.transferobjects.UsersPM;
import com.messcode.transferobjects.User;

import java.io.Serializable;

public class PrivateMessage extends PublicMessage implements Serializable 
{
  private String User;
  private UsersPM usersPM;

  public PrivateMessage(User username, String message)
  {
    super(username, message);
  }
 
  public PrivateMessage(User user, UsersPM usersPM, String msg)
  {
    super(user, msg);
    this.usersPM = usersPM;
    
  }

  public User getUserOne()
  {
    return usersPM.getSender();
  }

  public User getUserTwo()
  {
    return usersPM.getReceiver();
  }


}
