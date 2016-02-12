package fjt.test;

// junit.framework.Test package is the legacy namespace used with Junit v3 and older versions of Java that do not support annotations.
// org.junit.Test is the new namespace used by JUnit v4 and requires Java v1.5 or later for its annotations.
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import fjt.database.MicrosoftSQLImpl;
import fjt.exceptions.RSAException;
import fjt.support.RSA;
import fjt.utils.SysUtils;
import org.junit.Ignore;
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
public class TestMicrosoftImpl {

    private static MicrosoftSQLImpl microsoftSqlImpl;

    /**
     * Do not use the constructor to set up a test case. Use setupClass() method
     * below.
     *
     * The reason is the stack crashes if first test fails to instantiate.
     */
    public TestMicrosoftImpl() {
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
    public static void setUpClass() throws SQLException, RSAException {
        String host = "NTSQLTST09.micron.com";
        String instance = "BOMSSDEVL123";
        String port = "58067";
        String database = "wormhole";
        String username = "WORMHOLE_APP";
        String password = "";
//        String password = "To get an encrypted password place your password here, then uncomment the rsa.encryptBase64 and System calls to see the encrypted string.";
//        String encString = "ovMog5cnR1ixt04IZv38JgUHtR4OqtldMbzmPYTHmdKlyURbVAHhlansJRsufrHZryj13VLuJAZSwIAPNJEoIB6gLO/xW+uxPf+nMcTLp++3gPBYTWtz9TH6jCp9oO1756HyRPdGVfu0ATxM5iUlfvVTjp+qy3j6n2UhdG7w6n8=";

//        RSA rsa = new RSA(Paths.get("src", "main", "resources").toString(), "WormholeWebApp", 1024);
//        rsa.setDebug(true);
//        String encString = rsa.encryptBase64(password);
//        System.out.println("RSA");
//        System.out.println("  ENCODED =>" + encString + "<=");
//        System.out.println("DECRYPTED =>" + rsa.decryptBase64(encString) + "<=");
//        System.exit(1);
//        microsoftSqlImpl = new MicrosoftSQLImpl(host, instance, port, database, username, rsa.decryptBase64(encString));
//        microsoftSqlImpl = new MicrosoftSQLImpl(host, instance, port, database, username, password);
//        microsoftSqlImpl.openConnection();
    }

    /**
     * If you have to clear up resources like closing database, then do it here.
     *
     * @throws java.sql.SQLException
     */
    @AfterClass
    public static void tearDownClass() throws SQLException {
        if (microsoftSqlImpl != null) {
            microsoftSqlImpl.closeConnection();
        }
    }

    /**
     * Login to a sample database.
     *
     * @throws java.sql.SQLException
     */
    @Test
    @Ignore
    public void test() throws SQLException {

        List<String> tablesList = microsoftSqlImpl.getTables();
        System.out.println("-------------------------------------------------");
        SysUtils.displayList(tablesList);
        System.out.println("-------------------------------------------------");

    }

}
