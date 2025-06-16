package algorithms;

import model.MetroGraph;
import model.Station;
import java.util.*;

public class DijkstraFinder implements PathFinder {
    public enum OptimizationCriteria {
        DISTANCE, TIME
    }

    private final OptimizationCriteria criteria;

    public DijkstraFinder(OptimizationCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public List<Station> findPath(MetroGraph graph, Station source, Station destination) {
        Map<Station, Integer> distances = new HashMap<>();
        Map<Station, Integer> times = new HashMap<>();
        Map<Station, Station> parentMap = new HashMap<>();
        PriorityQueue<Station> pq = new PriorityQueue<>(
                Comparator.comparingInt(s -> criteria == OptimizationCriteria.DISTANCE
                        ? distances.get(s)
                        : times.get(s))
        );

        // Initialize
        for (Station station : graph.getAllStations()) {
            distances.put(station, Integer.MAX_VALUE);
            times.put(station, Integer.MAX_VALUE);
        }
        distances.put(source, 0);
        times.put(source, 0);
        pq.add(source);

        while (!pq.isEmpty()) {
            Station current = pq.poll();

            if (current.equals(destination)) {
                return reconstructPath(parentMap, destination);
            }

            for (MetroGraph.Edge edge : graph.getAdjacentStations(current)) {
                int newDist = distances.get(current) + edge.getDistance();
                int newTime = times.get(current) + edge.getTime();

                boolean shouldUpdate = false;
                if (criteria == OptimizationCriteria.DISTANCE) {
                    shouldUpdate = newDist < distances.get(edge.getDestination());
                } else {
                    shouldUpdate = newTime < times.get(edge.getDestination());
                }

                if (shouldUpdate) {
                    distances.put(edge.getDestination(), newDist);
                    times.put(edge.getDestination(), newTime);
                    parentMap.put(edge.getDestination(), current);
                    pq.add(edge.getDestination());
                }
            }
        }

        return Collections.emptyList();
    }

    private List<Station> reconstructPath(Map<Station, Station> parentMap, Station destination) {
        LinkedList<Station> path = new LinkedList<>();
        Station current = destination;

        while (current != null) {
            path.addFirst(current);
            current = parentMap.get(current);
        }

        return path;
    }
}
