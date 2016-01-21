package org.nve.cliapp;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.nve.cliapp.exceptions.MySqlBuildException;
import org.nve.cliapp.utils.RegExp;

public class MySqlBuild {

    public class ColData {
        private String tableRef;
        private String colName;
        private String colAlias;
        private String macroCall;

        public ColData() {
            this.tableRef = "";
            this.colName = "";
            this.colAlias = "";
            this.macroCall = "";
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("%-15s %-15s %-15s %-15s", this.tableRef, this.colName, this.colAlias, this.macroCall));
            return(sb.toString());
        }

        public String getMacroCall() {
            return macroCall;
        }

        public void setMacroCall(String macroCall) {
            if (macroCall != null) {
                this.macroCall = macroCall;
            }
        }

        public String getTableRef() {
            return tableRef;
        }

        public void setTableRef(String tableRef) {
            if (tableRef != null) {
                this.tableRef = tableRef;
            }
        }

        public String getColName() {
            return colName;
        }

        public void setColName(String colName) {
            if (colName != null) {
                this.colName = colName;
            }
        }

        public String getColAlias() {
            return colAlias;
        }

        public void setColAlias(String colAlias) {
            if (colAlias != null) {
                this.colAlias = colAlias;
            }
        }
    }

    private final static String indent = "   ";
    private StringBuilder sqlStatement;
    private Map<String, ColData> columnDataMap;

    public MySqlBuild() {
        this.sqlStatement = new StringBuilder();
        this.columnDataMap = new LinkedHashMap<>();
    }

    public void clear() {
        this.sqlStatement.setLength(0);
    }

    public MySqlBuild bSELECT(String[] columnNames) throws MySqlBuildException {
        this.clear();
        this.sqlStatement.append("SELECT").append("\n");
        List<String> subExps;

        for (int ii = 0; ii < columnNames.length; ii++) {
            if (RegExp.isMatch("([A-Z]+\\(([a-zA-Z0-9\\*\\._]+)\\))(\\s+AS\\s+([a-zA-Z_0-9]+))?", columnNames[ii].trim())) {
                subExps = RegExp.getSubExps();
                if (subExps.size() == 5) {
                    ColData colData = new ColData();
                    colData.setMacroCall(subExps.get(1)); // This one is always defined. See regex above.
                    colData.setColName(subExps.get(2));   //  This is inside #1. Always defined.
                    colData.setColAlias(subExps.get(4));  //  If null, setter method wont set.
                    this.columnDataMap.put(subExps.get(3), colData);
                } else {
                    String msg = "ERROR1: Developer modfied the REGEX without updating the logic OR REGEX is wrong.";
                    throw new MySqlBuildException(msg);
                }
            
            } else if (RegExp.isMatch("(([a-zA-Z0-9]+)\\.)?([a-zA-Z_0-9]+)(\\s+AS\\s+([a-zA-Z_0-9]+))?", columnNames[ii].trim())) {
                subExps = RegExp.getSubExps();

                if (subExps.size() == 6) {
                    ColData colData = new ColData();
                    colData.setTableRef(subExps.get(2)); // if null, setter method wont set.
                    colData.setColName(subExps.get(3)); // This one is always defined.  See regex above.
                    colData.setColAlias(subExps.get(5));// if null, setter method wont set.
                    this.columnDataMap.put(subExps.get(3), colData);
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
        
        Iterator<String> itr = this.columnDataMap.keySet().iterator();
        while(itr.hasNext()) {
            String colName = itr.next();
            ColData colData = this.columnDataMap.get(colName);
            System.out.println(colData.toString() + "\n");
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
