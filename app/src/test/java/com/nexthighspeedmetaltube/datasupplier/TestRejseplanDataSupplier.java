package com.nexthighspeedmetaltube.datasupplier;

import com.google.common.collect.ImmutableList;
import com.nexthighspeedmetaltube.model.Stop;
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

    private static final String STOP1_ID = "751464200";
    private static final String STOP1_NAME = "Paludan-Müllers Vej/Ekkodalen (Aarhus)";
    private static final int STOP1_LATITUDE = 56172728;
    private static final int STOP1_LONGITUDE = 10183438;

    private static final String STOP2_ID = "751464100";
    private static final String STOP2_NAME = "Paludan-Müllers Vej/Åbogade (Aarhus)";
    private static final int STOP2_LATITUDE = 56170247;
    private static final int STOP2_LONGITUDE = 10186692;


    private DataSupplier dataSupplier;

    @Before
    public void before() {
        dataSupplier = new RejseplanDataSupplier();
    }

    @Test
    public void testFindStops() throws IOException {
        // Uber office is approximately at 56.1726287,10.1869956
        // Test only a simple case: http://xmlopen.rejseplanen.dk/bin/rest.exe/stopsNearby?coordX=10186995&coordY=56172628&maxRadius=1000&maxNumber=2
        ImmutableList<Stop> stops = dataSupplier.getNearbyStops(UBER_OFFICE_LATITUDE, UBER_OFFICE_LONGITUDE, NEARBY_STOPS_RADIUS, NEARBY_STOPS_STOP_COUNT);

        assertNotNull(stops);
        assertEquals(NEARBY_STOPS_STOP_COUNT, stops.size());

        Stop stop1 = stops.get(0);
        assertEquals(STOP1_ID, stop1.getId());
        assertEquals(STOP1_NAME, stop1.getName());
        assertEquals(STOP1_LATITUDE, stop1.getLatitude());
        assertEquals(STOP1_LONGITUDE, stop1.getLongitude());

        Stop stop2 = stops.get(1);
        assertEquals(STOP2_ID, stop2.getId());
        assertEquals(STOP2_NAME, stop2.getName());
        assertEquals(STOP2_LATITUDE, stop2.getLatitude());
        assertEquals(STOP2_LONGITUDE, stop2.getLongitude());
    }
}
