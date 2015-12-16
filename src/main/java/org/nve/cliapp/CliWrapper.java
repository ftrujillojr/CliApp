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
 */
public class CliWrapper {

    private Options options;
    private List<Option> optionList;
    private CommandLineParser parser;
    private Map<String, Object> cliMap;
    private String programName;
    private String emailAddress;
    private String appDescription;

    @SuppressWarnings("RedundantStringConstructorCall")
    public CliWrapper(List<Option> optList, String progName, String appDesc, String emailContact) {
        options = new Options();
        optionList = optList;
        parser = new DefaultParser();
        cliMap = new TreeMap<>();
        programName = new String(progName);
        appDescription = new String(appDesc);
        emailAddress = new String(emailContact);
    }

    public Map<String, Object> parseArgsToMap(String[] args) throws CliWrapperException {
        this.initOptions();
        boolean allowNonOptions = false;  // set to false to throw exception on non arg parsed.  

        try {
            CommandLine cmd = parser.parse(options, args, allowNonOptions);
            this.generateCliMap(cmd);
            
            if ((Integer) cliMap.get("debug") >= 3) {
                this.displayCliMap();
            }

            // If you threw the --help or -h option.
            if ((Boolean) cliMap.get("help")) {
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
        String footer = "\nPlease report any issues to "+  emailAddress + "\n";
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
    }

    private void generateCliMap(CommandLine cmd) throws CliWrapperException  {
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
