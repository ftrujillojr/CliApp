package fjt.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CommonDBInterface {

    public void openConnection() throws SQLException;
    
    public int insertUpdateDelete(String sqlString) throws SQLException;
    
    public List<Map<String, String>> execute(String sqlString) throws SQLException;
    
    public void executeUpdate(String sqlString) throws SQLException;
    
    public List<Map<String, String>> executeQuery(String sqlString) throws SQLException;
    
    public void closeConnection() throws SQLException;
}
