package fjt.database;

import java.sql.SQLException;


//Download driver from Microsoft, then install in local MAVEN.
//
//$ mvn install:install-file -Dfile=sqljdbc4-4.0.jar -DgroupId=com.microsoft.sqlserver -DartifactId=sqljdbc4 -Dversion=4.0 -Dpackaging=jar
//
//Verify....
//
//$ ll /home/ftrujillo/.m2/repository/com/microsoft/sqlserver/sqljdbc4/4.0/sqljdbc4-4.0.jar
//-rw-r--r-- 1 ftrujillo ftrujillo 584207 Feb 12 13:03 /home/ftrujillo/.m2/repository/com/microsoft/sqlserver/sqljdbc4/4.0/sqljdbc4-4.0.jar
//
//Compile....
//
//Now.....   mvn clean install


public class MicrosoftSQLImpl extends CommonDBAbstract {

    public MicrosoftSQLImpl(String l_host, String l_instance, String l_port, String l_database, String l_username, String l_password) throws SQLException {
        super(l_host, l_instance, l_port, l_database, l_username, l_password);

        StringBuilder sb_urlConnection = new StringBuilder();
        sb_urlConnection.append("jdbc:sqlserver://").append(l_host).append("\\").append(l_instance).append(":").append(l_port).append(";databaseName=").append(l_database).append(";");
        //System.out.println("JDBC => " + sb_urlConnection.toString());
        this.init(sb_urlConnection.toString());
    }

    private void init(String urlConnectionString) {
        this.setJDBCString(urlConnectionString);
        this.setJDBCDriverClass("com.microsoft.sqlserver.jdbc.SQLServerDriver");
    }

}
