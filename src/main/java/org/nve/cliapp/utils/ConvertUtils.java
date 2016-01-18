package org.nve.cliapp.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConvertUtils {

    public ConvertUtils() {

    }

    public static String formatDoubleToString(Double value) {
        DecimalFormat df = new DecimalFormat("#.0000");
        String result = df.format(value);
        return result;
    }

    public static String getEpochAsString() {
        return formatDoubleToString(ConvertUtils.getEpochAsDouble());
    }

    public static Double getEpochAsDouble() {
        Date mydate = new Date();
        long epoch = mydate.getTime(); // returns milliseconds, need to / 1000
        Double result = (epoch / 1000.0);
        return result;
    }

    public static String epochToAsciiDate(String strEpoch) {
        Double dbEpoch = Double.parseDouble(strEpoch);
        Date myDate = new Date((dbEpoch.longValue() * 1000));
        return myDate.toString();
    }

    public static String epochToAsciiDate(double epoch) {
        @SuppressWarnings("UnnecessaryBoxing")
        Double dbEpoch = new Double(epoch);
        Date myDate = new Date((dbEpoch.longValue() * 1000));
        return myDate.toString();
    }

    public static Double atof(String mydouble) {
        Double result = Double.parseDouble(mydouble);
        return result;
    }

    public static Integer atoi(String value) {
        Integer result = Integer.parseInt(value);
        return (result);
    }

    public String itoa(int value) {
        String result = Integer.toString(value);
        return (result);
    }

    public static byte[] longToByteArray(long longValue) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(Long.SIZE / Byte.SIZE);
        byte[] result = null;
        
        try (DataOutputStream dos = new DataOutputStream(baos)) {
            dos.writeLong(longValue);
            result = baos.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(ConvertUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public static long byteArrayToLong(byte[] byteArray) {
        ByteArrayInputStream baos=new ByteArrayInputStream(byteArray);
        long result = 0;
        try (DataInputStream dos = new DataInputStream(baos)) {
            result = dos.readLong();
        } catch (IOException ex) {
            Logger.getLogger(ConvertUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

}
