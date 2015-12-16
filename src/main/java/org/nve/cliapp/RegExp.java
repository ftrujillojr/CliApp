package org.nve.cliapp;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExp {
    private static List<String> subExps = new ArrayList<>();
    
    public RegExp() {
    }

    public static boolean isMatch(String myRegEx, String myString) {
        subExps.clear();
        Pattern pattern = Pattern.compile(myRegEx);
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

    public static List<String> getSubExps() {
        return subExps;
    }
}
