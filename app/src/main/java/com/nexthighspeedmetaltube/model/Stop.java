package com.nexthighspeedmetaltube.model;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * A {@code Stop} is somewhere where some form of public transport can stop to pick up passengers.
 * Its component parts are a system id, a friendly name, and a {@link com.nexthighspeedmetaltube.model.Coordinate}.
 * <p>
 * This class is immutable and thread-safe.
 */
public final class Stop {

    private final String id, name;
    private final Coordinate coordinate;

    private Stop(Builder builder) {
        id = builder.id;
        name = builder.name;
        coordinate = builder.coordinate;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Stop stop = (Stop) o;

        if (!coordinate.equals(stop.coordinate)) {
            return false;
        }
        if (!id.equals(stop.id)) {
            return false;
        }
        if (!name.equals(stop.name)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, coordinate);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("coordinate", coordinate)
                .toString();
    }

    /**
     * This {@code Builder} is used to build a {@link com.nexthighspeedmetaltube.model.Stop}.
     */
    public static final class Builder {

        private static final String BUILDER_ILLEGAL_STATE_MESSAGE = "One or more of id, name, latitude, and/or longitude hasn't been set.";

        private String id, name;
        private Coordinate coordinate;

        public Builder setId(String id) {
            this.id = Preconditions.checkNotNull(id);
            return this;
        }

        public Builder setName(String name) {
            this.name = Preconditions.checkNotNull(name);
            return this;
        }

        public Builder setCoordinate(Coordinate coordinate) {
            this.coordinate = Preconditions.checkNotNull(coordinate);
            return this;
        }

        public Stop build() {
            Preconditions.checkState(id != null && name != null && coordinate != null, BUILDER_ILLEGAL_STATE_MESSAGE);
            return new Stop(this);
        }
    }
}
