package org.nve.cliapp.utils;

import org.nve.cliapp.exceptions.CalendarUtilsException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.nve.cliapp.comparators.CalendarComparator;

/**
 * Parsing formats.
 * <pre>
 * Letter   Date or Time Component     Presentation         Examples
 * G        Era designator             Text                 AD
 * y        Year                       Year                 1996;    96
 * M        Month in year              Month                July; Jul; 07
 * w        Week in year               Number               27
 * W        Week in month              Number               2
 * D        Day in year                Number               189
 * d        Day in month               Number               10
 * F        Day of week in month       Number               2
 * E        Day in week                Text                 Tuesday; Tue
 * a        Am/pm marker               Text                 PM
 * H        Hour in day (0-23)         Number               0
 * k        Hour in day (1-24)         Number               24
 * K        Hour in am/pm (0-11)       Number               0
 * h        Hour in am/pm (1-12)       Number               12
 * m        Minute in hour             Number               30
 * s        Second in minute           Number               55
 * S        Millisecond                Number               978
 * z        Time zone                  General time zone   Pacific Standard Time; PST; GMT-08:00
 * Z        Time zone                  RFC 822 time zone   -0800
 *
 * int year = calendar.get(Calendar.YEAR);
 * int month = calendar.get(Calendar.MONTH);              // Jan = 0, dec = 11
 * int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
 * int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);    // Sun = 1, Sat = 7
 * int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);  // This is WW, but start on Sunday
 * int weekOfMonth = calendar.get(Calendar.WEEK_OF_MONTH);
 *
 * int hour = calendar.get(Calendar.HOUR);                // 12 hour clock
 * int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);    // 24 hour clock
 * int minute = calendar.get(Calendar.MINUTE);
 * int second = calendar.get(Calendar.SECOND);
 * int millisecond = calendar.get(Calendar.MILLISECOND);
 *
 *
 *
 *
 * A list of the available time zone ids in the TimeZone class, printed as returned by TimeZone.getAvailableIDs().
 *
 * </pre>
 */
public final class CalendarUtils {

    public CalendarUtils() {

    }

    public static Calendar copy(Calendar calendar) {
        Calendar calendarCopy = Calendar.getInstance();
        calendarCopy.setTimeInMillis(calendar.getTimeInMillis());
        return (calendarCopy);
    }

