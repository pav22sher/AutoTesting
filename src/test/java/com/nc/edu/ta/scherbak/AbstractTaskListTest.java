package com.nc.edu.ta.scherbak;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public abstract class AbstractTaskListTest {

    protected TaskList tasks;
    
    private Class<? extends TaskList> tasksClass;
    
    public AbstractTaskListTest(Class<? extends TaskList> tasksClass) {
        this.tasksClass = tasksClass;
    }
    
    @Before
    public void setUp() throws Exception {
        tasks = createList();
    }
    
    public TaskList createList() throws Exception {
        return tasksClass.newInstance();
    }
    
    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
            { ArrayTaskList.class  },
            { LinkedTaskList.class }
        });
    }
    
    protected String getTitle() {
        return tasksClass.getSimpleName();
    }
}
