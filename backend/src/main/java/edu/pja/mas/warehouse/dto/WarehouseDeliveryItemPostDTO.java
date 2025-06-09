package edu.pja.mas.warehouse.dto;

public record WarehouseDeliveryItemPostDTO(
        Long deliveryId,
        Long itemId,
        int quantity
) {
}