    public static Calendar parse(String calString) throws ParseException, CalendarUtilsException {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mmm_dd_yyyy_DateFormat = new SimpleDateFormat("MMM dd yyyy");
        SimpleDateFormat mmm_dd_yyyy2_DateFormat = new SimpleDateFormat("MMM dd yyyy HH:mm:ss");
        SimpleDateFormat slashDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat slash2DateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        SimpleDateFormat slash3DateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy z Z");
        SimpleDateFormat fullDate2Format = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy");
        SimpleDateFormat fullDate3Format = new SimpleDateFormat("MMM dd HH:mm:ss yyyy");
        SimpleDateFormat mysqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat mysql2DateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        SimpleDateFormat tteDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss");
        SimpleDateFormat tteDate2Format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss z");

        String tmpCalString = calString.trim();

        // Nov 05 2014
        if (tmpCalString.matches("^[A-Za-z]{3}\\s+[0-9]{1,2}\\s+[0-9]{4}$")) {
            Date date = mmm_dd_yyyy_DateFormat.parse(tmpCalString);
            calendar.setTime(date);
        } // Nov 05 2014 12:23:12
        else if (tmpCalString.matches("^[A-Za-z]{3}\\s+[0-9]{1,2}\\s+[0-9]{4}\\s+[0-9]{2}:[0-9]{2}:[0-9]{2}$")) {
            Date date = mmm_dd_yyyy2_DateFormat.parse(tmpCalString);
            calendar.setTime(date);
        } // 11/05/2014
        else if (tmpCalString.matches("^[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}$")) {
            Date date = slashDateFormat.parse(tmpCalString);
            calendar.setTime(date);
        } // 11/05/2014 12:23:12
        else if (tmpCalString.matches("^[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}\\s+[0-9]{2}:[0-9]{2}:[0-9]{2}$")) {
            Date date = slash2DateFormat.parse(tmpCalString);
            calendar.setTime(date);
        }// 11/05/2014 12:23:12 UTC
        else if (tmpCalString.matches("^[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}\\s+[0-9]{2}:[0-9]{2}:[0-9]{2}\\s+[A-Z]+$")) {
            Date date = slash3DateFormat.parse(tmpCalString);
            calendar.setTime(date);
        } // Wed Nov 05 13:24:44 2014 UTC -0000
        else if (tmpCalString.matches("^[A-Za-z]{3}\\s+[A-Za-z]{3}\\s+[0-9]{1,2}\\s+[0-9]{2}:[0-9]{2}:[0-9]{2}\\s+[0-9]{4}\\s+[A-Z]+\\s+[\\-\\+]*[0-9]+$")) {
            Date date = fullDateFormat.parse(tmpCalString);
            calendar.setTime(date);
        } // Wed Nov 05 13:24:44 2014
        else if (tmpCalString.matches("^[A-Za-z]{3}\\s+[A-Za-z]{3}\\s+[0-9]{1,2}\\s+[0-9]{2}:[0-9]{2}:[0-9]{2}\\s+[0-9]{4}$")) {
            Date date = fullDate2Format.parse(tmpCalString);
            calendar.setTime(date);
        } // Nov 05 13:24:44 2014
        else if (tmpCalString.matches("^[A-Za-z]{3}\\s+[0-9]{1,2}\\s+[0-9]{2}:[0-9]{2}:[0-9]{2}\\s+[0-9]{4}$")) {
            Date date = fullDate3Format.parse(tmpCalString);
            calendar.setTime(date);
        } // 2014-11-06 09:38:41
        else if (tmpCalString.matches("^[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}\\s+[0-9]{2}:[0-9]{2}:[0-9]{2}$")) {
            Date date = mysqlDateFormat.parse(tmpCalString);
            calendar.setTime(date);
        } // 2014-11-06 09:38:41 UTC
        else if (tmpCalString.matches("^[0-9]{4}\\-[0-9]{2}\\-[0-9]{2}\\s+[0-9]{2}:[0-9]{2}:[0-9]{2}\\s+[A-Z]+$")) {
            Date date = mysql2DateFormat.parse(tmpCalString);
            calendar.setTime(date);
        } // 11-06-2014 16:56:38
        else if (tmpCalString.matches("^[0-9]{2}\\-[0-9]{2}\\-[0-9]{4}\\s+[0-9]{2}:[0-9]{2}:[0-9]{2}$")) {
            Date date = tteDateFormat.parse(tmpCalString);
            calendar.setTime(date);
        }// 11-06-2014 16:56:38 UTC
        else if (tmpCalString.matches("^[0-9]{2}\\-[0-9]{2}\\-[0-9]{4}\\s+[0-9]{2}:[0-9]{2}:[0-9]{2}\\s+[A-Z]+$")) {
            Date date = tteDate2Format.parse(tmpCalString);
            calendar.setTime(date);
        } else {
            String msg = "ERROR: Unable to parse Calendar input. => " + calString + "\n=>" + tmpCalString + "<=";
            throw new CalendarUtilsException(msg);
        }


        return (calendar);
    }

