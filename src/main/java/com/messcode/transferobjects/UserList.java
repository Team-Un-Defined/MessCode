package com.messcode.transferobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The UserList is a list of users.
 * @author Olivér Izsák
 */
public class UserList implements Serializable {

    private ArrayList<User> users = new ArrayList<>();

    /**
     * Adds a new user to the list
     * @param newUser User to add
     */
    public void addUser(User newUser) {
        users.add(newUser);
    }

    /**
     * Adds a list of users to the main list
     * @param user List of Users to add
     */
    public void addList(List<User> user) {
        users.addAll(user);
    }

    /**
     * Returns the size of the list
     * @return int size
     */
    public int getSize() {
        return users.size();
    }

    /**
     * Returns the i-th User
     * @param i i-th User to get
     * @return User
     */
    public User get(int i) {
        return users.get(i);
    }
}
