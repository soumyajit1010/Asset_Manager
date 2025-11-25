package assetmamager.service;

import assetmamager.model.Asset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Service class responsible for managing {@link Asset} entities.
 * Provides CRUD-like operations for in-memory asset storage.
 * <p>
 * This implementation is thread-safe only if a single instance is used in a single-threaded environment
 * or properly synchronized externally. For production use in multi-threaded environments,
 * consider wrapping the list with {@link java.util.Collections#synchronizedList(List)} or using
 * a {@link java.util.concurrent.CopyOnWriteArrayList}.
 * </p>
 *
 * @author Soumyajit Rout
 * @version 1.0
 * @since 2025
 */
public class AssetService {

    /** In-memory storage for assets. Package-private for potential testing access. */
    private final List<Asset> assets = new ArrayList<>();

    /**
     * Adds a new asset to the repository.
     *
     * @param asset the {@link Asset} object to be added; must not be {@code null}
     * @throws NullPointerException if {@code asset} is {@code null}
     */
    public void addAsset(Asset asset) {
        Objects.requireNonNull(asset, "Asset must not be null");
        assets.add(asset);
    }

    /**
     * Retrieves an unmodifiable view of all stored assets.
     * <p>
     * Returns a new {@link ArrayList} copy to prevent external modification of the internal list.
     * </p>
     *
     * @return a {@link List} containing all assets (never {@code null})
     */
    public List<Asset> getAllAssets() {
        return new ArrayList<>(assets); // Defensive copy
    }

    /**
     * Finds an asset by its unique identifier (case-insensitive).
     *
     * @param id the asset ID to search for; must not be {@code null}
     * @return the matching {@link Asset} or {@code null} if not found
     * @throws NullPointerException if {@code id} is {@code null}
     */
    public Asset findAssetById(String id) {
        Objects.requireNonNull(id, "Asset ID must not be null");
        return assets.stream()
                .filter(a -> a.getAssetId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    /**
     * Updates the name and expiry date of an existing asset identified by its ID.
     *
     * @param id        the asset ID to update (case-insensitive)
     * @param newName   the new asset name (can be {@code null} if no change is desired)
     * @param newExpiry the new expiry date string (can be {@code null} if no change is desired)
     * @return {@code true} if the asset was found and updated, {@code false} otherwise
     */
    public boolean updateAsset(String id, String newName, String newExpiry) {
        Asset asset = findAssetById(id);
        if (asset != null) {
            // Only update fields when a non-null value is provided
            if (newName != null) {
                asset.setAssetName(newName);
            }
            if (newExpiry != null) {
                asset.setAssetExpiry(newExpiry);
            }
            return true;
        }
        return false;
    }

    /**
     * Deletes an asset by its unique identifier (case-insensitive).
     *
     * @param id the asset ID to delete
     * @return {@code true} if an asset was removed, {@code false} if no asset with the given ID existed
     * @throws NullPointerException if {@code id} is {@code null}
     */
    public boolean deleteAsset(String id) {
        Objects.requireNonNull(id, "Asset ID must not be null");
        return assets.removeIf(a -> a.getAssetId().equalsIgnoreCase(id));
    }
}