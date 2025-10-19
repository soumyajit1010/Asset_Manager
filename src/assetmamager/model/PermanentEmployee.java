package assetmamager.model;

import assetmamager.exception.InvalidAssetsException;
import assetmamager.exception.InvalidExperienceException;
import assetmamager.service.Resources;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a permanent employee with salary calculation,
 * bonus eligibility, and asset allocation tracking.
 */

// PermanentEmployee class
public class PermanentEmployee extends Employee {
    private double basicPay;
    private String[] salaryComponents;
    private float experience; // in years
    private Asset[] assets;

    // ---- Constructor ----
    public PermanentEmployee(String employeeName, double basicPay, String[] salaryComponents, float experience, Asset[] assets) {
        super(employeeName);
        this.basicPay = basicPay;
        this.salaryComponents = salaryComponents;
        this.experience = experience;
        this.assets = assets;

        // Generate unique permanent employee ID
        setEmployeeId(generatePermanentId());
    }

    // ---- Salary Calculation ----
    @Override
    public void calculateSalary() {
        double salary = basicPay;

        // Add DA and HRA from salary components like "DA-10" or "HRA-15"
        if (salaryComponents != null) {
            for (String component : salaryComponents) {
                if (component != null && component.contains("-")) {
                    String[] parts = component.split("-");
                    if (parts.length == 2) {
                        try {
                            double percentage = Double.parseDouble(parts[1]) / 100;
                            salary += basicPay * percentage;
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid component format: " + component);
                        }
                    }
                }
            }
        }

        // Add bonus based on experience
        try {
            salary += calculateBonus(experience);
        } catch (InvalidExperienceException e) {
            System.err.println(e.getMessage());
        }

        setSalary(Math.round(salary));
    }

    // ---- Bonus Calculation ----
    public double calculateBonus(float experience) throws InvalidExperienceException {
        if (experience < 2.5) {
            throw new InvalidExperienceException("Minimum 2.5 years of experience required for bonus eligibility.");
        } else if (experience < 4) return 2550;
        else if (experience < 8) return 5000;
        else if (experience < 12) return 8750;
        else return 13000;
    }



    // ---- Asset Filtering ----
    public Asset[] getAssetsByDate(String lastDate) throws InvalidAssetsException {
        if (assets == null || assets.length == 0) {
            throw new InvalidAssetsException("No assets assigned to this employee.");
        }

        List<Asset> validAssets = new ArrayList<>();
        for (Asset asset : assets) {
            if (asset != null && isAssetExpiredByDate(asset.getAssetExpiry(), lastDate)) {
                validAssets.add(asset);
            }
        }

        if (validAssets.isEmpty()) {
            throw new InvalidAssetsException("No assets found for the given criteria.");
        }

        return validAssets.toArray(new Asset[0]);
    }

    private boolean isAssetExpiredByDate(String assetExpiry, String lastDate) {
        if (assetExpiry == null || lastDate == null) return false;

        String[] assetParts = assetExpiry.split("-");
        String[] lastParts = lastDate.split("-");
        if (assetParts.length != 3 || lastParts.length != 3) return false;

        int assetYear = Integer.parseInt(assetParts[0]);
        int assetMonth = Resources.getMonth(assetParts[1]);
        int assetDay = Integer.parseInt(assetParts[2]);
        int lastYear = Integer.parseInt(lastParts[0]);
        int lastMonth = Resources.getMonth(lastParts[1]);
        int lastDay = Integer.parseInt(lastParts[2]);

        if (assetYear < lastYear) return true;
        if (assetYear == lastYear && assetMonth < lastMonth) return true;
        return assetYear == lastYear && assetMonth == lastMonth && assetDay <= lastDay;
    }

    // ---- Getters & Setters ----
    public double getBasicPay() {
        return basicPay;
    }

    public void setBasicPay(double basicPay) {
        this.basicPay = basicPay;
    }

    public String[] getSalaryComponents() {
        return salaryComponents;
    }

    public void setSalaryComponents(String[] salaryComponents) {
        this.salaryComponents = salaryComponents;
    }

    public float getExperience() {
        return experience;
    }

    public void setExperience(float experience) {
        this.experience = experience;
    }

    public Asset[] getAssets() {
        return assets;
    }

    public void setAssets(Asset[] assets) {
        this.assets = assets;
    }


    // ---- toString ----
    @Override
    public String toString() {
        String assetInfo = (assets != null && assets.length > 0)
                ? Arrays.stream(assets)
                .filter(a -> a != null)
                .map(Asset::getAssetId)
                .reduce((a, b) -> a + ", " + b)
                .orElse("No assets")
                : "No assets";

        return String.format("Permanent Employee [%s] | Name: %s | Basic Pay: %.2f | Experience: %.1f yrs | Salary: %.2f | Assets: %s",
                getEmployeeId(), getEmployeeName(), basicPay, experience, getSalary(), assetInfo);
    }
}
