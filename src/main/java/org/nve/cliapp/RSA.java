package org.nve.cliapp;

import org.nve.cliapp.utils.SysUtils;
import org.nve.cliapp.exceptions.SysUtilsException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public final class RSA {

    private static String ENC_ALGORITHM = "RSA";
    private Cipher cipher;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public RSA(String dirName, String projectName, int numBits) throws SysUtilsException {
        Path privateKeyPath = Paths.get(dirName, projectName, "rsa");
        Path publicKeyPath = Paths.get(dirName, projectName, "rsa.pub");
        this.initialize(privateKeyPath, publicKeyPath, numBits);
    }

    private void initialize(Path privateKeyPath, Path publicKeyPath, int numBits) throws SysUtilsException {
        try {
            if (privateKeyPath.toFile().exists() == false || publicKeyPath.toFile().exists() == false) {
                this.generateKeys(privateKeyPath, publicKeyPath, numBits);
            }

            byte[] privateByteArray = SysUtils.readBinaryFile(privateKeyPath.toString());
            byte[] publicByteArray = SysUtils.readBinaryFile(publicKeyPath.toString());
            
            this.privateKey = KeyFactory.getInstance(ENC_ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(privateByteArray));
            this.publicKey = KeyFactory.getInstance(ENC_ALGORITHM).generatePublic(new X509EncodedKeySpec(publicByteArray));
            this.cipher = Cipher.getInstance(ENC_ALGORITHM);

        } catch (NoSuchAlgorithmException ex) {
            String msg = "ERROR: RSA.readKeys()  NoSuchAlgorithmException\n";
            msg += ex.getMessage();
            throw new SysUtilsException(msg);
        } catch (InvalidKeySpecException ex) {
            String msg = "ERROR: RSA.readKeys()  InvalidKeySpecException\n";
            msg += ex.getMessage();
            throw new SysUtilsException(msg);
        } catch (NoSuchPaddingException ex) {
            String msg = "ERROR: RSA.readKeys()  NoSuchPaddingException\n";
            msg += ex.getMessage();
            throw new SysUtilsException(msg);
        }

    }
    
    private void generateKeys(Path privateKeyPath, Path publicKeyPath, int numBits) throws SysUtilsException {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ENC_ALGORITHM);
            keyGen.initialize(numBits);
            KeyPair keyPair = keyGen.generateKeyPair();
            // persist the keys in files that YOU MUST protect
            SysUtils.writeBinaryFile(privateKeyPath.toString(), keyPair.getPrivate().getEncoded(), false);
            SysUtils.writeBinaryFile(publicKeyPath.toString(), keyPair.getPublic().getEncoded(), false);
        } catch (NoSuchAlgorithmException ex) {
            String msg = "ERROR: RSA.generateKeys()  NoSuchAlgorithmException\n";
            msg += ex.getMessage();
            throw new SysUtilsException(msg);
        }
    }

}
