package com.messcode.client;

import JDBC.DBConn.DatabaseConnection;
import JDBC.ImportData;
import com.messcode.transferobjects.AccountManager;
import com.messcode.transferobjects.MessageEncryptionManager;
import com.messcode.transferobjects.User;
import javafx.application.Application;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.sql.*;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class Start {

    public static void main(String[] args) {
        /*
        MessageEncryptionManager myMessageEncryptionManager = new MessageEncryptionManager();

        KeyPair myKeypair = myMessageEncryptionManager.generateKeyPair();
        PrivateKey myGeneratedPrivateKey = myKeypair.getPrivate();
        PublicKey myGeneratedPublicKey = myKeypair.getPublic();

        byte[] myGeneratedPrivateKey_bytes = myGeneratedPrivateKey.getEncoded();
        byte[] myGeneratedPublicKey_bytes = myGeneratedPublicKey.getEncoded();

        String myPassword_str = "slabeheslo";
        byte[] myPassword_bytes = myPassword_str.getBytes();

        byte[] myStoredPrivateKey = myMessageEncryptionManager.symmetricDataEncryption(myGeneratedPrivateKey_bytes, myPassword_bytes);
        byte[] myStoredPublicKey = myGeneratedPublicKey_bytes;

        byte[] myRetrievedPrivateKey = myMessageEncryptionManager.symmetricDataDecryption(myStoredPrivateKey, myPassword_bytes);
        byte[] myRetrievedPublicKey = myStoredPublicKey;

        System.out.println("My generated private key: " + Arrays.toString(myGeneratedPrivateKey_bytes));
        System.out.println("My generated public key: " + Arrays.toString( myGeneratedPublicKey_bytes));
        System.out.println();
        System.out.println("My stored private key: " + Arrays.toString(myStoredPrivateKey));
        System.out.println("My stored public key: " + Arrays.toString( myStoredPublicKey));
        System.out.println();
        System.out.println("My retrieved private key: " + Arrays.toString(myRetrievedPrivateKey));
        System.out.println("My retrieved public key: " + Arrays.toString(myRetrievedPublicKey));
        System.out.println();

        String myMessage = "Je tu nejaka mila a naozaj sympaticka zena do 45 r. " +
                "ktora chce spoznat super chalana?Dialka nie je problem. " +
                "Ak sa nebojis tak mi napis mi cakam na teba.";
        byte[] sent_message = myMessageEncryptionManager.asymmetricDataEncryption(myMessage.getBytes(), myRetrievedPublicKey);
        byte[] received_message = myMessageEncryptionManager.asymmetricDataDecryption(sent_message, myRetrievedPrivateKey);
        String sent_message_str = new String(sent_message);
        String received_message_str = new String(received_message);

        System.out.println("Original message: " + myMessage);
        //System.out.println("Message was encrypted with public key: " + sent_message_str);
        System.out.println("Message was decrypted with private key: " + received_message_str);
         */

        /*
        java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.SEVERE, "sadsd");
        Application.launch(RunApp.class);
         */

        MessageEncryptionManager myMessageEncryptionManager = new MessageEncryptionManager();
        KeyPair myKeyPair = myMessageEncryptionManager.generateKeyPair();
        PrivateKey myPrivateKey = myKeyPair.getPrivate();
        PublicKey myPublicKey = myKeyPair.getPublic();

        AccountManager myAccountManager = new AccountManager();
        String password = "heslo";
        String salt = myAccountManager.generateSalt();
        byte[] encryptedPassword = myAccountManager.hashPassword(password, salt);

        System.out.println("password: " + new String(encryptedPassword));
        System.out.println("salt: " + salt);
        System.out.println("public key: " + Arrays.toString(myPublicKey.getEncoded()));
        System.out.println("private key: " + Arrays.toString(myPrivateKey.getEncoded()));

        DatabaseConnection conn = new DatabaseConnection();
        Connection c;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(conn.getConn(), conn.getName(), conn.getPass());

            //User newUser = new User("Martin", "Svab", "xsvab@stuba.sk", encryptedPassword, salt, "employee", myPrivateKey.getEncoded(), myPublicKey.getEncoded());
            //User newUser = new User("Peter", "Petruch", "peterpetruch@gmail.com", encryptedPassword, salt, "employee", myPrivateKey.getEncoded(), myPublicKey.getEncoded());
            //User newUser = new User("Jozef", "Mrkvicka", "jozefmrkvicka@gmail.com", encryptedPassword, salt, "employee", myPrivateKey.getEncoded(), myPublicKey.getEncoded());

            /*
            String query = "INSERT INTO account (id, fname, lname, pwd_hash, pwd_salt, type, email, private_key, public_key) " +
                    "VALUES(default, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement myPreparedStatement = c.prepareStatement(query);
            myPreparedStatement.setString(1, newUser.getName());
            myPreparedStatement.setString(2, newUser.getSurname());
            myPreparedStatement.setString(3, Arrays.toString(newUser.getHashedPassword()));
            myPreparedStatement.setString(4, newUser.getSalt());
            myPreparedStatement.setString(5, newUser.getType());
            myPreparedStatement.setString(6, newUser.getEmail());
            myPreparedStatement.setBytes(7, myMessageEncryptionManager.symmetricDataEncryption(newUser.getMyPrivateKey(), "heslo".getBytes()));
            myPreparedStatement.setBytes(8, newUser.getMyPublicKey());
            myPreparedStatement.executeUpdate();
             */

            String query = "INSERT INTO projects (id, leader_id, name, description) VALUES (default, ?, ?, ?)";
            PreparedStatement myPreparedStatement = c.prepareStatement(query);
            myPreparedStatement.setInt(1, 22);
            myPreparedStatement.setString(2, "encryptedGroup");
            myPreparedStatement.setString(3, "testing group message encryption");
            myPreparedStatement.executeUpdate();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
