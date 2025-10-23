package assetmamager.main;

import assetmamager.model.Asset;
import assetmamager.service.AssetService;

import java.util.List;
import java.util.Scanner;

public class Tester {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AssetService service = new AssetService();
        boolean running = true;

        while (running) {
            System.out.println("\n=== ASSET MANAGER MENU ===");
            System.out.println("1. Add Asset");
            System.out.println("2. View All Assets");
            System.out.println("3. Search Asset by ID");
            System.out.println("4. Update Asset");
            System.out.println("5. Delete Asset");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Asset ID: ");
                    String id = sc.nextLine();
                    System.out.print("Enter Asset Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Asset Expiry (YYYY-MMM-DD): ");
                    String expiry = sc.nextLine();

                    try {
                        service.addAsset(new Asset(id, name, expiry));
                        System.out.println("✅ Asset added successfully!");
                    } catch (IllegalArgumentException e) {
                        System.out.println("❌ Error: " + e.getMessage());
                    }
                    break;

                case 2:
                    List<Asset> all = service.getAllAssets();
                    if (all.isEmpty()) {
                        System.out.println("⚠️ No assets found!");
                    } else {
                        System.out.println("\n--- All Assets ---");
                        all.forEach(System.out::println);
                    }
                    break;

                case 3:
                    System.out.print("Enter Asset ID to search: ");
                    String searchId = sc.nextLine();
                    Asset found = service.findAssetById(searchId);
                    System.out.println(found != null ? found : "⚠️ Asset not found!");
                    break;

                case 4:
                    System.out.print("Enter Asset ID to update: ");
                    String updateId = sc.nextLine();
                    System.out.print("Enter new name: ");
                    String newName = sc.nextLine();
                    System.out.print("Enter new expiry (YYYY-MMM-DD): ");
                    String newExpiry = sc.nextLine();

                    if (service.updateAsset(updateId, newName, newExpiry))
                        System.out.println("✅ Asset updated successfully!");
                    else
                        System.out.println("⚠️ Asset not found!");
                    break;

                case 5:
                    System.out.print("Enter Asset ID to delete: ");
                    String deleteId = sc.nextLine();
                    if (service.deleteAsset(deleteId))
                        System.out.println("🗑️ Asset deleted successfully!");
                    else
                        System.out.println("⚠️ Asset not found!");
                    break;

                case 6:
                    running = false;
                    System.out.println("👋 Exiting Asset Manager...");
                    break;

                default:
                    System.out.println("❌ Invalid choice, please try again!");
            }
        }

        sc.close();
    }
}
