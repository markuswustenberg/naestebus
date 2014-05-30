package com.nexthighspeedmetaltube.datasupplier;

import com.google.common.collect.ImmutableList;
import com.nexthighspeedmetaltube.model.Coordinate;
import com.nexthighspeedmetaltube.model.Departure;
import com.nexthighspeedmetaltube.model.Stop;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests the {@link com.nexthighspeedmetaltube.datasupplier.RejseplanDataSupplier}.
 */
public class TestRejseplanDataSupplier {

    private static final int UBER_OFFICE_LATITUDE = 56172628;
    private static final int UBER_OFFICE_LONGITUDE = 10186995;
    private static final int NEARBY_STOPS_RADIUS = 1000;
    private static final int NEARBY_STOPS_STOP_COUNT = 2;

    private static final int DEPARTURES_STOP_COUNT = 1;
    private static final DateTime DEPARTURES_TIME = new DateTime("2014-05-30T13:36:00+02:00");

    private static final String STOP1_NAME = "Paludan-Müllers Vej/Ekkodalen (Aarhus)";
    private static final Coordinate STOP1_COORDINATE = new Coordinate(56172728, 10183438);

    private static final String STOP2_NAME = "Paludan-Müllers Vej/Åbogade (Aarhus)";
    private static final Coordinate STOP2_COORDINATE = new Coordinate(56170247, 10186692);

    private static final String DEPARTURE1_NAME = "Bus 2A";
    private static final DateTime DEPARTURE1_TIME = new DateTime("2014-05-30T13:37:00+02:00");
    private static final String DEPARTURE1_DIRECTION = "Bjødstrupvej/Karetmagertoften (Aarhus)";

    private static final String DEPARTURE2_NAME = DEPARTURE1_NAME;
    private static final DateTime DEPARTURE2_TIME = new DateTime("2014-05-30T13:43:00+02:00");
    private static final String DEPARTURE2_DIRECTION = "Aarhus Uni. hospital  Skejby indg 8-11";

    private DataSupplier dataSupplier;

    @Before
    public void before() {
        dataSupplier = new RejseplanDataSupplier();
    }

    @Test
    public void getStops() throws IOException {
        // Uber office is approximately at 56.1726287,10.1869956
        // Test only a simple case: http://xmlopen.rejseplanen.dk/bin/rest.exe/stopsNearby?coordX=10186995&coordY=56172628&maxRadius=1000&maxNumber=2
        ImmutableList<Stop> stops = dataSupplier.getNearbyStops(UBER_OFFICE_LATITUDE, UBER_OFFICE_LONGITUDE, NEARBY_STOPS_RADIUS, NEARBY_STOPS_STOP_COUNT);

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
        ImmutableList<Stop> stops = dataSupplier.getNearbyStops(UBER_OFFICE_LATITUDE, UBER_OFFICE_LONGITUDE, NEARBY_STOPS_RADIUS, DEPARTURES_STOP_COUNT);

        ImmutableList<Departure> departures = dataSupplier.getNextDepartures(stops.get(0).getId(), DEPARTURES_TIME);

        // Test a few
        Departure departure1 = departures.get(0);
        assertEquals(DEPARTURE1_NAME, departure1.getName());
        assertEquals(DEPARTURE1_TIME, departure1.getTime());
        assertEquals(DEPARTURE1_DIRECTION, departure1.getDirection());

        Departure departure2 = departures.get(1);
        assertEquals(DEPARTURE2_NAME, departure2.getName());
        assertEquals(DEPARTURE2_TIME, departure2.getTime());
        assertEquals(DEPARTURE2_DIRECTION, departure2.getDirection());
    }
}
