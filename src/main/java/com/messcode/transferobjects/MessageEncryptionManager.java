package com.messcode.transferobjects;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

/*
* This method is used for encrypting message and private keys.
* Asymmetric encryption is used for encrypting private and group messages.
* Symmetric encryption is used for encrypting private keys.
*/
public class MessageEncryptionManager {
    private String asymmetricCryptographyAlgorithm = "RSA";
    private String symmetricCryptographyAlgorithm = "AES";

    /*
    * Generates RSA key pair.
    *
    * @return   RSA key pair
     */
    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(asymmetricCryptographyAlgorithm);
            keyGen.initialize(4096, new SecureRandom());

            return keyGen.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
    * Turns input key into valid symmetric key.
    * Valid symmetric key used in symmetric encryption and decryption must be 16 characters long.
    * For example when you want to use String "12345" for encryption this method turns it into "1234512345123451".
    *
    * @return   valid symmetric key
     */
    private byte[] generateCorrectSymmetricKey(byte[] key) {
        int correct_key_size = 16;
        byte[] correct_key = new byte[16];

        int j = 0;
        int j_max = key.length - 1;
        for (int i = 0; i < correct_key_size; i++) {
            correct_key[i] = key[j];
            if (j == j_max) {
                j = j;
            } else {
                j++;
            }
        }

        return correct_key;
    }

    /*
    * Encrypts message that we want to send using receiver's public key.
    *
    * @param    data    open message that we want to encrypt
    * @param    encryptionKey   public key used for encrypting message
    * @return   encrypted message
     */
    public byte[] asymmetricDataEncryption(byte[] data, byte[] encryptionKey) {
        try {
            PublicKey key = KeyFactory.getInstance(asymmetricCryptographyAlgorithm).generatePublic(new X509EncodedKeySpec(encryptionKey));
            Cipher cipher = Cipher.getInstance(asymmetricCryptographyAlgorithm);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Decrypts message that we received using our private key.
     *
     * @param    data    encrypted message that we want to decrypt
     * @param    decryptionKey   private key used for decrypting message
     * @return   decrypted message
     */
    public byte[] asymmetricDataDecryption(byte[] data, byte[] decryptionKey) {
        try {
            PrivateKey key = KeyFactory.getInstance(asymmetricCryptographyAlgorithm).generatePrivate(new PKCS8EncodedKeySpec(decryptionKey));
            Cipher cipher = Cipher.getInstance(asymmetricCryptographyAlgorithm);
            cipher.init(Cipher.DECRYPT_MODE, key);

            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Encrypts private key that we want to send using our password
     *
     * @param    data    private key that we want to encrypt
     * @param    key   our password that we use to encrypt our private key
     * @return   encrypted private key
     */
    public byte[] symmetricDataEncryption(byte[] data, byte[] key) {
        try {
            byte[] correctSymmetricKey = generateCorrectSymmetricKey(key);
            SecretKey mySecretKey = new SecretKeySpec(correctSymmetricKey, 0, correctSymmetricKey.length, symmetricCryptographyAlgorithm);
            Cipher cipher = Cipher.getInstance(symmetricCryptographyAlgorithm);
            cipher.init(Cipher.ENCRYPT_MODE, mySecretKey);

            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Decrypts private key that we received from database using our password
     *
     * @param    data    private key that we want to decrypt
     * @param    key   our password that we use to decrypt our private key
     * @return   decrypted private key
     */
    public byte[] symmetricDataDecryption(byte[] data, byte[] key) {
        try {
            byte[] correctSymmetricKey = generateCorrectSymmetricKey(key);
            SecretKey mySecretKey = new SecretKeySpec(correctSymmetricKey, 0, correctSymmetricKey.length, symmetricCryptographyAlgorithm);
            Cipher cipher = Cipher.getInstance(symmetricCryptographyAlgorithm);
            cipher.init(Cipher.DECRYPT_MODE, mySecretKey);

            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }
}
