package assetmamager.main;

import assetmamager.model.*;
import assetmamager.service.AssetDatabase;
import assetmamager.service.EmployeeService;

import java.util.List;
import java.util.Scanner;

/**
 * Interactive console application for managing employees and asset assignments.
 * <p>
 * Integrates with {@link EmployeeService} (singleton) and {@link AssetDatabase} to provide
 * a full-featured HR module with:
 * <ul>
 *   <li>Contract & Permanent employee creation</li>
 *   <li>Strict business rules (only Permanent employees get assets)</li>
 *   <li>Asset ownership tracking and duplicate prevention</li>
 *   <li>Rich, colorful, user-friendly console interface</li>
 * </ul>
 * </p>
 *
 * @version 1.0
 * @since 2025
 */
public class EmployeeManagerApp {

    private static final EmployeeService empService = EmployeeService.getInstance();

    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String RED = "\u001B[31m";
    private static final String PURPLE = "\u001B[35m";
    private static final String RESET = "\u001B[0m";

    public static void main(String[] args) {
        printWelcomeBanner();
        manageEmployees(new Scanner(System.in));
    }

    public static void manageEmployees(Scanner sc) {
        boolean running = true;

        while (running) {
            printEmployeeMenu();
            System.out.print(YELLOW + "Enter your choice (1-6): " + RESET);
            String choice = sc.nextLine().trim();

            switch (choice) {
                case "1" -> addContractEmployee(sc);
                case "2" -> addPermanentEmployee(sc);
                case "3" -> viewAllEmployees();
                case "4" -> assignAssetToPermanentEmployee(sc);
                case "5" -> viewEmployeeWithAssets(sc);
                case "6" -> {
                    System.out.println(CYAN + "\nReturning to Main Menu..." + RESET);
                    running = false;
                }
                default -> System.out.println(RED + "Invalid option! Please select 1–6." + RESET);
            }
        }
    }

