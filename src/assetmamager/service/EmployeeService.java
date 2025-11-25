package assetmamager.service;

import assetmamager.model.Asset;
import assetmamager.model.Employee;
import assetmamager.model.ContractEmployee;
import assetmamager.model.PermanentEmployee;

import java.util.*;

/**
 * Singleton service responsible for managing employees and asset assignments.
 * <p>
 * Supports polymorphic storage of {@link Employee} subclasses ({@link PermanentEmployee},
 * {@link ContractEmployee}) while enforcing business rules such as:
 * <ul>
 *   <li>Only Permanent Employees can be assigned assets</li>
 *   <li>Each Asset can be assigned to only one employee at a time</li>
 * </ul>
 * </p>
 * <p>
 * This is an in-memory implementation suitable for prototyping, testing, or small-scale applications.
 * For production use, consider persisting data and adding proper synchronization or
 * using {@link java.util.concurrent.ConcurrentHashMap}.
 * </p>
 *
 * @author Soumyajit Rout
 * @version 1.0
 * @since 2025
 */
public class EmployeeService {

    /** Single instance of the service (Singleton pattern) */
    private static final EmployeeService instance = new EmployeeService();

    /** Master list of all employees (preserves insertion order) */
    private final List<Employee> employees = new ArrayList<>();

    /** Fast lookup map by employee ID */
    private final Map<String, Employee> employeeMap = new HashMap<>();

    /**
     * Mapping of employeeId → List of assigned assets.
     * Acts as the source of truth for asset ownership.
     */
    private final Map<String, List<Asset>> employeeAssets = new HashMap<>();

    /** Private constructor to enforce singleton pattern */
    private EmployeeService() {
        // Prevent instantiation from outside
    }

    /**
     * Returns the singleton instance of {@link EmployeeService}.
     *
     * @return the single {@link EmployeeService} instance
     */
    public static EmployeeService getInstance() {
        return instance;
    }

    /**
     * Adds a new employee (Permanent or Contract) to the system.
     * <p>
     * Automatically calculates salary and initializes an empty asset list for the employee.
     * </p>
     *
     * @param emp the employee to add; must not be {@code null}
     * @throws NullPointerException if {@code emp} is {@code null}
     * @throws IllegalArgumentException if an employee with the same ID already exists
     */
    public void addEmployee(Employee emp) {
        Objects.requireNonNull(emp, "Employee cannot be null");

        String id = emp.getEmployeeId();
        if (employeeMap.containsKey(id)) {
            throw new IllegalArgumentException("Employee with ID '" + id + "' already exists");
        }

        employees.add(emp);
        employeeMap.put(id, emp);
        employeeAssets.put(id, new ArrayList<>());

        // Trigger salary calculation (e.g., based on type-specific logic)
        emp.calculateSalary();
    }

    /**
     * Returns a defensive copy of all registered employees.
     *
     * @return unmodifiable view-safe list of employees
     */
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    /**
     * Finds an employee by their ID (fast O(1) lookup).
     *
     * @param id the employee ID (case-sensitive)
     * @return the {@link Employee} or {@code null} if not found
     */
    public Employee findEmployeeById(String id) {
        return employeeMap.get(Objects.requireNonNull(id, "Employee ID cannot be null"));
    }

    /**
     * Finds a contract employee by ID.
     *
     * @param id the employee ID
     * @return the {@link ContractEmployee} or {@code null} if not found or wrong type
     */
    public ContractEmployee findContractEmployeeById(String id) {
        Employee emp = employeeMap.get(Objects.requireNonNull(id));
        return (emp instanceof ContractEmployee) ? (ContractEmployee) emp : null;
    }

    /**
     * Finds a permanent employee by ID.
     *
     * @param id the employee ID
     * @return the {@link PermanentEmployee} or {@code null} if not found or wrong type
     */
    public PermanentEmployee findPermanentEmployeeById(String id) {
        Employee emp = employeeMap.get(Objects.requireNonNull(id));
        return (emp instanceof PermanentEmployee) ? (PermanentEmployee) emp : null;
    }

