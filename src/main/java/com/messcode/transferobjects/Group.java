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
public class Group implements Serializable{

    private String name;
    private String description;
    private User leader;
    private ArrayList<User> members;

    public Group(String name, String description, User leader) {
        this.name = name;
        this.description = description;
        this.leader = leader;
        this.members = new ArrayList<User>();
        members.add(leader);
    }

    public void addMember(User u) {
        members.add(u);
    }
 public void addMembers(ArrayList<User> u) {
        members.addAll(u);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getLeader() {
        return leader;
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    public ArrayList<User> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }
}
