package com.nexthighspeedmetaltube.model;

import org.joda.time.DateTime;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests all aspects of {@link com.nexthighspeedmetaltube.model.Departure}.
 */
public class TestDeparture {

    private static final String DEPARTURE_NAME = "Bus 100";
    private static final DateTime DEPARTURE_TIME = DateTime.now();
    private static final String DEPARTURE_DIRECTION = "Market";

    @Test
    public void testBasicDeparture() {
        Departure departure = Departure.newBuilder()
                .setName(DEPARTURE_NAME)
                .setTime(DEPARTURE_TIME)
                .setDirection(DEPARTURE_DIRECTION)
                .build();

        assertEquals(DEPARTURE_NAME, departure.getName());
        assertEquals(DEPARTURE_TIME, departure.getTime());
        assertTrue(departure.hasDirection());
        assertEquals(DEPARTURE_DIRECTION, departure.getDirection());
    }

    @Test
    public void mayNotHaveDirection() {
        Departure departure = Departure.newBuilder()
                .setName(DEPARTURE_NAME)
                .setTime(DEPARTURE_TIME)
                .build();

        assertFalse(departure.hasDirection());
    }

    @Test(expected = NullPointerException.class)
    public void nameCantBeNull() {
        Departure.newBuilder().setName(null);
    }

    @Test(expected = NullPointerException.class)
    public void timeCantBeNull() {
        Departure.newBuilder().setTime(null);
    }

    @Test(expected = NullPointerException.class)
    public void directionCantBeNull() {
        Departure.newBuilder().setDirection(null);
    }

    @Test(expected = IllegalStateException.class)
    public void mustHaveName() {
        Departure.newBuilder()
                .setTime(DEPARTURE_TIME)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void mustHaveTime() {
        Departure.newBuilder()
                .setName(DEPARTURE_NAME)
                .build();
    }
}
