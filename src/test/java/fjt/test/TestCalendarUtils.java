package fjt.test;

// junit.framework.Test package is the legacy namespace used with Junit v3 and older versions of Java that do not support annotations.
// org.junit.Test is the new namespace used by JUnit v4 and requires Java v1.5 or later for its annotations.
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import fjt.comparators.CalendarComparator;
import fjt.exceptions.CalendarUtilsException;
import fjt.utils.CalendarUtils;
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
public class TestCalendarUtils {

    private static Long epoch1;
    private static Long epoch2;
    private static Long epoch3;
    private static Calendar cal1;
    private static Calendar cal2;
    private static Calendar cal3;
    private static CalendarComparator calComp;

    /**
     * Do not use the constructor to set up a test case. Use setupClass() method
     * below.
     *
     * The reason is the stack crashes if first test fails to instantiate.
     */
    public TestCalendarUtils() {
        // Best practice => Do not put anything here.
    }

    /**
     * Sometimes several tests need to share computationally expensive setup
     * like logging into a database. If you need to do an initialization once
     * before testing all methods.
     */
    @BeforeClass
    public static void setUpClass() {
        epoch1 = 1404677706L;
        cal1 = Calendar.getInstance();
        cal1.setTimeInMillis(epoch1 * 1000L);

        epoch2 = 1415294110L;
        cal2 = Calendar.getInstance();
        cal2.setTimeInMillis(epoch2 * 1000L);

        epoch3 = 1415381809L;
        cal3 = Calendar.getInstance();
        cal3.setTimeInMillis(epoch3 * 1000L);

        calComp = new CalendarComparator();
    }

    /**
     * If you have to clear up resources like closing database, then do it here.
     */
    @AfterClass
    public static void tearDownClass() {
    }
    
    /**
     * Test of copy method, of class CalendarUtils.
     */
    @Test
    public void testCopy() {
        Calendar expResult = cal1;
        Calendar result = CalendarUtils.copy(cal1);
        if (calComp.objectsSame(expResult, result)) {
            fail("FAILED to create a NEW instance of Calendar using CalendarUtils.copy()");
        }
        assertEquals(expResult, result);
    }

    @Test
    public void testCopyNegative() {
        Calendar expResult = cal1;
        Calendar result = CalendarUtils.copy(cal2);
        if (calComp.objectsSame(expResult, result)) {
            fail("FAILED to create a NEW instance of Calendar using CalendarUtils.copy()");
        }
        assertNotEquals(expResult, result);
    }

