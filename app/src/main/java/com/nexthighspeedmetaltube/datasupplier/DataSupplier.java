package com.nexthighspeedmetaltube.datasupplier;

import com.google.common.collect.ImmutableList;
import com.nexthighspeedmetaltube.model.Coordinate;
import com.nexthighspeedmetaltube.model.Departure;
import com.nexthighspeedmetaltube.model.Stop;
import org.joda.time.ReadableDateTime;

import java.io.IOException;

/**
 * A {@code DataSupplier} supplies nearby {@link com.nexthighspeedmetaltube.model.Stop}s and {@link com.nexthighspeedmetaltube.model.Departure}s.
 */
public interface DataSupplier {

    /**
     * Get nearby stops based on certain search criteria. Stops are returned in order of proximity to the latitude/longitude given.
     *
     * @param coordinate Coordinate.
     * @param radius Radius in meters.
     * @param max Max numbers of stops to return.
     * @return An {@link com.google.common.collect.ImmutableList} of {@link com.nexthighspeedmetaltube.model.Stop}s, ordered by proximity.
     * @throws IOException If an error occurs searching for stops.
     */
    ImmutableList<Stop> getNearbyStops(Coordinate coordinate, int radius, int max) throws IOException;

    /**
     * Get next departures based on certain search criteria. Departures are returned chronologically.
     * @param stopId The stop id to lookup departures from.
     * @param time The time to lookup departures from.
     * @return An {@link com.google.common.collect.ImmutableList} of {@link com.nexthighspeedmetaltube.model.Departure}s, ordered chronologically.
     * @throws IOException If an error occurs getting departure information.
     */
    ImmutableList<Departure> getNextDepartures(String stopId, ReadableDateTime time) throws IOException;
}
