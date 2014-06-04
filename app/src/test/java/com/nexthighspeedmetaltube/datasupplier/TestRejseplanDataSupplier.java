package com.nexthighspeedmetaltube.datasupplier;

import com.google.common.collect.ImmutableList;
import com.nexthighspeedmetaltube.model.Coordinate;
import com.nexthighspeedmetaltube.model.Departure;
import com.nexthighspeedmetaltube.model.Stop;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * Tests the {@link com.nexthighspeedmetaltube.datasupplier.RejseplanDataSupplier}.
 */
public class TestRejseplanDataSupplier {

    private static final Coordinate UBER_OFFICE_COORDINATE = new Coordinate(56172628, 10186995);
    private static final int NEARBY_STOPS_RADIUS = 1000;
    private static final int NEARBY_STOPS_STOP_COUNT = 2;

    private static final int DEPARTURES_STOP_COUNT = 1;

    private static final String STOP1_NAME = "Paludan-Müllers Vej/Ekkodalen (Aarhus)";
    private static final Coordinate STOP1_COORDINATE = new Coordinate(56172728, 10183438);

    private static final String STOP2_NAME = "Paludan-Müllers Vej/Åbogade (Aarhus)";
    private static final Coordinate STOP2_COORDINATE = new Coordinate(56170247, 10186692);

    private DataSupplier dataSupplier;

    @Before
    public void before() {
        dataSupplier = new RejseplanDataSupplier();
    }

    @Test
    public void getStops() throws IOException {
        // Uber office is approximately at 56.1726287,10.1869956
        // Test only a simple case: http://xmlopen.rejseplanen.dk/bin/rest.exe/stopsNearby?coordX=10186995&coordY=56172628&maxRadius=1000&maxNumber=2
        ImmutableList<Stop> stops = dataSupplier.getNearbyStops(UBER_OFFICE_COORDINATE, NEARBY_STOPS_RADIUS, NEARBY_STOPS_STOP_COUNT);

        assertNotNull(stops);
        assertEquals(NEARBY_STOPS_STOP_COUNT, stops.size());

        Stop stop1 = stops.get(0);
        // Stops might be tested, but their ids change frequently!
        //assertEquals(STOP1_ID, stop1.getId());
        assertEquals(STOP1_NAME, stop1.getName());
        assertEquals(STOP1_COORDINATE, stop1.getCoordinate());

        Stop stop2 = stops.get(1);
        // Stops might be tested, but their ids change frequently!
        //assertEquals(STOP2_ID, stop2.getId());
        assertEquals(STOP2_NAME, stop2.getName());
        assertEquals(STOP2_COORDINATE, stop2.getCoordinate());
    }

    @Test
    public void getDepartures() throws IOException {
        // We need an id that changes frequently according to spec
        ImmutableList<Stop> stops = dataSupplier.getNearbyStops(UBER_OFFICE_COORDINATE, NEARBY_STOPS_RADIUS, DEPARTURES_STOP_COUNT);

        ImmutableList<Departure> departures = dataSupplier.getNextDepartures(stops.get(0).getId());

        // We can only really test that there are some departures, as they change frequently
        assertFalse(departures.isEmpty());
    }
}
