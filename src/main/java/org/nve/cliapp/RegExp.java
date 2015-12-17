package org.nve.cliapp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExp {
    private static List<String> subExps = new ArrayList<>();
    
    public RegExp() {
    }
    
    /**
     * Case sensitive regex match.
     * 
     * @param myRegEx
     * @param myString
     * @return 
     */

    public static boolean isMatch(String myRegEx, String myString) {
        Pattern pattern = Pattern.compile(myRegEx);
        boolean found = isPatternMatch(pattern, myString);
        return (found);
    }

    /**
     * patternFlags are from java.util.regex.Pattern enums.
     * You can OR these as needed and pass into patternFlags in this method.
     * 
     * UNICODE_CASE | UNIX_LINES | CASE_INSENSITIVE | COMMENTS | MULTILINE
     * 
     * Or, you can use these prefixes for the regular expression itself.
     * ?u:            ?d:          ?i:                ?x:        ?m:
     * 
     * String re = "(?i:.*this is case insensitive reg ex.*)";  // MUST HAVE ()
     * 
     * @param myRegEx
     * @param myString
     * @param patternFlags
     * @return 
     */
    public static boolean isMatch(String myRegEx, String myString, int patternFlags) {
        Pattern pattern = Pattern.compile(myRegEx, patternFlags);
        boolean found = isPatternMatch(pattern, myString);
        return (found);
    }
    
    /**
     * After you call isMatch() or other public methods, then subExps will contain
     * the LAST group match.
     * 
     * @return 
     */
    public static List<String> getSubExps() {
        return subExps;
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
