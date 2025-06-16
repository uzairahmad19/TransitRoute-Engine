package model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class MetroGraph {
    private final Map<Station, List<Edge>> adjacencyList;

    public MetroGraph() {
        this.adjacencyList = new HashMap<>();
    }
    public void addStation(Station station) {
        adjacencyList.putIfAbsent(station, new LinkedList<>());
    }

    public void addEdge(Station source, Station destination, int distance, int time, boolean isTransfer) {
        adjacencyList.get(source).add(new Edge(destination, distance, time, isTransfer));
        adjacencyList.get(destination).add(new Edge(source, distance, time, isTransfer));
    }

    public List<Edge> getAdjacentStations(Station station) {
        return adjacencyList.getOrDefault(station, new LinkedList<>());
    }
    public Set<Station> getAllStations() {
        return new HashSet<>(adjacencyList.keySet());
    }

    public static class Edge {
        private final Station destination;
        private final int distance;
        private final int time;
        private final boolean isTransfer;

        public Edge(Station destination, int distance, int time, boolean isTransfer) {
            this.destination = destination;
            this.distance = distance;
            this.time = time;
            this.isTransfer = isTransfer;
        }

        public Station getDestination() {
            return destination;
        }

        public int getDistance() {
            return distance;
        }

        public int getTime() {
            return time;
        }

        public boolean isTransfer() {
            return isTransfer;
        }

        @Override
        public String toString() {
            return String.format("To: %s (Distance: %dkm, Time: %dmin, Transfer: %b)",
                    destination.getStationName(), distance, time, isTransfer);
        }
    }
}