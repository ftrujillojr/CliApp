package org.nve.cliapp;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.nve.cliapp.exceptions.MySqlBuildException;
import org.nve.cliapp.utils.RegExp;
import org.nve.cliapp.utils.SysUtils;

public class MySqlBuild {

    private final static String indent = "   ";
    private StringBuilder sqlStatement;
    private Map<String, String> nameAliasMap;
    private Map<String, String> nameTableRefMap;
    private Map<String, String> nameMacroMap;

    public MySqlBuild() {
        this.sqlStatement = new StringBuilder();
        this.nameAliasMap = new LinkedHashMap<>();
        this.nameTableRefMap = new LinkedHashMap<>();
        this.nameMacroMap = new LinkedHashMap<>();
    }

    public void clear() {
        this.sqlStatement.setLength(0);
    }

    public MySqlBuild bSELECT(String[] columnNames) throws MySqlBuildException {
        this.clear();
        this.sqlStatement.append("SELECT").append("\n");
        List<String> subExps;

        for (int ii = 0; ii < columnNames.length; ii++) {
            if (RegExp.isMatch("([A-Z]+\\((([a-zA-Z0-9]+)\\.)?([a-zA-Z0-9\\*\\._]+)\\))(\\s+AS\\s+([a-zA-Z_0-9]+))?", columnNames[ii].trim())) {
                subExps = RegExp.getSubExps();
                SysUtils.displayList(subExps);
                System.out.println("=======================");
                if (subExps.size() == 7) {

//                    this.nameTableRefMap.put(subExps.get(3), "");
//                    this.nameAliasMap.put(subExps.get(4), (subExps.get(5) == null)?"":subExps.get(5));
                } else {
                    String msg = "ERROR1: Developer modfied the REGEX without updating the logic OR REGEX is wrong.";
                    throw new MySqlBuildException(msg);
                }

            } else if (RegExp.isMatch("(([a-zA-Z0-9]+)\\.)?([a-zA-Z_0-9]+)(\\s+AS\\s+([a-zA-Z_0-9]+))?", columnNames[ii].trim())) {
                subExps = RegExp.getSubExps();
                SysUtils.displayList(subExps);
                System.out.println("=======================");

                if (subExps.size() == 6) {

                } else {
                    String msg = "ERROR2: Developer modfied the REGEX without updating the logic OR REGEX is wrong.";
                    throw new MySqlBuildException(msg);
                }

            } else if (RegExp.isMatch("([\\*])", columnNames[ii].trim())) {
            } else {
                String msg = "ERROR3: ColumnName is invalid => " + columnNames[ii];
                throw new MySqlBuildException(msg);
            }

            this.sqlStatement.append(indent).append(columnNames[ii]).append("\n");
        }

        return (this);
    }

    public MySqlBuild bFROM(String[] tableNames) {
        this.sqlStatement.append("FROM").append("\n");
        for (int ii = 0; ii < tableNames.length; ii++) {
            this.sqlStatement.append(indent).append(tableNames[ii]).append("\n");
        }
        return (this);
    }

}
