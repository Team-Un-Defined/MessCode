package com.messcode.transferobjects;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

/*
* Asymmetric encryption is used for encrypting private and group messages.
* Symmetric encryption is used for encrypting private keys.
*/

public class MessageEncryptionManager {
    private String asymmetricCryptographyAlgorithm = "RSA";
    private String symmetricCryptographyAlgorithm = "AES";

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

    public byte[] generateIV() {
        try {
            Cipher cipher = Cipher.getInstance(symmetricCryptographyAlgorithm);
            return cipher.getIV();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

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
