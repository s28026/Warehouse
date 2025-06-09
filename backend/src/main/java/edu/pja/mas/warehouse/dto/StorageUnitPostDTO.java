package edu.pja.mas.warehouse.dto;

public record StorageUnitPostDTO(
        Integer roomNumber,
        String qrCode,
        String nfcTag
) {
}
