package edu.pja.mas.warehouse.dto;

import edu.pja.mas.warehouse.entity.StorageUnit;


public record StorageUnitDTO(
        Long id,
        Long roomNumber,
        Long warehouseId,
        Long warehouseDeliveryItemStorageItemId,
        int itemQuantity
) {
    public static StorageUnitDTO from(StorageUnit storageUnit) {
        return new StorageUnitDTO(
                storageUnit.getId(),
                storageUnit.getRoomNumber(),
                storageUnit.getWarehouse().getId(),
                storageUnit.getWarehouseDeliveryItem().getId(),
                storageUnit.getItemQuantity()
        );
    }
}
