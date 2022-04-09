package com.messcode.transferobjects.messages;

import com.messcode.transferobjects.User;

import java.io.Serializable;
import java.sql.Timestamp;

public class PublicMessage implements Serializable
{ 
  private User user;
  private String msg;
  private Timestamp time;
  
  
  public PublicMessage(User username, String message)
  { this.time=new Timestamp(System.currentTimeMillis());
    this.user = username;
    this.msg = message;
  }

  public User getSender()
  {
    return user;
  }

  public String getUsername()
  {
    return user.getUsername();
  }
    public void setUser(User user) {
        this.user = user;
    }
    public String getMsg()
  {
    return msg;
  }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
    
  
}
