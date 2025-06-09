package edu.pja.mas.warehouse.dto;

public record DeliveryDriverShiftDTO(
        DeliveryDriverDTO driver,
        Integer numberOfDeliveriesToday,
        Double hoursWorkedToday
) {
}
