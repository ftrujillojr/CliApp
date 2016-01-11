package org.nve.cliapp;

import java.math.BigInteger;
import javax.xml.bind.DatatypeConverter;

public class UnsignedBigIntUtils {

    public UnsignedBigIntUtils() {
    }

    /**
     * By default, BigInteger is signed unless you use the constructor in this
     * method that sets the unsigned bit. There is no class named Unsigned
     * BigInteger.
     *
     * @param data
     * @return Unsigned BigInteger
     */
    public static BigInteger toBI(BigInteger data) {
        // The "1" sets unsigned bit.
        return (new BigInteger(1, data.toByteArray()));
    }

    /**
     * Convert byte[] to Unsigned BigInteger. There is no class named Unsigned
     * BigInteger.
     *
     * @param byteArray
     * @return Unsigned BigInteger
     */
    public static BigInteger toBI(byte[] byteArray) {
        // The "1" sets unsigned bit.  
        return (new BigInteger(1, byteArray));
    }

    /**
     * Convert a hex string to Unsigned BigInteger. There is no class named
     * Unsigned BigInteger.
     *
     * @param hexStr
     * @return
     * @throws UnsignedBigIntUtilsException
     */
    public static BigInteger toBI(String hexStr) throws UnsignedBigIntUtilsException {
        // The "1" sets unsigned bit.  
        BigInteger biResult = new BigInteger(1, UnsignedBigIntUtils.toByteArray(hexStr));
        return (biResult);
    }

    // ================================================================================================================================
    /**
     * Convert byte[] to a hex String output.
     *
     * DatatypeConverter is base Java out of the box. No external libraries
     * required. This method will prepend a 0x in String result.
     *
     * @param array
     * @return
     */
    public static String toHexString(byte[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append("0x").append(DatatypeConverter.printHexBinary(array));
        return sb.toString();
    }
 
    /**
     * Converts BigInteger to hex String with prepended 0x.
     * 
     * @param data
     * @return 
     */
    public static String toHexString(BigInteger data) {
        StringBuilder sb = new StringBuilder();
        sb.append("0x").append(data.toString(16));
        return sb.toString();
    }

    /**
     * Convert hex String to byte[].
     *
     * DatatypeConverter is base Java out of the box. No external libraries
     * required.
     *
     * @param hexStr
     * @return
     * @throws UnsignedBigIntUtilsException
     */
    public static byte[] toByteArray(String hexStr) throws UnsignedBigIntUtilsException {

        return DatatypeConverter.parseHexBinary(UnsignedBigIntUtils.validateHexStr(hexStr));
    }

    // ================================================================================================================================
    public static BigInteger maskBitRange(BigInteger data, int msb, int lsb) {
        BigInteger mask = toBI(BigInteger.valueOf(0x0L));
        for (int i = lsb; i <= msb; i++) {
            mask = mask.or(UnsignedBigIntUtils.toBI(BigInteger.valueOf(1L << i)));
        }
        return (data.and(mask));
    }

    // ================================================================================================================================
    /**
     * Convert BigInteger to hex String with zero padded to numBits if >
     * hexStr.length
     *
     * @param data
     * @param numBits
     * @return
     */
    public static String toPaddedHexString(BigInteger data, int numBits) {
        String hexStr = UnsignedBigIntUtils.toHexString(data);
        hexStr = RegExp.replaceFirst("0x", hexStr, "");
        StringBuilder sb = new StringBuilder();
        
        if (RegExp.isMatch("^0x", hexStr) == false) {
            sb.append("0x");
        }

        int stop = numBits / 4;

        if ((numBits % 4) != 0) {
            stop++;
        }

        if (stop > hexStr.length()) {
            for (int ii = stop - hexStr.length(); ii > 0; ii--) {
                sb.append('0');
            }
        }
        sb.append(hexStr);
        return (sb.toString());
    }

    /**
     * Convert BigInteger to hex String that is formated every 4 hex characters
     * for easier reading.
     *
     * @param data
     * @param numBits
     * @return
     */
    public static String toFormattedHexString(BigInteger data, int numBits) {
        //BigInteger.toString(radix) does not pad to full word sizes.
        String tmpStr = UnsignedBigIntUtils.toPaddedHexString(data, numBits);
        tmpStr = RegExp.replaceFirst("0x", tmpStr, "");
        
        StringBuilder formattedHexString = new StringBuilder();
        
        if (RegExp.isMatch("^0x", tmpStr) == false) {
            formattedHexString.append("0x");
        }

        // If you modify anything in this loop, then you must modify tests.
        for (int ii = 0; ii < tmpStr.length(); ii++) {
            if (ii % 4 == 0 && ii != 0) {
                formattedHexString.append(" | "); 
            }
            formattedHexString.append(tmpStr.charAt(ii));
        }

        tmpStr = formattedHexString.toString();

        return (tmpStr);
    }

    /**
     * This method is used to compress a possible formatted hex String by
     * stripping off leading 0x and removing white space and vertical spacers.
     *
     * @param hexStr
     * @return
     * @throws UnsignedBigIntUtilsException
     */
    private static String validateHexStr(String hexStr) throws UnsignedBigIntUtilsException {
        String tmpStr = RegExp.replaceAll("[ \\|\\t\\n]+", hexStr, "");
        tmpStr = RegExp.replaceFirst("0[xX]", tmpStr, "");
        if (tmpStr.matches("[0-9a-fA-F]+") == false) {
            String msg = "ERROR: Invalid HEX characters in input => " + tmpStr + "\n";
            throw new UnsignedBigIntUtilsException(msg);
        }
        return (tmpStr);
    }

}
