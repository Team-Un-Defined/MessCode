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
 * These messages are sent to group chats.
 * Therefore, group to which this message belongs to must be provided.
 */
public class GroupMessages extends PublicMessage implements Serializable {

    private Group group;

    /**
     * This constructor is when sending new group message.
     * Timestamp is set automatically.
     *
     * @param username group member who sent this message
     * @param message message that was sent
     * @param group group to which this message belongs to
     */
    public GroupMessages(User username, String message, Group group) {
        super(username, message);
        this.group = group;
    }

    /**
     * This constructor is used when taking group message from database.
     * Timestamp should be provided in parameters.
     *
     * @param username group member who sent this message
     * @param message message that was sent
     * @param group group to which this message belongs to
     * @param time time at which this message was sent
     */
    public GroupMessages(User username, String message, Group group, Timestamp time) {
        super(username, message, time);
        this.group = group;
    }

    /**
     * @return group to which this message belongs to
     */
    public Group getGroup() {
        return group;
    }

    /**
     * @param group group to which this message belongs to
     */
    public void setGroup(Group group) {
        this.group = group;
    }
}
