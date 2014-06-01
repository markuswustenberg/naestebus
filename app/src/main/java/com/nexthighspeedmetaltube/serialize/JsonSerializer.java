package com.nexthighspeedmetaltube.serialize;

import com.google.common.collect.ImmutableList;
import com.google.gson.*;
import com.google.inject.Singleton;
import com.nexthighspeedmetaltube.model.Departure;
import com.nexthighspeedmetaltube.model.Stop;
import org.joda.time.DateTime;

import java.lang.reflect.Type;

/**
 * A {@link com.nexthighspeedmetaltube.serialize.Serializer} that converts to JSON representation.
 * <p>
 * This class is immutable and thread-safe.
 */
@Singleton
public final class JsonSerializer implements Serializer {

    private static final String TIME_PATTERN = "HH:mm";

    private final transient Gson serializer = new GsonBuilder()
            .registerTypeAdapter(DateTime.class, new DateTimeSerializer())
            .create();

    @Override
    public String serializeStops(ImmutableList<Stop> stops) {
        return serializer.toJson(stops);
    }

    @Override
    public String serializeDepartures(ImmutableList<Departure> departures) {
        return serializer.toJson(departures);
    }

    /**
     * A {@link com.google.gson.JsonSerializer} that serializes Joda-Time's {@link org.joda.time.DateTime} to just HH:mm.
     */
    private static class DateTimeSerializer implements com.google.gson.JsonSerializer<DateTime> {
        @Override
        public JsonElement serialize(DateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString(TIME_PATTERN));
        }
    }
}
