package dk.naestebus.datasupplier;

import com.google.common.collect.ImmutableList;
import com.google.inject.BindingAnnotation;
import dk.naestebus.model.Coordinate;
import dk.naestebus.model.Departure;
import dk.naestebus.model.Stop;

import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A {@code DataSupplier} supplies nearby {@link dk.naestebus.model.Stop}s and {@link dk.naestebus.model.Departure}s.
 */
public interface DataSupplier {

    /**
     * Get nearby stops based on certain search criteria. Stops are returned in order of proximity to the latitude/longitude given.
     *
     * @param coordinate The {@link dk.naestebus.model.Coordinate} to search around.
     * @param radius Radius in meters.
     * @param max Max numbers of stops to return.
     * @return An {@link com.google.common.collect.ImmutableList} of {@link dk.naestebus.model.Stop}s, ordered by proximity.
     * @throws IOException If an error occurs searching for stops.
     */
    ImmutableList<Stop> getNearbyStops(Coordinate coordinate, int radius, int max) throws IOException;

    /**
     * Get next departures from a stop. Departures are returned chronologically.
     *
     * @param stopId The stop id to lookup departures from.
     * @return An {@link com.google.common.collect.ImmutableList} of {@link dk.naestebus.model.Departure}s, ordered chronologically.
     * @throws IOException If an error occurs getting departure information.
     */
    ImmutableList<Departure> getNextDepartures(String stopId) throws IOException;

    @BindingAnnotation @Target({ FIELD, PARAMETER, METHOD }) @Retention(RUNTIME)
    public @interface Caching {

    }
}
