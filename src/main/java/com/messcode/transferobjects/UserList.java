package com.messcode.transferobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class UserList implements Serializable {

    private ArrayList<User> users = new ArrayList<>();

    /**
     * @param newUser
     */
    public void addUser(User newUser) {
        users.add(newUser);
    }

    /**
     * @param user
     */
    public void addList(List<User> user) {
        users.addAll(user);
    }

    /**
     * @return
     */
    public int getSize() {
        return users.size();
    }

    /**
     * @param i
     * @return
     */
    public User get(int i) {
        return users.get(i);
    }
}
