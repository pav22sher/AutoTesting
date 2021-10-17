package com.nc.edu.ta.scherbak;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

public class TaskXMLSerializer {
    /**
     * сохранение списка задач в файл
     */
    public static void save(AbstractTaskList list, String file) {
        String xml;
        try {
            xml = serializeToXml(list);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");//дополнительные пробелы при выводе дерева
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");//опустить-xml-объявление
            StreamSource source = new StreamSource(new StringReader(xml));
            StringWriter target = new StringWriter();
            transformer.transform(source, new StreamResult(target));
            Files.writeString(Paths.get(file), target.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static String serializeToXml(AbstractTaskList list) throws XMLStreamException {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        XMLStreamWriter writer = factory.createXMLStreamWriter(stream);
        writer.writeStartDocument();
        writer.writeStartElement("tasks");
        for (Task task : list) {
            boolean active = task.isActive();
            Date time = task.getTime();
            Date startTime = task.getStartTime();
            Date endTime = task.getEndTime();
            int repeat = task.getRepeatInterval();
            boolean repeated = task.isRepeated();
            writer.writeStartElement("task");
            writer.writeAttribute("active", Boolean.toString(active));
            writer.writeAttribute("time",
                    time == null
                            ? ""
                            : Long.toString(time.getTime())
            );
            writer.writeAttribute("start",
                    startTime == null || !repeated
                            ? ""
                            : Long.toString(startTime.getTime())
            );
            writer.writeAttribute("end",
                    endTime == null || !repeated
                            ? ""
                            : Long.toString(endTime.getTime())
            );
            writer.writeAttribute("repeat", Integer.toString(repeat));
            writer.writeAttribute("repeated", Boolean.toString(repeated));
            writer.writeCharacters(task.getTitle());
            writer.writeEndElement();
        }
        writer.writeEndElement();
        writer.writeEndDocument();
        writer.flush();
        writer.close();
        return stream.toString();
    }

    /**
     * загрузка списка задач из файла file
     */
    public static AbstractTaskList load(String file) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            DeserializeFromXml handler = new DeserializeFromXml();
            parser.parse(new File(file), handler);
            return handler.getTaskList();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    private static class DeserializeFromXml extends DefaultHandler {
        private static final String TASKS = "tasks";
        private static final String TASK = "task";
        private AbstractTaskList list;
        private Task lastTask;
        private String lastTaskTitle;

        public AbstractTaskList getTaskList() {
            return list;
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            lastTaskTitle = String.valueOf(ch, start, length);
        }

        @Override
        public void startElement(String uri, String lName, String qName, Attributes attributes) throws SAXException {
            switch (qName) {
                case TASKS:
                    list = new ArrayTaskList();
                    break;
                case TASK:
                    Task task = new Task();
                    task.setActive(Boolean.parseBoolean(attributes.getValue("active")));
                    boolean repeated = Boolean.parseBoolean(attributes.getValue("repeated"));
                    task.setActive(repeated);
                    if (!repeated) {
                        task.setTime(new Date(Integer.parseInt(attributes.getValue("time"))));
                    } else {
                        task.setTime(
                                new Date(Integer.parseInt(attributes.getValue("start"))),
                                new Date(Integer.parseInt(attributes.getValue("end"))),
                                Integer.parseInt(attributes.getValue("repeat"))
                        );
                    }
                    lastTask = task;
                    break;
                default:
                    throw new SAXException();
            }

        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            switch (qName) {
                case TASKS:
                    break;
                case TASK:
                    lastTask.setTitle(lastTaskTitle);
                    list.add(lastTask);
                    break;
                default:
                    throw new SAXException();
            }
        }
    }
}