    /**
     * Assigns an asset exclusively to a Permanent Employee.
     * <p>
     * Enforces two critical business rules:
     * <ol>
     *   <li>Only Permanent Employees can receive assets</li>
     *   <li>An asset cannot be assigned to multiple employees</li>
     * </ol>
     * </p>
     *
     * @param empId the ID of the permanent employee
     * @param asset the asset to assign
     * @return {@code true} if assignment succeeded, {@code false} otherwise
     */
    public boolean assignAssetToPermanentEmployee(String empId, Asset asset) {
        Objects.requireNonNull(empId, "Employee ID cannot be null");
        Objects.requireNonNull(asset, "Asset cannot be null");

        PermanentEmployee emp = findPermanentEmployeeById(empId);
        if (emp == null) {
            System.out.println("Assignment failed: Only Permanent Employees can be assigned assets!");
            return false;
        }

        if (isAssetAlreadyAssigned(asset)) {
            System.out.println("Assignment failed: Asset '" + asset.getAssetId() + "' is already assigned!");
            return false;
        }

        // 1. Update central asset tracking map
        employeeAssets.get(empId).add(asset);

        // 2. Update the employee's internal assets array (for persistence/serialization)
        Asset[] current = emp.getAssets();
        Asset[] updated = Arrays.copyOf(current, current.length + 1);
        updated[current.length] = asset;
        emp.setAssets(updated);

        System.out.println("Success: Asset '" + asset.getAssetId() + "' assigned to " +
                emp.getEmployeeName() + " (" + emp.getEmployeeId() + ")");
        return true;
    }

    /**
     * Legacy/simple asset assignment (no type or uniqueness checks).
     * Use {@link #assignAssetToPermanentEmployee(String, Asset)} for strict enforcement.
     *
     * @param empId employee ID
     * @param asset asset to assign
     */
    public void assignAsset(String empId, Asset asset) {
        Objects.requireNonNull(empId);
        Objects.requireNonNull(asset);
        employeeAssets.computeIfAbsent(empId, k -> new ArrayList<>()).add(asset);
    }

    /**
     * Returns a defensive copy of assets assigned to an employee.
     *
     * @param empId the employee ID
     * @return list of assigned assets (never {@code null})
     */
    public List<Asset> getAssignedAssets(String empId) {
        Objects.requireNonNull(empId);
        return new ArrayList<>(employeeAssets.getOrDefault(empId, Collections.emptyList()));
    }

    /**
     * Finds the current owner of a given asset.
     *
     * @param asset the asset to check
     * @return formatted string with owner name and ID, or {@code null} if unassigned
     */
    public String getAssetOwner(Asset asset) {
        Objects.requireNonNull(asset);
        for (Map.Entry<String, List<Asset>> entry : employeeAssets.entrySet()) {
            if (entry.getValue().contains(asset)) {
                Employee emp = employeeMap.get(entry.getKey());
                if (emp != null) {
                    return emp.getEmployeeName() + " (" + emp.getEmployeeId() + ")";
                }
            }
        }
        return null;
    }

    /**
     * Checks if an asset is already assigned to any employee.
     *
     * @param asset the asset to verify
     * @return {@code true} if already assigned
     */
    public boolean isAssetAlreadyAssigned(Asset asset) {
        Objects.requireNonNull(asset);
        return employeeAssets.values().stream()
                .flatMap(List::stream)
                .anyMatch(a -> a.getAssetId().equals(asset.getAssetId()));
    }

    /**
     * Returns a list of all permanent employees.
     *
     * @return list of {@link PermanentEmployee} instances
     */
    public List<PermanentEmployee> getAllPermanentEmployees() {
        return employees.stream()
                .filter(e -> e instanceof PermanentEmployee)
                .map(e -> (PermanentEmployee) e)
                .toList(); // Java 16+ immutable list
    }
}