package dk.naestebus.datasupplier;

import com.google.common.collect.ImmutableList;
import dk.naestebus.model.Coordinate;
import dk.naestebus.model.Departure;
import dk.naestebus.model.Stop;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.ReadableInstant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Tests the {@link dk.naestebus.datasupplier.CachingDataSupplier}.
 */
public class TestCachingDataSupplier {

    private static final Coordinate COORDINATE = new Coordinate(56000000, 10000000);
    private static final int RADIUS = 1000;
    private static final int STOP_COUNT = 2;

    private static final ImmutableList<Stop> STOPS = ImmutableList.of(
            Stop.newBuilder().setId("stop1").setName("Stop 1").setCoordinate(COORDINATE).build(),
            Stop.newBuilder().setId("stop2").setName("Stop 2").setCoordinate(COORDINATE).build()
    );

    private static final String STOP_ID = "id";
    private static final Departure DEPARTURE1 = Departure.newBuilder()
            .setName("bus1")
            .setTime(new DateTime("2014-05-30T23:50:00Z"))
            .build();
    private static final Departure DEPARTURE2 = Departure.newBuilder()
            .setName("bus2")
            .setTime(DEPARTURE1.getTime().plusHours(5))
            .build();
    private static final Departure DEPARTURE3 = Departure.newBuilder()
            .setName("bus3")
            .setTime(DEPARTURE2.getTime().plusMinutes(10))
            .build();

    private static final ImmutableList<Departure> FIRST_DEPARTURES = ImmutableList.of(DEPARTURE1, DEPARTURE2);
    private static final ImmutableList<Departure> SECOND_DEPARTURES = ImmutableList.of(DEPARTURE2, DEPARTURE3);

    private static final DateTime TWO_SECOND_BEFORE = DEPARTURE1.getTime().minusSeconds(2);
    private static final DateTime ONE_SECOND_BEFORE = DEPARTURE1.getTime().minusSeconds(1);
    private static final DateTime AT = DEPARTURE1.getTime();
    private static final DateTime ONE_SECOND_AFTER = DEPARTURE1.getTime().plusSeconds(1);

    private DataSupplier mockDepartureDataSupplier;
    private DataSupplier cachingDepartureDataSupplier;

    @Before
    public void setUp() throws IOException {
        mockDepartureDataSupplier = mock(DataSupplier.class);
        cachingDepartureDataSupplier = new CachingDataSupplier(mockDepartureDataSupplier);
    }

