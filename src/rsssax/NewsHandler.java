package rsssax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class NewsHandler extends DefaultHandler {

    private static final String TITLE_TAG = "title";
    private static final String URL_TAG = "link";
    private static final String DESCRIPTION_TAG = "description";
    private static final String PUB_DATE_TAG = "pubDate";
    private static final String CATEGORY_TAG = "category";
    private static final String CHANNEL_TAG = "channel";

    private StringBuilder buffer;
    private List<New> news;
    private New currentNew;

    private ChannelHandler channelHandler;

    public NewsHandler(ChannelHandler channelHandler) {
        buffer = new StringBuilder();
        news = new ArrayList<>();
        this.channelHandler = channelHandler;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (qName) {
            case TITLE_TAG:
                currentNew = new New();
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
                currentNew.setTitle(buffer.toString());
                break;
            case URL_TAG:
                currentNew.setLink(buffer.toString());
                break;
            case DESCRIPTION_TAG:
                currentNew.setDescription(buffer.toString());
                break;
            case PUB_DATE_TAG:
                currentNew.setPubDate(buffer.toString());
                break;
            case CATEGORY_TAG:
                currentNew.setCategory(buffer.toString());
                news.add(currentNew);
                break;
            case CHANNEL_TAG:
                channelHandler.endElement(uri, localName, qName);
                channelHandler.restore();
        }
    }

    public List<New> getNews() {
        return news;
    }
}
