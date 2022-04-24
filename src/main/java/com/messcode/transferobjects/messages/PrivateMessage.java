package com.messcode.transferobjects.messages;

import com.messcode.transferobjects.MessageEncryptionManager;
import com.messcode.transferobjects.User;
import org.apache.logging.log4j.message.Message;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Arrays;

public class PrivateMessage extends PublicMessage implements Serializable {
    private User receiver;
    private User encryptedFor;
    private byte[] encryptedMessage;

    public PrivateMessage(User user, User receiver, User encrypted_for, byte[] encryptedMessage) {
        super(user, null);
        this.receiver = receiver;
        this.encryptedFor = encrypted_for;
        this.encryptedMessage = encryptedMessage;
    }
    public PrivateMessage(User user, User receiver, User encryptedFor, byte[] encryptedMessage, Timestamp time) {
        super(user, null, time);
        this.receiver = receiver;
        this.encryptedFor = encryptedFor;
        this.encryptedMessage = encryptedMessage;
    }

    public void decryptMessage(byte[] key) {
        MessageEncryptionManager myMessageEncryptionManager = new MessageEncryptionManager();
        byte[] decryptedMessage = myMessageEncryptionManager.asymmetricDataDecryption(encryptedMessage, key);

        super.setMsg(new String(decryptedMessage));
    }

    public User getReceiver() {
        return this.receiver;
    }

    public User setReceiver() {
        return this.receiver;
    }

    public User getEncryptedFor() { return  this.encryptedFor; }

    public byte[] getEncryptedMessage() { return this.encryptedMessage; }
}
