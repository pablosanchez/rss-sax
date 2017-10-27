package rsssax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This class parses each item tag, storing its information
 */
public class NewsHandler extends DefaultHandler {

    private static final String ITEM_TAG = "item";
    private static final String TITLE_TAG = "title";
    private static final String URL_TAG = "link";
    private static final String DESCRIPTION_TAG = "description";
    private static final String PUB_DATE_TAG = "pubDate";
    private static final String CATEGORY_TAG = "category";

    private ChannelHandler channelHandler;
    private StringBuilder buffer;
    private ItemNew currentItemNew;

    public NewsHandler(ChannelHandler channelHandler) {
        this.channelHandler = channelHandler;
        buffer = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case TITLE_TAG:
                currentItemNew = new ItemNew();
                buffer.delete(0, buffer.length());
                break;
            case URL_TAG:
                buffer.delete(0, buffer.length());
                break;
            case DESCRIPTION_TAG:
                buffer.delete(0, buffer.length());
                break;
            case PUB_DATE_TAG:
                buffer.delete(0, buffer.length());
                break;
            case CATEGORY_TAG:
                buffer.delete(0, buffer.length());
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
                currentItemNew.setTitle(buffer.toString());
                break;
            case URL_TAG:
                currentItemNew.setLink(buffer.toString());
                break;
            case DESCRIPTION_TAG:
                currentItemNew.setDescription(buffer.toString());
                break;
            case PUB_DATE_TAG:
                currentItemNew.setPubDate(buffer.toString());
                break;
            case CATEGORY_TAG:
                currentItemNew.setCategory(buffer.toString());
                break;
            case ITEM_TAG:
                channelHandler.addNew(currentItemNew);
                channelHandler.endElement(uri, localName, qName);
                channelHandler.restore();
                break;
        }
    }
}
