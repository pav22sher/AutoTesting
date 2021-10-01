package com.nc.edu.ta.scherbak;

import java.util.Date;

import org.junit.*;
import static org.junit.Assert.*;

public class CloneTaskTest {

    @Test
    public void testClone() throws Exception {
        Task task = new Task("Title", new Date());
        Task clone = task.clone();
        assertEquals("Clone must equal original", task, clone);
        assertNotSame("Clone must be different object", task, clone);
        clone.setTitle("Clone");
        assertEquals("Title", task.getTitle());
        assertEquals("Clone", clone.getTitle());
    }
}










