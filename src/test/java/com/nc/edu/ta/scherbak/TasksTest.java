package com.nc.edu.ta.scherbak;

import static org.junit.Assert.*;
import static com.nc.edu.ta.scherbak.Utils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Test;

public class TasksTest
{
    @Test
    public void testIncomingInactive() {
        Task[] ts = {task("A",0,false), task("B",1,false), task("C",2,false)};
        List<Task> tasks = Arrays.asList(ts);
        assertTrue(Tasks.incoming(tasks, YESTERDAY, TOMORROW).isEmpty());
    }
    
    @Test
    public void testIncoming() {
        // range: 50 60
        Task[] ts = {
            task("Simple IN", 55, true), 
            task("Simple OUT", 10, true),
            task("Inactive OUT", 0, 1000, 1, false), 
            task("Simple bound OUT", 50, true), 
            task("Simple bound IN", 60, true),
            task("Repeat inside IN", 51, 58, 2, true),
            task("Repeat outside IN", 0, 100, 5, true),
            task("Repeat outside OUT", 0, 100, 22, true),
            task("Repeat left OUT", 0, 40, 1, true),
            task("Repeat right OUT", 65, 100, 1, true),
            task("Repeat left intersect IN", 0, 55, 13, true),
            task("Repeat left intersect IN", 0, 60, 30, true),
            task("Repeat left intersect OUT", 0, 55, 22, true),
            task("Repeat right intersect IN", 55, 100, 20, true)
        };
        List<Task> tasks = Arrays.asList(ts);
        List<Task> incoming = new ArrayList<Task>();
        for (Task t : ts)
            if (t.getTitle().contains("IN"))
                incoming.add(t);
        
        assertContains("incoming test",
                incoming.toArray(new Task[0]), 
                Tasks.incoming(tasks, new Date(50*1000), new Date(60*1000)));
    }
    
    @Test
    public void testTimeline() {
        Task daily = task("Daily", YESTERDAY, TOMORROW, DAY, true);
        Task hourly = task("Hourly", TODAY, TOMORROW, HOUR, true);
        Task every3h = task("Every 3 hours", 
                new Date(TODAY.getTime() + HOUR*1000), 
                TOMORROW, 
                3*HOUR, true);
        
        List<Task> tasks = Arrays.asList(daily, hourly, every3h);
        SortedMap<Date, Set<Task>> timeline = new TreeMap<Date, Set<Task>>();
        timeline.put(TODAY, set(daily, hourly));
        timeline.put(TODAY_1H, set(hourly, every3h));
        timeline.put(TODAY_2H, set(hourly));
        timeline.put(TODAY_3H, set(hourly));
        timeline.put(TODAY_4H, set(hourly, every3h));
        assertEquals(timeline, Tasks.timeline(tasks, ALMOST_TODAY, TODAY_4H));
    }
    
    private Set<Task> set(Task ... tasks) {
        return new HashSet<>(Arrays.asList(tasks));
    }
}











