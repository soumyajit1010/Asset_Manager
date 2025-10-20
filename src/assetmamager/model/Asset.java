package assetmamager.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Represents an Asset assigned to employees.
 * Each asset has a unique ID, name, and expiry date.
 */
public class Asset {

    private String assetId;
    private String assetName;
    private String assetExpiry; // Format: YYYY-MMM-DD, e.g., 2025-Oct-20

    private static final String ASSET_ID_REGEX = "^(DSK|LTP|IPH)-\\d{6}[HhLl]$";

    /**
     * Constructor to create an Asset with validation.
     *
     * @param assetId     Unique asset ID
     * @param assetName   Name of the asset
     * @param assetExpiry Expiry date in format YYYY-MMM-DD
     */
    public Asset(String assetId, String assetName, String assetExpiry) {
        setAssetId(assetId);
        setAssetName(assetName);
        setAssetExpiry(assetExpiry);
    }

    // ----- Getters & Setters -----

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        if (assetId != null && assetId.matches(ASSET_ID_REGEX)) {
            this.assetId = assetId;
        } else {
            throw new IllegalArgumentException("Invalid assetId: " + assetId +
                    " (Format: DSK|LTP|IPH-######H/L)");
        }
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName != null ? assetName.trim() : null;
    }

    public String getAssetExpiry() {
        return assetExpiry;
    }

    public void setAssetExpiry(String assetExpiry) {
        if (isValidDate(assetExpiry)) {
            this.assetExpiry = assetExpiry;
        } else {
            throw new IllegalArgumentException("Invalid asset expiry date: " + assetExpiry +
                    " (Format: YYYY-MMM-DD)");
        }
    }

    // ----- Utility Methods -----

    /**
     * Check if the asset ID is valid.
     */
    public boolean isValidId() {
        return assetId != null && assetId.matches(ASSET_ID_REGEX);
    }

    /**
     * Checks if the asset is expired compared to the given date.
     *
     * @param currentDate in format YYYY-MMM-DD
     * @return true if assetExpiry <= currentDate
     */
    public boolean isExpired(String currentDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
            LocalDate expiry = LocalDate.parse(assetExpiry, formatter);
            LocalDate current = LocalDate.parse(currentDate, formatter);
            return !expiry.isAfter(current);
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    /**
     * Validates date string format.
     */
    private boolean isValidDate(String date) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
            LocalDate.parse(date, formatter);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    // ----- toString -----

    @Override
    public String toString() {
        return String.format("Asset [ID=%s, Name=%s, Expiry=%s]",
                assetId, assetName, assetExpiry);
    }
}
