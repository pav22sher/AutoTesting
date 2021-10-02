package com.nc.edu.ta.scherbak;

import java.io.*;
import java.util.*;

public abstract class AbstractTaskList implements Serializable, Iterable<Task> {
    protected int size;
    public abstract void add(Task task);
    public abstract void remove(Task task);
    public abstract Task getTask(int index);
    public int size() {
        return size;
    }

    @Override
    public Iterator<Task> iterator() {
        return new TaskIterator();
    }

    private class TaskIterator implements Iterator<Task> {
        int cursor = 0;
        int lastRet = -1;
        @Override
        public boolean hasNext() {
            return cursor != size;
        }
        @Override
        public Task next() {
            if (hasNext()) {
                int i = cursor;
                Task task = getTask(i);
                lastRet = i;
                cursor = i + 1;
                return task;
            } else {
                throw new NoSuchElementException();
            }
        }
        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            if (hasNext()) {
                Task task = getTask(lastRet);
                AbstractTaskList.this.remove(task);
                if (lastRet < cursor) {
                    cursor--;
                }
                lastRet = -1;
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}
