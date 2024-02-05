package it.codingjam;


import lombok.SneakyThrows;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class WebsiteStaxParser {

    public static void onWebsite(InputStream stream, Consumer<WebSite> consumer) throws XMLStreamException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = xmlInputFactory.createXMLEventReader(stream);

        emitWebsite(reader, consumer);
    }

    public static Stream<WebSite> streamWebsite(InputStream stream) throws XMLStreamException {
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        XMLEventReader reader = xmlInputFactory.createXMLEventReader(stream);

        return streamWebsite(reader);
    }

    private static void emitWebsite(XMLEventReader reader, Consumer<WebSite> consumer) throws XMLStreamException {
        WebSite website = null;
        while (reader.hasNext()) {
            XMLEvent nextEvent = reader.nextEvent();
            if (nextEvent.isStartElement()) {
                StartElement startElement = nextEvent.asStartElement();
                switch (startElement.getName().getLocalPart()) {
                    case "website":
                        website = new WebSite();
                        Attribute url = startElement.getAttributeByName(new QName("url"));
                        if (url != null) {
                            website.setUrl(url.getValue());
                        }
                        break;
                    case "name":
                        nextEvent = reader.nextEvent();
                        website.setName(nextEvent.asCharacters().getData());
                        break;
                    case "category":
                        nextEvent = reader.nextEvent();
                        website.setCategory(nextEvent.asCharacters().getData());
                        break;
                    case "status":
                        nextEvent = reader.nextEvent();
                        website.setStatus(nextEvent.asCharacters().getData());
                        break;
                }
            }
            if (nextEvent.isEndElement()) {
                EndElement endElement = nextEvent.asEndElement();
                if (endElement.getName().getLocalPart().equals("website")) {
                    consumer.accept(website);
                }
            }
        }
    }

    private static Stream<WebSite> streamWebsite(XMLEventReader reader) throws XMLStreamException {
        Iterator<WebSite> iterator = new Iterator<>() {

            @Override
            public boolean hasNext() {
                return reader.hasNext();
            }

            @Override
            @SneakyThrows
            public WebSite next() {
                XMLEvent nextEvent = reader.nextEvent();
                WebSite website = null;
                while (!isWebsiteEndElement(nextEvent) && reader.hasNext()) {
                    if (nextEvent.isStartElement()) {
                        StartElement startElement = nextEvent.asStartElement();
                        switch (startElement.getName().getLocalPart()) {
                            case "website":
                                website = new WebSite();
                                Attribute url = startElement.getAttributeByName(new QName("url"));
                                if (url != null) {
                                    website.setUrl(url.getValue());
                                }
                                break;
                            case "name":
                                nextEvent = reader.nextEvent();
                                website.setName(nextEvent.asCharacters().getData());
                                break;
                            case "category":
                                nextEvent = reader.nextEvent();
                                website.setCategory(nextEvent.asCharacters().getData());
                                break;
                            case "status":
                                nextEvent = reader.nextEvent();
                                website.setStatus(nextEvent.asCharacters().getData());
                                break;
                        }
                    }
                    nextEvent = reader.nextEvent();
                }
                return website;
            }

            private boolean isWebsiteEndElement(XMLEvent nextEvent) {
                if (nextEvent.isEndElement()) {
                    EndElement endElement = nextEvent.asEndElement();
                    return endElement.getName().getLocalPart().equals("website");
                } else {
                    return false;
                }
            }
        };
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.DISTINCT), false)
                .takeWhile(Objects::nonNull);
    }
}
