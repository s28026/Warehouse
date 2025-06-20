package edu.pja.mas.warehouse.dto;

import edu.pja.mas.warehouse.entity.WarehouseDelivery;
import edu.pja.mas.warehouse.enums.WarehouseDeliveryStatus;

import java.time.LocalDateTime;
import java.util.List;


public record WarehouseDeliveryDTO(
        Long id,
        String pickupAddress,
        LocalDateTime registeredAt,
        LocalDateTime deliveredAt,
        WarehouseDeliveryStatus status
//        DeliveryDriverDTO driver
) {
    public static WarehouseDeliveryDTO from(WarehouseDelivery delivery) {
        return new WarehouseDeliveryDTO(
                delivery.getId(),
                delivery.getPickupAddress(),
                delivery.getRegisteredAt(),
                delivery.getDeliveredAt(),
                delivery.getStatus()
        );
    }

    public static List<WarehouseDeliveryDTO> from(List<WarehouseDelivery> deliveries) {
        return deliveries.stream()
                .map(WarehouseDeliveryDTO::from)
                .toList();
    }
}
