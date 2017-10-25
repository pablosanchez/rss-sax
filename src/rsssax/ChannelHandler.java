package rsssax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.util.List;

public class ChannelHandler extends DefaultHandler {

    private static final String CHANNEL_TAG = "channel";
    private static final String TITLE_TAG = "title";
    private static final String URL_TAG = "link";
    private static final String DESCRIPTION_TAG = "description";
    private static final String ITEM_TAG = "item";

    private XMLReader xmlReader;
    private StringBuilder buffer;
    private boolean channelInfoRead;  // Indicates whether channel info has been read in order not to print out image info (which has the same tags)

    private NewsHandler newsHandler;
    private List<New> news;
    private String channelTitle;

    public ChannelHandler(XMLReader xmlReader) {
        this.xmlReader = xmlReader;
        buffer = new StringBuilder();
        channelInfoRead = false;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case TITLE_TAG:
                buffer.delete(0, buffer.length());
                break;
            case URL_TAG:
                buffer.delete(0, buffer.length());
                break;
            case DESCRIPTION_TAG:
                buffer.delete(0, buffer.length());
                break;
            case ITEM_TAG:
                newsHandler = new NewsHandler(this);
                xmlReader.setContentHandler(newsHandler);
                newsHandler.startElement(uri, localName, qName, attributes);
                break;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        buffer.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case TITLE_TAG:
                if (!channelInfoRead) {
                    System.out.println("Informacion del canal");
                    channelTitle = buffer.toString();
                    System.out.println("\tTitulo: " + channelTitle);
                }
                break;
            case URL_TAG:
                if (!channelInfoRead) {
                    System.out.println("\tUrl: " + buffer.toString());
                }
                break;
            case DESCRIPTION_TAG:
                if (!channelInfoRead) {
                    System.out.println("\tDescripcion: " + buffer.toString());
                }
                channelInfoRead = true;  // Channel info has been read
                break;
            case CHANNEL_TAG:
                news = newsHandler.getNews();
                break;
        }
    }

    public void restore() {
        this.xmlReader.setContentHandler(this);
    }

    public List<New> getNews() {
        return news;
    }

    public String getChannelTitle() {
        return channelTitle;
    }
}
