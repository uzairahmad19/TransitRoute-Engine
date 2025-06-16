package model;

import java.util.List;

public class MetroLine {
    private final String lineName;
    private final List<Station> stations;

    public MetroLine(String lineName, List<Station> stations) {
        this.lineName = lineName;
        this.stations = stations;
    }

    // Getters
    public String getLineName() { return lineName; }
    public List<Station> getStations() { return stations; }
}
