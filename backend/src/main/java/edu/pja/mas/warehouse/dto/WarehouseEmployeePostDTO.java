package edu.pja.mas.warehouse.dto;

public record WarehouseEmployeePostDTO(
        Long warehouseId,
        WarehouseWorkerPostDTO worker,
        WarehouseOperatorPostDTO operator
) {
}
