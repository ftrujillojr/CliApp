package org.nve.cliapp;

import java.text.DecimalFormat;
import java.util.Date;

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
    
    
}
