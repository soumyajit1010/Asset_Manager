package assetmamager.main;


import assetmamager.model.Asset;

import java.util.ArrayList;
import java.util.List;

public class Tester {
    public static void main(String[] args) {

        List<Asset> assets = new ArrayList<>();

        // Add assets using helper method
        addAsset(assets, "DSK-876761L", "Dell-Desktop", "2020-Dec-01");
        addAsset(assets, "DSK-876762L", "Acer-Desktop", "2021-Mar-31");
        addAsset(assets, "IPH-1101201h", "VoIP", "2020-Dec-31"); // invalid
        addAsset(assets, "IPH-110130h", "VoIP", "2020-Nov-30");

        // Print all assets
        System.out.println("\nDetails of all available assets:\n");
        printAssets(assets);

        // Correct invalid asset IDs
        System.out.println("\nCorrecting all invalid asset IDs:\n");
        for (Asset asset : assets) {
            if (!asset.isValidId()) {
                String correctedId = correctAssetId(asset.getAssetId());
                asset.setAssetId(correctedId);
                System.out.println("Corrected asset: " + asset);
            }
        }

        // Print assets again after correction
        System.out.println("\nAll assets after correction:\n");
        printAssets(assets);






    }

    // Helper method: add asset safely
    private static void addAsset(List<Asset> assets, String id, String name, String expiry) {
        try {
            assets.add(new Asset(id, name, expiry));
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating asset: " + e.getMessage());
        }
    }

    // Print all assets and warnings for invalid IDs
    private static void printAssets(List<Asset> assets) {
        int counter = 1;
        for (Asset asset : assets) {
            System.out.println("Details of asset " + counter++);
            System.out.println("\t" + asset);
            if (!asset.isValidId()) {
                System.out.println("⚠️ Warning: Invalid asset ID for " + asset.getAssetName());
            }
            System.out.println();
        }
    }

    // Dummy correction logic (customize as needed)
    private static String correctAssetId(String invalidId) {
        // Example: just remove extra digits if format is wrong
        if (invalidId.startsWith("IPH-") && invalidId.length() > 11) {
            return "IPH-" + invalidId.substring(4, 10) + "h";
        }
        return invalidId;
    }
}
