package com.messcode.transferobjects.messages;

import com.messcode.transferobjects.User;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * These messages are sent to public chat
 * Therefore, messages only have one sender.
 */
public class PublicMessage implements Serializable {

    private User user;
    private String msg;
    private Timestamp time;

    /**
     * This constructor is when sending new public message.
     * Timestamp is set automatically.
     *
     * @param username user who sent this message
     * @param message message that was sent
     */
    public PublicMessage(User username, String message) {
        this.time = new Timestamp(System.currentTimeMillis());
        this.user = username;
        this.msg = message;
    }

    /**
     * This constructor is when sending new public message.
     * Timestamp is set automatically.
     *
     * @param username user who sent this message
     * @param message message that was sent
     * @param time time at which the message was sent
     */
    public PublicMessage(User username, String message, Timestamp time) {
        this.time = time;
        this.user = username;
        this.msg = message;
    }

    /**
     * @return user who sent this message
     */
    public User getSender() {
        return user;
    }

    /**
     * @return name of user who sent this message
     */
    public String getUsername() {
        return user.getUsername();
    }

    /**
     * @param user user who sent this message
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return message that was sent
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @return time at which the message was sent
     */
    public Timestamp getTime() {
        return time;
    }

    /**
     * @param time time at which the message was sent
     */
    public void setTime(Timestamp time) {
        this.time = time;
    }
}
