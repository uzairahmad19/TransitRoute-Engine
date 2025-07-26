package model;

import java.util.*;
import java.util.stream.Collectors;

public class MetroGraph {
    private final Map<Station, List<Edge>> adjacencyList;

    public MetroGraph() {
        this.adjacencyList = new HashMap<>();
    }

    public void addStation(Station station) {
        adjacencyList.putIfAbsent(station, new LinkedList<>());
    }

    public void addEdge(Station source, Station destination, int distance, int time, boolean isTransfer, int fare) {
        Edge forwardEdge = new Edge(destination, distance, time, isTransfer, fare);
        // To avoid duplicating edges when getting all edges, we can add a reference to the source
        forwardEdge.setSource(source);
        adjacencyList.computeIfAbsent(source, k -> new LinkedList<>()).add(forwardEdge);

        Edge backwardEdge = new Edge(source, distance, time, isTransfer, fare);
        backwardEdge.setSource(destination);
        adjacencyList.computeIfAbsent(destination, k -> new LinkedList<>()).add(backwardEdge);
    }

    public List<Edge> getAdjacentStations(Station station) {
        return adjacencyList.getOrDefault(station, Collections.emptyList());
    }

    public Set<Station> getAllStations() {
        return new HashSet<>(adjacencyList.keySet());
    }

    public List<Edge> getAllEdges() {
        return adjacencyList.values().stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public static class Edge {
        private Station source;
        private final Station destination;
        private final int distance;
        private final int time;
        private final boolean isTransfer;
        private final int fare;
        private int delay;

        public Edge(Station destination, int distance, int time, boolean isTransfer, int fare) {
            this.destination = destination;
            this.distance = distance;
            this.time = time;
            this.isTransfer = isTransfer;
            this.fare = fare;
            this.delay = 0;
        }

        public Station getSource() { return source; }
        public Station getDestination() { return destination; }
        public int getDistance() { return distance; }
        public int getTime() { return time + delay; }
        public boolean isTransfer() { return isTransfer; }
        public int getFare() { return fare; }
        public int getDelay() { return delay; }

        public void setSource(Station source) { this.source = source; }
        public void setDelay(int delay) { this.delay = delay; }

        @Override
        public String toString() {
            return String.format("To: %s (Distance: %dkm, Time: %dmin, Fare: ₹%d, Transfer: %b)",
                    destination.getStationName(), distance, time, fare, isTransfer);
        }
    }
}