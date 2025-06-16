package utils;

import model.MetroGraph;
import model.Station;
import java.io.IOException;
import java.util.*;

public class MetroDataLoader {
    private static final String STATIONS_CSV = "data/stations.csv";
    private static final String CONNECTIONS_CSV = "data/connections.csv";

    public static MetroGraph loadMetroGraph() throws IOException {
        MetroGraph graph = new MetroGraph();
        Map<String, Station> stations = loadStations();

        // Add all stations to graph
        stations.values().forEach(graph::addStation);

        // Load connections
        List<String[]> connections = CSVReader.readCSV(CONNECTIONS_CSV);
        // In loadMetroGraph method
        for (String[] connection : connections) {
            String fromId = connection[0];
            String toId = connection[1];
            int distance = Integer.parseInt(connection[2]);
            int time = Integer.parseInt(connection[3]);
            boolean isTransfer = connection.length > 4 && Boolean.parseBoolean(connection[4]);

            Station fromStation = stations.get(fromId);
            Station toStation = stations.get(toId);

            if (fromStation != null && toStation != null) {
                graph.addEdge(fromStation, toStation, distance, time, isTransfer);
            }
        }

        return graph;
    }

    private static Map<String, Station> loadStations() throws IOException {
        Map<String, Station> stations = new HashMap<>();
        List<String[]> stationRecords = CSVReader.readCSV(STATIONS_CSV);

        for (String[] record : stationRecords) {
            String stationId = record[0];
            String stationName = record[1];
            Set<String> lines = new HashSet<>(Arrays.asList(record[2].split("\\|")));
            double latitude = Double.parseDouble(record[3]);
            double longitude = Double.parseDouble(record[4]);

            stations.put(stationId,
                    new Station(stationId, stationName, lines, latitude, longitude));
        }

        return stations;
    }

    public static List<Station> getAllStations() throws IOException {
        return new ArrayList<>(loadStations().values());
    }
}