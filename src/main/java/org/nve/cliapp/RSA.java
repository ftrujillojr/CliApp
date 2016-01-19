package org.nve.cliapp;

<<<<<<< HEAD
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
=======
import org.nve.cliapp.utils.SysUtils;
import org.nve.cliapp.exceptions.SysUtilsException;
>>>>>>> c4a48c9cca69cc7eba9cf4ba3d25cfb5680b7834
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.codehaus.plexus.util.Base64;

/**
 * I am using SysUtils/ConvertUtils/*** classes to support this class. Those
 * classes are located in the same package as this class.
 *
 * My goal is to make the code: 1) Easier to read code in this class. 2) RE-use
 * unit tested methods to keep bug count down. 3) Combine Exceptions into
 * SysUtilsException and keep Exception try/catch count down.
 */
public final class RSA {

    private static final String ENC_ALGORITHM = "RSA";
    private Cipher cipher;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public RSA(String dirName, String projectName, int numBits) throws SysUtilsException {
        // You MUST protect these two files.
        Path privateKeyPath = Paths.get(dirName, projectName, "rsa");
        Path publicKeyPath = Paths.get(dirName, projectName, "rsa.pub");
        this.initialize(privateKeyPath, publicKeyPath, numBits);
    }

    public String encrypt(String plaintext) throws SysUtilsException  {
        StringBuilder sb = new StringBuilder();
        try {
            this.cipher.init(Cipher.ENCRYPT_MODE, this.publicKey);
            byte[] bytes = plaintext.getBytes("UTF-8");
            
            System.out.println("DEBUG: bytes");
            SysUtils.displayHexDump(bytes);
            
            byte[] encrypted = this.blockCipher(bytes, Cipher.ENCRYPT_MODE);
            
            System.out.println("DEBUG: encrypted");
            SysUtils.displayHexDump(encrypted);
            
            byte[] encryptedHex = Base64.encodeBase64(encrypted);
            sb.append(new String(encryptedHex));
        } catch (InvalidKeyException ex) {
            String msg = "ERROR: RSA.encrypt() InvalidKeyException  PublicKey maybe in invalid.\n";
            msg += ex.getMessage();
            throw new SysUtilsException(msg);
        } catch (UnsupportedEncodingException ex) {
            String msg = "ERROR: RSA.encrypt() UnsupportedEncodingException\n";
            msg += ex.getMessage();
            throw new SysUtilsException(msg);
        } catch (SysUtilsException ex) {
            throw ex;
        }
        return (sb.toString());
    }

    public String decrypt(String encrypted) throws SysUtilsException  {
        StringBuilder sb = new StringBuilder();

        try {
            this.cipher.init(Cipher.DECRYPT_MODE, this.privateKey);
            byte[] bytes = Base64.decodeBase64(encrypted.getBytes());
            
            System.out.println("DEBUG: decode bytes");
            SysUtils.displayHexDump(bytes);
            
            byte[] decrypted = this.blockCipher(bytes, Cipher.DECRYPT_MODE);
            
            System.out.println("DEBUG: decrypted bytes");
            SysUtils.displayHexDump(decrypted);
           
            
            sb.append(new String(decrypted, "UTF-8"));
        } catch (InvalidKeyException ex) {
            String msg = "ERROR: RSA.decrypt() InvalidKeyException  PrivateKey maybe in invalid.\n";
            msg += ex.getMessage();
            throw new SysUtilsException(msg);
        } 
//        catch (DecoderException ex) {
//            String msg = "ERROR: RSA.decrypt() DecoderException  decodeHex failed.\n";
//            msg += ex.getMessage();
//            throw new SysUtilsException(msg);
//        } 
        catch (UnsupportedEncodingException ex) {
            String msg = "ERROR: RSA.decrypt() UnsupportedEncodingException\n";
            msg += ex.getMessage();
            throw new SysUtilsException(msg);
        } catch (SysUtilsException ex) {
            throw ex;
        }
        return (sb.toString());
    }

