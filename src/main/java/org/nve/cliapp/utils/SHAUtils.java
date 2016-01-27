package org.nve.cliapp.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * http://en.wikipedia.org/wiki/SHA-2
 *
 * Cryptographic hash functions are mathematical operations run on digital data;
 * by comparing the computed "hash" (the execution of the algorithm) to a known
 * and expected hash value, a person can determine the data's integrity.
 *
 * For example, computing the hash of a downloaded file and comparing the result
 * to a previously published hash result can show whether the download has been
 * modified or tampered with.
 *
 * Applications should not refer to the clear text versions of passwords - not
 * in the database, not when logging, nor in any other area. The clear text
 * version of the password should be the end user's secret. It should never be
 * directly repeated or stored by an application.
 *
 * A hash function is not an encryption. Encrypted items are always meant for
 * eventual decryption. A hash function, on the other hand, is meant only as a
 * one-way operation. The whole idea of a hash function is that it should be
 * very difficult to calculate the original input value, given the hash value.
 *
 */
public class SHAUtils {

    private static final int ITERATIONS = 1520; // for PBKDF2
    private static final int KEY_LENGTH = 512; // bits for PBKDF2
    private static final String PEPPER = "r5zob55OCerlFwGXU3F4aSlIVYQef349KqMmGOjyMQ92aUIrBhecw7anBHwJypHmPa9mKR3q3A+OiT34mrfZeg=="; // for PBKDF2

    public SHAUtils() {
    }
    // http://www.mkyong.com/java/java-sha-hashing-example/

//    public static String getSHA256ForFile(String fileName) throws FileNotFoundException, IOException {
//        StringBuilder hexString = new StringBuilder();
//        // try with resources closes the filestream when done reading.
//        try (FileInputStream fis = new FileInputStream(fileName)) {
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            // https://www.owasp.org/index.php/Hashing_Java
//            // SALT your digest to keep hackers at bay.
//            md.reset();
//            md.update(SHAUtils.salt.getBytes());
//
//            byte[] dataBytes = new byte[1024];
//
//            int nread;
//            while ((nread = fis.read(dataBytes)) != -1) {
//                md.update(dataBytes, 0, nread);
//            }
//            byte[] mdbytes = md.digest();
//
//            //convert the byte to hex format method 2
//            for (int i = 0; i < mdbytes.length; i++) {
//                String hexValue = String.format("%02x", 0xFF & mdbytes[i]);
//                hexString.append(hexValue);
//            }
//        } catch (NoSuchAlgorithmException ex) {
//            Logger.getLogger(SHAUtils.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return (hexString.toString());
//    }
//
//    public static String getSHA512ForFile(String fileName) throws FileNotFoundException, IOException {
//        StringBuilder hexString = new StringBuilder();
//
//        // try with resources closes the filestream when done reading.
//        try (FileInputStream fis = new FileInputStream(fileName)) {
//            MessageDigest md = MessageDigest.getInstance("SHA-512");
//            // https://www.owasp.org/index.php/Hashing_Java
//            // SALT your digest to keep hackers at bay.
//            md.reset();
//            md.update(SHAUtils.salt.getBytes());
//
//            byte[] dataBytes = new byte[1024];
//
//            int nread;
//            while ((nread = fis.read(dataBytes)) != -1) {
//                md.update(dataBytes, 0, nread);
//            }
//            byte[] mdbytes = md.digest();
//
//            //convert the byte to hex format method 2
//            for (int i = 0; i < mdbytes.length; i++) {
//                String hexValue = String.format("%02x", 0xFF & mdbytes[i]);
//                hexString.append(hexValue);
//            }
//        } catch (NoSuchAlgorithmException ex) {
//            Logger.getLogger(SHAUtils.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return (hexString.toString());
//    }
//
//    public static String getSHA512ForString(String myString) {
//        StringBuilder hexString = new StringBuilder();
//
//        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-512");
//            // https://www.owasp.org/index.php/Hashing_Java
//            // SALT your digest to keep hackers at bay.
//            md.reset();
//            md.update(SHAUtils.salt.getBytes());
//
//            byte byteData[] = md.digest(myString.getBytes());
//
//            //convert the byte to hex format method 2
//            for (int i = 0; i < byteData.length; i++) {
//                String hex = String.format("%02x", 0xFF & byteData[i]);
//                hexString.append(hex);
//            }
//        } catch (NoSuchAlgorithmException ex) {
//            Logger.getLogger(SHAUtils.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        return (hexString.toString());
//    }
    public static String generateSHA256Hash(String myString) {
        StringBuilder sb = new StringBuilder();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.reset();
            String salt = SHAUtils.generateSalt();
            byte[] saltByteArray = SHAUtils.base64StringToByteArray(salt);
            md.update(saltByteArray);
            byte byteData[] = md.digest(myString.getBytes());
            sb.append(salt).append(":").append(SHAUtils.byteArrayToBase64String(byteData));

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SHAUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return (sb.toString());
    }

