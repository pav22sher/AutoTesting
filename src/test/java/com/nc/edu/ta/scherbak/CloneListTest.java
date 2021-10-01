package com.nc.edu.ta.scherbak;

import java.util.*;

import org.junit.*;

import static com.nc.edu.ta.scherbak.Utils.assertContains;
import static com.nc.edu.ta.scherbak.Utils.task;
import static org.junit.Assert.*;

public class CloneListTest extends AbstractTaskListTest {

    public CloneListTest(Class<? extends TaskList> tasksClass) {
        super(tasksClass);
    }
    
    @Test
    public void testClone() {
        Task[] elements = { task("A"), task("B"), task("C") };
        for (Task task : elements)
            tasks.add(task);
        
        TaskList clone = tasks.clone();
        
        assertEquals(getTitle(), tasks.size(), clone.size());
        
        Iterator<Task> i = tasks.iterator(), j = clone.iterator();
        while (i.hasNext())
            assertEquals(getTitle(), i.next(), j.next());
            
        assertNotSame(getTitle(), tasks, clone);
        
        clone.add(task("D"));
        assertEquals(getTitle(), clone.size() - 1, tasks.size());
        
        clone.remove(task("B"));
        assertContains(getTitle(), elements, tasks);
    }
}
