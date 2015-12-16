package org.nve.cliapp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.cli.Option;

public class Main {

    public Main() {
    }

    public static void main(String[] args) {
        List<Option> optionList = new ArrayList<>();

        optionList.add(Option.builder() // short option here. blank for no short option
                .longOpt("debug") // long option here
                .required(false)
                .hasArg(true)
                .argName("LEVEL")
                .desc("Debug  1,2,3,...")
                .type(Integer.class)
                .build());

        optionList.add(Option.builder("h")
                .longOpt("help")
                .required(false)
                .hasArg(false)
                .desc("This help message")
                .type(Boolean.class)
                .build());

        // CliWrapper is based on Apache CLI with code to check for duplicate keys.
        // That functionality is missing in their implementation.
        CliWrapper cliWrapper = new CliWrapper(
                optionList, 
                "CliApp",
                "This app is a base line app to building Java CLI applications.",
                "me@somecompany.org"
        );

        try {
            // cliWrapper.parseArgsToMap() will display usage prior to throwing 
            // exceptions and also if --help or -h is thrown.
            Map<String, Object> cliMap = cliWrapper.parseArgsToMap(args);
            // If you get this far, then cliMap will contain your parsed values.

            if (cliMap.containsKey("debug")) {
                // If any of the values in cliMap are Double, Integer, or Boolean; then, they are
                // already converted for you.  You will have to type cast to quiet the compiler.
                Integer debug = (Integer) cliMap.get("debug");
            }

            // Your code here.
            
            System.exit(0);

        } catch (CliWrapperException ex) {
            System.out.println(ex.getMessage());
            System.exit(10);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            System.exit(11);
        }
    }

}