    /**
     * Use this one for hashing passwords.
     *
     * http://howtodoinjava.com/optimization/how-to-generate-secure-password-hash-md5-sha-pbkdf2-bcrypt-examples/
     *
     * @param password
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static String generatePBKDF2Hash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        char[] chars = password.toCharArray();
        String salt = SHAUtils.generateSalt();
        byte[] saltByteArray = SHAUtils.base64StringToByteArray(salt);

        PBEKeySpec spec = new PBEKeySpec(chars, SHAUtils.pepperedSalt(saltByteArray), ITERATIONS, KEY_LENGTH);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        byte[] hash = skf.generateSecret(spec).getEncoded();

        return salt + ":" + SHAUtils.byteArrayToBase64String(hash);
    }

    /**
     * Use this method to validate a password with a known salt+pbkdf2Hash.
     * 
     * @param originalPassword
     * @param pbkdf2Hash
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException 
     */
    public static boolean validatePBKDF2(String originalPassword, String pbkdf2Hash) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = pbkdf2Hash.split(":");
        byte[] saltByteArray = SHAUtils.base64StringToByteArray(parts[0]);
        byte[] hash = SHAUtils.base64StringToByteArray(parts[1]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), SHAUtils.pepperedSalt(saltByteArray), ITERATIONS, KEY_LENGTH);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    /**
     * Generate a random byte array and then Base64 encode into a String.
     *
     * @return Base64 encoded string
     */
    public static String generateSalt() {
        Random random = new SecureRandom();
        byte[] bytes = new byte[64];  // If you change this, then static PEPPER must change to match length.
        random.nextBytes(bytes);
        return SHAUtils.byteArrayToBase64String(bytes);
    }

    /**
     * byte[] to Base64 encoded String.
     *
     * @param byteArray
     * @return Base64 encoded String.
     */
    public static String byteArrayToBase64String(byte[] byteArray) {
        Base64.Encoder encoder = Base64.getEncoder(); // Java 1.8 ships with this now.
        String base64EncodedString = encoder.encodeToString(byteArray);
        return (base64EncodedString);
    }

    /**
     * Base64 encoded to byte[].
     *
     * @param myStr
     * @return byte[]
     */
    public static byte[] base64StringToByteArray(String myStr) {
        Base64.Decoder decoder = Base64.getDecoder(); // Java 1.8 ships with this now.
        byte[] byteArray = decoder.decode(myStr);
        return (byteArray);
    }

    /**
     * Not used in SHAUtils, but thought this may come in handy later.
     *
     * @param array
     * @return String padded hex.
     * @throws NoSuchAlgorithmException
     */
    public static String byteArraytoHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);
        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    /**
     * Not used in SHAUtils, but thought this may come in handy later.
     *
     * @param hex
     * @return byte[]
     * @throws NoSuchAlgorithmException
     */
    public static byte[] hexToByteArray(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    /**
     * This method will take an input byte[] (salt) and then pepper it up; return new byte[]
     * I know the data is USUALLY peppered, but so can the salt.
     * 
     * @param saltByteArray
     * @return  byte[]
     */
    private static byte[] pepperedSalt(byte[] saltByteArray) {
        byte[] resultByteArray = Arrays.copyOf(saltByteArray, saltByteArray.length);
        byte[] pepperByteArray = SHAUtils.base64StringToByteArray(PEPPER);
        boolean debug = false;

        // bitwise XOR
        for (int ii = 0; ii < saltByteArray.length; ii++) {
            resultByteArray[ii] ^= pepperByteArray[ii];
        }

        if (debug) {
            SysUtils.displayHexDump(saltByteArray);
            SysUtils.displayHexDump(pepperByteArray);
            SysUtils.displayHexDump(resultByteArray);
            String hdr = SysUtils.repeatString("=", 73);
            System.out.println(hdr);
        }

        return resultByteArray;
    }

}
