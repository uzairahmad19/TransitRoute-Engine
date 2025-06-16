package algorithms;

import model.MetroGraph;
import model.Station;
import java.util.*;


public class BFSFinder implements PathFinder {
    @Override
    public List<Station> findPath(MetroGraph graph, Station source, Station destination) {
        // Implementation of BFS
        Map<Station, Station> parentMap = new HashMap<>();
        Queue<Station> queue = new LinkedList<>();
        Set<Station> visited = new HashSet<>();

        queue.add(source);
        visited.add(source);
        parentMap.put(source, null);

        while (!queue.isEmpty()) {
            Station current = queue.poll();

            if (current.equals(destination)) {
                return reconstructPath(parentMap, destination);
            }

            for (MetroGraph.Edge edge : graph.getAdjacentStations(current)) {
                Station neighbor = edge.getDestination();
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    parentMap.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        return Collections.emptyList(); // No path found
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