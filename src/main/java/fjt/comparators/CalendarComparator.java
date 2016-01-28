package fjt.comparators;

import java.util.Calendar;
import java.util.Comparator;

public class CalendarComparator implements Comparator<Calendar> {

    public CalendarComparator() {
    }

    @Override
    public int compare(Calendar o1, Calendar o2) {
        long aValue = o1.getTimeInMillis() / 1000;
        long bValue = o2.getTimeInMillis() / 1000;
        int diff = (int) (aValue - bValue);
        return diff;
    }

    /**
     * You use operator != or operator == when you want to check for identity of two objects [if they are actually the same object]
     * @param o1
     * @param o2
     * @return boolean
     */
    public boolean objectsSame(Calendar o1, Calendar o2) {
        boolean result = false;
        
        if(o1 == o2) {
            result = true;
        }
        
        return (result);
    }
}
