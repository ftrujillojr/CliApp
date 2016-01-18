package org.nve.cliapp.interfaces;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class MyDBAbstract implements MyDBInterface {

    protected String host;
    protected String database;
    protected String username;
    protected String password;
    protected List<String> subExps;
    protected Connection connection = null;
    protected String jdbcString;
    protected String jdbcDriverClass;

    public MyDBAbstract(String host, String dataBase, String userName, String passWord) {
        this.host = host;
        this.database = dataBase;
        this.username = userName;
        this.password = passWord;
        this.subExps = new ArrayList<>();
    }

    @Override
    public void setJDBCString(String jdbcString) {
        this.jdbcString = jdbcString;
    }
    
    @Override
    public void setJDBCDriverClass(String jdbcDriverClass) {
        this.jdbcDriverClass = jdbcDriverClass;
    }
    
    @Override
    public final void openConnection() throws SQLException {
        try {
            // A call to Class.forName("X") causes the class named X to be dynamically loaded (at runtime). 
            // This loads the Driver.
            Class.forName(""); // use dots, not slashes.

            this.connection = DriverManager.getConnection(this.jdbcString, this.username, this.password);
            //
        } catch (SQLException ex) {
            String msg = "ERROR: MySQLImpl.openConnection() for database " + this.database + "\n";
            msg += ex.getMessage();
            throw new SQLException(msg);
        } catch (ClassNotFoundException ex) {
            String msg = "ERROR: MySQLImpl.openConnection() ClassNotFound.  Could not load => com.mysql.jdbc.Driver\n";
            msg += ex.getMessage();
            throw new SQLException(msg);
        }
    }
    
    @Override
    public int insertUpdateDelete(String sqlString) throws SQLException {
        int numAffectedRows = 0;
        try (Statement stmt = (Statement) this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            numAffectedRows = stmt.executeUpdate(sqlString);
        } catch (SQLException ex) {
            String msg = "ERROR: insertUpdateDelete() for database => " + this.database + "\n";
            msg += sqlString + "\n";
            msg += ex.getMessage();
            throw new SQLException(msg);
        }
        return (numAffectedRows);
    }

    @Override
    public List<Map<String, String>> executeProcedure(String sqlString) throws SQLException {
        List<Map<String, String>> resultsList = new ArrayList<>();

        try (CallableStatement stmt = this.connection.prepareCall(sqlString)) {
            boolean more_results = stmt.execute();

            //Loop through the available result sets.
            while (more_results) {
                try (ResultSet rs = stmt.getResultSet()) {
                    List<Map<String, String>> tmpResultsList = this.convertResultSet2List(rs);
                    resultsList.addAll(tmpResultsList);
                }
                //Check for next result set
                more_results = stmt.getMoreResults();
            }
        } catch (SQLException ex) {
            String msg = "ERROR: executeProcedure() for database => " + this.database + "\n";
            msg += sqlString + "\n";
            msg += ex.getMessage();
            throw new SQLException(msg);
        }

        return resultsList;
    }

    @Override
    public List<Map<String, String>> query(String sqlString) throws SQLException {
        List<Map<String, String>> resultsArray = null;
        try (Statement stmt = (Statement) this.connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY)) {
            try (ResultSet rs = stmt.executeQuery(sqlString)) {
                resultsArray = this.convertResultSet2List(rs);
            }
        } catch (SQLException ex) {
            String msg = "ERROR: query() for database => " + this.database + "\n";
            msg += sqlString + "\n";
            msg += ex.getMessage();
            throw new SQLException(msg);
        }
        return (resultsArray);
    }

    @Override
    public void closeConnection() throws SQLException {
        this.connection.close();
    }
    

    public void displayCSV(List<Map<String, String>> arrayOfMaps) {
        List<String> resultsList = this.toCSV(arrayOfMaps);
        Iterator<String> itr = resultsList.listIterator();
        while (itr.hasNext()) {
            String line = itr.next();
            System.out.println(line);
        }
    }

    public List<String> toCSV(List<Map<String, String>> arrayOfMaps) {
        List<String> outputList = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        Iterator<Map<String, String>> itrList = arrayOfMaps.listIterator();
        boolean firstTime = true;
        Set<String> prevColumnNames = new TreeSet<>();

        // EACH line
        while (itrList.hasNext()) {
            Map<String, String> map = itrList.next();

            Set<String> columnNames = map.keySet();
            if (columnNames.containsAll(prevColumnNames) == false) {
                firstTime = true;
            }

            if (firstTime) {
                prevColumnNames.clear();
                Iterator<String> setItr = columnNames.iterator();
                while (setItr.hasNext()) {
                    String columnName = setItr.next();
                    prevColumnNames.add(columnName);
                    columnName = columnName.replaceAll("[\\n]+", ";");
                    sb.append(columnName).append(",");
                }
                sb.replace(sb.length() - 1, sb.length(), "");
                outputList.add(sb.toString());
                sb.setLength(0);
                firstTime = false;
            }

            Iterator<String> itrMap = map.keySet().iterator();

            // CONTAINS a hash map of data
            while (itrMap.hasNext()) {
                String key = itrMap.next();
                String val = map.get(key);
                sb.append(val).append(",");
            }
            if (sb.length() > 2) {
                sb.replace(sb.length() - 1, sb.length(), "");
                outputList.add(sb.toString());
                sb.setLength(0);
            }
        }

        return outputList;
    }

    public void displayArrayListOfMaps(List<Map<String, String>> arrayOfMaps) {
        Iterator<Map<String, String>> itr2 = arrayOfMaps.listIterator();
        while (itr2.hasNext()) {
            Map<String, String> map = itr2.next();
            this.displayMap(map);
        }
    }

    public void displayMap(Map<String, String> map) {
        Iterator<String> itr = map.keySet().iterator();
        while (itr.hasNext()) {
            String key = itr.next();
            String val = map.get(key);
            System.out.println(String.format("%45s => %s", key, val));
        }
        System.out.println("");
    }

    // ============================  PROTECTED ===========================================
    
    protected List<String> determineColumnNames(ResultSet rs) throws SQLException {
        ArrayList<String> columnNamesList = new ArrayList<>();
        int columnCount = rs.getMetaData().getColumnCount();
        for (int colNo = 1; colNo <= columnCount; colNo++) {
            String columnName = rs.getMetaData().getColumnName(colNo);
            columnNamesList.add(columnName);
        }
        return columnNamesList;
    }

    @SuppressWarnings("RedundantStringConstructorCall")
    protected String validateEncoding(String encoding) {
        String enc = "UTF-8";

        if (encoding != null) {
            switch (encoding) {
                case "ISO-8859-1":
                case "US-ASCII":
                case "UTF-16":
                case "UTF-16BE":
                case "UTF-16LE":
                case "UTF-8":
                    enc = new String(encoding);
                    break;
                default:
                // just use UTF-8 above.
            }
        }
        return enc;
    }

    protected List<Map<String, String>> convertResultSet2List(ResultSet rs) throws SQLException {
        List<Map<String, String>> resultsArray = new ArrayList<>();
        List<String> columnNamesList = determineColumnNames(rs);

        // create data rows
        while (rs.next()) {
            Map<String, String> rowMap = new LinkedHashMap<>();
            for (int ii = 0; ii < columnNamesList.size(); ii++) {
                String columnName = columnNamesList.get(ii);
                String columnValue = rs.getString(columnNamesList.get(ii));
                if (this.isMatch(".+_date", columnName) && (columnValue == null || columnValue.isEmpty())) {
                    columnValue = "0000-00-00";
                }
                if (columnValue == null) {
                    columnValue = "NULL";
                }
                rowMap.put(columnName, columnValue);
            }
            resultsArray.add(rowMap);
        }

        return (resultsArray);
    }

    protected boolean isMatch(String myRegEx, String myString) {
        this.subExps.clear();
        Pattern pattern = Pattern.compile(myRegEx);
        Matcher matcher = pattern.matcher(myString);
        boolean found = matcher.matches();

        if (found) {
            Integer numGroups = matcher.groupCount();
            for (int ii = 0; ii <= numGroups; ii++) {
                this.subExps.add(matcher.group(ii));
            }
        }

        return (found);
    }

}
