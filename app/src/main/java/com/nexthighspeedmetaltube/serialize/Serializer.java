package com.nexthighspeedmetaltube.serialize;

import com.google.common.collect.ImmutableList;
import com.nexthighspeedmetaltube.model.Departure;
import com.nexthighspeedmetaltube.model.Stop;

/**
 * A {@code Serializer} serializes lists of {@link com.nexthighspeedmetaltube.model.Stop}s and
 * {@link com.nexthighspeedmetaltube.model.Departure}s.
 * <p>
 * Implementations are thread-safe.
 */
public interface Serializer {

    /**
     * Serialize a list of {@link com.nexthighspeedmetaltube.model.Stop}s to a string.
     * @param stops A list of {@link com.nexthighspeedmetaltube.model.Stop}s.
     * @return A string representation of the input values.
     */
    String serializeStops(ImmutableList<Stop> stops);

    /**
     * Serialize a list of {@link com.nexthighspeedmetaltube.model.Departure}s to a string.
     * @param departures A list of {@link com.nexthighspeedmetaltube.model.Departure}s.
     * @return A string representation of the input values.
     */
    String serializeDepartures(ImmutableList<Departure> departures);
}
