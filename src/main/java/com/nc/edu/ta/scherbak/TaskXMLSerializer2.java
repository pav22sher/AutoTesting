package com.nc.edu.ta.scherbak;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;

public class TaskXMLSerializer2 {
    /**
     * сохранение списка задач в файл
     */
    public static void save(AbstractTaskList list, String file) {
        try {
            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = documentFactory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element elements = document.createElement("tasks");
            elements.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            elements.setAttribute("xsi:noNamespaceSchemaLocation", XMLValidator.SCHEMA_FILE);
            for (Task task : list) {
                boolean active = task.isActive();
                Date time = task.getTime();
                Date startTime = task.getStartTime();
                Date endTime = task.getEndTime();
                int repeat = task.getRepeatInterval();
                boolean repeated = task.isRepeated();
                Element element = document.createElement("task");
                element.setAttribute("active", Boolean.toString(active));
                element.setAttribute("time",
                        time == null
                                ? ""
                                : Long.toString(time.getTime())
                );
                element.setAttribute("start",
                        startTime == null || !repeated
                                ? ""
                                : Long.toString(startTime.getTime())
                );
                element.setAttribute("end",
                        endTime == null || !repeated
                                ? ""
                                : Long.toString(endTime.getTime())
                );
                element.setAttribute("repeat", Integer.toString(repeat));
                element.setAttribute("repeated", Boolean.toString(repeated));
                element.setTextContent(task.getTitle());
                elements.appendChild(element);
            }
            document.appendChild(elements);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");//дополнительные пробелы при выводе дерева
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");//опустить-xml-объявление
            Source source = new DOMSource(document);
            Result result = new StreamResult(new FileOutputStream(file));
            transformer.transform(source, result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * загрузка списка задач из файла file
     */
    public static AbstractTaskList load(String file) {
        try {
            ArrayTaskList result = new ArrayTaskList();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document document = documentBuilder.parse(new FileInputStream(file));
            Element rootElement = document.getDocumentElement();
            if("tasks".equals(rootElement.getNodeName())) {
                NodeList elements = rootElement.getChildNodes();
                for (int i = 0; i < elements.getLength(); i++) {
                    Node item = elements.item(i);
                    String itemName = item.getNodeName();
                    if ("task".equals(itemName)) {
                        Task task = new Task();
                        NamedNodeMap attributes = item.getAttributes();
                        task.setActive(Boolean.parseBoolean(attributes.getNamedItem("active").getTextContent()));
                        boolean repeated = Boolean.parseBoolean(attributes.getNamedItem("repeated").getTextContent());
                        task.setActive(repeated);
                        if (!repeated) {
                            task.setTime(new Date(Integer.parseInt(attributes.getNamedItem("time").getTextContent())));
                        } else {
                            task.setTime(
                                    new Date(Integer.parseInt(attributes.getNamedItem("start").getTextContent())),
                                    new Date(Integer.parseInt(attributes.getNamedItem("end").getTextContent())),
                                    Integer.parseInt(attributes.getNamedItem("repeat").getTextContent())
                            );
                        }
                        task.setTitle(item.getTextContent());
                        result.add(task);
                    }
                }
            }
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
