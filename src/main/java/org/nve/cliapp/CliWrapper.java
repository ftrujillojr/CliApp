package org.nve.cliapp;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 *
 * Need help on Apache Cli
 *
 * http://www.javaworld.com/article/2072482/command-line-parsing-with-apache-commons-cli.html
 *
 * https://commons.apache.org/proper/commons-cli/javadocs/api-release/org/apache/commons/cli/package-tree.html
 *
 * <pre>
 * {@code
 *     public static void main(String[] args) {
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
 * </pre>
 */
@SuppressWarnings("FieldMayBeFinal")
public class  CliWrapper {
    private Options options;
    private List<Option> optionList;
    private CommandLineParser parser;
    private Map<String, Object> cliMap;
    private String programName;
    private String emailAddress;
    private String appDescription;
    private Boolean allowNonOptions;

    @SuppressWarnings("RedundantStringConstructorCall")
    public CliWrapper(List<Option> optList, String progName, String appDesc, String emailContact) {
        options = new Options();
        optionList = optList;
        parser = new DefaultParser();
        cliMap = new TreeMap<>();
        programName = new String(progName);
        appDescription = new String(appDesc);
        emailAddress = new String(emailContact);
        allowNonOptions = false;  // set to false to throw exception on non arg parsed.  
    }

    public void setAllowNonOptions() {
        allowNonOptions = true; // This will allow non options to fall through without throwing exception.
    }

    public void clrAllowNonOptions() {
        allowNonOptions = false; // This is the default and WILL throw exception if command line switch not in options list.
    }

    public Map<String, Object> parseArgsToMap(String[] args) throws CliWrapperException {
        this.initOptions();

        try {
            CommandLine cmd = parser.parse(options, args, allowNonOptions);
            this.generateCliMap(cmd);

            if (cliMap.containsKey("debug") && (Integer) cliMap.get("debug") >= 3) {
                this.displayCliMap();
            }

            // If you threw the --help or -h option.
            if (cliMap.containsKey("help") && (Boolean) cliMap.get("help")) {
                this.displayUsage();
            }

        } catch (ParseException ex) {
            String msg = "ERROR: parseArgsToMap(args) could not parse arguments based on optionsList";
            msg += ex.getMessage();
            throw new CliWrapperException(msg);
        }
        return (cliMap);
    }

    private void displayUsage() {
        HelpFormatter formatter = new HelpFormatter();
        System.out.println("");
        String header = "\n" + appDescription + "\n\n";
        String footer = "\nPlease report any issues to " + emailAddress + "\n";
        int width = 132;
        formatter.printHelp(width, programName, header, options, footer, true);
    }

    private void displayCliMap() {
        Iterator<String> itr = cliMap.keySet().iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            String val = cliMap.get(key).toString();
            System.out.println(String.format("%25s: %s", key, val));
        }
        System.out.println("");
    }

    private void initOptions() throws CliWrapperException {
        Iterator<Option> itr = optionList.iterator();
        Set<String> shortArgs = new TreeSet<>();
        Set<String> longArgs = new TreeSet<>();

        while (itr.hasNext()) {
            Option option = itr.next();
            String key = option.getOpt();
            String lkey = option.getLongOpt();

            if (key != null) {
                if (shortArgs.contains(key)) {
                    String msg = "ERROR:  Duplicate SHORT arg => " + key;
                    throw new CliWrapperException(msg);
                } else {
                    shortArgs.add(key);
                }
            }

            if (lkey != null) {
                if (longArgs.contains(lkey)) {
                    String msg = "ERROR:  Duplicate LONG arg => " + lkey;
                    throw new CliWrapperException(msg);
                } else {
                    longArgs.add(lkey);
                }
            }

            // If you get this far, then no duplicates.  Just add option.
            // This entire method should be part of Apache CLI in addOption method.
            options.addOption(option);
        }

        // if developer forgot --help, then add it.
        if (longArgs.contains("help") == false) {
            options.addOption(Option.builder()
                    .longOpt("help")
                    .required(false)
                    .hasArg(false)
                    .desc("This help message")
                    .type(Boolean.class)
                    .build());
        }
    }

    private void generateCliMap(CommandLine cmd) throws CliWrapperException {
        Collection<Option> collection = options.getOptions();  // Original Options
        Iterator<Option> itr = collection.iterator();

        while (itr.hasNext()) {
            Option l_option = itr.next();
            String key = (l_option.hasLongOpt()) ? l_option.getLongOpt() : l_option.getOpt();
            String type = l_option.getType().toString();
            String val = cmd.getOptionValue(key);

            switch (type) {
                case "class java.lang.Boolean":
                    val = (cmd.hasOption(key)) ? "true" : "false";
                    cliMap.put(key, Boolean.parseBoolean(val));
                    break;
                case "class java.lang.Integer":
                    if (cmd.hasOption(key) == false) {
                        val = "0";
                    }
                    try {
                        cliMap.put(key, Integer.parseInt(val));
                    } catch (NumberFormatException ex) {
                        String msg = "\nERROR: CLI parse error for KEY[" + key + "].  The VALUE should have been an Integer.  The VALUE was => " + val + "\n";
                        throw new CliWrapperException(msg);
                    }
                    break;
                case "class java.lang.Double":
                    if (cmd.hasOption(key) == false) {
                        val = "0.0";
                    }
                    try {
                        cliMap.put(key, Double.parseDouble(val));
                    } catch (NumberFormatException ex) {
                        String msg = "\nERROR: CLI parse error for KEY[" + key + "].  The VALUE should have been a Double.  The VALUE was => " + val + "\n";
                        throw new CliWrapperException(msg);
                    }
                    break;
                default:
                    if (cmd.hasOption(key) == false) {
                        val = "";
                    }
                    cliMap.put(key, val);
                    break;
            }
        }
    }
}