    public static Calendar toCalendar(Long epoch) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(epoch * 1000);
        return (calendar);
    }

    public static Long toEpoch(Calendar calendar) {
        Long epoch = calendar.getTimeInMillis() / 1000;
        return (epoch);
    }

    public static String toMDY(Calendar calendar) {
        //Wed Nov 05 2014 12:14:31 WW: 44
        SimpleDateFormat mdyDateFormat = new SimpleDateFormat("MMM dd yyyy");
        String tmp = String.format("%s", mdyDateFormat.format(calendar.getTime()));
        return (tmp);
    }

    public static String toLocal(Calendar calendar) {
        //Wed Nov 05 2014 12:14:31 WW: 44
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy z Z");
        String tmp = String.format("%s", fullDateFormat.format(calendar.getTime()));
        return (tmp);
    }

    public static String toBoise(Calendar calendar) {
        TimeZone timeZone = TimeZone.getTimeZone("America/Boise");
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy z Z");
        fullDateFormat.setTimeZone(timeZone);
        String tmp = String.format("%s", fullDateFormat.format(calendar.getTime()));
        return (tmp);
    }

    public static String toSingapore(Calendar calendar) {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Singapore");
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy z Z");
        fullDateFormat.setTimeZone(timeZone);
        String tmp = String.format("%s", fullDateFormat.format(calendar.getTime()));
        return (tmp);
    }

    public static String toGMT(Calendar calendar) {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy z Z");
        fullDateFormat.setTimeZone(timeZone);
        String tmp = String.format("%s", fullDateFormat.format(calendar.getTime()));
        return (tmp);
    }

    public static String toMySQL(Calendar calendar) {
        //YYYY-MM-DD HH:MM:SS
        SimpleDateFormat mysqlDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String tmp = String.format("%s", mysqlDateFormat.format(calendar.getTime()));
        return (tmp);
    }

    public static void displayLocal(Calendar calendar) {
        System.out.println(CalendarUtils.toLocal(calendar));
    }

    public static int getWW(Calendar calendar) throws ParseException, CalendarUtilsException {
        // This is length of week in milliseconds.
        int LENGTH_OF_WEEK = 7 * 24 * 60 * 60 * 1000;

        // This is the first day of the first Work Week.  You may need to adjust this value every 4 years for leap year.
        // This is your stick in the sand of where work weeks start.   GMT midnight.   It could easily have been Boise or Singapore.
        Calendar wwCalendarStart = CalendarUtils.parse("Fri Jan 03 00:00:00 2014 UTC -0000");

        long millisElapsed = calendar.getTimeInMillis() - wwCalendarStart.getTimeInMillis();
        int weeksElapsed = (int) (millisElapsed / LENGTH_OF_WEEK);
        int ww = (weeksElapsed + 1) % 52;
        if (ww == 0) {
            ww = 52;
        }
        return (ww);
    }

    public static Long getEpoch() {
        Date mydate = new Date();
        Long epoch = mydate.getTime() / 1000;
        return (epoch);
    }

    public static Long time() {
        return (CalendarUtils.getEpoch());
    }

    public static String ctime(Long epoch) {
        Calendar cal = CalendarUtils.toCalendar(epoch);
        return (CalendarUtils.toLocal(cal));
    }

    public static void displayAllTZ(Calendar calendar) {
        System.out.println("\nLOCAL     => " + CalendarUtils.toLocal(calendar) + "\n");
        System.out.println("GMT       => " + CalendarUtils.toGMT(calendar));
        System.out.println("Boise     => " + CalendarUtils.toBoise(calendar));
        System.out.println("Singapore => " + CalendarUtils.toSingapore(calendar));
        System.out.println("MySQL     => " + CalendarUtils.toMySQL(calendar));
    }

    public static void displayCalEpochWW(String calendarString) throws ParseException, CalendarUtilsException {
        Calendar cal1 = CalendarUtils.parse(calendarString);
        System.out.println("\nCALENDAR => " + calendarString);
        Long epoch1 = CalendarUtils.toEpoch(cal1);
        System.out.println("EPOCH    => " + epoch1);
        System.out.println("   WW    => " + CalendarUtils.getWW(cal1));
    }

    public static void zeroOutTime(Calendar calendar) {
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
    }

    public static boolean isEqualByDateOnly(Calendar c1, Calendar c2) {
        boolean result = false;

        int yearDiff = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
        int monthDiff = c1.get(Calendar.MONTH) - c2.get(Calendar.MONTH);
        int dayDiff = c1.get(Calendar.DAY_OF_MONTH) - c2.get(Calendar.DAY_OF_MONTH);

        if (yearDiff == 0 && monthDiff == 0 && dayDiff == 0) {
            result = true;
        }

        return (result);
    }

    public static boolean isEqual(Calendar cal1, Calendar cal2) {
        boolean result = false;
        CalendarComparator calendarComparator = new CalendarComparator();
        int comp = calendarComparator.compare(cal1, cal2);
        if (comp == 0) {
            result = true;
        }
        return (result);
    }
    
    public static boolean isOlder(Calendar cal1, Calendar cal2) {
        boolean result = false;
        CalendarComparator calendarComparator = new CalendarComparator();
        int comp = calendarComparator.compare(cal1, cal2);
        // an integer < 0 if lhs is less than rhs, 0 if they are equal, and > 0 if lhs is greater than rhs.
        if (comp > 0) {
            result = true;
        }
        return (result);
    }

    public static boolean isNewer(Calendar cal1, Calendar cal2) {
        boolean result = false;
        CalendarComparator calendarComparator = new CalendarComparator();
        // an integer < 0 if lhs is less than rhs, 0 if they are equal, and > 0 if lhs is greater than rhs.
        int comp = calendarComparator.compare(cal1, cal2);
        if (comp < 0) {
            result = true;
        }
        return (result);
    }

    public static boolean isBetween(Calendar calendarToCompare, Calendar startCalendar, Calendar endCalendar) {
        boolean result = false;
        CalendarComparator calendarComparator = new CalendarComparator();

        int startCompareValue = calendarComparator.compare(calendarToCompare, startCalendar);
        int endCompareValue = calendarComparator.compare(calendarToCompare, endCalendar);

        // an integer < 0 if lhs is less than rhs, 0 if they are equal, and > 0 if lhs is greater than rhs.
        if ((startCompareValue > 0) && (endCompareValue <= 0)) {
            result = true;
        }

        return (result);
    }
}
