/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.messcode.transferobjects.messages;

import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.MessageEncryptionManager;
import com.messcode.transferobjects.User;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * @author Nao
 */
public class GroupMessages extends PublicMessage implements Serializable {

    private Group group;
    private User encryptedFor;
    private byte[] encryptedMessage;

    public GroupMessages(User username, User encryptedFor, byte[] encryptedMessage, Group group) {
        super(username, null);
        this.group = group;
        this.encryptedFor = encryptedFor;
        this.encryptedMessage = encryptedMessage;
    }

    public GroupMessages(User username, User encryptedFor, byte[] encryptedMessage, Group group,Timestamp time) {
        super(username, null, time);
        this.group = group;
        this.encryptedFor = encryptedFor;
        this.encryptedMessage = encryptedMessage;
    }

    public void decryptMessage(byte[] key) {
        MessageEncryptionManager myMessageEncryptionManager = new MessageEncryptionManager();
        byte[] decryptedMessage = myMessageEncryptionManager.asymmetricDataDecryption(encryptedMessage, key);

        super.setMsg(new String(decryptedMessage));
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getEncryptedFor() { return  this.encryptedFor; }

    public byte[] getEncryptedMessage() { return this.encryptedMessage; }
    
}
