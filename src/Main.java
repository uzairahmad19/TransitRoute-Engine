import model.MetroGraph;
import model.Station;
import algorithms.*;
import utils.MetroDataLoader;
import java.util.List;
import java.util.Scanner;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            // Load metro data from CSV
            MetroGraph graph = MetroDataLoader.loadMetroGraph();
            List<Station> allStations = MetroDataLoader.getAllStations();

            Scanner scanner = new Scanner(System.in);

            System.out.println("====================================");
            System.out.println("   DELHI METRO PATH FINDER SYSTEM   ");
            System.out.println("====================================");

            while (true) {
                printMenu();
                int choice = getUserChoice(scanner);

                switch (choice) {
                    case 1:
                        listAllStations(allStations);
                        break;
                    case 2:
                        findAndDisplayPath(graph, allStations, scanner, new BFSFinder(), "minimum stops");
                        break;
                    case 3:
                        findAndDisplayPath(graph, allStations, scanner, new DijkstraFinder(DijkstraFinder.OptimizationCriteria.DISTANCE), "shortest distance");
                        break;
                    case 4:
                        findAndDisplayPath(graph, allStations, scanner, new DijkstraFinder(DijkstraFinder.OptimizationCriteria.TIME), "minimum time");
                        break;
                    case 5:
                        System.out.println("\nThank you for using Delhi Metro Path Finder!");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading metro data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void printMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. List all stations");
        System.out.println("2. Find path with minimum stops");
        System.out.println("3. Find path with shortest distance");
        System.out.println("4. Find path with minimum time");
        System.out.println("5. Exit");
        System.out.print("Enter your choice (1-5): ");
    }

    private static int getUserChoice(Scanner scanner) {
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a number!");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private static void listAllStations(List<Station> stations) {
        System.out.println("\nList of All Stations:");
        System.out.println("--------------------------------------------------");
        System.out.printf("%-5s %-25s %-15s%n", "ID", "Station Name", "Lines");
        System.out.println("--------------------------------------------------");

        for (Station station : stations) {
            System.out.printf("%-5s %-25s %-15s%n",
                    station.getStationId(),
                    station.getStationName(),
                    String.join(",", station.getLines()));
        }
    }

    private static void findAndDisplayPath(MetroGraph graph, List<Station> allStations,
                                           Scanner scanner, PathFinder finder, String pathType) {
        scanner.nextLine(); // Consume newline

        System.out.println("\nAvailable Stations:");
        listAllStations(allStations);

        System.out.print("\nEnter source station ID: ");
        String sourceId = scanner.nextLine().trim();

        System.out.print("Enter destination station ID: ");
        String destId = scanner.nextLine().trim();

        Station source = findStationById(allStations, sourceId);
        Station destination = findStationById(allStations, destId);

        if (source == null || destination == null) {
            System.out.println("Invalid station IDs entered. Please try again.");
            return;
        }

        System.out.println("\nFinding path with " + pathType + "...");
        List<Station> path = finder.findPath(graph, source, destination);

        if (path.isEmpty()) {
            System.out.println("No path found between " + source.getStationName() +
                    " and " + destination.getStationName());
        } else {
            displayPath(path, graph, pathType);
        }
    }

    private static Station findStationById(List<Station> stations, String id) {
        return stations.stream()
                .filter(s -> s.getStationId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    private static void displayPath(List<Station> path, MetroGraph graph, String pathType) {
        System.out.println("\nPath with " + pathType + ":");
        System.out.println("--------------------------------------------------");

        int totalStops = path.size() - 1;
        int totalDistance = 0;
        int totalTime = 0;

        for (int i = 0; i < path.size(); i++) {
            Station current = path.get(i);
            System.out.printf("%d. %s (%s)%n", i+1, current.getStationName(), String.join(",", current.getLines()));

            if (i < path.size() - 1) {
                Station next = path.get(i+1);
                MetroGraph.Edge edge = findEdge(graph, current, next);

                if (edge != null) {
                    totalDistance += edge.getDistance();
                    totalTime += edge.getTime();

                    System.out.printf("   → %d km, %d min%s%n",
                            edge.getDistance(),
                            edge.getTime(),
                            edge.isTransfer() ? " (Transfer)" : "");
                }
            }
        }

        System.out.println("--------------------------------------------------");
        System.out.printf("Total: %d stops, %d km, %d minutes%n", totalStops, totalDistance, totalTime);
    }

    private static MetroGraph.Edge findEdge(MetroGraph graph, Station from, Station to) {
        return graph.getAdjacentStations(from).stream()
                .filter(edge -> edge.getDestination().equals(to))
                .findFirst()
                .orElse(null);
    }
}