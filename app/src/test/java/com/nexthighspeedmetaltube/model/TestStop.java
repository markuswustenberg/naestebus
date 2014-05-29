package com.nexthighspeedmetaltube.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests a {@link com.nexthighspeedmetaltube.model.Stop}.
 * <p>
 * We are not currently testing {@link com.nexthighspeedmetaltube.model.Stop#equals(Object)}, {@link com.nexthighspeedmetaltube.model.Stop#hashCode()}, nor
 * {@link com.nexthighspeedmetaltube.model.Stop#toString()}.
 */
public class TestStop {

    private static final String STOP_ID = "8f943f9034";
    private static final String STOP_NAME = "Street Station";
    private static final int STOP_LATITUDE = 56000000;
    private static final int STOP_LONGITUDE = 10000000;

    private static final int ILLEGAL_LATITUDE_MIN = -90000001;
    private static final int ILLEGAL_LATITUDE_MAX = 90000001;
    private static final int ILLEGAL_LONGITUDE_MIN = -180000001;
    private static final int ILLEGAL_LONGITUDE_MAX = 180000001;

    @Test
    public void testBasicStop() {
        Stop stop = Stop.newBuilder()
                .setId(STOP_ID)
                .setName(STOP_NAME)
                .setLatitude(STOP_LATITUDE)
                .setLongitude(STOP_LONGITUDE)
                .build();

        assertEquals(STOP_ID, stop.getId());
        assertEquals(STOP_NAME, stop.getName());
        assertEquals(STOP_LATITUDE, stop.getLatitude());
        assertEquals(STOP_LONGITUDE, stop.getLongitude());
    }

    @Test(expected = NullPointerException.class)
    public void stopIdCantBeNull() {
        Stop.newBuilder().setId(null);
    }

    @Test(expected = NullPointerException.class)
    public void stopNameCantBeNull() {
        Stop.newBuilder().setName(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void stopLatitudeMustBeInRange() {
        Stop.newBuilder().setLatitude(ILLEGAL_LATITUDE_MIN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void stopLatitudeMustBeInRange2() {
        Stop.newBuilder().setLatitude(ILLEGAL_LATITUDE_MAX);
    }

    @Test(expected = IllegalArgumentException.class)
    public void stopLongitudeMustBeInRange() {
        Stop.newBuilder().setLatitude(ILLEGAL_LONGITUDE_MIN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void stopLongitudeMustBeInRange2() {
        Stop.newBuilder().setLatitude(ILLEGAL_LONGITUDE_MAX);
    }

    @Test(expected = IllegalStateException.class)
    public void stopMustHaveId() {
        Stop.newBuilder()
                .setName(STOP_NAME)
                .setLatitude(STOP_LATITUDE)
                .setLongitude(STOP_LONGITUDE)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void stopMustHaveName() {
        Stop.newBuilder()
                .setId(STOP_ID)
                .setLatitude(STOP_LATITUDE)
                .setLongitude(STOP_LONGITUDE)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void stopMustHaveLatitude() {
        Stop.newBuilder()
                .setId(STOP_ID)
                .setName(STOP_NAME)
                .setLongitude(STOP_LONGITUDE)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void stopMustHaveLongitude() {
        Stop.newBuilder()
                .setId(STOP_ID)
                .setName(STOP_NAME)
                .setLatitude(STOP_LATITUDE)
                .build();
    }
}
