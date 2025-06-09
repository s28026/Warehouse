package edu.pja.mas.warehouse.dto;

import java.time.LocalDate;


public record WarehouseOperatorPostDTO(
        String driverLicense,
        LocalDate driverLicenseValidUntil,
        String vehicleType
) {
}
