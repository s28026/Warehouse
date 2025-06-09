package edu.pja.mas.warehouse.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseOperator implements IDriver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "warehouse_employee_id", unique = true)
    private WarehouseEmployee warehouseEmployee;

    @NotBlank
    private String driverLicense;

    @NotBlank
    private String vehicleType;

    @NotNull
    @FutureOrPresent
    private LocalDate driverLicenseValidUntil;

    public void validateDriverLicense() {
        if (driverLicense == null || driverLicense.isBlank())
            throw new IllegalArgumentException("Driver license cannot be null or blank");

        String pre = "Driver license [" + driverLicense + "] ";

        if (driverLicense.length() != IDriver.FORKLIFT_LICENSE_LEN)
            throw new IllegalArgumentException(pre + "must be " + IDriver.FORKLIFT_LICENSE_LEN + " characters long");
        if (!driverLicense.startsWith(IDriver.FORKLIFT_LICENSE_PREFIX))
            throw new IllegalArgumentException(pre + "must start with " + IDriver.FORKLIFT_LICENSE_PREFIX);
        if (!driverLicenseValidUntil.isAfter(LocalDate.now()))
            throw new IllegalArgumentException(pre + "is not valid, it must be in the future or today");
    }
}
