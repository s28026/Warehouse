package edu.pja.mas.warehouse.dto;

public record StorageItemPostDTO(
        String name,
        String barcode,
        double weight
) {
}
