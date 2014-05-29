package com.nexthighspeedmetaltube.datasupplier;

import com.google.common.collect.ImmutableList;
import com.nexthighspeedmetaltube.model.Stop;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Supplies data to our web service through Rejseplanen. See the <a href="http://labs.rejseplanen.dk/labs/data__brug/rejseplanens_api/">Rejseplan API doc</a>.
 */
public final class RejseplanDataSupplier implements DataSupplier {

    private static final String BASE_URL = "http://xmlopen.rejseplanen.dk/bin/rest.exe/";
    private static final String NEARBY_STOPS_URL = "stopsNearby?coordX=%d&coordY=%d&maxRadius=%d&maxNumber=%d";

    private static final String XML_STOP = "StopLocation";
    private static final String XML_STOP_ID = "id";
    private static final String XML_STOP_NAME = "name";
    private static final String XML_STOP_LATITUDE = "y";
    private static final String XML_STOP_LONGITUDE = "x";

    @Override
    public ImmutableList<Stop> findNearbyStops(int latitude, int longitude, int radius, int max) throws IOException {
        final ImmutableList.Builder<Stop> stops = ImmutableList.builder();

        try {
            URL url = new URL(BASE_URL + String.format(NEARBY_STOPS_URL, longitude, latitude, radius, max));
            URLConnection connection = url.openConnection();
            BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
            SAXParserFactory parserFactory = SAXParserFactory.newInstance();
            SAXParser parser = parserFactory.newSAXParser();
            parser.parse(in, new DefaultHandler() {
                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if (!XML_STOP.equals(qName)) {
                        return;
                    }

                    Stop stop = Stop.newBuilder()
                            .setId(attributes.getValue(XML_STOP_ID))
                            .setName(attributes.getValue(XML_STOP_NAME))
                            .setLatitude(Integer.parseInt(attributes.getValue(XML_STOP_LATITUDE)))
                            .setLongitude(Integer.parseInt(attributes.getValue(XML_STOP_LONGITUDE)))
                            .build();
                    stops.add(stop);
                }
            });
        } catch (MalformedURLException e) {
            // This should never happen, and is a programming error. Therefore, throw it again as a RuntimeException.
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            // This likewise should never happen, and is a programming error. Therefore, throw it again as a RuntimeException.
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new IOException(e);
        }

        return stops.build();
    }
}
