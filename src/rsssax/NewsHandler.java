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

    private StringBuilder buffer;
    private List<New> news;

    public NewsHandler() {
        buffer = new StringBuilder();
        news = new ArrayList<>();
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
                System.out.println("Titulo: " + buffer.toString());
                break;
            case URL_TAG:
                System.out.println("Url: " + buffer.toString());
                break;
            case DESCRIPTION_TAG:
                System.out.println("Description: " + buffer.toString());
                break;
            case PUB_DATE_TAG:
                System.out.println("Publication date: " + buffer.toString());
                break;
            case CATEGORY_TAG:
                System.out.println("Category: " + buffer.toString());
                break;
        }
    }

    public List<New> getNews() {
        return news;
    }
}
