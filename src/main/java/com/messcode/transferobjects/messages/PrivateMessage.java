package com.messcode.transferobjects.messages;

import com.messcode.transferobjects.User;

import java.io.Serializable;
import java.sql.Timestamp;

public class PrivateMessage extends PublicMessage implements Serializable {

    private User receiver;

    public PrivateMessage(User user, User receiver, String msg) {
        super(user, msg);
        this.receiver = receiver;
    }
    public PrivateMessage(User user, User receiver, String msg,Timestamp time) {
        super(user, msg, time);
        this.receiver = receiver;
    }
    public PrivateMessage(User user, String msg) {
        super(user, msg);
    }

    public User getReceiver() {
        return this.receiver;
    }

    public User setReceiver() {
        return this.receiver;
    }
}
