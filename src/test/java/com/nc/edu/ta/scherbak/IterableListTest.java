package com.nc.edu.ta.scherbak;

import java.util.*;

import org.junit.*;

import static com.nc.edu.ta.scherbak.Utils.assertContains;
import static com.nc.edu.ta.scherbak.Utils.task;
import static org.junit.Assert.*;

public class IterableListTest extends AbstractTaskListTest {

    public IterableListTest(Class<? extends TaskList> tasksClass) {
        super(tasksClass);
    }

    @Test
    public void testIteration() {
        Task[] array = {task("A"), task("B"), task("C"), task("D")};
        for (Task task : array)
            tasks.add(task);
        
        int i = 0;
        for (Task task : tasks) {
            assertEquals(getTitle() + " at "+ i, array[i], task);
            i++;
        }
        assertEquals(getTitle() + " total elements", array.length, i);
    }
    
    @Test
    public void testIteratorRemove() {
        Task[] array = {task("A"), task("B"), task("C"), task("D")};
        for (Task task : array)
            tasks.add(task);
        Iterator<?> iterator = tasks.iterator();
        iterator.next();
        iterator.next();
        iterator.remove();
        assertContains(getTitle(), new Task[]{ array[0], array[2], array[3] }, tasks);
    }
    
    @Test
    public void testParallelIteration() {
        Task[] array = {task("A"), task("B"), task("C")};
        for (Task task : array)
            tasks.add(task);
        String[] pairs = { "AA", "AB", "AC", "BA", "BB", "BC", "CA", "CB", "CC" };
        int i = 0;
        for (Task a : tasks)
            for (Task b : tasks)
                assertEquals(getTitle(), pairs[i++], a.getTitle() + b.getTitle());
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testAfterLast() {
        tasks.iterator().next();
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRemoveTwice() {
        Task[] array = { task("A"), task("B"), task("C"), task("D") };
        for (Task task : array)
            tasks.add(task);
        Iterator<?> iterator = tasks.iterator();
        iterator.next();
        iterator.next();
        iterator.remove();
        iterator.remove();
    }
}


