    private byte[] blockCipher(byte[] bytes, int mode) throws SysUtilsException {
        byte[] results = null;
        int blockSize = (mode == Cipher.ENCRYPT_MODE) ? 100 : 128;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        System.out.println("BLOCKSIZE " + blockSize);
        
        // try with resources will autoclose ObjectOutput
        try (ObjectOutput out = new ObjectOutputStream(bos)) {
            int start = 0;
            // Division returns whole number blockSize will divide into bytes.length.
            for (int ii = 0; ii < (bytes.length / blockSize); ii++) {
                int stop = start + blockSize - 1;
                byte[] buffer = java.util.Arrays.copyOfRange(bytes, start, stop + 1);
                int outputSize = this.cipher.getOutputSize(blockSize);
                System.out.println("block cipher for loop   outputSize " + outputSize);
                SysUtils.displayHexDump(buffer);
                out.write(this.cipher.doFinal(buffer));
                start = stop + 1; // update start index 1 past stop
            }

            // Modulus returns any remaining bytes
            if ((bytes.length % blockSize) > 0) {
                int stop = start + (bytes.length % blockSize) - 1;
                byte[] buffer = java.util.Arrays.copyOfRange(bytes, start, stop + 1);
                System.out.println("block cipher remaining");
                SysUtils.displayHexDump(buffer);
                out.write(this.cipher.doFinal(buffer));
            }
            // flush ByteArrayOutputStream, then retrieve results
            out.flush();
            results = bos.toByteArray();
            bos.close();
        } catch (IOException ex) {
            String msg = "ERROR: RSA.blockCipher() IOException\n";
            msg += ex.getMessage();
            throw new SysUtilsException(msg);
        }catch (IllegalBlockSizeException ex) {
            String msg = "ERROR: RSA.blockCipher() IllegalBlockSizeException\n";
            msg += ex.getMessage();
            throw new SysUtilsException(msg);
        }catch (BadPaddingException ex) {
            String msg = "ERROR: RSA.blockCipher() BadPaddingException\n";
            msg += ex.getMessage();
            throw new SysUtilsException(msg);
        }

        return results;
    }

    private void initialize(Path privateKeyPath, Path publicKeyPath, int numBits) throws SysUtilsException {
        try {
            // If either file is missing, then re-create them both.
            if (privateKeyPath.toFile().exists() == false || publicKeyPath.toFile().exists() == false) {
                this.generateKeys(privateKeyPath, publicKeyPath, numBits);
            }

            // reading in the keys as binary. 
//            byte[] privateByteArray = SysUtils.readBinaryFile(privateKeyPath.toString());
//            byte[] publicByteArray = SysUtils.readBinaryFile(publicKeyPath.toString());

            // then, reconstitute Private and Public keys from binary byte[]
//            this.privateKey = KeyFactory.getInstance(ENC_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(privateByteArray));
//            this.publicKey = KeyFactory.getInstance(ENC_ALGORITHM).generatePublic(new X509EncodedKeySpec(publicByteArray));

            // This is your cipher instance.
            this.cipher = Cipher.getInstance(ENC_ALGORITHM);

        } catch (NoSuchAlgorithmException ex) {
            String msg = "ERROR: RSA.initialize()  NoSuchAlgorithmException\n";
            msg += ex.getMessage();
            throw new SysUtilsException(msg);
        } 
//        catch (InvalidKeySpecException ex) {
//            String msg = "ERROR: RSA.initialize()  InvalidKeySpecException\n";
//            msg += ex.getMessage();
//            throw new SysUtilsException(msg);
//        } 
        catch (NoSuchPaddingException ex) {
            String msg = "ERROR: RSA.initialize()  NoSuchPaddingException\n";
            msg += ex.getMessage();
            throw new SysUtilsException(msg);
        }
//        catch (NoSuchProviderException ex) {
//            String msg = "ERROR: RSA.initialize()  NoSuchProviderException\n";
//            msg += ex.getMessage();
//            throw new SysUtilsException(msg);
//        }

    }

    private void generateKeys(Path privateKeyPath, Path publicKeyPath, int numBits) throws SysUtilsException {
        try {
//            SecureRandom random = new SecureRandom();
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ENC_ALGORITHM);
            keyGen.initialize(numBits);
//            keyGen.initialize(numBits, random);
            KeyPair keyPair = keyGen.generateKeyPair();
            // persist the keys in files that YOU MUST protect from other users.
//            SysUtils.writeBinaryFile(privateKeyPath.toString(), keyPair.getPrivate().getEncoded(), false);
//            SysUtils.writeBinaryFile(publicKeyPath.toString(), keyPair.getPublic().getEncoded(), false);
            this.privateKey = keyPair.getPrivate();
            this.publicKey = keyPair.getPublic();
        
        } catch (NoSuchAlgorithmException ex) {
            String msg = "ERROR: RSA.generateKeys()  NoSuchAlgorithmException\n";
            msg += ex.getMessage();
            throw new SysUtilsException(msg);
        } 
//        catch (NoSuchProviderException ex) {
//            String msg = "ERROR: RSA.generateKeys()  NoSuchProviderException\n";
//            msg += ex.getMessage();
//            throw new SysUtilsException(msg);
//        }
    }

}