    private static void printWelcomeBanner() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println(PURPLE + "     HR & EMPLOYEE MANAGEMENT SYSTEM v1.0" + RESET);
        System.out.println("═".repeat(60));
    }

    private static void printEmployeeMenu() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println(CYAN + "          EMPLOYEE MANAGEMENT SYSTEM" + RESET);
        System.out.println("═".repeat(60));
        System.out.println("  1. Add Contract Employee");
        System.out.println("  2. Add Permanent Employee");
        System.out.println("  3. View All Employees");
        System.out.println("  4. Assign Asset to Permanent Employee");
        System.out.println("  5. View Employee + Assigned Assets");
        System.out.println("  6. Back to Main Menu");
        System.out.println("═".repeat(60));
    }

    private static void addContractEmployee(Scanner sc) {
        System.out.println(GREEN + "\nADD CONTRACT EMPLOYEE" + RESET);
        System.out.println("─".repeat(50));

        String name = promptValidName(sc);
        double hourlyWage = promptPositiveDouble(sc, "Enter hourly wage (e.g., 850.50)");
        float hoursWorked = promptPositiveFloat(sc, "Enter hours worked this month");

        ContractEmployee emp = new ContractEmployee(name, hourlyWage, hoursWorked);
        empService.addEmployee(emp);

        System.out.println(GREEN + "Contract employee added successfully!" + RESET);
        System.out.println(emp);
    }

    private static void addPermanentEmployee(Scanner sc) {
        System.out.println(GREEN + "\nADD PERMANENT EMPLOYEE" + RESET);
        System.out.println("─".repeat(50));

        String name = promptValidName(sc);
        double basicPay = promptPositiveDouble(sc, "Enter basic salary");
        float experience = promptPositiveFloat(sc, "Enter years of experience");

        PermanentEmployee emp = new PermanentEmployee(
                name,
                basicPay,
                new String[]{"DA-20", "HRA-15"}, // Default allowances
                experience,
                new Asset[0]
        );

        empService.addEmployee(emp);
        System.out.println(GREEN + "Permanent employee added successfully!" + RESET);
        System.out.println(emp);
    }

    private static void viewAllEmployees() {
        System.out.println(CYAN + "\nALL EMPLOYEES IN SYSTEM" + RESET);
        System.out.println("─".repeat(80));

        List<Employee> employees = empService.getAllEmployees();
        if (employees.isEmpty()) {
            System.out.println(YELLOW + "No employees registered yet." + RESET);
        } else {
            employees.forEach(e -> System.out.println("  " + e));
            System.out.println(CYAN + "\nTotal employees: " + employees.size() + RESET);
        }
    }

    private static void assignAssetToPermanentEmployee(Scanner sc) {
        System.out.println(PURPLE + "\nASSIGN ASSET TO PERMANENT EMPLOYEE" + RESET);
        System.out.println("─".repeat(60));

        System.out.print(YELLOW + "Enter Permanent Employee ID: " + RESET);
        String empId = sc.nextLine().trim().toUpperCase();

        System.out.print(YELLOW + "Enter Asset ID (e.g., LTP-123456H): " + RESET);
        String assetId = sc.nextLine().trim().toUpperCase();

        PermanentEmployee emp = empService.findPermanentEmployeeById(empId);
        Asset asset = AssetDatabase.findAssetById(assetId);

        if (emp == null) {
            System.out.println(RED + "Permanent employee not found! Use option 2 to add one first." + RESET);
            return;
        }
        if (asset == null) {
            System.out.println(RED + "Asset not found! Add it first in the Asset Manager." + RESET);
            return;
        }

        boolean success = empService.assignAssetToPermanentEmployee(empId, asset);
        if (success) {
            System.out.println(GREEN + "Asset assigned successfully!" + RESET);
            System.out.println("\nUpdated Employee:");
            System.out.println(emp);
        }
    }

    private static void viewEmployeeWithAssets(Scanner sc) {
        System.out.print(YELLOW + "\nEnter Employee ID: " + RESET);
        String id = sc.nextLine().trim().toUpperCase();

        Employee emp = empService.findEmployeeById(id);
        if (emp == null) {
            System.out.println(RED + "Employee not found!" + RESET);
            return;
        }

        System.out.println(CYAN + "\nEMPLOYEE DETAILS" + RESET);
        System.out.println("┌" + "─".repeat(78) + "┐");
        System.out.println("│ " + emp);
        System.out.println("└" + "─".repeat(78) + "┘");

        List<Asset> assets = empService.getAssignedAssets(id);
        if (assets.isEmpty()) {
            System.out.println(YELLOW + "   No assets assigned yet." + RESET);
        } else {
            System.out.println(GREEN + "   Assigned Assets (" + assets.size() + "):" + RESET);
            assets.forEach(a -> System.out.println("      • " + a));
        }
    }

    // ────────────────────── Input Validation Helpers ──────────────────────

    private static String promptValidName(Scanner sc) {
        while (true) {
            System.out.print("Enter full name (e.g., Alice Johnson): ");
            String name = sc.nextLine().trim();
            if (isValidEmployeeName(name)) {
                return name;
            }
            System.out.println(RED + "Invalid name! Must be at least two words, each starting with a capital letter." + RESET);
        }
    }

    private static boolean isValidEmployeeName(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        String[] parts = name.trim().split("\\s+");
        if (parts.length < 2) return false;
        for (String part : parts) {
            if (part.isEmpty() || !Character.isUpperCase(part.charAt(0)) || !part.matches("[A-Za-z\\-]+")) {
                return false;
            }
        }
        return true;
    }

    private static double promptPositiveDouble(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            try {
                double value = Double.parseDouble(sc.nextLine().trim());
                if (value >= 0) return value;
                System.out.println(RED + "Value must be positive!" + RESET);
            } catch (NumberFormatException e) {
                System.out.println(RED + "Please enter a valid number!" + RESET);
            }
        }
    }

    private static float promptPositiveFloat(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt + ": ");
            try {
                float value = Float.parseFloat(sc.nextLine().trim());
                if (value >= 0) return value;
                System.out.println(RED + "Value must be positive!" + RESET);
            } catch (NumberFormatException e) {
                System.out.println(RED + "Please enter a valid number!" + RESET);
            }
        }
    }
}