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
     * @param data A BigInteger that we will set unsigned bit on.
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
     * @param byteArray data input
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
     * @param hexStr data input
     * @return Unsigned BigInteger
     * @throws UnsignedBigIntUtilsException sub method throws
     */
    public static BigInteger toBI(String hexStr) throws UnsignedBigIntUtilsException {
        // The "1" sets unsigned bit.  
        BigInteger biResult = new BigInteger(1, UnsignedBigIntUtils.toByteArray(hexStr));
        return (biResult);
    }
    
    
    /**
     * Binary string in and Big Integer out.
     * 
     * @param binStr   Binary string
     * @return  Big Integer
     * @throws UnsignedBigIntUtilsException sub method throws
     */
    public static BigInteger binaryToBI(String binStr) throws UnsignedBigIntUtilsException {
        BigInteger biResult = new BigInteger(UnsignedBigIntUtils.validateBinaryStr(binStr), 2);
        return biResult;
    }

    // ================================================================================================================================
    /**
     * Convert byte[] to a hex String output.
     *
     * DatatypeConverter is base Java out of the box. No external libraries
     * required. This method will prepend a 0x in String result.
     *
     * @param array byte[] input
     * @return hex string
     */
    public static String toHexString(byte[] array) {
        StringBuilder sb = new StringBuilder();
        sb.append("0x").append(DatatypeConverter.printHexBinary(array));
        return sb.toString();
    }

    /**
     * Converts BigInteger to hex String with prepended 0x.
     *
     * @param data BigInteger
     * @return hex string
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
     * @param hexStr A hex string
     * @return byte[]
     * @throws UnsignedBigIntUtilsException sub method throws
     */
    public static byte[] toByteArray(String hexStr) throws UnsignedBigIntUtilsException {
        return DatatypeConverter.parseHexBinary(UnsignedBigIntUtils.validateHexStr(hexStr));
    }

    // ================================================================================================================================
    
    /**
     * 
     * @param data  Given this BigInteger, mask off consecutive bits
     * @param msb   ending with msb
     * @param lsb   and starting with lsb.
     * @return  BigInteger
     */
    
    public static BigInteger maskBitRange(BigInteger data, int msb, int lsb) {
        BigInteger mask = toBI(BigInteger.valueOf(0x0L));
        for (int i = lsb; i <= msb; i++) {
            mask = mask.or(UnsignedBigIntUtils.toBI(BigInteger.valueOf(1L << i)));
        }
        return (data.and(mask));
    }

    // ================================================================================================================================
    /**
     * Convert BigInteger to hex String with zero padded to numBits if &gt;
     * hexStr.length
     *
     * @param data  BigInteger
     * @param numBits number of bits to pad zeros for output
     * @return  hex string padded with zeros
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
     * @param data  BigInteger
     * @param numBits num bits to to pad zeros.
     * @return zero padded hex string broken apart on each 16 bit word and every 16 bits.
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
     * @param hexStr  The hex string to validate
     * @return  stripped hex string usable by BigInteger.
     * @throws UnsignedBigIntUtilsException  If hexStr is invalid, then tell us why.
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
    
    // ==========================================================================================

    /**
     * BigInteger to binary string.
     * @param data  BigInteger
     * @return  binary String
     */
    public static String toBinaryString(BigInteger data) {
        StringBuilder sb = new StringBuilder();
        sb.append("0b").append(data.toString(2));
        return sb.toString();
    }

    /**
     * BigInteger to binary string padded with zeros.
     * 
     * @param data  BigInteger
     * @param numBits  numbits to pad zeros for.
     * @return   binary String padded with zeros.
     */
    public static String toPaddedBinaryString(BigInteger data, int numBits) {
        String binStr = UnsignedBigIntUtils.toBinaryString(data);
        binStr = RegExp.replaceFirst("0b", binStr, "");
        StringBuilder sb = new StringBuilder();

        if (RegExp.isMatch("^0b", binStr) == false) {
            sb.append("0b");
        }

        if (numBits > binStr.length()) {
            for (int ii = numBits - binStr.length(); ii > 0; ii--) {
                sb.append('0');
            }
        }
        sb.append(binStr);
        return (sb.toString());
    }

    /**
     * BigInteger to binary String padding zeros wvery numBits and breaking output up by words.
     * @param data  BigInteger
     * @param numBits numbits to pad zeroes for 
     * @return  binary string
     */
    public static String toFormattedBinaryString(BigInteger data, int numBits) {
        //BigInteger.toString(radix) does not pad to full word sizes.
        String tmpStr = UnsignedBigIntUtils.toPaddedBinaryString(data, numBits);
        tmpStr = RegExp.replaceFirst("0b", tmpStr, "");

        StringBuilder formattedBinaryString = new StringBuilder();

        if (RegExp.isMatch("^0b", tmpStr) == false) {
            formattedBinaryString.append("0b");
        }

        // If you modify anything in this loop, then you must modify tests.
        for (int ii = 0; ii < tmpStr.length(); ii++) {
            if (ii % 16 == 0 && ii != 0) {
                formattedBinaryString.append(" | ");
            } else if (ii % 4 == 0 && ii != 0) {
                formattedBinaryString.append(" ");
            }
            formattedBinaryString.append(tmpStr.charAt(ii));
        }

        tmpStr = formattedBinaryString.toString();

        return (tmpStr);
    }
    

    /**
     * This method is used to compress a possible formatted binary String by
     * stripping off leading 0b and removing white space and vertical spacers.
     *
     * @param binStr Binary String
     * @return stripped valid binary string
     * @throws UnsignedBigIntUtilsException   If string is not valid then this is thrown.
     */
    private static String validateBinaryStr(String binStr) throws UnsignedBigIntUtilsException {
        String tmpStr = RegExp.replaceAll("[ \\|\\t\\n]+", binStr, "");
        tmpStr = RegExp.replaceFirst("0b", tmpStr, "");
        if (tmpStr.matches("[0-1]+") == false) {
            String msg = "ERROR: Invalid BINARY characters in input => " + tmpStr + "\n";
            throw new UnsignedBigIntUtilsException(msg);
        }
        return (tmpStr);
    }

}
