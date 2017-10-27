package rsssax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * This class starts parsing the XML document and stores channel info.
 * Every time it encounters <item> tag, it gives the control to NewsHandler
 */
public class ChannelHandler extends DefaultHandler {

    private static final String TITLE_TAG = "title";
    private static final String URL_TAG = "link";
    private static final String DESCRIPTION_TAG = "description";
    private static final String ITEM_TAG = "item";

    private XMLReader xmlReader;
    private StringBuilder buffer;
    private boolean channelInfoRead;  // Indicates whether channel info has been read in order not to print out image info (which has the same tags)

    private Channel channel;
    private List<ItemNew> news;

    public ChannelHandler(XMLReader xmlReader) {
        this.xmlReader = xmlReader;
        buffer = new StringBuilder();
        channelInfoRead = false;
        news = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case TITLE_TAG:
                if (!channelInfoRead) {
                    channel = new Channel();
                }
                buffer.delete(0, buffer.length());
                break;
            case URL_TAG:
                buffer.delete(0, buffer.length());
                break;
            case DESCRIPTION_TAG:
                buffer.delete(0, buffer.length());
                break;
            case ITEM_TAG:  // Every item is handled by NewsHandler
                NewsHandler newsHandler = new NewsHandler(this);
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
                    channel.setTitle(buffer.toString());
                }
                break;
            case URL_TAG:
                if (!channelInfoRead) {
                    channel.setLink(buffer.toString());
                }
                break;
            case DESCRIPTION_TAG:
                if (!channelInfoRead) {
                    channel.setDescription(buffer.toString());
                }
                channelInfoRead = true;  // Channel info has been read
                break;
        }
    }

    public void restore() {
        xmlReader.setContentHandler(this);
    }

    public void addNew(ItemNew currentItemNew) {
        news.add(currentItemNew);
    }

    public Channel getChannel() {
        return channel;
    }

    public List<ItemNew> getNews() {
        return news;
    }
}
