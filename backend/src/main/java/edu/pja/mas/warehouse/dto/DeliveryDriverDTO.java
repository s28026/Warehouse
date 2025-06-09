package edu.pja.mas.warehouse.dto;

import edu.pja.mas.warehouse.entity.DeliveryDriver;
import edu.pja.mas.warehouse.enums.DriverStatus;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;


public record DeliveryDriverDTO(
        Long id,
        EmployeeDTO employee,
        String driverLicense,
        LocalDate driverLicenseValidUntil,
        DriverStatus status
) {
    public static DeliveryDriverDTO from(DeliveryDriver driver) {
        return new DeliveryDriverDTO(
                driver.getId(),
                EmployeeDTO.from(driver.getEmployee()),
                driver.getDriverLicense(),
                driver.getDriverLicenseValidUntil(),
                driver.getStatus()
        );
    }

    public static List<DeliveryDriverDTO> from(List<DeliveryDriver> drivers) {
        return drivers.stream()
                .map(DeliveryDriverDTO::from)
                .toList();
    }
}
