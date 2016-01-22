package org.nve.cliapp_test;

// junit.framework.Test package is the legacy namespace used with Junit v3 and older versions of Java that do not support annotations.
// org.junit.Test is the new namespace used by JUnit v4 and requires Java v1.5 or later for its annotations.
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.nve.cliapp.MySqlBuild;
import org.nve.cliapp.exceptions.MySqlBuildException;
import org.nve.cliapp.interfaces.MySQLImpl;
import org.nve.cliapp.utils.RegExp;
//import org.junit.Ignore;

/*   ARRANGE    ACT    ASSERT

 To run tests:
 ============================
 mvn test
 mvn -Dtest=TestCircle test
 (in Netbeans, Alt+F6)

 Your pom.xml should have this plugin.

 <plugin>
 <groupId>org.apache.maven.plugins</groupId>
 <artifactId>maven-surefire-plugin</artifactId>
 <version>2.12.4</version>
 <configuration>
 <skipTests>false</skipTests>
 </configuration>
 </plugin>
 */
/**
 * <pre>
 * http://junit.org/javadoc/latest/index.html     (JUnit must be >= 4.11)
 *
 * assertArrayEquals(String msg,    **[] expect,   **[] actual)
 *      assertEquals(String msg,    ** expect,     ** actual)
 *   assertNotEquals(String msg,    ** unexpected, ** actual)
 *        assertTrue(String msg,    boolean condition)
 *       assertFalse(String msg,    boolean condition)
 *     assertNotNull(String msg,    Object object)
 *     assertNotSame(String msg,    Object unexpected, Object actual)
 *        assertNull(String msg,    Object object)
 *        assertSame(String msg,    Object expected, Object actual)
 *        assertThat(String reason, T actual, Match<T> matcher)
 *              fail()
 *              fail(String msg)
 *
 *
 * Matchers are used in assertThat() calls.
 *
 * http://junit.org/javadoc/latest/org/hamcrest/core/package-frame.html
 *
 *   assertThat("myValue",                          allOf(startsWith("my"), containsString("Val")))
 *   assertThat("myValue",                          anyOf(startsWith("foo"), containsString("Val")))
 *   assertThat("fab",                              both(containsString("a")).and(containsString("b")))
 *   assertThat("fab",                              both(containsString("a")).or(containsString("b")))
 *   assertThat("fab",                              either(containsString("a")).and(containsString("b")))
 *   assertThat(Arrays.asList("bar", "baz"),        everyItem(startsWith("ba")))
 *   assertThat(cheese,                             is(equalTo(smelly)))
 *   assertThat(Arrays.asList("foo", "bar"),        hasItem(startsWith("ba")))
 *   assertThat(Arrays.asList("foo", "bar", "baz"), hasItems(endsWith("z"), endsWith("o")))
 *   assertThat("foo",                              equalTo("foo"));
 *   assertThat(new Canoe(),                        instanceOf(Paddlable.class));
 *   assertThat(cheese,                             is(not(equalTo(smelly))))
 *   assertThat("myStringOfNote",                   containsString("ring"))
 *   assertThat("myStringOfNote",                   endsWith("Note"))
 *   assertThat("myStringOfNote",                   startsWith("my"))
 *
 * </pre>
 */
public class TestMySQLImpl {

    private static MySQLImpl mySQLImpl;

    /**
     * Do not use the constructor to set up a test case. Use setupClass() method
     * below.
     *
     * The reason is the stack crashes if first test fails to instantiate.
     */
    public TestMySQLImpl() {
        // Best practice => Do not put anything here.
    }

    /**
     * Sometimes several tests need to share computationally expensive setup
     * like logging into a database. If you need to do an initialization once
     * before testing all methods.
     *
     * @throws java.sql.SQLException
     */
    @BeforeClass
    public static void setUpClass() throws SQLException {
        String host = "nsglnxdev1.micron.com";
        String database = "tmpuser";
        String username = "tmpuser";
        String password = "tmpuser";

        mySQLImpl = new MySQLImpl("nsglnxdev1.micron.com", "tmpuser", "tmpuser", "tmpuser");
        mySQLImpl.openConnection();
    }

    /**
     * If you have to clear up resources like closing database, then do it here.
     *
     * @throws java.sql.SQLException
     */
    @AfterClass
    public static void tearDownClass() throws SQLException {
        mySQLImpl.closeConnection();
    }

    /**
     * Login to a sample database.
     *
     * @throws java.sql.SQLException
     */
    @Test
    public void test() throws SQLException {

        mySQLImpl.executeUpdate("DROP TABLE IF EXISTS `tmpuser`.`Person`;");

        StringBuilder sb = new StringBuilder();
        sb.append("CREATE  TABLE IF NOT EXISTS `tmpuser`.`Person` (").append("\n");
        sb.append("`id` INT NOT NULL AUTO_INCREMENT ,").append("\n");
        sb.append("`first_name` VARCHAR(45) NULL ,").append("\n");
        sb.append("`last_name` VARCHAR(45) NULL ,").append("\n");
        sb.append("`age` INT NULL ,").append("\n");
        sb.append("`salary` FLOAT NULL ,").append("\n");
        sb.append("`is_student` TINYINT(1) NULL ,").append("\n");
        sb.append("PRIMARY KEY (`id`) ,").append("\n");
        sb.append("INDEX `name_idx` (`last_name` ASC, `first_name` ASC) )").append("\n");
        sb.append("ENGINE = InnoDB;").append("\n");
        mySQLImpl.executeUpdate(sb.toString());

        String insPerson = "INSERT INTO\n"
                + "   `tmpuser`.`Person`\n"
                + "   ( `first_name`, `last_name`, `age`, `salary`, `is_student` )\n"
                + "VALUES\n"
                + "(__REPLACE__);";

        String[] rowsToInsert = {
            "'Bugs', 'Bunny', 51, 8.50, 0",
            "'Elmer', 'Fudd', 52, 19.00, 0",
            "'Spongebob', 'Squarepants', 10, 1000.01, 1",
            "'Patrick', 'Starfish', 12, 1.25, 1"
        };

        int numRows = 0;
        for (int ii = 0; ii < rowsToInsert.length; ii++) {
            String tmpStr = RegExp.replaceFirst("__REPLACE__", insPerson, rowsToInsert[ii]);
            numRows += mySQLImpl.insertUpdateDelete(tmpStr);
        }

        if (numRows != 4) {
            System.out.println("ERROR:  Expected 4 Rows, Inserted " + numRows + " Rows");
        }

        assertEquals(4, numRows);

        String sqlString = "SELECT * FROM Person;";
        List<Map<String, String>> results = mySQLImpl.executeQuery(sqlString);

        if (results.size() != 4) {
            System.out.println("ERROR: Expected 4 Rows.  Returned " + results.size() + " Rows.\n");
            mySQLImpl.displayArrayListOfMaps(results);
            System.out.println("===========================");
            mySQLImpl.displayCSV(results);
        } else {
            //mySQLImpl.displayArrayListOfMaps(results);
        }

        assertEquals(4, results.size());

        String jsonResults = mySQLImpl.executeQueryToJson(sqlString);

        System.out.println("JSON RESULTS\n" + jsonResults);
    }

    @Test
    public void mysqlbuild() throws MySqlBuildException {
        MySqlBuild mySqlBuild = new MySqlBuild();

        System.out.println(
                mySqlBuild.SELECT(new String[]{
                    "first_name",
                    "last_name",
                    "age",
                }).FROM(new String[] {
                    "Person AS p"
                }).toString()
        );

    }

}
