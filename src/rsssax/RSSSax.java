package rsssax;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.net.URL;

public class RSSSax {

    private static final String RSS_URL = "http://www.europapress.es/rss/rss.aspx";

    private RSSSax() {}

    public static void main(String[] args) {
        new RSSSax().onInitialize();
    }

    private void onInitialize() {
        try {
            initSAXParser(RSS_URL);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initSAXParser(String url) throws ParserConfigurationException, SAXException, IOException {
        if (url.isEmpty()) {
            throw new IllegalArgumentException("URL must not be null");
        }

        if (!url.startsWith("http")) {
            throw new IllegalArgumentException("URL must start by \"http\"");
        }

        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser parser = spf.newSAXParser();
        parser.parse(new URL(url).openStream(), new NewsHandler());
    }
}
