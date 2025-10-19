package assetmamager.model;

// ContractEmployee class
class ContractEmployee extends Employee {
    private double wagePerHour;

    public ContractEmployee(String employeeName, double wagePerHour) {
        super(employeeName);
        this.wagePerHour = wagePerHour;
        // Use getter and setter for contractIdCounter
        int newCounter = getContractIdCounter() + 1;
        setContractIdCounter(newCounter);
        setEmployeeId("C" + newCounter);
    }

    @Override
    public void calculateSalary(float hoursWorked) {
        double calculatedSalary;
        if (hoursWorked >= 190) {
            calculatedSalary = wagePerHour * hoursWorked;
        } else {
            double deduction = 0.5 * wagePerHour * (190 - hoursWorked);
            calculatedSalary = (wagePerHour * hoursWorked) - deduction;
        }
        setSalary(Math.round(calculatedSalary));
    }

    public double getWagePerHour() {
        return wagePerHour;
    }

    public void setWagePerHour(double wagePerHour) {
        this.wagePerHour = wagePerHour;
    }

    @Override
    public String toString() {
        return "Employee Id: " + getEmployeeId() + ", Employee Name: " + getEmployeeName() + ", Wage Per Hour: " + getWagePerHour();
    }
}