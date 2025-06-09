package edu.pja.mas.warehouse.entity;

import edu.pja.mas.warehouse.enums.EmployeeType;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.*;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Employee extends Person {
    public static final int MIN_SALARY = 3000;
    public static final int MAX_BREAK_LENGTH = 90; // in minutes

    @Pattern(regexp = "^[0-9]{9}$", message = "Phone number must be 9 digits")
    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @NotNull
    @Builder.Default
    private LocalDate employmentDate = LocalDate.now();

    @Nullable
    private LocalDate terminationDate;

    @Min(value = MIN_SALARY)
    private Integer salary;

    @Builder.Default
    private List<Integer> breaksTakenToday = new ArrayList<>();

    @Nullable
    private LocalDate shiftStart;

    @Nullable
    private LocalDate shiftEnd;

    @NotNull
    @ElementCollection(targetClass = EmployeeType.class)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<EmployeeType> employeeTypes = new HashSet<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @Builder.Default
    private Set<EmployeeComplaint> complaints = new HashSet<>();

    @OneToOne(mappedBy = "employee")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private WarehouseEmployee warehouseEmployee;

    @OneToOne(mappedBy = "employee")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private DeliveryDriver deliveryDriver;

    public boolean isDeliveryDriver() {
        return deliveryDriver != null && employeeTypes.contains(EmployeeType.DELIVERY_DRIVER);
    }

    public boolean isWarehouseEmployee() {
        return warehouseEmployee != null && employeeTypes.contains(EmployeeType.WAREHOUSE_EMPLOYEE);
    }

    public void setSalary(int salary) {
        if (salary < MIN_SALARY)
            throw new IllegalArgumentException("Salary must be at least " + MIN_SALARY);
        if (salary < this.salary)
            throw new IllegalArgumentException("Salary cannot be decreased");
        this.salary = salary;
    }

    public boolean isEmployed() {
        return terminationDate == null || terminationDate.isAfter(LocalDate.now());
    }

    public boolean canTakeBreak(int length) {
        if (length <= 0 || length > MAX_BREAK_LENGTH)
            return false;
        int totalBreaksToday = breaksTakenToday.stream().mapToInt(Integer::intValue).sum();
        return totalBreaksToday + length <= MAX_BREAK_LENGTH;
    }

    public boolean isOnShift() {
        return shiftStart != null && shiftEnd == null;
    }
}
