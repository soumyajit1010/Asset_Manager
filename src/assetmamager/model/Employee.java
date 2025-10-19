package assetmamager.model;

/**
 * Abstract base class representing a general Employee.
 * Demonstrates abstraction, encapsulation, and inheritance.
 */

// Employee class
public abstract class Employee {
    private String employeeId;
    private String employeeName;
    private double salary;

    // Static counters to generate unique IDs for each type
    private static int contractIdCounter = 10000;
    private static int permanentIdCounter = 10000;

    // Constructor
    public Employee(String employeeName) {
        setEmployeeName(employeeName);
    }

    // Abstract method - subclasses must define their own salary calculation logic
    public abstract void calculateSalary();


    // ---- Getters & Setters ----

    public String getEmployeeId() {
        return employeeId;
    }

    protected void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        if (isValidName(employeeName)) {
            this.employeeName = employeeName.trim();
        } else {
            throw new IllegalArgumentException("Invalid employee name: must contain at least two words, start with capital letters, and only alphabets.");
        }
    }

    public double getSalary() {
        return salary;
    }

    protected void setSalary(double salary) {
        this.salary = Math.max(salary, 0);
    }


    // ---- Utility Methods ----

    /**
     * Validates employee name: at least two words, capitalized, alphabetic only
     */
    private boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        String[] words = name.trim().split("\\s+");
        if (words.length < 2) return false;

        for (String word : words) {
            if (!Character.isUpperCase(word.charAt(0)) || !word.matches("[A-Za-z]+")) {
                return false;
            }
        }
        return true;
    }


    /**
     * Generates unique ID for permanent employee
     */
    protected static String generatePermanentId() {
        return "PERM-" + (permanentIdCounter++);
    }

    /**
     * Generates unique ID for contract employee
     */
    protected static String generateContractId() {
        return "CONT-" + (contractIdCounter++);
    }

    @Override
    public String toString() {
        return String.format("Employee ID: %s | Name: %s | Salary: %.2f",
                employeeId, employeeName, salary);
    }
}





