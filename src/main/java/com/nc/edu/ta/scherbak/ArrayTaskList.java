package com.nc.edu.ta.scherbak;

import java.util.Arrays;
import java.util.Objects;

public class ArrayTaskList extends TaskList{
    private static final String TITLE_PREFIX = "[ArrayTaskList]";
    private static int count;
    private Task[] tasks;
    public ArrayTaskList() {
        tasks = new Task[10];
        count++;
    }
    public static int getCount() {
        return count;
    }
    @Override
    public void add(Task task) {
        if(task != null){
            if(tasks.length == size) {
                final int DEFAULT_TASK_GROW = 10;
                tasks = Arrays.copyOf(tasks, tasks.length + DEFAULT_TASK_GROW);
            }
            tasks[size] = task;
            size++;
        } else {
            throw new RuntimeException();
        }
    }
    @Override
    public void remove(Task task) {
        if(task != null) {
            Task[] beforeRemove = Arrays.stream(this.tasks)
                    .filter(Objects::nonNull)
                    .filter(tmp -> !tmp.equals(task))
                    .toArray(Task[]::new);
            size = beforeRemove.length;
            tasks = Arrays.copyOf(beforeRemove, tasks.length);
        } else {
            throw new RuntimeException();
        }
    }
    @Override
    public Task getTask(int index) {
        if (index < 0 || index >= size){
            throw new IndexOutOfBoundsException(index);
        }
        return tasks[index];
    }
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ArrayTaskList").append("(").append(size).append(")\n");
        builder.append("[");
        for (int i = 0; i < size; i++) {
            Task task = tasks[i];
            if(task != null) {
                builder.append("\n");
                builder.append(task.getStringByTitle(TITLE_PREFIX + " ", task.getTitle()));
            }
        }
        builder.append("\n]");
        return builder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArrayTaskList list = (ArrayTaskList) o;
        if(this.size != list.size) return false;
        for (int i = 0; i < size; i++) {
            if(!Objects.equals(this.tasks[i], list.tasks[i])) {
                return false;
            }
        }
        return true;
    }
}
