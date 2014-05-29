package com.nexthighspeedmetaltube.datasupplier;

import com.google.common.collect.ImmutableList;
import com.nexthighspeedmetaltube.model.Stop;

import java.io.IOException;

/**
 * A {@code DataSupplier} supplies nearby {@link com.nexthighspeedmetaltube.model.Stop}s and {@link com.nexthighspeedmetaltube.model.Departure}s.
 */
public interface DataSupplier {

    /**
     * This method finds nearby stops based on certain search criteria. Stops are returned in order of proximity to the latitude/longitude.
     *
     * @param latitude Latitude in WGS84 format.
     * @param longitude Longitude in WGS84 format.
     * @param radius Radius in meters.
     * @param max Max numbers of stops to return.
     * @return An {@link com.google.common.collect.ImmutableList} of {@link com.nexthighspeedmetaltube.model.Stop}s, ordered by proximity.
     * @throws IOException If an error occurs searching for stops.
     */
    ImmutableList<Stop> findNearbyStops(int latitude, int longitude, int radius, int max) throws IOException;
}
