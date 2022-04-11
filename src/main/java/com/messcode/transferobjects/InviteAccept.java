package com.messcode.transferobjects;

import java.io.Serializable;

public class InviteAccept implements Serializable {

    private User user;

    public InviteAccept(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof InviteAccept))
            return false;
        InviteAccept other = (InviteAccept) obj;
        return user.equals(other.user);
    }
}
