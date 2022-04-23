package com.messcode.client;

import JDBC.ImportData;
import com.messcode.transferobjects.MessageEncryptionManager;
import javafx.application.Application;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class Start {

    public static void main(String[] args) {
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

        /*
        java.util.logging.Logger.getLogger(Start.class.getName()).log(Level.SEVERE, "sadsd");
        Application.launch(RunApp.class);
         */
    }
}
