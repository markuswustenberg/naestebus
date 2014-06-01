package com.nexthighspeedmetaltube;

import com.google.common.collect.ImmutableList;
import com.nexthighspeedmetaltube.model.Coordinate;
import com.nexthighspeedmetaltube.model.Departure;
import com.nexthighspeedmetaltube.model.Stop;
import com.nexthighspeedmetaltube.serialize.JsonSerializer;
import com.nexthighspeedmetaltube.serialize.Serializer;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests a {@link com.nexthighspeedmetaltube.serialize.JsonSerializer}.
 */
public class TestJsonSerializer {

    private static final ImmutableList<Stop> STOPS = ImmutableList.of(
            Stop.newBuilder().setId("id1").setName("stop1").setCoordinate(new Coordinate(0, 0)).build(),
            Stop.newBuilder().setId("id2").setName("stop2").setCoordinate(new Coordinate(1, 1)).build()
    );

    private static final String SERIALIZED_STOPS = "[{\"id\":\"id1\",\"name\":\"stop1\",\"coordinate\":{\"latitude\":0,\"longitude\":0}}," +
            "{\"id\":\"id2\",\"name\":\"stop2\",\"coordinate\":{\"latitude\":1,\"longitude\":1}}]";

    private static final ImmutableList<Departure> DEPARTURES = ImmutableList.of(
            Departure.newBuilder().setName("dep1").setTime(new DateTime(0)).build(),
            Departure.newBuilder().setName("dep2").setTime(new DateTime(1)).setDirection("home").build()
    );

    private static final String SERIALIZED_DEPARTURES = "[{\"name\":\"dep1\",\"direction\":\"\",\"hasDirection\":false,\"time\":\"01:00\"}," +
            "{\"name\":\"dep2\",\"direction\":\"home\",\"hasDirection\":true,\"time\":\"01:00\"}]";

    private Serializer serializer;

    @Before
    public void setUp() {
        serializer = new JsonSerializer();
    }

    @Test
    public void stopsSerialization() {
        String serializedStops = serializer.serializeStops(STOPS);
        assertEquals(SERIALIZED_STOPS, serializedStops);
    }

    @Test
    public void departuresSerialization() {
        String serializedDepartures = serializer.serializeDepartures(DEPARTURES);
        assertEquals(SERIALIZED_DEPARTURES, serializedDepartures);
    }
}
