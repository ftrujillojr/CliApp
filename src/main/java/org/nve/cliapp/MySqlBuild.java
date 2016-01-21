package org.nve.cliapp;

import java.util.List;
import org.nve.cliapp.exceptions.MySqlBuildException;
import org.nve.cliapp.utils.RegExp;

public class MySqlBuild {

    private final static String indent = "   ";
    private StringBuilder sqlStatement;

    public MySqlBuild() {
        this.sqlStatement = new StringBuilder();
    }

    public void clear() {
        this.sqlStatement.setLength(0);
    }

    @Override
    public String toString() {
        return this.sqlStatement.toString();
    }
    
    public MySqlBuild bSELECT(String[] columnNames) throws MySqlBuildException {
        this.clear();
        this.sqlStatement.append("SELECT").append("\n");
        List<String> subExps;

        for (int ii = 0; ii < columnNames.length; ii++) {
            // Do a quick check on columnNames input.
            if (RegExp.isMatch("(([a-zA-Z0-9]+)\\.)?([a-zA-Z_0-9]+)(\\s+AS\\s+([a-zA-Z_0-9]+))?", columnNames[ii].trim())) {
            } else if (RegExp.isMatch("([A-Z]+\\((([a-zA-Z0-9]+)\\.)?([a-zA-Z0-9\\*\\._]+)\\))(\\s+AS\\s+([a-zA-Z_0-9]+))?", columnNames[ii].trim())) {
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
