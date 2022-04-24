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
    private User encrypted_for;

    public PrivateMessage(User user, User receiver, User encrypted_for, String msg) {
        super(user, msg);
        this.receiver = receiver;
        this.encrypted_for = encrypted_for;
    }
    public PrivateMessage(User user, User receiver, User encrypted_for, String msg, Timestamp time) {
        super(user, msg, time);
        this.receiver = receiver;
        this.encrypted_for = encrypted_for;
    }

    public void encryptMessage(byte[] key) {
        String originalMessage = super.getMsg();

        MessageEncryptionManager myMessageEncryptionManager = new MessageEncryptionManager();
        byte[] encryptedMessage = myMessageEncryptionManager.asymmetricDataEncryption(originalMessage.getBytes(), key);

        super.setMsg(Arrays.toString(encryptedMessage));
    }

    public void decryptMessage(byte[] key) {
        String encryptedMessage = super.getMsg();

        MessageEncryptionManager myMessageEncryptionManager = new MessageEncryptionManager();
        byte[] decryptedMessage = myMessageEncryptionManager.asymmetricDataEncryption(encryptedMessage.getBytes(), key);

        super.setMsg(Arrays.toString(decryptedMessage));
    }

    public User getReceiver() {
        return this.receiver;
    }

    public User setReceiver() {
        return this.receiver;
    }

    public User getEncrypted_for() { return  this.encrypted_for; }
}
