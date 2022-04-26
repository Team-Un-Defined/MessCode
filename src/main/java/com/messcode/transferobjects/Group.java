/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.messcode.transferobjects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Each project has its own group.
 * This class holds the project's information and list of its members
 * @author Noémi Farkas
 */
public class Group implements Serializable {

    private String name;
    private String description;
    private User leader;
    private ArrayList<User> members;

    /**
     * Creates group for project.
     *
     * @param name project name
     * @param description additional information about project
     * @param leader member that is responsible for this project
     */
    public Group(String name, String description, User leader) {
        this.name = name;
        this.description = description;
        this.leader = leader;
        this.members = new ArrayList<User>();
        members.add(leader);
    }

    /**
     * @param u new group member
     */
    public void addMember(User u) {
        members.add(u);
    }

    /**
     * @param u list of group members
     */
    public void addMembers(ArrayList<User> u) {
        members.addAll(u);
    }

    /**
     * @return project name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name project name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return additional information about project
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description additional information about project
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return member that is responsible for this project
     */
    public User getLeader() {
        return leader;
    }

    /**
     * @param leader member that is responsible for this project
     */
    public void setLeader(User leader) {
        this.leader = leader;
    }

    /**
     * @return list of group members
     */
    public ArrayList<User> getMembers() {
        return members;
    }

    /**
     * @param members list of group members
     */
    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    /**
     * @param members list of group members to be removed
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
     * Check if user is part of a group.
     *
     * @param user user to be checked
     * @return true if user is group member
     */
    public boolean isMember(User user) {
        for (User u: members) {
            if (u.getEmail().equals(user.getEmail()))
                return true;
        }
        return false;
    }
}
