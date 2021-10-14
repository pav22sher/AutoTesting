package com.nc.edu.ta.scherbak;

import java.util.Date;

class Main {
    public static void main(String[] args) {
        Task task1 = new Task("Проснуться утром", new Date(28800));
        Task task2 = new Task("Покушать (завтрак, обед, ужин)", new Date(30000), new Date(64800), 14400);

        ArrayTaskList arrayTaskList = new ArrayTaskList();
        arrayTaskList.add(task1);
        arrayTaskList.add(task2);
        TaskXMLSerializer.save(arrayTaskList, "arrayTaskList.xml");

        LinkedTaskList linkedTaskList = new LinkedTaskList();
        linkedTaskList.add(task1);
        linkedTaskList.add(task2);
        TaskXMLSerializer.save(linkedTaskList, "linkedTaskList.xml");

        System.out.println(TaskXMLSerializer.load("arrayTaskList.xml"));
        System.out.println(TaskXMLSerializer.load("linkedTaskList.xml"));
    }
}
