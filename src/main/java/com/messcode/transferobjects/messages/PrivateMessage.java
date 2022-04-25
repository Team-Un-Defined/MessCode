package com.messcode.transferobjects.messages;

import com.messcode.transferobjects.User;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 */
public class PrivateMessage extends PublicMessage implements Serializable {

    private User receiver;

    /**
     * @param user
     * @param receiver
     * @param msg
     */
    public PrivateMessage(User user, User receiver, String msg) {
        super(user, msg);
        this.receiver = receiver;
    }

    /**
     * @param user
     * @param receiver
     * @param msg
     * @param time
     */
    public PrivateMessage(User user, User receiver, String msg,Timestamp time) {
        super(user, msg, time);
        this.receiver = receiver;
    }

    /**
     * @param user
     * @param msg
     */
    public PrivateMessage(User user, String msg) {
        super(user, msg);
    }

    /**
     * @return
     */
    public User getReceiver() {
        return this.receiver;
    }

    /**
     * @return
     */
    public User setReceiver() {
        return this.receiver;
    }
}
