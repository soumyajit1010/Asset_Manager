package assetmamager.main;

import java.util.Scanner;

public class Main {

    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String PURPLE = "\u001B[35m";
    private static final String RED = "\u001B[31m";
    private static final String BOLD = "\u001B[1m";
    private static final String RESET = "\u001B[0m";

    private static final Scanner SC = new Scanner(System.in);

    public static void main(String[] args) {
        printWelcomeBanner();

        while (true) {
            printMainMenu();

            System.out.print(YELLOW + BOLD + "Enter your choice (1-3): " + RESET);
            String choice = SC.nextLine().trim();

            switch (choice) {
                case "1" -> {
                    System.out.println(GREEN + "\nLaunching Asset Manager..." + RESET);
                    AssetManagerApp.main(new String[]{});
                }
                case "2" -> {
                    System.out.println(PURPLE + "\nLaunching Employee Management System..." + RESET);
                    EmployeeManagerApp.main(new String[]{});
                }
                case "3" -> {
                    printGoodbyeMessage();
                    return; // Clean exit
                }
                default -> System.out.println(RED + "Invalid choice! Please enter 1, 2, or 3." + RESET);
            }

            System.out.println(CYAN + "─".repeat(60) + RESET);
        }
    }

    private static void printWelcomeBanner() {
        System.out.println("\n╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("║                                                                    ║");
        System.out.println("║" + center("HR & ASSET MANAGEMENT SYSTEM v1.0", 68) + "║");
        System.out.println("║                                                                    ║");
        System.out.println("╚════════════════════════════════════════════════════════════════════╝");
        System.out.println(CYAN + center("Powered by Java • Built with love in 2025", 52) + RESET + "\n");
    }

    private static void printMainMenu() {
        System.out.println("╔════════════════════════════════════════════════════════════╗");
        System.out.println("║" + center("MAIN MENU", 60) + "║");
        System.out.println("╠════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                            ║");
        System.out.println("║" + center("1. Asset Manager", 60) + "║");
        System.out.println("║" + center("2. Employee Management System", 60) + "║");
        System.out.println("║" + center("3. Exit", 60) + "║");
        System.out.println("║                                                            ║");
        System.out.println("╚════════════════════════════════════════════════════════════╝");
    }

    private static void printGoodbyeMessage() {
        System.out.println("\n" + GREEN + "Thank you for using the HR & Asset Management System!" + RESET);
        System.out.println(YELLOW + center("Session ended successfully.", 50) + RESET);
        System.out.println(CYAN + "Goodbye! Have a wonderful day! " + RESET + "\n");
    }

    private static String center(String text, int width) {
        if (text == null) text = "";
        int padding = width - text.length();
        if (padding <= 0) return text;
        int left = padding / 2;
        int right = padding - left;
        return " ".repeat(left) + BOLD + text + RESET + " ".repeat(right);
    }
}