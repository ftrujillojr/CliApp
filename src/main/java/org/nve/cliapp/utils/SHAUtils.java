package org.nve.cliapp.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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

    // 10k => 0.265s   
    // 20k -> 0.321s  
    // 50k => 0.582s  
    // 80k => 0.773  
    // 100k => 0.892
    private static final int ITERATIONS = 100211; // for PBKDF2.  keep this number HIGH
    private static final int KEY_LENGTH = 512; // bits for PBKDF2
    // use SHAUtils.generateSalt() to get a new PEPPER value and then REplace in here to allow your usage to not be crackable by this string.
    private static final String PEPPER = "r5zob55OCerlFwGXU3F4aSlIVYQef349KqMmGOjyMQ92aUIrBhecw7anBHwJypHmPa9mKR3q3A+OiT34mrfZeg=="; // for PBKDF2

    public SHAUtils() {
    }
    
    /**
     * Base64:Base64 string to HEX string for any of the generated Hashes below.
     * 
     * @param hashStr
     * @return 
     */
    public static String hashToHex(String hashStr) {
        StringBuilder sb = new StringBuilder();
        
        String tmp[] = hashStr.split(":");
        if (tmp.length == 2) {
            sb.append(SHAUtils.base64StringToHex(tmp[1]));
        }
        return(sb.toString());
    }
    

    /**
     * Generate a SHA256 Hash for a file.
     * 
     * @param fileName
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static String generateSHA256HashForFile(String fileName) throws FileNotFoundException, IOException {
        StringBuilder sb = new StringBuilder();

        try (FileInputStream fis = new FileInputStream(fileName)) {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.reset();
            String salt = SHAUtils.generateSalt();
            byte[] saltByteArray = SHAUtils.base64StringToByteArray(salt);
            md.update(SHAUtils.pepperedSalt(saltByteArray));

            byte[] dataBytes = new byte[1024];

            int nread;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
            byte[] mdbytes = md.digest();
            sb.append(salt).append(":").append(SHAUtils.byteArrayToBase64String(mdbytes));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SHAUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return (sb.toString());
    }

    /**
     * Validate a file to a known sha256hash.
     * 
     * @param fileName
     * @param sha256Hash
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static boolean validateSHA256ForFile(String fileName, String sha256Hash) throws FileNotFoundException, IOException {
        boolean isValid = false;
        String[] parts = sha256Hash.split(":");

        try (FileInputStream fis = new FileInputStream(fileName)) {
            String salt = parts[0];
            byte[] hash = SHAUtils.base64StringToByteArray(parts[1]);
            byte[] saltByteArray = SHAUtils.base64StringToByteArray(salt);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.reset();
            md.update(SHAUtils.pepperedSalt(saltByteArray));

            byte[] dataBytes = new byte[1024];

            int nread;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
            byte[] mdbytes = md.digest();
            isValid = Arrays.equals(hash, mdbytes);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SHAUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return isValid;
    }

    /**
     * Generate SHA512 for a File.
     * 
     * @param fileName
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static String generateSHA512HashForFile(String fileName) throws FileNotFoundException, IOException {
        StringBuilder sb = new StringBuilder();

        try (FileInputStream fis = new FileInputStream(fileName)) {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.reset();
            String salt = SHAUtils.generateSalt();
            byte[] saltByteArray = SHAUtils.base64StringToByteArray(salt);
            md.update(SHAUtils.pepperedSalt(saltByteArray));

            byte[] dataBytes = new byte[1024];

            int nread;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
            byte[] mdbytes = md.digest();
            sb.append(salt).append(":").append(SHAUtils.byteArrayToBase64String(mdbytes));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SHAUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return (sb.toString());
    }

    /**
     * Validate hash from a File matches known sha512Hash.
     * 
     * @param fileName
     * @param sha512Hash
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public static boolean validateSHA512ForFile(String fileName, String sha512Hash) throws FileNotFoundException, IOException {
        boolean isValid = false;
        String[] parts = sha512Hash.split(":");

        try (FileInputStream fis = new FileInputStream(fileName)) {
            String salt = parts[0];
            byte[] hash = SHAUtils.base64StringToByteArray(parts[1]);
            byte[] saltByteArray = SHAUtils.base64StringToByteArray(salt);
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.reset();
            md.update(SHAUtils.pepperedSalt(saltByteArray));

            byte[] dataBytes = new byte[1024];

            int nread;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            }
            byte[] mdbytes = md.digest();
            isValid = Arrays.equals(hash, mdbytes);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SHAUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return isValid;
    }


    /**
     * DO NOT USE THIS METHOD to store passwords in database.  (see generatePBKDF2Hash)
     * @param myString
     * @return 
     */
    public static String generateSHA256Hash(String myString) {
        StringBuilder sb = new StringBuilder();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.reset();
            String salt = SHAUtils.generateSalt();
            byte[] saltByteArray = SHAUtils.base64StringToByteArray(salt);
            md.update(SHAUtils.pepperedSalt(saltByteArray));
            byte byteData[] = md.digest(myString.getBytes());
            sb.append(salt).append(":").append(SHAUtils.byteArrayToBase64String(byteData));

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SHAUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return (sb.toString());
    }

    /**
     * 
     * @param originalStr
     * @param sha256Hash
     * @return 
     */
    public static boolean validateSHA256(String originalStr, String sha256Hash) {
        String[] parts = sha256Hash.split(":");
        boolean isValid = false;

        try {
            String salt = parts[0];
            byte[] saltByteArray = SHAUtils.base64StringToByteArray(salt);
            byte[] hash = SHAUtils.base64StringToByteArray(parts[1]);

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.reset();
            md.update(SHAUtils.pepperedSalt(saltByteArray));

            byte byteData[] = md.digest(originalStr.getBytes());
            isValid = Arrays.equals(hash, byteData);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SHAUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return isValid;
    }

    /**
     * DO NOT USE THIS METHOD to store passwords in database.  (see generatePBKDF2Hash)
     * 
     * @param myString
     * @return 
     */
    public static String generateSHA512Hash(String myString) {
        StringBuilder sb = new StringBuilder();

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.reset();
            String salt = SHAUtils.generateSalt();
            byte[] saltByteArray = SHAUtils.base64StringToByteArray(salt);
            md.update(SHAUtils.pepperedSalt(saltByteArray));
            byte byteData[] = md.digest(myString.getBytes());
            sb.append(salt).append(":").append(SHAUtils.byteArrayToBase64String(byteData));

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SHAUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return (sb.toString());
    }

    /**
     * Given a string and a pre-generated 
     * @param originalStr
     * @param sha512Hash
     * @return 
     */
    public static boolean validateSHA512(String originalStr, String sha512Hash) {
        String[] parts = sha512Hash.split(":");
        boolean isValid = false;

        try {
            String salt = parts[0];
            byte[] saltByteArray = SHAUtils.base64StringToByteArray(salt);
            byte[] hash = SHAUtils.base64StringToByteArray(parts[1]);

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.reset();
            md.update(SHAUtils.pepperedSalt(saltByteArray));

            byte byteData[] = md.digest(originalStr.getBytes());

            isValid = Arrays.equals(hash, byteData);

        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SHAUtils.class.getName()).log(Level.SEVERE, null, ex);
        }

        return isValid;
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

        return Arrays.equals(hash, testHash);
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

    public static String base64StringToHex(String base64Str) {
        byte[] byteArray = SHAUtils.base64StringToByteArray(base64Str);
        String hexStr = SHAUtils.byteArraytoHex(byteArray);
        return hexStr;
    }

    /**
     * Not used in SHAUtils, but thought this may come in handy later.
     *
     * @param byteArray
     * @return String padded hex.
     */
    public static String byteArraytoHex(byte[] byteArray)  {
        BigInteger bi = new BigInteger(1, byteArray);
        String hex = bi.toString(16);
        int paddingLength = (byteArray.length * 2) - hex.length();
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
     */
    public static byte[] hexToByteArray(String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    /**
     * This method will take an input byte[] (salt) and then pepper it up;
     * return new byte[] I know the data is USUALLY peppered, but so can the
     * salt.
     *
     * @param saltByteArray
     * @return byte[]
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
