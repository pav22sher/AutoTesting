package com.nc.edu.ta.scherbak;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.StringReader;
import java.io.StringWriter;
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
            xml = createXml(list);
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

    private static String createXml(AbstractTaskList list) throws XMLStreamException {
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
            AbstractTaskList result = new ArrayTaskList();
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLEventReader reader = factory.createXMLEventReader(new FileInputStream(file));
            if (reader.hasNext() && reader.nextEvent().isStartDocument()) {
                if (reader.hasNext()) {
                    XMLEvent rootXmlEvent = reader.nextEvent();
                    if (rootXmlEvent.isStartElement()) {
                        StartElement rootStartElement = rootXmlEvent.asStartElement();
                        if (rootStartElement.getName().getLocalPart().equals("tasks")) {
                            while (reader.hasNext()) {
                                XMLEvent xmlEvent = reader.nextEvent();
                                if (xmlEvent.isStartElement()) {
                                    StartElement startElement = xmlEvent.asStartElement();
                                    if (startElement.getName().getLocalPart().equals("task")) {
                                        Task task = new Task();
                                        Attribute active = startElement.getAttributeByName(new QName("active"));
                                        if (active != null) {
                                            task.setActive(Boolean.parseBoolean(active.getValue()));
                                        }
                                        Attribute repeated = startElement.getAttributeByName(new QName("repeated"));
                                        if (repeated != null) {
                                            boolean repeatedFlag = Boolean.parseBoolean(repeated.getValue());
                                            task.setActive(repeatedFlag);
                                            if(!repeatedFlag) {
                                                Attribute time = startElement.getAttributeByName(new QName("time"));
                                                if (time != null) {
                                                    task.setTime(new Date(Integer.parseInt(time.getValue())));
                                                }
                                            } else {
                                                Attribute start = startElement.getAttributeByName(new QName("start"));
                                                Attribute end = startElement.getAttributeByName(new QName("end"));
                                                Attribute repeat = startElement.getAttributeByName(new QName("repeat"));
                                                if (start != null && end != null && repeat !=null) {
                                                    task.setTime(
                                                            new Date(Integer.parseInt(start.getValue())),
                                                            new Date(Integer.parseInt(end.getValue())),
                                                            Integer.parseInt(repeat.getValue())
                                                    );
                                                }
                                            }
                                        }
                                        xmlEvent = reader.nextEvent();
                                        task.setTitle(xmlEvent.asCharacters().getData());
                                        result.add(task);
                                    }
                                }

                            }
                        }
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
