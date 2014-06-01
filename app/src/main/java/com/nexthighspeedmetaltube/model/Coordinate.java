package com.nexthighspeedmetaltube.model;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;

/**
 * A {@code Coordinate} holds a latitude and longitude, and makes sure these have legal values.
 * <p>
 * Latitude and longitude are represented as integers, which give the correct coordinate in WGS84 when divided by a million (1000000).
 * <p>
 * This class is immutable and thread-safe.
 */
public final class Coordinate {

    private static final int LATITUDE_MIN = -90000000;
    private static final int LATITUDE_MAX = 90000000;
    private static final int LONGITUDE_MIN = -180000000;
    private static final int LONGITUDE_MAX = 180000000;

    private static final String LATITUDE_NOT_WITHIN_REQUIRED_RANGE_MESSAGE = "Latitude not within required range. Look up WGS84.";
    private static final String LONGITUDE_NOT_WITHIN_REQUIRED_RANGE_MESSAGE = "Longitude not within required range. Look up WGS84.";

    private final int latitude;
    private final int longitude;

    public Coordinate(int latitude, int longitude) {
        Preconditions.checkArgument(LATITUDE_MIN <= latitude && latitude <= LATITUDE_MAX, LATITUDE_NOT_WITHIN_REQUIRED_RANGE_MESSAGE);
        Preconditions.checkArgument(LONGITUDE_MIN <= longitude && longitude <= LONGITUDE_MAX, LONGITUDE_NOT_WITHIN_REQUIRED_RANGE_MESSAGE);
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Get the latitude as an integer. Convert to WGS84 by dividing by a million (1000000).
     *
     * @return latitude as integer
     */
    public int getLatitude() {
        return latitude;
    }

    /**
     * Get the longitude as an integer. Convert to WGS84 by dividing by a million (1000000).
     *
     * @return longitude as integer
     */
    public int getLongitude() {
        return longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Coordinate that = (Coordinate) o;

        if (latitude != that.latitude) {
            return false;
        }
        if (longitude != that.longitude) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(latitude, longitude);
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("latitude", latitude)
                .add("longitude", longitude)
                .toString();
    }
}
