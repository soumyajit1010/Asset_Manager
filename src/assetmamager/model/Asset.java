package assetmamager.model;

public class Asset {
    private String assetId;
    private String assetName;
    private String assetExpiry;

    public Asset(String assetId, String assetName, String assetExpiry) {
        setAssetId(assetId); // Validate assetId
        this.assetName = assetName;
        this.assetExpiry = assetExpiry;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        if (assetId != null && assetId.matches("^(DSK|LTP|IPH)-\\d{6}[HhLl]$")) {
            this.assetId = assetId;
        } else {
            throw new IllegalArgumentException("Invalid assetId: " + assetId);
        }
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public String getAssetExpiry() {
        return assetExpiry;
    }

    public void setAssetExpiry(String assetExpiry) {
        this.assetExpiry = assetExpiry;
    }

    @Override
    public String toString() {
        return "Asset Id: " + getAssetId() + ", Asset Name: " + getAssetName() + ", Asset Expiry: " + getAssetExpiry();
    }

    public boolean isValidId() {
        return assetId != null && assetId.matches("^(DSK|LTP|IPH)-\\d{6}[HhLl]$");
    }


}
