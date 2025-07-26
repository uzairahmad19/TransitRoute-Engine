import ui.ConsoleUI;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            ConsoleUI ui = new ConsoleUI();
            ui.start();
        } catch (IOException e) {
            System.err.println("Error loading metro data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}