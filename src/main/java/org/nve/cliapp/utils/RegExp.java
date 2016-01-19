package org.nve.cliapp.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * One goal of this class is to use use java.utils.regex AS IS as much as
 * possible.
 *
 * The second goal is to have static methods to use without having to think
 * about regex pattern matching.
 *
 * *** Set TestRegExp.java for examples.
 *
 */
public class RegExp {

    private static List<String> subExps = new ArrayList<>();

    public RegExp() {
    }

    /**
     * Split a String on whitespace and return List&lt;String&gt;
     *
     * @param myString String to act upon.
     * @return List&lt;String&gt;
     */
    public static List<String> split(String myString) {
        String myRegEx = "[ \t\n]+";
        // since this method is splitting on white space, then it is safe
        // to trim() myString to prevent errors.
        Pattern pattern = Pattern.compile(myRegEx);
        List<String> list = new ArrayList<>(Arrays.asList(pattern.split(myString.trim())));
        return (list);
    }

    /**
     * Split a String on regexp and return List&lt;String&gt;
     *
     * @param myRegEx  A regular expression
     * @param myString String to act upon
     * @return List&lt;String&gt; 
     */
    public static List<String> split(String myRegEx, String myString) {
        Pattern pattern = Pattern.compile(myRegEx);
        // Cannot trim() myString because you might be spliting on non-whitespace.
        List<String> list = new ArrayList<>(Arrays.asList(pattern.split(myString)));
        return (list);
    }

    /**
     * Case sensitive regex match.
     *
     * @param myRegEx A regular expression
     * @param myString String to act upon
     * @return boolean 
     */
    public static boolean isMatch(String myRegEx, String myString) {
        Pattern pattern = Pattern.compile(myRegEx);
        boolean found = RegExp.isPatternMatch(pattern, myString);
        return (found);
    }

    /**
     * patternFlags are from java.util.regex.Pattern enums.
     *
     * You can OR these as needed and pass into patternFlags in this method.
     *
     * UNICODE_CASE | UNIX_LINES | CASE_INSENSITIVE | COMMENTS | MULTILINE
     *
     * Or, you can use these prefixes for the regular expression itself. ?u: ?d:
     * ?i: ?x: ?m:
     *
     * String re = "(?i:.*this is case insensitive reg ex.*)"; // MUST HAVE ()
     *
     * @param myRegEx A Regular Expression
     * @param myString String to act upon
     * @param patternFlags UNICODE_CASE | UNIX_LINES | CASE_INSENSITIVE | COMMENTS | MULTILINE
     * @return boolean
     */
    public static boolean isMatch(String myRegEx, String myString, int patternFlags) {
        Pattern pattern = Pattern.compile(myRegEx, patternFlags);
        boolean found = RegExp.isPatternMatch(pattern, myString);
        return (found);
    }

    /**
     * This just allows me to check if two objects are the same or not.
     * I left this public.  Maybe someone will use it.
     * 
     * @param o An object
     * @return String
     */
    public static String objectToString(Object o) {
        //prevent a NullPointerException by returning null if o is null
        String result = null;
        if (o != null) {
            result = o.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(o));
        }
        return result;
    }

    /**
     * After you call isMatch() or other public methods, then subExps will
     * contain the LAST group match.
     *
     * @return List&lt;String&gt; containing any grouped results from regexp parse.
     */
    public static List<String> getSubExps() {
        // I want a new object to hold the sub expression results.
        List<String> newSubExps = new ArrayList<>(subExps);
//        String str1 = RegExp.objectToString(subExps);
//        String str2 = RegExp.objectToString(newSubExps);
//        System.out.println(str1 + " <=> " + str2);
        return newSubExps;
    }

    /**
     * Yes, there is String.replaceAll(), but I wanted a java.utils.regex
     * solution
     *
     * @param myRegEx  A regular expression
     * @param myString String to act upon
     * @param myReplace Replacement string
     * @return New String with replaced values
     */
    public static String replaceAll(String myRegEx, String myString, String myReplace) {
        Pattern pattern = Pattern.compile(myRegEx);
        Matcher matcher = pattern.matcher(myString);
        String result = matcher.replaceAll(myReplace);
        return (result);
    }

    /**
     * Yes, there is String.replaceFirst(), but I wanted a java.utils.regex
     * solution
     *
     * @param myRegEx A Regular Expression
     * @param myString String to act upon
     * @param myReplace Replacement String
     * @return New String with replaced values
     */
    public static String replaceFirst(String myRegEx, String myString, String myReplace) {
        Pattern pattern = Pattern.compile(myRegEx);
        Matcher matcher = pattern.matcher(myString);
        String result = matcher.replaceFirst(myReplace);
        return (result);
    }

    /**
     * Java does NOT have a replaceLast. Using look arounds to go through string
     * backwards.
     *
     * http://www.rexegg.com/regex-lookarounds.html
     * http://www.ocpsoft.org/opensource/guide-to-regular-expressions-in-java-part-2/
     *
     * @param myRegEx  A Regular Expression
     * @param myString String to act upon
     * @param myReplace Replacement string to use for replacement.
     * @return New String with replacement in place
     */
    public static String replaceLast(String myRegEx, String myString, String myReplace) {
        String negativeRegEx = "(?s)" + myRegEx + "(?!.*?" + myRegEx + ")"; // This is the magic.
        Pattern pattern = Pattern.compile(negativeRegEx);
        Matcher matcher = pattern.matcher(myString);
        String result = matcher.replaceFirst(myReplace); // The replaceFirst will actual replaceLast.  Not a typo.
        return (result);
    }

    /**
     * For re-usability in this class and consistency.
     *
     * @param pattern   A regular expression Pattern
     * @param myString  String to act upon.
     * @return boolean does it match
     */
    private static boolean isPatternMatch(Pattern pattern, String myString) {
        subExps.clear();
        Matcher matcher = pattern.matcher(myString);
        boolean found = matcher.matches();

        if (found) {
            Integer numGroups = matcher.groupCount();
            for (int ii = 0; ii <= numGroups; ii++) {
                subExps.add(matcher.group(ii));
            }
        }

        return (found);
    }

}
