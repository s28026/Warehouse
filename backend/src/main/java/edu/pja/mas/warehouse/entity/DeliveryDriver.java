package edu.pja.mas.warehouse.entity;

import edu.pja.mas.warehouse.enums.DriverStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryDriver implements IDriver {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "employee_pesel")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Employee employee;

    @NotBlank
    private String driverLicense;

    @NotNull
    @FutureOrPresent
    private LocalDate driverLicenseValidUntil; // YYYYMMDD format

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private DriverStatus status = DriverStatus.UNAVAILABLE;

    @OneToMany(mappedBy = "assignedDriver", cascade = CascadeType.REMOVE)
    @Builder.Default
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<WarehouseDelivery> assignedDeliveries = new ArrayList<>();

    public void validateDriverLicense() {
        if (driverLicense == null || driverLicense.isBlank())
            throw new IllegalArgumentException("Driver license cannot be null or blank");

        String pre = "Driver license [" + driverLicense + "] ";

        if (!driverLicense.startsWith(IDriver.NORMAL_LICENSE_PREFIX))
            throw new IllegalArgumentException(pre + "must start with " + IDriver.NORMAL_LICENSE_PREFIX);
        if (driverLicense.length() != IDriver.NORMAL_LICENSE_LEN)
            throw new IllegalArgumentException(pre + "must be " + IDriver.NORMAL_LICENSE_LEN + " characters long");
        if (!driverLicenseValidUntil.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(pre + "is not valid, it must be in the future or today");
        }
    }
}
