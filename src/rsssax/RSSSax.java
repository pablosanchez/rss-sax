package rsssax;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

public class RSSSax {

    private static final String RSS_URL = "http://www.europapress.es/rss/rss.aspx";

    private ChannelHandler channelHandler;

    private RSSSax() {}

    public static void main(String[] args) {
        new RSSSax().onInitialize();
    }

    private void onInitialize() {
        try {
            initSAXParser(RSS_URL);
            printNews();
            generateXML();
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
        channelHandler = new ChannelHandler(parser.getXMLReader());
        parser.parse(new URL(url).openStream(), channelHandler);
    }

    private void printNews() {
        System.out.println("Noticias");
        for (int i = 0; i < channelHandler.getNews().size(); i++) {
            New currentNew = channelHandler.getNews().get(i);
            System.out.println("\tNoticia " + (i+1));
            System.out.println("\t\tTitulo: " + currentNew.getTitle());
            System.out.println("\t\tUrl: " + currentNew.getLink());
            System.out.println("\t\tDescripcion: " + currentNew.getDescription());
            System.out.println("\t\tFecha de publicacion: " + currentNew.getPubDate());
            System.out.println("\t\tCategoria: " + currentNew.getCategory());
        }
    }

    private void generateXML() throws TransformerConfigurationException, SAXException, IOException {
        SAXTransformerFactory stf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        TransformerHandler handler = stf.newTransformerHandler();

        Transformer transformer = handler.getTransformer();
        transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
        transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        BufferedWriter bw = new BufferedWriter(
                new FileWriter(new File("noticias_" + channelHandler.getChannelTitle() + ".xml")));
        handler.setResult(new StreamResult(bw));

        handler.startDocument();
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", "", "canal", "id", channelHandler.getChannelTitle());
        handler.startElement("", "", "noticias", attrs);

        for (New currentNew : channelHandler.getNews()) {
            handler.startElement("", "", "noticia", null);
            handler.characters(currentNew.getTitle().toCharArray(), 0, currentNew.getTitle().length());
            handler.endElement("", "", "noticia");
        }

        handler.endElement("", "", "noticias");
        handler.endDocument();
    }
}
