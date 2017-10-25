package rsssax;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
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
            System.out.println("\tNoticia " + (i+1));
            System.out.println("\t\tTitulo: " + channelHandler.getNews().get(i).getTitle());
            System.out.println("\t\tUrl: " + channelHandler.getNews().get(i).getLink());
            System.out.println("\t\tDescripcion: " + channelHandler.getNews().get(i).getDescription());
            System.out.println("\t\tFecha de publicacion: " + channelHandler.getNews().get(i).getPubDate());
            System.out.println("\t\tCategoria: " + channelHandler.getNews().get(i).getCategory());
        }
    }
}
