package assetmamager.main;

import assetmamager.model.Asset;
import assetmamager.service.AssetDatabase;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Main console application for the Asset Manager system.
 * <p>
 * Provides an interactive menu-driven interface to perform CRUD operations on assets
 * using the in-memory {@link AssetDatabase}.
 * </p>
 * <p>
 * Features:
 * <ul>
 *   <li>Full validation for Asset ID (DSK/LTP/IPH-######H/L)</li>
 *   <li>Strict YYYY-MMM-DD expiry date format</li>
 *   <li>User-friendly prompts and error messages</li>
 *   <li>Case-insensitive ID handling</li>
 * </ul>
 * </p>
 *
 */
public class AssetManagerApp {

    private static final Scanner sc = new Scanner(System.in);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MMM-dd");

    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) {
        printBanner();
        manageAssets();
        System.out.println(ANSI_CYAN + "\nThank you for using Asset Manager. Goodbye!" + ANSI_RESET);

    }

    private static void printBanner() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println(ANSI_CYAN + "     ASSET MANAGER v1.0 - ENTERPRISE EDITION" + ANSI_RESET);
        System.out.println("=".repeat(50));
    }

    private static void manageAssets() {
        boolean running = true;

        while (running) {
            printMenu();
            System.out.print(ANSI_YELLOW + "Enter your choice (1-6): " + ANSI_RESET);
            String input = sc.nextLine().trim();

            if (input.isEmpty()) {
                System.out.println(ANSI_RED + "Please enter a number!" + ANSI_RESET);
                continue;
            }

            switch (input) {
                case "1" -> addAssetInteractive();
                case "2" -> viewAllAssets();
                case "3" -> searchAssetById();
                case "4" -> updateAssetInteractive();
                case "5" -> deleteAssetInteractive();
                case "6" -> running = false;
                default -> System.out.println(ANSI_RED + "Invalid choice! Please select 1-6." + ANSI_RESET);
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n" + "═".repeat(50));
        System.out.println(ANSI_CYAN + "           ASSET MANAGEMENT SYSTEM" + ANSI_RESET);
        System.out.println("═".repeat(50));
        System.out.println("  1. Add New Asset");
        System.out.println("  2. View All Assets");
        System.out.println("  3. Search Asset by ID");
        System.out.println("  4. Update Asset");
        System.out.println("  5. Delete Asset");
        System.out.println("  6. Exit");
        System.out.println("═".repeat(50));
    }

    private static void addAssetInteractive() {
        System.out.println("\n" + ANSI_GREEN + "ADD NEW ASSET" + ANSI_RESET);
        System.out.println("─".repeat(40));

        System.out.println(ANSI_YELLOW + """
            Asset ID Format: DSK/LTP/IPH-######H/L
              Examples:
                • DSK-123456H  (High-value Desktop)
                • LTP-000001L  (Low-value Laptop)
                • IPH-987654H  (High-value iPhone)

            Expiry Date Format: YYYY-MMM-DD
              Examples:
                • 2025-Dec-31
                • 2026-Apr-01
                • 2024-Oct-15
            """ + ANSI_RESET);

        String id = readValidAssetId();
        if (id == null) return;

        System.out.print("Enter Asset Name (e.g., MacBook Pro M3): ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) name = "Unknown Asset";

        String expiry = readValidExpiryDate();
        if (expiry == null) return;

        try {
            Asset asset = new Asset(id, name, expiry);
            AssetDatabase.addAsset(asset);
            System.out.println(ANSI_GREEN + "Asset added successfully!" + ANSI_RESET);
            System.out.println("   → " + asset);
        } catch (IllegalArgumentException e) {
            System.out.println(ANSI_RED + "Failed: " + e.getMessage() + ANSI_RESET);
        }
    }

    private static String readValidAssetId() {
        while (true) {
            System.out.print(ANSI_YELLOW + "Enter Asset ID: " + ANSI_RESET);
            String id = sc.nextLine().trim().toUpperCase();

            if (!id.matches("^(DSK|LTP|IPH)-\\d{6}[HL]$")) {
                System.out.println(ANSI_RED + "Invalid format! Must be like: LTP-123456H" + ANSI_RESET);
                continue;
            }
            if (AssetDatabase.findAssetById(id) != null) {
                System.out.println(ANSI_RED + "This Asset ID already exists!" + ANSI_RESET);
                continue;
            }
            return id;
        }
    }

    private static String readValidExpiryDate() {
        while (true) {
            System.out.print(ANSI_YELLOW + "Enter Expiry Date (YYYY-MMM-DD): " + ANSI_RESET);
            String expiry = sc.nextLine().trim();
            try {
                DATE_FORMATTER.parse(expiry);
                return expiry;
            } catch (DateTimeParseException e) {
                System.out.println(ANSI_RED + "Invalid date! Use format: 2025-Dec-31" + ANSI_RESET);
            }
        }
    }

    private static void viewAllAssets() {
        System.out.println("\n" + ANSI_CYAN + "ALL REGISTERED ASSETS" + ANSI_RESET);
        System.out.println("─".repeat(80));

        List<Asset> assets = AssetDatabase.getAllAssets();
        if (assets.isEmpty()) {
            System.out.println(ANSI_YELLOW + "No assets found in the system." + ANSI_RESET);
        } else {
            assets.forEach(asset -> System.out.println("  • " + asset));
            System.out.println("\n" + ANSI_CYAN + "Total assets: " + assets.size() + ANSI_RESET);
        }
    }

    private static void searchAssetById() {
        System.out.print(ANSI_YELLOW + "\nEnter Asset ID to search: " + ANSI_RESET);
        String id = sc.nextLine().trim().toUpperCase();
        Asset asset = AssetDatabase.findAssetById(id);

        System.out.println("\n" + (asset != null
                ? ANSI_GREEN + "FOUND: " + asset + ANSI_RESET
                : ANSI_RED + "Asset not found!" + ANSI_RESET));
    }

    private static void updateAssetInteractive() {
        System.out.print(ANSI_YELLOW + "\n_embedEnter Asset ID to update: " + ANSI_RESET);
        String id = sc.nextLine().trim().toUpperCase();

        Asset asset = AssetDatabase.findAssetById(id);
        if (asset == null) {
            System.out.println(ANSI_RED + "Asset not found!" + ANSI_RESET);
            return;
        }

        System.out.println(ANSI_CYAN + "\nCurrent: " + asset + ANSI_RESET);
        System.out.println("Leave blank to keep current value.\n");

        System.out.print("New name [" + asset.getAssetName() + "]: ");
        String newName = sc.nextLine().trim();
        if (newName.isEmpty()) newName = asset.getAssetName();

        System.out.print("New expiry [" + asset.getAssetExpiry() + "]: ");
        String newExpiry = sc.nextLine().trim();
        if (newExpiry.isEmpty()) newExpiry = asset.getAssetExpiry();
        else if (!isValidDate(newExpiry)) {
            System.out.println(ANSI_RED + "Invalid date format! Update cancelled." + ANSI_RESET);
            return;
        }

        if (AssetDatabase.updateAsset(id, newName, newExpiry)) {
            System.out.println(ANSI_GREEN + "Asset updated successfully!" + ANSI_RESET);
        }
    }

    private static void deleteAssetInteractive() {
        System.out.print(ANSI_YELLOW + "\nEnter Asset ID to delete: " + ANSI_RESET);
        String id = sc.nextLine().trim().toUpperCase();

        if (AssetDatabase.deleteAsset(id)) {
            System.out.println(ANSI_GREEN + "Asset deleted successfully!" + ANSI_RESET);
        } else {
            System.out.println(ANSI_RED + "Asset not found!" + ANSI_RESET);
        }
    }

    private static boolean isValidDate(String date) {
        try {
            DATE_FORMATTER.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}