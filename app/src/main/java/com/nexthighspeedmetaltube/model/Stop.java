package com.nexthighspeedmetaltube.model;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * A {@code Stop} is somewhere where some form of public transport can stop to pick up passengers.
 * It has a system id, a friendly name, and coordinates in WGS84 format (represented as integers).
 * <p>
 * This class is thread-safe and immutable.
 */
public final class Stop {

    private final String id, name;
    private final int latitude, longitude;

    private Stop(Builder builder) {
        id = builder.id;
        name = builder.name;
        latitude = builder.latitude;
        longitude = builder.longitude;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLatitude() {
        return latitude;
    }

    public int getLongitude() {
        return longitude;
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

        if (latitude != stop.latitude) {
            return false;
        }
        if (longitude != stop.longitude) {
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
        return Objects.hashCode(id, name, latitude, longitude);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("latitude", latitude)
                .add("longitude", longitude)
                .toString();
    }

    /**
     * This {@code Builder} is used to build a {@link com.nexthighspeedmetaltube.model.Stop}.
     */
    public static final class Builder {

        private static final int LATITUDE_MIN = -90000000;
        private static final int LATITUDE_MAX = 90000000;
        private static final int LONGITUDE_MIN = -180000000;
        private static final int LONGITUDE_MAX = 180000000;

        private static final String BUILDER_ILLEGAL_STATE_MESSAGE = "One or more of id, name, latitude, and/or longitude hasn't been set.";
        private static final String LATITUDE_NOT_WITHIN_REQUIRED_RANGE_MESSAGE = "Latitude not within required range. Look up WGS84.";
        private static final String LONGITUDE_NOT_WITHIN_REQUIRED_RANGE_MESSAGE = "Longitude not within required range. Look up WGS84.";

        private String id, name;
        private int latitude, longitude;
        private boolean hasLatitude, hasLongitude;

        public Builder setId(String id) {
            this.id = Preconditions.checkNotNull(id);
            return this;
        }

        public Builder setName(String name) {
            this.name = Preconditions.checkNotNull(name);
            return this;
        }

        public Builder setLatitude(int latitude) {
            Preconditions.checkArgument(LATITUDE_MIN <= latitude && latitude <= LATITUDE_MAX, LATITUDE_NOT_WITHIN_REQUIRED_RANGE_MESSAGE);
            this.latitude = latitude;
            hasLatitude = true;
            return this;
        }

        public Builder setLongitude(int longitude) {
            Preconditions.checkArgument(LONGITUDE_MIN <= latitude && latitude <= LONGITUDE_MAX, LONGITUDE_NOT_WITHIN_REQUIRED_RANGE_MESSAGE);
            this.longitude = longitude;
            hasLongitude = true;
            return this;
        }

        public Stop build() {
            Preconditions.checkState(id != null && name != null && hasLatitude && hasLongitude, BUILDER_ILLEGAL_STATE_MESSAGE);
            return new Stop(this);
        }
    }
}
