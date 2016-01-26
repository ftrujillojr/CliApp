package org.nve.cliapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
//import org.apache.commons.codec.binary.Base64;
//import org.nve.cliapp.exceptions.RSAException;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;
import java.util.Base64;
import org.nve.cliapp.exceptions.RSAException;

public final class RSA {

    private String algorithm;
    private String privateKey;
    private String publicKey;
    private int numBitsEncryption;
    private Cipher cipher;
    private int debug;

    public RSA() {
        this.algorithm = "RSA";
        this.privateKey = "keys/private.key";
        this.publicKey = "keys/public.key";
        this.numBitsEncryption = 1024;  // do not change this or blockCipher is invalid.
        this.cipher = null;
        this.debug = 0;

        if (this.areKeysPresent() == false) {
            this.generateKey();
        }
    }

    public RSA(int debug) {
        this.algorithm = "RSA";
        this.privateKey = "keys/private.key";
        this.publicKey = "keys/public.key";
        this.numBitsEncryption = 1024;  // do not change this or blockCipher is invalid.
        this.cipher = null;
        this.debug = debug;

        if (this.areKeysPresent() == false) {
            this.generateKey();
        }
    }
    /**
     * This will allow you to use different paths to public and private key
     * files.
     *
     * @param privateKeyFileName
     * @param publicKeyFileName
     */
    public RSA(String privateKeyFileName, String publicKeyFileName) {
        this.algorithm = "RSA";
        this.privateKey = privateKeyFileName;
        this.publicKey = publicKeyFileName;
        this.numBitsEncryption = 1024;  // do not change this or blockCipher is invalid.
        this.cipher = null;
        this.debug = 0;

        if (this.areKeysPresent() == false) {
            this.generateKey();
        }
    }

    public int getDebug() {
        return debug;
    }

    public void setDebug(int debug) {
        this.debug = debug;
    }

    public void clrDebug() {
        this.debug = 0;
    }