    /**
     * Test of parse method, of class CalendarUtils.
     * @throws java.lang.Exception
     */
    @Test
    public void testParse() throws Exception {
        HashMap<String, Long> formatStrings = new HashMap<>();
        formatStrings.put("Wed Nov 05 13:24:44 2014", 1415219084L);
        formatStrings.put("Nov 05 2014", 1415170800L);
        formatStrings.put("Nov 05 2014 12:23:12", 1415215392L);
        formatStrings.put("Wed Nov 05 13:24:44 2014 UTC -0000", 1415193884L);
        formatStrings.put("Thu Nov 6 17:15:10 2014 UTC -0000", 1415294110L);
        formatStrings.put("Nov 05 13:24:44 2014", 1415219084L);
        formatStrings.put("2014-11-06 09:38:41", 1415291921L);
        formatStrings.put("2014-11-06 09:38:41 UTC", 1415266721L);
        formatStrings.put("11-06-2014 09:38:41", 1415291921L);
        formatStrings.put("11-06-2014 09:38:41 UTC", 1415266721L);
        formatStrings.put("11/05/2014 12:23:12 UTC", 1415190192L);
        formatStrings.put("11/05/2014 12:23:12", 1415215392L);
        formatStrings.put("11/05/2014", 1415170800L);
        formatStrings.put("1/05/2014", 1388905200L);
        formatStrings.put("1/5/2014", 1388905200L);

        Iterator<String> itr = formatStrings.keySet().iterator();
        while (itr.hasNext()) {
            String formatStr = itr.next();
            Long expResult = formatStrings.get(formatStr);
            
            Calendar cal = CalendarUtils.parse(formatStr);
            Long result = CalendarUtils.toEpoch(cal);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of toCalendar method, of class CalendarUtils.
     */
    @Test
    public void testToCalendar() {
        Long epoch = epoch1;
        Calendar expResult = cal1;
        Calendar result = CalendarUtils.toCalendar(epoch);
        assertEquals(expResult, result);
    }

    /**
     * Test of toEpoch method, of class CalendarUtils.
     */
    @Test
    public void testToEpoch() {
        Long expResult = epoch1;
        Long result = CalendarUtils.toEpoch(cal1);
        assertEquals(expResult, result);
    }

    /**
     * Test of toMDY method, of class CalendarUtils.
     */
    @Test
    public void testToMDY() {
        String expResult = "Jul 06 2014";
        String result = CalendarUtils.toMDY(cal1);
        assertEquals(expResult, result);
    }

    /**
     * Test of toLocal method, of class CalendarUtils.
     */
    @Test
    public void testToLocal() {
        String expResult = "Sun Jul 06 14:15:06 2014 MDT -0600";
        String result = CalendarUtils.toLocal(cal1);
        assertEquals(expResult, result);
    }

    /**
     * Test of toBoise method, of class CalendarUtils.
     */
    @Test
    public void testToBoise() {
        String expResult = "Thu Nov 06 10:15:10 2014 MST -0700";
        String result = CalendarUtils.toBoise(cal2);
        assertEquals(expResult, result);
    }

    /**
     * Test of toSingapore method, of class CalendarUtils.
     */
    @Test
    public void testToSingapore() {
        String expResult = "Fri Nov 07 01:15:10 2014 SGT +0800";
        String result = CalendarUtils.toSingapore(cal2);
        assertEquals(expResult, result);
    }

    /**
     * Test of toGMT method, of class CalendarUtils.
     */
    @Test
    public void testToGMT() {
        String expResult = "Sun Jul 06 20:15:06 2014 UTC +0000";
        String result = CalendarUtils.toGMT(cal1);
        assertEquals(expResult, result);
    }

    /**
     * Test of toMySQL method, of class CalendarUtils.
     */
    @Test
    public void testToMySQL() {
        String expResult = "2014-07-06 14:15:06";
        String result = CalendarUtils.toMySQL(cal1);
        assertEquals(expResult, result);
    }

    /**
     * Test of getWW method, of class CalendarUtils.
     */
    @Test
    public void testGetWW() throws Exception {
        HashMap<String, Integer> dateWWMap = new HashMap<String, Integer>();
        dateWWMap.put("Jan 09 2014", 1);
        dateWWMap.put("Jan 10 2014", 2);
        dateWWMap.put("Jul 24 2014", 29);
        dateWWMap.put("Jul 25 2014", 30);
        dateWWMap.put("Jan 01 2015", 52);
        dateWWMap.put("Jan 02 2015", 1);
        dateWWMap.put("May 14 2015", 19);
        dateWWMap.put("May 15 2015", 20);
        dateWWMap.put("Jan 01 2016", 1);

        Iterator<String> itr = dateWWMap.keySet().iterator();
        while (itr.hasNext()) {
            String calString = itr.next();
            Integer expResult = dateWWMap.get(calString);
            Calendar calendar = CalendarUtils.parse(calString);
            Integer result = CalendarUtils.getWW(calendar);
            assertEquals(expResult, result);
        }
    }

    /**
     * Test of ctime method, of class CalendarUtils.
     */
    @Test
    public void testCtime() {
        String expResult = "Thu Nov 06 10:15:10 2014 MST -0700";
        String result = CalendarUtils.ctime(epoch2);
        assertEquals(expResult, result);
    }

    /**
     * Test of zeroOutTime method, of class CalendarUtils.
     */
    @Test
    public void testZeroOutTime() {
        Calendar calendar = CalendarUtils.copy(cal1);
        CalendarUtils.zeroOutTime(calendar);

        int hour = calendar.get(Calendar.HOUR);                // 12 hour clock
        assertEquals(0, hour);
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);    // 24 hour clock
        assertEquals(0, hourOfDay);
        int minute = calendar.get(Calendar.MINUTE);
        assertEquals(0, minute);
        int second = calendar.get(Calendar.SECOND);
        assertEquals(0, second);
        int millisecond = calendar.get(Calendar.MILLISECOND);
        assertEquals(0, millisecond);

    }

    /**
     * Test of isEqualByDateOnly method, of class CalendarUtils.
     * 
     * @throws java.text.ParseException
     * @throws fjt.exceptions.CalendarUtilsException
     */
    @Test
    public void testIsEqualByDateOnly() throws ParseException, CalendarUtilsException {
        Calendar c1 = CalendarUtils.parse("Thu Nov 06 05:05:27 2014 MST -0700");
        Calendar c2 = CalendarUtils.parse("Thu Nov 06 01:30:00 2014 MST -0700");
        boolean expResult = true;
        boolean result = CalendarUtils.isEqualByDateOnly(c1, c2);
        assertEquals(expResult, result);
    }

    /**
     * Test of isEqualByDateOnlyNeg method, of class CalendarUtils.
     *
     * @throws java.text.ParseException
     * @throws fjt.exceptions.CalendarUtilsException
     */
    @Test
    public void testIsEqualByDateOnlyNeg() throws ParseException, CalendarUtilsException {
        Calendar c1 = CalendarUtils.parse("Thu Nov 06 05:05:27 2014 MST -0700");
        Calendar c2 = CalendarUtils.parse("Thu Nov 06 01:30:00 2015 MST -0700");
        boolean expResult = true;
        boolean result = CalendarUtils.isEqualByDateOnly(c1, c2);
        assertNotEquals(expResult, result);
    }

    /**
     * Test of isEqual method, of class CalendarUtils.
     *
     * @throws java.text.ParseException
     * @throws fjt.exceptions.CalendarUtilsException
     */
    @Test
    public void testIsEqual() throws ParseException, CalendarUtilsException {
        Calendar c1 = CalendarUtils.parse("Thu Nov 06 05:05:27 2014 MST -0700");
        Calendar c2 = CalendarUtils.parse("Thu Nov 06 05:05:27 2014 MST -0700");
        boolean expResult = true;
        boolean result = CalendarUtils.isEqual(c1, c2);
        assertEquals(expResult, result);
    }

    /**
     * Test of isEqualNeg method, of class CalendarUtils.
     *
     * @throws java.text.ParseException
     * @throws fjt.exceptions.CalendarUtilsException
     */
    @Test
    public void testIsEqualNeg() throws ParseException, CalendarUtilsException {
        Calendar c1 = CalendarUtils.parse("Thu Nov 06 05:05:27 2014 MST -0700");
        Calendar c2 = CalendarUtils.parse("Thu Nov 06 05:05:26 2014 MST -0700");
        boolean expResult = false;
        boolean result = CalendarUtils.isEqual(c1, c2);
        assertEquals(expResult, result);
    }

    /**
     * Test of isOlder method, of class CalendarUtils.
     */
    @Test
    public void testIsOlder() {
        boolean expResult = false;
        boolean result = CalendarUtils.isOlder(cal1, cal2);
        assertEquals(expResult, result);
    }

    /**
     * Test of isNewer method, of class CalendarUtils.
     */
    @Test
    public void testIsNewer() {
        boolean expResult = true;
        boolean result = CalendarUtils.isNewer(cal1, cal2);
        assertEquals(expResult, result);
    }

    /**
     * Test of isBetween method, of class CalendarUtils.
     */
    @Test
    public void testIsBetween() {
        boolean expResult = true;
        boolean result = CalendarUtils.isBetween(cal2, cal1, cal3);
        assertEquals(expResult, result);
    }

    /**
     * Test of isBetween method, of class CalendarUtils.
     */
    @Test
    public void testIsBetweenNeg() {
        boolean expResult = false;
        boolean result = CalendarUtils.isBetween(cal1, cal2, cal3);
        assertEquals(expResult, result);
    }

}
