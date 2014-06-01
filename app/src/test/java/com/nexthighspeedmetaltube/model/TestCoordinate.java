package com.nexthighspeedmetaltube.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests a {@link com.nexthighspeedmetaltube.model.Coordinate}.
 */
public class TestCoordinate {

    private static final int LATITUDE = 56000000;
    private static final int LONGITUDE = 10000000;

    private static final int ILLEGAL_LATITUDE_MIN = -90000001;
    private static final int ILLEGAL_LATITUDE_MAX = 90000001;
    private static final int ILLEGAL_LONGITUDE_MIN = -180000001;
    private static final int ILLEGAL_LONGITUDE_MAX = 180000001;

    @Test
    public void basicCoordinate() {
        Coordinate coordinate = new Coordinate(LATITUDE, LONGITUDE);
        assertEquals(LATITUDE, coordinate.getLatitude());
        assertEquals(LONGITUDE, coordinate.getLongitude());
    }

    @Test(expected = IllegalArgumentException.class)
    public void stopLatitudeMustBeInRange() {
        new Coordinate(ILLEGAL_LATITUDE_MIN, LONGITUDE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void stopLatitudeMustBeInRange2() {
        new Coordinate(ILLEGAL_LATITUDE_MAX, LONGITUDE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void stopLongitudeMustBeInRange() {
        new Coordinate(LATITUDE, ILLEGAL_LONGITUDE_MIN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void stopLongitudeMustBeInRange2() {
        new Coordinate(LATITUDE, ILLEGAL_LONGITUDE_MAX);
    }
}
