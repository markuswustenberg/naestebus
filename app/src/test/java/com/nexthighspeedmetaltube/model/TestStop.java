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
    private static final Coordinate STOP_COORDINATE = new Coordinate(56000000, 10000000);

    @Test
    public void testBasicStop() {
        Stop stop = Stop.newBuilder()
                .setId(STOP_ID)
                .setName(STOP_NAME)
                .setCoordinate(STOP_COORDINATE)
                .build();

        assertEquals(STOP_ID, stop.getId());
        assertEquals(STOP_NAME, stop.getName());
        assertEquals(STOP_COORDINATE, stop.getCoordinate());
    }

    @Test(expected = NullPointerException.class)
    public void stopIdCantBeNull() {
        Stop.newBuilder().setId(null);
    }

    @Test(expected = NullPointerException.class)
    public void stopNameCantBeNull() {
        Stop.newBuilder().setName(null);
    }

    @Test(expected = NullPointerException.class)
    public void stopCoordinateCantBeNull() {
        Stop.newBuilder().setCoordinate(null);
    }

    @Test(expected = IllegalStateException.class)
    public void stopMustHaveId() {
        Stop.newBuilder()
                .setName(STOP_NAME)
                .setCoordinate(STOP_COORDINATE)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void stopMustHaveName() {
        Stop.newBuilder()
                .setId(STOP_ID)
                .setCoordinate(STOP_COORDINATE)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void stopMustHaveCoordinate() {
        Stop.newBuilder()
                .setId(STOP_ID)
                .setName(STOP_NAME)
                .build();
    }
}