    @After
    public void resetTime() {
        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test
    public void testNearbyStopsNotCaching() throws IOException {
        DataSupplier mockDataSupplier = mock(DataSupplier.class);
        when(mockDataSupplier.getNearbyStops(COORDINATE, RADIUS, STOP_COUNT)).thenReturn(STOPS);
        DataSupplier dataSupplier = new CachingDataSupplier(mockDataSupplier);

        // Stop time
        DateTimeUtils.setCurrentMillisFixed(0);

        // Get stops and verify that this happened once
        ImmutableList<Stop> stops = dataSupplier.getNearbyStops(COORDINATE, RADIUS, STOP_COUNT);
        assertEquals(STOPS, stops);
        Mockito.verify(mockDataSupplier, times(1)).getNearbyStops(COORDINATE, RADIUS, STOP_COUNT);

        // Do the same, and verify that getting nearby stops is not cached (i.e. underlying method called again)
        stops = dataSupplier.getNearbyStops(COORDINATE, RADIUS, STOP_COUNT);
        assertEquals(STOPS, stops);
        Mockito.verify(mockDataSupplier, times(2)).getNearbyStops(COORDINATE, RADIUS, STOP_COUNT);
    }

    @Test
    public void cacheReturnsCorrectDataAtCacheMiss() throws IOException {
        // Test that the method will be called once
        when(mockDepartureDataSupplier.getNextDepartures(STOP_ID)).thenReturn(FIRST_DEPARTURES);

        setTime(TWO_SECOND_BEFORE);
        ImmutableList<Departure> departures = cachingDepartureDataSupplier.getNextDepartures(STOP_ID);
        assertEquals(FIRST_DEPARTURES, departures);
        Mockito.verify(mockDepartureDataSupplier, times(1)).getNextDepartures(STOP_ID);
    }

    @Test
    public void departureCachingWorkingSameTime() throws IOException {
        // Call twice with same time, must be same results, but called once
        when(mockDepartureDataSupplier.getNextDepartures(STOP_ID)).thenReturn(FIRST_DEPARTURES);

        setTime(TWO_SECOND_BEFORE);
        cachingDepartureDataSupplier.getNextDepartures(STOP_ID);
        ImmutableList<Departure> departures = cachingDepartureDataSupplier.getNextDepartures(STOP_ID);

        assertEquals(FIRST_DEPARTURES, departures);
        Mockito.verify(mockDepartureDataSupplier, times(1)).getNextDepartures(STOP_ID);
    }

    @Test
    public void departureCacheWorkingNewTime() throws IOException {
        // Call twice with different time, must be same results, but not called second time
        when(mockDepartureDataSupplier.getNextDepartures(STOP_ID)).thenReturn(FIRST_DEPARTURES);

        setTime(TWO_SECOND_BEFORE);
        cachingDepartureDataSupplier.getNextDepartures(STOP_ID);

        setTime(ONE_SECOND_BEFORE);
        ImmutableList<Departure> departures = cachingDepartureDataSupplier.getNextDepartures(STOP_ID);

        assertEquals(FIRST_DEPARTURES, departures);
        Mockito.verify(mockDepartureDataSupplier, times(1)).getNextDepartures(STOP_ID);
    }

    @Test
    public void clearCacheAtDepartureTime() throws IOException {
        // Call twice, just before and at departure time. Must be called twice, different results
        when(mockDepartureDataSupplier.getNextDepartures(STOP_ID)).thenReturn(FIRST_DEPARTURES, SECOND_DEPARTURES);

        setTime(ONE_SECOND_BEFORE);
        ImmutableList<Departure> departuresBefore = cachingDepartureDataSupplier.getNextDepartures(STOP_ID);
        assertEquals(FIRST_DEPARTURES, departuresBefore);

        setTime(AT);
        ImmutableList<Departure> departuresAt = cachingDepartureDataSupplier.getNextDepartures(STOP_ID);
        assertEquals(SECOND_DEPARTURES, departuresAt);

        Mockito.verify(mockDepartureDataSupplier, times(2)).getNextDepartures(STOP_ID);
    }

    @Test
    public void clearCacheAtDepartureTimeThenCacheAgain() throws IOException {
        // Call four times, just before twice and at departure time, then after. Must be called twice.
        when(mockDepartureDataSupplier.getNextDepartures(STOP_ID)).thenReturn(FIRST_DEPARTURES, SECOND_DEPARTURES);

        setTime(ONE_SECOND_BEFORE);
        cachingDepartureDataSupplier.getNextDepartures(STOP_ID);
        cachingDepartureDataSupplier.getNextDepartures(STOP_ID);
        setTime(AT);
        cachingDepartureDataSupplier.getNextDepartures(STOP_ID);
        setTime(ONE_SECOND_AFTER);
        ImmutableList<Departure> departuresAfter = cachingDepartureDataSupplier.getNextDepartures(STOP_ID);
        assertEquals(SECOND_DEPARTURES, departuresAfter);

        Mockito.verify(mockDepartureDataSupplier, times(2)).getNextDepartures(STOP_ID);
    }

    @Test
    public void dontCacheEmptyDepartures() throws IOException {
        // Call twice at same time. Must be called twice
        ImmutableList<Departure> empty = ImmutableList.of();
        when(mockDepartureDataSupplier.getNextDepartures(STOP_ID)).thenReturn(empty);

        setTime(ONE_SECOND_BEFORE);
        cachingDepartureDataSupplier.getNextDepartures(STOP_ID);
        cachingDepartureDataSupplier.getNextDepartures(STOP_ID);

        Mockito.verify(mockDepartureDataSupplier, times(2)).getNextDepartures(STOP_ID);
    }

    private void setTime(ReadableInstant now) {
        DateTimeUtils.setCurrentMillisFixed(now.getMillis());
    }
}
