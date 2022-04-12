package com.messcode.transferobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserList implements Serializable {

    private ArrayList<User> users = new ArrayList<>();

  public void addUser(User newUser) {
    users.add(newUser);
  }

  public void addList(List<User> user){
    users.addAll(user);
  }

  public int getSize(){
    return users.size();
  }

    public User get(int i) {
        return users.get(i);
    }
}
