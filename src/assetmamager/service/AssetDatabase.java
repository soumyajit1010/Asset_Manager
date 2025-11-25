package assetmamager.service;

import assetmamager.model.Asset;

import java.util.*;

/**
 * A simple in-memory database for managing {@link Asset} entities.
 * <p>
 * This singleton-style static utility provides fast asset storage and retrieval using a
 * combination of a {@link List} (for ordered access) and a {@link HashMap} (for O(1) lookup by ID).
 * Asset IDs are stored and compared in <strong>uppercase</strong> for case-insensitive consistency.
 * </p>
 * <p>
 * Suitable for small applications, demos, or testing. For production systems,
 * replace with a proper database (e.g., JPA/Hibernate, Spring Data, etc.).
 * </p>
 *
 */
public final class AssetDatabase {

    /** Master ordered list of all assets */
    private static final List<Asset> assets = new ArrayList<>();

    /** Fast lookup map: Asset ID (UPPERCASE) → Asset object */
    private static final Map<String, Asset> assetMap = new HashMap<>();

    /** Private constructor to prevent instantiation */
    private AssetDatabase() {
        throw new UnsupportedOperationException("Utility class - instantiation not allowed");
    }

    /**
     * Adds a new asset to the database.
     * <p>
     * The asset ID is automatically converted to uppercase for consistent case-insensitive lookup.
     * </p>
     *
     * @param asset the {@link Asset} to add; must not be {@code null}
     * @throws NullPointerException if {@code asset} or its ID is {@code null}
     * @throws IllegalArgumentException if an asset with the same ID already exists
     */
    public static void addAsset(Asset asset) {
        Objects.requireNonNull(asset, "Asset cannot be null");
        Objects.requireNonNull(asset.getAssetId(), "Asset ID cannot be null");

        String key = asset.getAssetId().trim().toUpperCase();
        if (key.isEmpty()) {
            throw new IllegalArgumentException("Asset ID cannot be empty or blank");
        }
        if (assetMap.containsKey(key)) {
            throw new IllegalArgumentException("Asset with ID '" + asset.getAssetId() + "' already exists");
        }

        assets.add(asset);
        assetMap.put(key, asset);
    }

    /**
     * Retrieves an asset by its ID (case-insensitive).
     *
     * @param id the asset ID to search for (can be any case)
     * @return the matching {@link Asset}, or {@code null} if not found
     * @throws IllegalArgumentException if {@code id} is blank
     */
    public static Asset findAssetById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return null; // Or throw exception based on preference
        }
        String key = id.trim().toUpperCase();
        return assetMap.get(key);
    }

    /**
     * Returns a defensive copy of all stored assets.
     * <p>
     * The returned list is immutable with respect to modifications affecting the internal state.
     * </p>
     *
     * @return a new {@link List} containing all assets (never {@code null})
     */
    public static List<Asset> getAllAssets() {
        return new ArrayList<>(assets);
    }

    /**
     * Deletes an asset by its ID (case-insensitive).
     *
     * @param id the asset ID to delete
     * @return {@code true} if the asset was found and removed, {@code false} otherwise
     */
    public static boolean deleteAsset(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        String key = id.trim().toUpperCase();
        Asset asset = assetMap.remove(key);
        if (asset != null) {
            assets.remove(asset);
            return true;
        }
        return false;
    }

    /**
     * Updates the name and/or expiry date of an existing asset.
     *
     * @param id        the asset ID (case-insensitive)
     * @param newName   the new asset name (can be {@code null} to keep current)
     * @param newExpiry the new expiry date string (can be {@code null} to keep current)
     * @return {@code true} if the asset was found and updated, {@code false} otherwise
     */
    public static boolean updateAsset(String id, String newName, String newExpiry) {
        Asset asset = findAssetById(id);
        if (asset == null) {
            return false;
        }
        if (newName != null && !newName.trim().isEmpty()) {
            asset.setAssetName(newName.trim());
        }
        if (newExpiry != null && !newExpiry.trim().isEmpty()) {
            asset.setAssetExpiry(newExpiry.trim());
        }
        return true;
    }

    /**
     * Returns the current number of assets in the database.
     *
     * @return size of the asset repository
     */
    public static int getAssetCount() {
        return assets.size();
    }

    /**
     * Checks if an asset exists in the database.
     *
     * @param id the asset ID to check
     * @return {@code true} if an asset with this ID exists
     */
    public static boolean containsAsset(String id) {
        return id != null && assetMap.containsKey(id.trim().toUpperCase());
    }
}