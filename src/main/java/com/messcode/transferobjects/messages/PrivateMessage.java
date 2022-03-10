package com.messcode.transferobjects.messages;

import com.messcode.transferobjects.UsersPM;
import com.messcode.transferobjects.User;

import java.io.Serializable;

public class PrivateMessage implements Serializable
{
  private String msg;
  private User user;
  private UsersPM usersPM;

  public PrivateMessage(User user, String msg)
  {
    this.msg = msg;
    this.user = user;
  }

  public PrivateMessage(User user, UsersPM usersPM, String msg)
  {
    this.user = user;
    this.usersPM = usersPM;
    this.msg = msg;
  }

  public String getMsg()
  {
    return msg;
  }

  public String getUsername()
  {
    return user.getUsername();
  }

  public User getUserOne()
  {
    return usersPM.getSender();
  }

  public User getUserTwo()
  {
    return usersPM.getReceiver();
  }

  public User getUser()
  {
    return user;
  }
}
