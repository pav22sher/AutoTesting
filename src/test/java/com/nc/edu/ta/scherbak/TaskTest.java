package com.nc.edu.ta.scherbak;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.nc.edu.ta.scherbak.Utils.*;

import org.junit.Test;

public class TaskTest {

    @Test
    public void testTitle() {
        Task task = new Task("test", TODAY);
        assertEquals("test", task.getTitle());
        task.setTitle("other");
        assertEquals("other", task.getTitle());
    }
    
    @Test
    public void testActive() {
        Task task = new Task("test", TODAY);
        assertFalse(task.isActive());
        task.setActive(true);
        assertTrue(task.isActive());
    }
    @Test
    public void testConstructorNonrepeated() {
        Task task = new Task("test", TODAY);
        assertFalse("active", task.isActive());
        assertEquals("time", TODAY, task.getTime());
        assertEquals("start", TODAY, task.getStartTime());
        assertEquals("end", TODAY, task.getEndTime());
        assertFalse("repeated", task.isRepeated());
    }
    @Test
    public void testConstructorRepeated() {
        Task task = new Task("test", TODAY, TOMORROW, 5 * MINUTE);
        assertFalse("active", task.isActive());
        assertEquals("time", TODAY, task.getTime());
        assertEquals("start", TODAY, task.getStartTime());
        assertEquals("end", TOMORROW, task.getEndTime());
        assertTrue("repeated", task.isRepeated());
        assertEquals("repeatInterval", 5 * MINUTE, task.getRepeatInterval());
    }
    @Test
    public void testTimeNonRepeated() {
        Task task = new Task("test", TODAY, TOMORROW, 15 * MINUTE);
        task.setTime(TOMORROW);
        assertEquals("time", TOMORROW, task.getTime());
        assertEquals("start", TOMORROW, task.getStartTime());
        assertEquals("end", TOMORROW, task.getEndTime());
        assertFalse("repeated", task.isRepeated());
    }
    @Test
    public void testTimeRepeated() {
        Task task = new Task("test", TOMORROW);
        task.setTime(TODAY, TOMORROW, HOUR);
        assertEquals("time", TODAY, task.getTime());
        assertEquals("start", TODAY, task.getStartTime());
        assertEquals("end", TOMORROW, task.getEndTime());
        assertTrue("repeated", task.isRepeated());
        assertEquals("repeatInterval", HOUR, task.getRepeatInterval());
    }
    @Test
    public void testNextNonRepeative() {
        Task task = new Task("some", TODAY);
        task.setActive(true);
        assertEquals(TODAY, task.nextTimeAfter(YESTERDAY));
        assertEquals(TODAY, task.nextTimeAfter(ALMOST_TODAY));
        assertEquals(NEVER, task.nextTimeAfter(TODAY));
        assertEquals(NEVER, task.nextTimeAfter(TOMORROW));
    }
    @Test
    public void testNextRepeative() {
        Task task = new Task("some", TODAY, TOMORROW, HOUR);
        task.setActive(true);
        assertEquals(TODAY, task.nextTimeAfter(YESTERDAY));
        assertEquals(TODAY, task.nextTimeAfter(ALMOST_TODAY));
        assertEquals(TODAY_1H, task.nextTimeAfter(TODAY));
        assertEquals(TODAY_2H, task.nextTimeAfter(TODAY_1H));
        assertEquals(TODAY_3H, task.nextTimeAfter(TODAY_2H));
        assertEquals(TOMORROW, task.nextTimeAfter(ALMOST_TOMORROW));
        assertEquals(NEVER, task.nextTimeAfter(TOMORROW));
    }
    @Test
    public void testNextInactive() {
        Task task = new Task("some", TODAY);
        task.setActive(false);
        assertEquals(NEVER, task.nextTimeAfter(YESTERDAY));
    }
    @Test(expected = RuntimeException.class)
    public void testWrongTitle() {
        new Task(null, TODAY);
    }        

    @Test(expected = RuntimeException.class)
    public void testWrongTitle2() {
        Task task = new Task("OK", TODAY);
        task.setTitle(null);
    }        
    
    
    @Test(expected = RuntimeException.class)
    public void testWrongTime() {
        new Task("Title", null);
    }

    @Test(expected = RuntimeException.class)
    public void testWrongStart() {
        new Task("Title", null, TOMORROW, 5);
    }

    @Test(expected = RuntimeException.class)
    public void testWrongEnd() {
        new Task("Title", TOMORROW, TODAY, 3);
    }

    @Test(expected = RuntimeException.class)
    public void testWrongInterval() {
        new Task("Title", TODAY, TOMORROW, -1);
    }

    @Test(expected = RuntimeException.class)
    public void testWrongSetTime() {
        Task task = new Task("Title", TODAY);
        task.setTime(null);
    }

    @Test(expected = RuntimeException.class)
    public void testWrongSetStart() {
        Task task = new Task("Title", TODAY);
        task.setTime(null, TOMORROW, 3);
    }

    @Test(expected = RuntimeException.class)
    public void testWrongSetEnd() {
        Task task = new Task("Title", TODAY);
        task.setTime(TOMORROW, TODAY, 3);
    }

    @Test(expected = RuntimeException.class)
    public void testWrongSetInterval() {
        Task task = new Task("Title", TODAY);
        task.setTime(TODAY, TOMORROW, -1);
    }
    
    @Test(expected = RuntimeException.class)
    public void testWrongTimeAfter() {
        Task task = new Task("some", TODAY);
        task.nextTimeAfter(null);
    }
}
