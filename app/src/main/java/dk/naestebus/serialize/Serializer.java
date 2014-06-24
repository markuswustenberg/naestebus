package dk.naestebus.serialize;

import com.google.common.collect.ImmutableList;
import dk.naestebus.model.Departure;
import dk.naestebus.model.Stop;

/**
 * A {@code Serializer} serializes lists of {@link dk.naestebus.model.Stop}s and
 * {@link dk.naestebus.model.Departure}s.
 * <p>
 * Implementations are thread-safe.
 */
public interface Serializer {

    /**
     * Serialize a list of {@link dk.naestebus.model.Stop}s to a string.
     * @param stops A list of {@link dk.naestebus.model.Stop}s.
     * @return A string representation of the input values.
     */
    String serializeStops(ImmutableList<Stop> stops);

    /**
     * Serialize a list of {@link dk.naestebus.model.Departure}s to a string.
     * @param departures A list of {@link dk.naestebus.model.Departure}s.
     * @return A string representation of the input values.
     */
    String serializeDepartures(ImmutableList<Departure> departures);
}
