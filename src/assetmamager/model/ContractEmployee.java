package assetmamager.model;

/**
 * Represents a contract-based employee who is paid hourly.
 * Extends the abstract Employee class.
 */
public class ContractEmployee extends Employee {

    private double wagePerHour;
    private float hoursWorked; // store hours worked for salary calculation

    /**
     * Constructor initializes contract employee with name and hourly wage.
     * Automatically generates a unique contract employee ID.
     */
    public ContractEmployee(String employeeName, double wagePerHour, float hoursWorked) {
        super(employeeName);
        this.wagePerHour = wagePerHour > 0 ? wagePerHour : 0;
        this.hoursWorked = hoursWorked >= 0 ? hoursWorked : 0;

        // Generate unique ID using parent class method
        setEmployeeId(generateContractId());
    }

    /**
     * Calculates monthly salary based on hours worked.
     * If hoursWorked >= 190 → full salary.
     * If hoursWorked < 190 → deduction applied.
     */
    @Override
    public void calculateSalary() {
        double calculatedSalary;

        if (hoursWorked >= 190) {
            calculatedSalary = wagePerHour * hoursWorked;
        } else {
            double deduction = 0.5 * wagePerHour * (190 - hoursWorked);
            calculatedSalary = (wagePerHour * hoursWorked) - deduction;
        }

        setSalary(Math.round(calculatedSalary));
    }

    // ----- Getters & Setters -----

    public double getWagePerHour() {
        return wagePerHour;
    }

    public void setWagePerHour(double wagePerHour) {
        if (wagePerHour > 0) {
            this.wagePerHour = wagePerHour;
        } else {
            System.err.println("Invalid wage per hour. Must be greater than 0.");
        }
    }

    public float getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(float hoursWorked) {
        if (hoursWorked >= 0) {
            this.hoursWorked = hoursWorked;
        } else {
            System.err.println("Invalid hours worked. Must be non-negative.");
        }
    }

    // ----- toString -----

    @Override
    public String toString() {
        return String.format(
                "ContractEmployee [ID=%s, Name=%s, Wage/hour=%.2f, Hours=%.1f, Salary=%.2f]",
                getEmployeeId(), getEmployeeName(), wagePerHour, hoursWorked, getSalary()
        );
    }
}
