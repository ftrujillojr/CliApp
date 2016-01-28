package fjt.comparators;

import java.util.Calendar;
import java.util.Iterator;

public class CalendarIterator implements Iterator<Calendar>, Iterable<Calendar> {

    private Calendar end;
    private Calendar current;
    private int calField;
    private int step;

    public CalendarIterator(Calendar start, Calendar end, int calField, int step) {
        this.end = Calendar.getInstance();
        this.current = Calendar.getInstance();

        this.end.setTimeInMillis(end.getTimeInMillis());
        this.current.setTimeInMillis(start.getTimeInMillis());

        if (calField == Calendar.DATE) {
            this.calendarZeroOutTime(this.end);
            this.calendarZeroOutTime(this.current);
        }

        this.calField = calField;
        this.step = step;
    }

    @Override
    public boolean hasNext() {
        return !current.after(end);
    }

    @Override
    public Calendar next() {
        Calendar oldValue = Calendar.getInstance();
        oldValue.setTimeInMillis(current.getTimeInMillis());
        current.add(this.calField, this.step);
        return oldValue;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Can not remove");
    }

    @Override
    public Iterator<Calendar> iterator() {
        return (this);
    }

    private void calendarZeroOutTime(Calendar calendar) {
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
    }
}
