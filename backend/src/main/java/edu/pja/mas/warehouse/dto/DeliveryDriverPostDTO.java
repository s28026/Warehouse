package edu.pja.mas.warehouse.dto;

import java.time.LocalDate;


public record DeliveryDriverPostDTO(
        String driverLicense,
        LocalDate driverLicenseValidUntil
) {
}
