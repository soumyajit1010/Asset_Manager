package assetmamager.model;



// Employee class
abstract class Employee {
    private String employeeId;
    private String employeeName;
    private double salary;
    private static int contractIdCounter;
    private static int permanentIdCounter;

    static {
        contractIdCounter = 10000;
        permanentIdCounter = 10000;
    }

    public Employee(String employeeName) {
        setEmployeeName(employeeName);
    }

    // Abstract method
    public abstract void calculateSalary(float salaryFactor);

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        if (employeeName != null && isValidName(employeeName)) {
            this.employeeName = employeeName;
        } else {
            this.employeeName = null; // Invalid name set to null
        }
    }

    private boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) return false;
        String[] words = name.trim().split("\\s+");
        if (words.length < 2) return false;
        for (String word : words) {
            if (word.length() < 2 || !Character.isUpperCase(word.charAt(0)) || !word.matches("[A-Za-z]+")) {
                return false;
            }
        }
        return true;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary > 0 ? salary : 0;
    }

    public static int getContractIdCounter() {
        return contractIdCounter;
    }

    public static void setContractIdCounter(int contractIdCounter) {
        Employee.contractIdCounter = contractIdCounter;
    }

    public static int getPermanentIdCounter() {
        return permanentIdCounter;
    }

    public static void setPermanentIdCounter(int permanentIdCounter) {
        Employee.permanentIdCounter = permanentIdCounter;
    }

    @Override
    public String toString() {
        return "Employee Id: " + getEmployeeId() + ", Employee Name: " + getEmployeeName();
    }
}





