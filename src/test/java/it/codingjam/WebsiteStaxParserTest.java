package it.codingjam;

import org.junit.jupiter.api.Test;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WebsiteStaxParserTest {

   @Test
    void shouldCollect() throws IOException, XMLStreamException {
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("website.xml")) {
            List<WebSite> websites = new ArrayList<>();
            WebsiteStaxParser.onWebsite(in, websites::add);

            assertEquals(3, websites.size());
        }
    }

    @Test
    void shouldStream() throws IOException, XMLStreamException {
        try (InputStream in = this.getClass().getClassLoader().getResourceAsStream("website.xml")) {
            List<WebSite> webSites = WebsiteStaxParser.streamWebsite(in)
                    .toList();

            assertEquals(3, webSites.size());
        }
    }
}