package com.nc.edu.ta.scherbak;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class Utils
{
    public static final int MINUTE = 60;
    public static final int HOUR = 60 * MINUTE;
    public static final int DAY = 24 * HOUR;
    public static final int MONTH = 30 * DAY;
    public static final int YEAR = 365 * DAY;

    public static final Date TODAY              = new Date(); 
    public static final Date TOMORROW           = new Date(TODAY.getTime() + DAY * 1000); 
    public static final Date NEVER              = null; 
    public static final Date YESTERDAY          = new Date(TODAY.getTime() - DAY * 1000); 
    public static final Date ALMOST_TODAY       = new Date(TODAY.getTime() - 1); 
    public static final Date TODAY_1H           = new Date(TODAY.getTime() + HOUR * 1000); 
    public static final Date TODAY_2H           = new Date(TODAY.getTime() + 2 * HOUR * 1000); 
    public static final Date TODAY_3H           = new Date(TODAY.getTime() + 3 * HOUR * 1000); 
    public static final Date TODAY_4H           = new Date(TODAY.getTime() + 4 * HOUR * 1000); 
    public static final Date TODAY_5H           = new Date(TODAY.getTime() + 5 * HOUR * 1000); 
    public static final Date TODAY_6H           = new Date(TODAY.getTime() + 6 * HOUR * 1000); 
    public static final Date TODAY_7H           = new Date(TODAY.getTime() + 7 * HOUR * 1000); 
    public static final Date TODAY_8H           = new Date(TODAY.getTime() + 8 * HOUR * 1000); 
    public static final Date TODAY_9H           = new Date(TODAY.getTime() + 9 * HOUR * 1000); 
    public static final Date ALMOST_TOMORROW    = new Date(TOMORROW.getTime() - 1); 

    public static void assertContains(String title, Task[] expected, Task[] actualA) {
        List<Task> actual = new ArrayList<>();
        for (Task task : actualA)
            actual.add(task);
        assertContains(title, expected, actual);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void assertContains(String title, Task[] expected, Iterable<Task> actualA) {
        Collection<Task> actual;
        if (actualA instanceof Collection)
            actual = (Collection) actualA;
        else {
            actual = new ArrayList<>();
            for (Task task : actualA)
                actual.add(task);
        }
            
        assertEquals(title + ": Unexpected size of "+ actualA, 
            expected.length, actual.size());
            
        for (Task task : expected)
            if (actual.contains(task))
                actual.remove(task);
            else
                fail(title + ": Task "+ task +" expected to be in list");
                
        if (! actual.isEmpty())
            fail(title + ": Tasks "+ actual +" are unexpected in list");
    }

    public static Task task(String title) {
        return new Task(title, TODAY);
    }
    
    public static Task task(String title, int time, boolean active) {
        Task t = new Task(title, new Date(1000*time));
        t.setActive(active);
        return t;
    }

    public static Task task(String title, int from, int to, int interval, boolean active) {
        Task t = new Task(title, new Date(1000*from), new Date(1000*to), interval);
        t.setActive(active);
        return t;
    }

    public static Task task(String title, Date from, Date to, int interval, boolean active) {
        Task t = new Task(title, (from), (to), interval);
        t.setActive(active);
        return t;
    }
}
