package edu.pja.mas.warehouse.dto;

import edu.pja.mas.warehouse.entity.WarehouseDelivery;
import edu.pja.mas.warehouse.enums.WarehouseDeliveryStatus;

import java.time.LocalDateTime;


public record WarehouseDeliveryDTO(
        Long id,
        String pickupAddress,
        LocalDateTime registeredAt,
        LocalDateTime deliveredAt,
        LocalDateTime pickupAt,
        WarehouseDeliveryStatus status
//        DeliveryDriverDTO driver
) {
    public static WarehouseDeliveryDTO from(WarehouseDelivery delivery) {
        return new WarehouseDeliveryDTO(
                delivery.getId(),
                delivery.getPickupAddress(),
                delivery.getRegisteredAt(),
                delivery.getDeliveredAt(),
                delivery.getPickupAt(),
                delivery.getStatus()
        );
    }
}
