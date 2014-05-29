package com.nexthighspeedmetaltube;

import com.nexthighspeedmetaltube.model.Stop;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests a {@link com.nexthighspeedmetaltube.model.Stop}.
 * <p>
 * We are not currently testing {@link com.nexthighspeedmetaltube.model.Stop#equals(Object)}, {@link com.nexthighspeedmetaltube.model.Stop#hashCode()}, nor
 * {@link com.nexthighspeedmetaltube.model.Stop#toString()}.
 */
public class TestStop {

    // TODO: Not yet sure what the ID looks like, check spec.
    private static final String STOP_ID = "8f943f9034";
    private static final String STOP_NAME = "Street Station";
    private static final int STOP_LATITUDE = 56000000;
    private static final int STOP_LONGITUDE = 10000000;

    @Test
    public void testBasicStop() {
        Stop stop = Stop.newBuilder()
                .setId(STOP_ID)
                .setName(STOP_NAME)
                .setLatitude(STOP_LATITUDE)
                .setLongitude(STOP_LONGITUDE)
                .build();

        assertEquals(STOP_ID, stop.getId());
        assertTrue(stop.hasName());
        assertEquals(STOP_NAME, stop.getName());
        assertEquals(STOP_LATITUDE, stop.getLatitude());
        assertEquals(STOP_LONGITUDE, stop.getLongitude());
    }

    @Test
    public void testNoNameStop() {
        Stop stop = Stop.newBuilder()
                .setId(STOP_ID)
                .setLatitude(STOP_LATITUDE)
                .setLongitude(STOP_LONGITUDE)
                .build();
        assertFalse(stop.hasName());
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
        Stop.newBuilder().setLatitude(-90000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void stopLatitudeMustBeInRange2() {
        Stop.newBuilder().setLatitude(90000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void stopLongitudeMustBeInRange() {
        Stop.newBuilder().setLatitude(-180000001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void stopLongitudeMustBeInRange2() {
        Stop.newBuilder().setLatitude(180000001);
    }

    @Test(expected = IllegalStateException.class)
    public void stopMustHaveId() {
        Stop.newBuilder()
                .setLatitude(STOP_LATITUDE)
                .setLongitude(STOP_LONGITUDE)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void stopMustHaveLatitude() {
        Stop.newBuilder()
                .setId(STOP_ID)
                .setLongitude(STOP_LONGITUDE)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void stopMustHaveLongitude() {
        Stop.newBuilder()
                .setId(STOP_ID)
                .setLatitude(STOP_LATITUDE)
                .build();
    }
}
