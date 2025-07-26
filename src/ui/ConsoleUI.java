package ui;

import algorithms.BFSFinder;
import algorithms.DijkstraFinder;
import algorithms.PathFinder;
import model.MetroGraph;
import model.Station;
import utils.MetroDataLoader;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class ConsoleUI {
    private final MetroGraph graph;
    private final List<Station> allStations;
    private final Scanner scanner;
    private final Random random;

    public ConsoleUI() throws IOException {
        this.graph = MetroDataLoader.loadMetroGraph();
        this.allStations = MetroDataLoader.getAllStations();
        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }

    public void start() {
        System.out.println("====================================");
        System.out.println("   DELHI METRO PATH FINDER SYSTEM   ");
        System.out.println("====================================");

        while (true) {
            printMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    listAllStations();
                    break;
                case 2:
                    findAndDisplayPath(new BFSFinder(), "minimum stops");
                    break;
                case 3:
                    findAndDisplayPath(new DijkstraFinder(DijkstraFinder.OptimizationCriteria.DISTANCE), "shortest distance");
                    break;
                case 4:
                    findAndDisplayPath(new DijkstraFinder(DijkstraFinder.OptimizationCriteria.TIME), "minimum time");
                    break;
                case 5:
                    simulateRandomDelay();
                    break;
                case 6:
                    System.out.println("\nThank you for using Delhi Metro Path Finder!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\nMain Menu:");
        System.out.println("1. List all stations");
        System.out.println("2. Find path with minimum stops");
        System.out.println("3. Find path with shortest distance");
        System.out.println("4. Find path with minimum time");
        System.out.println("5. Simulate Random Delay");
        System.out.println("6. Exit");
        System.out.print("Enter your choice (1-6): ");
    }

    private int getUserChoice() {
        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a number!");
            scanner.next();
        }
        return scanner.nextInt();
    }

    private void listAllStations() {
        System.out.println("\nList of All Stations:");
        System.out.println("--------------------------------------------------");
        System.out.printf("%-5s %-25s %-15s%n", "ID", "Station Name", "Lines");
        System.out.println("--------------------------------------------------");

        for (Station station : allStations) {
            System.out.printf("%-5s %-25s %-15s%n",
                    station.getStationId(),
                    station.getStationName(),
                    String.join(",", station.getLines()));
        }
    }

    private void findAndDisplayPath(PathFinder finder, String pathType) {
        scanner.nextLine(); // Consume newline

        System.out.println("\nAvailable Stations:");
        listAllStations();

        System.out.print("\nEnter source station ID: ");
        String sourceId = scanner.nextLine().trim();

        System.out.print("Enter destination station ID: ");
        String destId = scanner.nextLine().trim();

        Station source = findStationById(sourceId);
        Station destination = findStationById(destId);

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
            displayPath(path, pathType);
        }
    }

    private Station findStationById(String id) {
        return allStations.stream()
                .filter(s -> s.getStationId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    private void simulateRandomDelay() {
        List<MetroGraph.Edge> allEdges = graph.getAllEdges();
        if (allEdges.isEmpty()) {
            System.out.println("No connections available to simulate a delay.");
            return;
        }

        MetroGraph.Edge randomEdge = allEdges.get(random.nextInt(allEdges.size()));
        int delay = 5 + random.nextInt(11); // Delay between 5 and 15 minutes
        randomEdge.setDelay(delay);

        // Also set delay on the reverse edge for consistency
        graph.getAdjacentStations(randomEdge.getDestination()).stream()
                .filter(e -> e.getDestination().equals(randomEdge.getSource()))
                .findFirst()
                .ifPresent(reverseEdge -> reverseEdge.setDelay(delay));

        System.out.printf("\n*** A %d-minute delay has been reported on the connection between %s and %s. ***%n",
                delay, randomEdge.getSource().getStationName(), randomEdge.getDestination().getStationName());
    }


    private void displayPath(List<Station> path, String pathType) {
        System.out.println("\nPath with " + pathType + ":");
        System.out.println("--------------------------------------------------");

        int totalStops = path.size() - 1;
        int totalDistance = 0;
        int totalTime = 0;
        int totalFare = 0;

        for (int i = 0; i < path.size(); i++) {
            Station current = path.get(i);
            System.out.printf("%d. %s (%s)%n", i + 1, current.getStationName(), String.join(",", current.getLines()));

            if (i < path.size() - 1) {
                Station next = path.get(i + 1);
                MetroGraph.Edge edge = findEdge(current, next);

                if (edge != null) {
                    totalDistance += edge.getDistance();
                    totalTime += edge.getTime();
                    totalFare += edge.getFare();
                    String delayInfo = edge.getDelay() > 0 ? " (" + edge.getDelay() + " min delay)" : "";

                    System.out.printf("   → %d km, %d min%s, ₹%d%s%n",
                            edge.getDistance(),
                            edge.getTime(),
                            delayInfo,
                            edge.getFare(),
                            edge.isTransfer() ? " (Transfer)" : "");
                }
            }
        }

        System.out.println("--------------------------------------------------");
        System.out.printf("Total: %d stops, %d km, %d minutes, ₹%d%n", totalStops, totalDistance, totalTime, totalFare);
    }

    private MetroGraph.Edge findEdge(Station from, Station to) {
        return graph.getAdjacentStations(from).stream()
                .filter(edge -> edge.getDestination().equals(to))
                .findFirst()
                .orElse(null);
    }
}