package dk.naestebus.model;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.joda.time.DateTime;
import org.joda.time.ReadableDateTime;

/**
 * A {@code Departure} is defined by a name, a time, and an optional direction.
 * <p>
 * This class is immutable and thread-safe.
 */
public final class Departure {

    private final String name, direction;
    private final boolean hasDirection;
    private ReadableDateTime time;

    private Departure(Builder builder) {
        name = builder.name;
        time = builder.time;
        if (builder.direction != null) {
            direction = builder.direction;
            hasDirection = true;
        } else {
            direction = "";
            hasDirection = false;
        }
    }

    public String getName() {
        return name;
    }

    public DateTime getTime() {
        return time.toDateTime();
    }

    public boolean hasDirection() {
        return hasDirection;
    }

    public String getDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Departure departure = (Departure) o;

        if (hasDirection != departure.hasDirection) {
            return false;
        }
        if (!direction.equals(departure.direction)) {
            return false;
        }
        if (!name.equals(departure.name)) {
            return false;
        }
        if (!time.equals(departure.time)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name, time, hasDirection, direction);
    }

    @Override
    public String toString() {
        Objects.ToStringHelper helper = Objects.toStringHelper(this)
                .add("name", name)
                .add("time", time)
                .add("hasDirection", hasDirection);

        if (hasDirection) {
            helper.add("direction", direction);
        }

        return helper.toString();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    /**
     * This {@code Builder} is used to build a {@link dk.naestebus.model.Departure}.
     */
    public static final class Builder {

        private static final String BUILDER_ILLEGAL_STATE_MESSAGE = "One or both of name and/or time hasn't been set.";

        private String name, direction;
        private ReadableDateTime time;

        public Builder setName(String name) {
            this.name = Preconditions.checkNotNull(name);
            return this;
        }

        public Builder setTime(ReadableDateTime time) {
            this.time = Preconditions.checkNotNull(time);
            return this;
        }

        public Builder setDirection(String direction) {
            this.direction = Preconditions.checkNotNull(direction);
            return this;
        }

        public Departure build() {
            Preconditions.checkState(name != null && time != null, BUILDER_ILLEGAL_STATE_MESSAGE);
            return new Departure(this);
        }
    }
}