    /**
     *
     * @param plainText
     * @return
     * @throws org.nve.cliapp.exceptions.RSAException
     */
    public String encryptBase64(String plainText) throws RSAException {
        String base64EncodedString = "";

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(this.publicKey))) {
            PublicKey publicKeyObj = (PublicKey) inputStream.readObject();
            byte[] cipherText = this.encrypt(plainText, publicKeyObj);
            // Java 1.7 and older do not have Base64.encode.  Need Apache 3rd party.
            //base64EncodedString = Base64.encodeBase64String(cipherText);
            Encoder encoder = Base64.getEncoder(); // Java 1.8 ships with this now.
            base64EncodedString = encoder.encodeToString(cipherText);
        } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);

            String msg = "\nERROR: Failed to encryptBase64(String plainText)";
            throw new RSAException(msg);
        }

        return (base64EncodedString);
    }

    public String decryptBase64(String encText) throws RSAException {
        String plainText = null;

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(this.privateKey))) {
            // Java 1.7 and older, you must use external library to do Base64.decode.  Apache.
            //byte[] decodedText = Base64.decodeBase64(encText);
            Decoder decoder = Base64.getDecoder();  // Java 1.8 ships with this now.
            byte[] decodedText = decoder.decode(encText);
            PrivateKey privateKeyObj = (PrivateKey) inputStream.readObject();
            plainText = this.decrypt(decodedText, privateKeyObj);
        } catch (IOException | ClassNotFoundException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
            String msg = "\nERROR: Failed to encryptBase64(String plainText)";
            throw new RSAException(msg);
        }

        return (this.rtrimZeros(plainText));
    }

    private String rtrimZeros(String plainText) {
        String tmp = null;
        byte[] bytes = plainText.getBytes();
        int ii = plainText.lastIndexOf(0);  // start at the end of the string

        while (ii >= 0) {                   // terminate at -1 if not found
            if (bytes[ii] != 0) {            // when we see a non-ZERO then break.
                break;
            }
            ii--;                           // check next index.
        }

        if (ii >= 0) {                          //  we found ZEROS
            tmp = plainText.substring(0, ii + 1); // return rtrim String
        } else {
            tmp = plainText;                   // don't allocate memory
        }

        return (tmp);
    }

    /**
     * Generate key which contains a pair of private and public key.
     *
     */
    private void generateKey() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(this.algorithm);
            keyGen.initialize(this.numBitsEncryption);
            KeyPair keyPair = keyGen.generateKeyPair();

            File privateKeyFile = new File(this.privateKey);
            File publicKeyFile = new File(this.publicKey);

            // Create files to store public and private key
            if (privateKeyFile.getParentFile() != null) {
                privateKeyFile.getParentFile().mkdirs();
            }
            privateKeyFile.createNewFile();

            if (publicKeyFile.getParentFile() != null) {
                publicKeyFile.getParentFile().mkdirs();
            }
            publicKeyFile.createNewFile();
            try (ObjectOutputStream publicKeyOS = new ObjectOutputStream(
                    new FileOutputStream(publicKeyFile))) {
                publicKeyOS.writeObject(keyPair.getPublic());
            }
            try (ObjectOutputStream privateKeyOS = new ObjectOutputStream(
                    new FileOutputStream(privateKeyFile))) {
                privateKeyOS.writeObject(keyPair.getPrivate());
            }
        } catch (NoSuchAlgorithmException | IOException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * The method checks if the pair of public and private key has been
     * generated.
     *
     * @return flag indicating if the pair of keys were generated.
     */
    private boolean areKeysPresent() {

        File privateKeyFile = new File(this.privateKey);
        File publicKeyFile = new File(this.publicKey);

        if (privateKeyFile.exists() && publicKeyFile.exists()) {
            return true;
        }
        return false;
    }

    /**
     * Encrypt the plain text using public key.
     *
     * @param text : original plain text
     * @param key :The public key
     * @return Encrypted text
     * @throws java.security.NoSuchAlgorithmException
     * @throws javax.crypto.NoSuchPaddingException
     * @throws javax.crypto.IllegalBlockSizeException
     * @throws javax.crypto.BadPaddingException
     * @throws java.security.InvalidKeyException
     */
    private byte[] encrypt(String plainText, PublicKey pubkey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] cipherText = null;

        try {
            // get an RSA cipher object and print the provider
            this.cipher = Cipher.getInstance(this.algorithm);
            // encrypt the plain text using the public key
            this.cipher.init(Cipher.ENCRYPT_MODE, pubkey);

            byte[] bytes = plainText.getBytes("UTF-8");
            cipherText = blockCipher(bytes, Cipher.ENCRYPT_MODE);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            throw ex;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cipherText;
    }

    /**
     * Decrypt text using private key.
     *
     * @param text :encrypted text
     * @param key :The private key
     * @return plain text
     * @throws java.security.NoSuchAlgorithmException
     * @throws javax.crypto.NoSuchPaddingException
     * @throws java.security.InvalidKeyException
     * @throws javax.crypto.IllegalBlockSizeException
     * @throws javax.crypto.BadPaddingException
     */
    private String decrypt(byte[] text, PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String decryptedStr = "";
        try {
            // get an RSA cipher object and print the provider
            this.cipher = Cipher.getInstance(this.algorithm);

            // decrypt the text using the private key
            this.cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] decryptedBytes = blockCipher(text, Cipher.DECRYPT_MODE);
            decryptedStr = new String(decryptedBytes, "UTF-8");

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException ex) {
            throw ex;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        }

        return decryptedStr;
    }

    
    private byte[] blockCipher(byte[] bytes, int mode) throws IllegalBlockSizeException, BadPaddingException {
        //System.out.println("blockCipher() " + mode);
        // string initialize 2 buffers.
        // scrambled will hold intermediate results
        byte[] scrambled = new byte[0];

        // toReturn will hold the total result
        byte[] toReturn = new byte[0];
        // if we encrypt we use 100 byte long blocks. Decryption requires 128 byte long blocks (because of RSA)
        // This keeps us below 117 byte blow up point.
        int length = (mode == Cipher.ENCRYPT_MODE) ? 100 : 128;
        //System.out.println("Choosing buffer length: " + length);

        // another buffer. this one will hold the bytes that have to be modified in this step
        byte[] buffer = new byte[length];

        for (int i = 0; i < bytes.length; i++) {

            // if we filled our buffer array we have our block ready for de- or encryption
            if ((i > 0) && (i % length == 0)) {
                //execute the operation
                scrambled = this.cipher.doFinal(buffer);
                // add the result to our total result.
                toReturn = this.append(toReturn, scrambled);
                // here we calculate the length of the next buffer required
                int newlength = length;

                // if newlength would be longer than remaining bytes in the bytes array we shorten it.
                if (i + length > bytes.length) {
                    newlength = bytes.length - i;
                }
                // clean the buffer array
                buffer = new byte[newlength];
            }
            // copy byte into our buffer.
            buffer[i % length] = bytes[i];
        }

        // this step is needed if we had a trailing buffer. should only happen when encrypting.
        // example: we encrypt 110 bytes. 100 bytes per run means we "forgot" the last 10 bytes. they are in the buffer array
        scrambled = this.cipher.doFinal(buffer);

        // final step before we can return the modified data.
        toReturn = append(toReturn, scrambled);

        return toReturn;
    }

    @SuppressWarnings("ManualArrayToCollectionCopy")
    private byte[] append(byte[] prefix, byte[] suffix) {
        byte[] toReturn = new byte[prefix.length + suffix.length];
        for (int i = 0; i < prefix.length; i++) {
            toReturn[i] = prefix[i];
        }
        for (int i = 0; i < suffix.length; i++) {
            toReturn[i + prefix.length] = suffix[i];
        }
        return toReturn;
    }

}
