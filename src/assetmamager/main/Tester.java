package assetmamager.main;

import assetmamager.model.Asset;

import java.util.ArrayList;
import java.util.List;

/**
 * Tester class to demonstrate Asset creation, validation, and correction.
 */
public class Tester {

    public static void main(String[] args) {

        List<Asset> assets = new ArrayList<>();


        // Add assets safely
        addAsset(assets, "DSK-876761L", "Dell-Desktop", "2020-Dec-01");
        addAsset(assets, "DSK-876762L", "Acer-Desktop", "2021-Mar-31");
        addAsset(assets, "IPH-1101201h", "VoIP", "2020-Dec-31"); // Invalid
        addAsset(assets, "IPH-110130h", "VoIP", "2020-Nov-30");

        System.out.println("\n--- All Assets ---\n");
        printAssets(assets);

        // Correct invalid asset IDs
        System.out.println("\n--- Correcting Invalid Asset IDs ---\n");
        for (Asset asset : assets) {
            if (!asset.isValidId()) {
                try {
                    String correctedId = correctAssetId(asset.getAssetId());
                    asset.setAssetId(correctedId);
                    System.out.println("✅ Corrected asset: " + asset);
                } catch (IllegalArgumentException e) {
                    System.out.println("⚠️ Could not correct asset ID for: " + asset.getAssetName());
                }
            }
        }

        System.out.println("\n--- Assets After Correction ---\n");
        printAssets(assets);

        // Optional: Check for expired assets
        System.out.println("\n--- Checking Expired Assets (Before Today) ---\n");
        String today = "2025-Oct-20"; // Example current date
        for (Asset asset : assets) {
            if (asset.isExpired(today)) {
                System.out.println("⚠️ Expired Asset: " + asset);
            }
        }
    }

    // ---------------- Helper Methods ----------------

    /**
     * Adds an asset safely, prints error if invalid
     */
    private static void addAsset(List<Asset> assets, String id, String name, String expiry) {
        try {
            assets.add(new Asset(id, name, expiry));
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Error creating asset: " + e.getMessage());
        }
    }

    /**
     * Prints a list of assets with index and warning for invalid IDs
     */
    private static void printAssets(List<Asset> assets) {
        int counter = 1;
        for (Asset asset : assets) {
            System.out.println("Asset " + counter++ + ": " + asset);
            if (!asset.isValidId()) {
                System.out.println("⚠️ Warning: Invalid asset ID for " + asset.getAssetName());
            }
        }
    }

    /**
     * Dummy correction logic for invalid asset IDs.
     * Can be improved to auto-correct based on company rules.
     */
    private static String correctAssetId(String invalidId) {
        if (invalidId == null || invalidId.length() < 11) return invalidId;

        // Example correction: IPH assets
        if (invalidId.startsWith("IPH-")) {
            return "IPH-" + invalidId.substring(4, 10) + "h";
        }

        // Example correction: DSK/LTP assets
        if (invalidId.startsWith("DSK-") || invalidId.startsWith("LTP-")) {
            return invalidId.substring(0, 10) + "L";
        }

        return invalidId; // fallback
    }
}
