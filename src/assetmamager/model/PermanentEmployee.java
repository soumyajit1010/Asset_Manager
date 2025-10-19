package assetmamager.model;

import assetmamager.exception.InvalidAssetsException;
import assetmamager.exception.InvalidExperienceException;
import assetmamager.service.Resources;

// PermanentEmployee class
class PermanentEmployee extends Employee {
    private double basicPay;
    private String[] salaryComponents;
    private float experience;
    private Asset[] assets;

    public PermanentEmployee(String employeeName, double basicPay, String[] salaryComponents, Asset[] assets) {
        super(employeeName);
        this.basicPay = basicPay;
        this.salaryComponents = salaryComponents;
        this.assets = assets;
        // Use getter and setter for permanentIdCounter
        int newCounter = getPermanentIdCounter() + 1;
        setPermanentIdCounter(newCounter);
        setEmployeeId("E" + newCounter);
    }

    public double calculateBonus(float experience) throws InvalidExperienceException {
        if (experience < 2.5) {
            throw new InvalidExperienceException("A minimum of 2.5 years is required for bonus!");
        }
        if (experience >= 2.5 && experience < 4) return 2550;
        else if (experience >= 4 && experience < 8) return 5000;
        else if (experience >= 8 && experience < 12) return 8750;
        else return 13000;
    }

    @Override
    public void calculateSalary(float salaryFactor){
        this.experience = experience;
        double salary = basicPay;
        // Calculate DA and HRA from salaryComponents
        for (String component : salaryComponents) {
            if (component != null && component.contains("-")) {
                String[] parts = component.split("-");
                if (parts.length == 2) {
                    double percentage = Double.parseDouble(parts[1]) / 100;
                    salary += basicPay * percentage;
                }
            }
        }
        // Add bonus
        try {
            salary += calculateBonus(experience);
        } catch (InvalidExperienceException e) {
            // Bonus set to 0 if exception occurs
        }
        setSalary(Math.round(salary));
    }

    public Asset[] getAssetsByDate(String lastDate) throws InvalidAssetsException {
        if (assets == null || assets.length == 0) {
            throw new InvalidAssetsException("No assets found for the given criteria!");
        }
        Asset[] result = new Asset[assets.length];
        int index = 0;
        for (Asset asset : assets) {
            if (asset != null && isAssetExpiredByDate(asset.getAssetExpiry(), lastDate)) {
                result[index++] = asset;
            }
        }
        if (index == 0) {
            throw new InvalidAssetsException("No assets found for the given criteria!");
        }
        return result;
    }

    private boolean isAssetExpiredByDate(String assetExpiry, String lastDate) {
        if (assetExpiry == null || lastDate == null) return false;
        String[] assetParts = assetExpiry.split("-");
        String[] lastParts = lastDate.split("-");
        if (assetParts.length != 3 || lastParts.length != 3) return false;

        int assetYear = Integer.parseInt(assetParts[0]);
        int lastYear = Integer.parseInt(lastParts[0]);
        int assetMonth = Resources.getMonth(assetParts[1]);
        int lastMonth = Resources.getMonth(lastParts[1]);
        int assetDay = Integer.parseInt(assetParts[2]);
        int lastDay = Integer.parseInt(lastParts[2]);

        if (assetYear < lastYear) return true;
        if (assetYear == lastYear && assetMonth < lastMonth) return true;
        if (assetYear == lastYear && assetMonth == lastMonth && assetDay <= lastDay) return true;
        return false;
    }

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

    @Override
    public String toString() {
        StringBuilder assetIds = new StringBuilder();
        if (assets != null) {
            for (Asset asset : assets) {
                if (asset != null) {
                    assetIds.append(asset.getAssetId()).append(" ");
                }
            }
        } else {
            assetIds.append("No assets allocated!");
        }
        return "Employee Id: " + getEmployeeId() + ", Employee Name: " + getEmployeeName() +
                ", Basic Pay: " + getBasicPay() + ", Salary Components: " +
                java.util.Arrays.toString(getSalaryComponents()) + ", Assets: " + assetIds.toString().trim();
    }
}
