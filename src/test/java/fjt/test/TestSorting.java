package fjt.test;

// junit.framework.Test package is the legacy namespace used with Junit v3 and older versions of Java that do not support annotations.
// org.junit.Test is the new namespace used by JUnit v4 and requires Java v1.5 or later for its annotations.
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;
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
public class TestSorting {

    /**
     * Do not use the constructor to set up a test case. Use setupClass() method
     * below.
     *
     * The reason is the stack crashes if first test fails to instantiate.
     */
    public TestSorting() {
        // Best practice => Do not put anything here.
    }

    /**
     * Sometimes several tests need to share computationally expensive setup
     * like logging into a database. If you need to do an initialization once
     * before testing all methods.
     */
    @BeforeClass
    public static void setUpClass() {
    }

    /**
     * If you have to clear up resources like closing database, then do it here.
     */
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test sorting
     */
    @Test
    public void testSorting_LastNameSalaryAge() {
        List<Person> originalList = new ArrayList<>();
        originalList.add(new Person("Wile", "Coyote", 80, 1.10, true));
        originalList.add(new Person("Road", "Runner", 81, 100.23, true));
        originalList.add(new Person("Bugs", "Bunny", 100, 5436.34, true));
        originalList.add(new Person("Lola", "Bunny", 100, 200.00, true));
        originalList.add(new Person("Elmer", "Fudd", 100, 200.00, true));
        originalList.add(new Person("Tweety", "Bird", 100, 200.00, true));
        originalList.add(new Person("Yosemite", "Sam", 50, 1000.00, true));
        originalList.add(new Person("Foghorn", "Leghorn", 50, 1000.00, true));
        originalList.add(new Person("Pinky", "Rat", 10, 100.00, true));
        originalList.add(new Person("Brain", "Rat", 10, 200.00, true));

        
        List<Person> expectList = new ArrayList<>();
        expectList.add(new Person("Tweety", "Bird", 100, 200.00, true));
        expectList.add(new Person("Bugs", "Bunny", 100, 5436.34, true));
        expectList.add(new Person("Lola", "Bunny", 100, 200.00, true));
        expectList.add(new Person("Wile", "Coyote", 80, 1.10, true));
        expectList.add(new Person("Elmer", "Fudd", 100, 200.00, true));
        expectList.add(new Person("Foghorn", "Leghorn", 50, 1000.00, true));
        expectList.add(new Person("Brain", "Rat", 10, 200.00, true));
        expectList.add(new Person("Pinky", "Rat", 10, 100.00, true));
        expectList.add(new Person("Road", "Runner", 81, 100.23, true));
        expectList.add(new Person("Yosemite", "Sam", 50, 1000.00, true));

        
        List<Person> expectList2 = new ArrayList<>();
        expectList2.add(new Person("Pinky", "Rat", 10, 100.00, true));
        expectList2.add(new Person("Brain", "Rat", 10, 200.00, true));
        expectList2.add(new Person("Yosemite", "Sam", 50, 1000.00, true));
        expectList2.add(new Person("Foghorn", "Leghorn", 50, 1000.00, true));
        expectList2.add(new Person("Wile", "Coyote", 80, 1.10, true));
        expectList2.add(new Person("Road", "Runner", 81, 100.23, true));
        expectList2.add(new Person("Lola", "Bunny", 100, 200.00, true));
        expectList2.add(new Person("Elmer", "Fudd", 100, 200.00, true));
        expectList2.add(new Person("Tweety", "Bird", 100, 200.00, true));
        expectList2.add(new Person("Bugs", "Bunny", 100, 5436.34, true));
        
        
        List<Person> actualList = new ArrayList<>(originalList);
        List<Person> actualList2 = new ArrayList<>(originalList);

        
        // ACT - test Comparable
        Collections.sort(actualList);

        if (expectList.equals(actualList) == false) {
            displayListsAsDiff(expectList, actualList);
        }

        // ASSERT - Comparable sort works
        assertTrue(expectList.equals(actualList));
        

        
        // ACT - test Comparator
        Collections.sort(actualList2, new PersonSortByAgeSalaryComparator());
        
        if (expectList2.equals(actualList2) == false) {
            displayListsAsDiff(expectList2, actualList2);
        }

        // ASSERT - Comparable sort works
        assertTrue(expectList2.equals(actualList2));
        

    }

    private void displayListsAsDiff(List<Person> expectList, List<Person> actualList) {
        StringBuilder sb = new StringBuilder();
        int expectNum = expectList.size();
        int actualNum = actualList.size();
        int num = (expectNum > actualNum) ? expectNum : actualNum;

        for (int ii = 0; ii < num; ii++) {
            String expectStr = (ii < expectNum) ? expectList.get(ii).toString2() : "(no data for expect)";
            String actualStr = (ii < actualNum) ? actualList.get(ii).toString2() : "(no data for actual)";

            int comp = expectStr.compareTo(actualStr);
            if (comp == 0) {
                sb.append(expectStr).append("   ").append(actualStr).append("\n");
            } else if (comp < 0) {
                sb.append(expectStr).append(" < ").append(actualStr).append("\n");
            } else if (comp > 0) {
                sb.append(expectStr).append(" > ").append(actualStr).append("\n");
            }
        }
        System.out.println(sb.toString());
    }

}
