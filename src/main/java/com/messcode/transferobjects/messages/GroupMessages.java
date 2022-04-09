/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.messcode.transferobjects.messages;

import com.messcode.transferobjects.User;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Nao
 */
public class GroupMessages extends PublicMessage implements Serializable{
    private User leader; 
    private ArrayList<User> members; 
    
    public GroupMessages(User username, String message, User lead) {
        super(username, message);
        this.leader=lead;
        this.members = new ArrayList<>();
    }
    public GroupMessages(User username, String message, User lead, ArrayList<User> g) {
        super(username, message);
        this.leader=lead;
        this.members=g;
    }
    
    public void addMember(User u){
        members.add(u);
    
    }
    public void removeMember(User u){
        members.remove(u);
    
    }

    public void setLeader(User leader) {
        this.leader = leader;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }

    public User getLeader() {
        return leader;
    }

    public ArrayList<User> getMembers() {
        return members;
    }
   
    
}
