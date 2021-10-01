package com.nc.edu.ta.scherbak;

import java.util.*;

public class Tasks {
    public static SortedMap<Date, Set<Task>> timeline(List<Task> tasks, Date from, Date to) {
        return null;
    }
    public static List<Task> incoming(List<Task> tasks, Date from, Date to) {
        List<Task> result = new ArrayList<>();
        for(Task task: tasks) {
            if(task.isActive()) {
                if (task.isRepeated()) {
                    for (Date time = task.getStartTime(); time.compareTo(task.getEndTime()) <= 0; time = new Date(time.getTime() + task.getRepeatInterval() * 1000L)) {
                        if(time.compareTo(from) > 0 && time.compareTo(to) <= 0) {
                            result.add(task);
                            break;
                        }
                    }
                } else {
                    Date time = task.getTime();
                    if(time.compareTo(from) > 0 && time.compareTo(to) <= 0) {
                        result.add(task);
                    }
                }
            }
        }
        return result;
    }
}
