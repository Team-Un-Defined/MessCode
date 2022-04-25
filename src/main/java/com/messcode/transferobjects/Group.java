/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.messcode.transferobjects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Nao
 */
public class Group implements Serializable {

    private String name;
    private String description;
    private User leader;
    private ArrayList<User> members;

    /**
     * @param name
     * @param description
     * @param leader
     */
    public Group(String name, String description, User leader) {
        this.name = name;
        this.description = description;
        this.leader = leader;
        this.members = new ArrayList<User>();
        members.add(leader);
    }

    /**
     * @param u
     */
    public void addMember(User u) {
        members.add(u);
    }

    /**
     * @param u
     */
    public void addMembers(ArrayList<User> u) {
        members.addAll(u);
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return
     */
    public User getLeader() {
        return leader;
    }

    /**
     * @param leader
     */
    public void setLeader(User leader) {
        this.leader = leader;
    }

    /**
     * @return
     */
    public ArrayList<User> getMembers() {
        return members;
    }

    /**
     * @param members
     */
    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    /**
     * @param members
     */
    public void removeMembers(ArrayList<User> members) {
        ArrayList<User> help = new ArrayList<User>();
        help.addAll(this.members);

        help.forEach(u -> {
            members.stream().filter(us -> (u.getEmail().equals(us.getEmail()))).forEachOrdered(_item -> {
                this.members.remove(u);
            });
        });
    }

    /**
     * @param user
     * @return
     */
    public boolean isMember(User user) {
        for (User u: members) {
            if (u.getEmail().equals(user.getEmail()))
                return true;
        }
        return false;
    }
}
