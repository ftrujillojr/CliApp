package org.nve.cliapp.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface MyDBInterface {
    
    public void setJDBCString(String jdbcString);
    
    public void setJDBCDriverClass(String jdbcDriverClass);
    
    public void openConnection() throws SQLException;
    
    public int insertUpdateDelete(String sqlString) throws SQLException;
    
    public List<Map<String, String>> executeProcedure(String sqlString) throws SQLException;
    
    public List<Map<String, String>> query(String sqlString) throws SQLException;
    
    public void closeConnection() throws SQLException;
    
}
