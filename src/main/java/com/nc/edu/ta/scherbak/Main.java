package com.nc.edu.ta.scherbak;

import java.util.Date;

class Main {
    public static void main(String[] args) {
        Task task1 = new Task("Проснуться утром", new Date(28800));
        Task task2 = new Task("Покушать (завтрак, обед, ужин)", new Date(30000), new Date(64800), 14400);

        ArrayTaskList arrayTaskList = new ArrayTaskList();
        arrayTaskList.add(task1);
        arrayTaskList.add(task2);

        LinkedTaskList linkedTaskList = new LinkedTaskList();
        linkedTaskList.add(task1);
        linkedTaskList.add(task2);

        version1(arrayTaskList,linkedTaskList);
        version2(arrayTaskList,linkedTaskList);
    }

    static void version1(ArrayTaskList arrayTaskList, LinkedTaskList linkedTaskList) {
        String arrayXmlFile = "arrayTaskList.xml";
        TaskXMLSerializer.save(arrayTaskList, arrayXmlFile);
        System.out.println(arrayXmlFile + " validation = " + XMLValidator.validate(arrayXmlFile));
        System.out.println(TaskXMLSerializer.load(arrayXmlFile));

        String linkedXmlFile = "linkedTaskList.xml";
        TaskXMLSerializer.save(linkedTaskList, linkedXmlFile);
        System.out.println(TaskXMLSerializer.load(linkedXmlFile));
        System.out.println(linkedXmlFile + " validation = " + XMLValidator.validate(linkedXmlFile));
    }

    static void version2(ArrayTaskList arrayTaskList, LinkedTaskList linkedTaskList) {
        TaskXMLSerializer2.save(arrayTaskList, "arrayTaskList2.xml");
        System.out.println(TaskXMLSerializer2.load("arrayTaskList2.xml"));
        TaskXMLSerializer2.save(linkedTaskList, "linkedTaskList2.xml");
        TaskXMLSerializer2.save(arrayTaskList, "arrayTaskList2.xml");
        System.out.println(TaskXMLSerializer2.load("linkedTaskList2.xml"));
    }
}
