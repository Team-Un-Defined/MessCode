/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.messcode.transferobjects.messages;

import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.User;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * @author Nao
 */
public class GroupMessages extends PublicMessage implements Serializable {

    private Group group;

    public GroupMessages(User username,String message, Group group) {
        super(username, message);
        this.group = group;
    }
     public GroupMessages(User username,String message, Group group,Timestamp time) {
        super(username, message,time);
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
    
}
