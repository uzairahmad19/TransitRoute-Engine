package model;

import java.util.Set;
import java.util.HashSet;

public class Station {
    private final String stationId;
    private final String stationName;
    private final Set<String> lines;
    private final double latitude;
    private final double longitude;

    public Station(String stationId, String stationName, Set<String> lines,
                   double latitude, double longitude) {
        this.stationId = stationId;
        this.stationName = stationName;
        this.lines = lines;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters
    public String getStationId() { return stationId; }
    public String getStationName() { return stationName; }
    public Set<String> getLines() { return new HashSet<>(lines); }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Station)) return false;
        Station other = (Station) obj;
        return stationId.equals(other.stationId);
    }

    @Override
    public int hashCode() {
        return stationId.hashCode();
    }

    @Override
    public String toString() {
        return stationName + " (" + String.join(",", lines) + ")";
    }
}