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

/**
 * This program takes a RSS url feed an parses the XML by using SAX approach;
 * then it prints out on screen channel and news info and generates a XML
 * file with the news titles
 *
 * @author Pablo Sanchez
 * @version 1.0
 */
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
            printInfo();
            generateXML();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * This method receives a RSS url and starts ChannelHandler
     *
     * @param url RSS url
     *
     * @throws ParserConfigurationException ParserConfigurationException
     * @throws SAXException SAXException
     * @throws IOException IOException
     */
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

    /**
     * This method prints out on screen channel and news info, once the handlers have finished
     */
    private void printInfo() {
        System.out.println("Informacion del canal");
        System.out.println("\tTitulo: " + channelHandler.getChannel().getTitle());
        System.out.println("\tUrl: " + channelHandler.getChannel().getLink());
        System.out.println("\tDescripcion: " + channelHandler.getChannel().getDescription());
        System.out.println("Noticias");
        for (int i = 0; i < channelHandler.getNews().size(); i++) {
            ItemNew currentItemNew = channelHandler.getNews().get(i);
            System.out.println("\tNoticia " + (i+1));
            System.out.println("\t\tTitulo: " + currentItemNew.getTitle());
            System.out.println("\t\tUrl: " + currentItemNew.getLink());
            System.out.println("\t\tDescripcion: " + currentItemNew.getDescription());
            System.out.println("\t\tFecha de publicacion: " + currentItemNew.getPubDate());
            System.out.println("\t\tCategoria: " + currentItemNew.getCategory());
        }
    }

    /**
     * This method generates a XML document with the following format:
     *  <noticias canal="titulo_canal">
     *     <noticia>titulo_noticia</noticia>
     * </noticias>
     *
     * @throws TransformerConfigurationException TransformerConfigurationException
     * @throws SAXException SAXException
     * @throws IOException IOException
     */
    private void generateXML() throws TransformerConfigurationException, SAXException, IOException {
        SAXTransformerFactory stf = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
        TransformerHandler handler = stf.newTransformerHandler();

        Transformer transformer = handler.getTransformer();
        transformer.setOutputProperty(OutputKeys.VERSION, "1.0");
        transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        BufferedWriter bw = new BufferedWriter(
                new FileWriter(new File("noticias_" + channelHandler.getChannel().getTitle() + ".xml")));
        handler.setResult(new StreamResult(bw));

        handler.startDocument();
        AttributesImpl attrs = new AttributesImpl();
        attrs.addAttribute("", "", "canal", "id", channelHandler.getChannel().getTitle());
        handler.startElement("", "", "noticias", attrs);

        for (ItemNew currentItemNew : channelHandler.getNews()) {
            handler.startElement("", "", "noticia", null);
            handler.characters(currentItemNew.getTitle().toCharArray(), 0, currentItemNew.getTitle().length());
            handler.endElement("", "", "noticia");
        }

        handler.endElement("", "", "noticias");
        handler.endDocument();
    }
}
