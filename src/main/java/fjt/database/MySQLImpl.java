package fjt.database;

import java.sql.SQLException;
import fjt.database.MyDBAbstract;

public class MySQLImpl extends MyDBAbstract {

    public MySQLImpl(String l_host, String l_database, String l_username, String l_password) throws SQLException {
        super(l_host, l_database, l_username, l_password);
        
        StringBuilder sb_urlConnection = new StringBuilder();
        sb_urlConnection.append("jdbc:mysql://").append(l_host).append("/").append(l_database).append("?");
        sb_urlConnection.append("useOldAliasMetadataBehavior=true").append("&");
        sb_urlConnection.append("noAccessToProcedureBodies=true").append("&");
        sb_urlConnection.append("zeroDateTimeBehavior=convertToNull").append("&");
        sb_urlConnection.append("autoReconnect=true").append("&");
        sb_urlConnection.append("characterEncoding=UTF-8").append("&");
        sb_urlConnection.append("characterSetResults=UTF-8").append("&");
        
        this.setJDBCString(sb_urlConnection.toString());
        this.setJDBCDriverClass("com.mysql.jdbc.Driver");
    }


}
