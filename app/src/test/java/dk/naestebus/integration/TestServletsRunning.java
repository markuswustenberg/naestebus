package dk.naestebus.integration;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;

/**
 * Integration tests for the server output for stops. The actual content of the output is not tested, only return HTTP status codes etc.
 */
public class TestServletsRunning {

    private static final Logger log = LoggerFactory.getLogger(TestServletsRunning.class);

    private static final String CONTENT_TYPE = "application/json; charset=UTF-8";
    private static final String BASE_URL = "http://localhost:8080/";

    static {
        log.warn("Because of a bug in Gradle, the jetty daemon isn't automatically stopped after the tests. Unfortunately, it has to be stopped manually.");
    }

    @Test
    public void stopsOk() throws IOException {
        HttpURLConnection connection = getConnection("stops?latitude=56&longitude=10&radius=1000&max=5");
        assertEquals(HttpServletResponse.SC_OK, connection.getResponseCode());
        assertEquals(CONTENT_TYPE, connection.getContentType());
    }

    @Test
    public void stopsNoLatitude() throws IOException {
        HttpURLConnection connection = getConnection("stops?longitude=10&radius=1000&max=5");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, connection.getResponseCode());
    }

    @Test
    public void stopsNoLongtitude() throws IOException {
        HttpURLConnection connection = getConnection("stops?latitude=56&radius=1000&max=5");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, connection.getResponseCode());
    }

    @Test
    public void stopsNoRadius() throws IOException {
        HttpURLConnection connection = getConnection("stops?latitude=56&longitude=10&max=5");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, connection.getResponseCode());
    }

    @Test
    public void stopsNoMax() throws IOException {
        HttpURLConnection connection = getConnection("stops?latitude=56&longitude=10&radius=1000");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, connection.getResponseCode());
    }

    @Test
    public void stopsBadRadius() throws IOException {
        HttpURLConnection connection = getConnection("stops?latitude=56&longitude=10&radius=0&max=5");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, connection.getResponseCode());
    }

    @Test
    public void stopsBadMax() throws IOException {
        HttpURLConnection connection = getConnection("stops?latitude=56&longitude=10&radius=1000&max=0");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, connection.getResponseCode());
    }

    @Test
    public void departuresOk() throws IOException {
        HttpURLConnection connection = getConnection("departures?stopId=42&max=5");
        assertEquals(HttpServletResponse.SC_OK, connection.getResponseCode());
        assertEquals(CONTENT_TYPE, connection.getContentType());
    }

    @Test
    public void departuresNoStopId() throws IOException {
        HttpURLConnection connection = getConnection("departures?max=5");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, connection.getResponseCode());
    }

    @Test
    public void departuresNoMax() throws IOException {
        HttpURLConnection connection = getConnection("departures?stopId=42");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, connection.getResponseCode());
    }

    @Test
    public void departuresBadMax() throws IOException {
        HttpURLConnection connection = getConnection("departures?stopId=42&max=0");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, connection.getResponseCode());
    }

    private HttpURLConnection getConnection(String urlAsString) throws IOException {
        URL url = new URL(BASE_URL + urlAsString);
        return (HttpURLConnection) url.openConnection();
    }
}
