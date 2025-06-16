package algorithms;

import model.MetroGraph;
import model.Station;
import java.util.List;

public interface PathFinder {
    List<Station> findPath(MetroGraph graph, Station source, Station destination);
}
