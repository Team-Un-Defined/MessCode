package com.messcode.transferobjects.messages;

import com.messcode.transferobjects.User;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * These messages are sent between 2 communicating users.
 * Therefore, messages have one sender and one receiver.
 */
public class PrivateMessage extends PublicMessage implements Serializable {

    private User receiver;

    /**
     * This constructor is when sending new private message.
     * Timestamp is set automatically.
     *
     * @param user user who sent this message
     * @param receiver user who received this message
     * @param msg message that was sent
     */
    public PrivateMessage(User user, User receiver, String msg) {
        super(user, msg);
        this.receiver = receiver;
    }

    /**
     * This constructor is used when taking private message from database.
     * Timestamp should be provided in parameters.
     *
     * @param user user who sent this message
     * @param receiver user who received this message
     * @param msg message that was sent
     * @param time time at which the message was sent
     */
    public PrivateMessage(User user, User receiver, String msg, Timestamp time) {
        super(user, msg, time);
        this.receiver = receiver;
    }

    /**
     * @return user who received this message
     */
    public User getReceiver() {
        return this.receiver;
    }

    /**
     * @param receiver user who received this message
     */
    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }
}
