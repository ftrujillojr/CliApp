package org.nve.cliapp;

import edu.emory.mathcs.backport.java.util.Arrays;
import java.util.ArrayList;
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
     * Split a String on whitespace and return List<String>
     * 
     * @param myString
     * @return List<String>
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
     * Split a String on regexp and return List<String>
     * 
     * @param myRegEx
     * @param myString
     * @return List<String>
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
     * @param myRegEx
     * @param myString
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
     * @param myRegEx
     * @param myString
     * @param patternFlags
     * @return boolean
     */
    public static boolean isMatch(String myRegEx, String myString, int patternFlags) {
        Pattern pattern = Pattern.compile(myRegEx, patternFlags);
        boolean found = RegExp.isPatternMatch(pattern, myString);
        return (found);
    }

    /**
     * After you call isMatch() or other public methods, then subExps will
     * contain the LAST group match.
     *
     * @return
     */
    public static List<String> getSubExps() {
        return subExps;
    }

    /**
     * Yes, there is String.replaceAll(), but I wanted a java.utils.regex
     * solution
     *
     * @param myRegEx
     * @param myString
     * @param myReplace
     * @return
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
     * @param myRegEx
     * @param myString
     * @param myReplace
     * @return
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
     * @param myRegEx
     * @param myString
     * @param myReplace
     * @return
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
     * @param pattern
     * @param myString
     * @return
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
