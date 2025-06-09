package edu.pja.mas.warehouse.dto;

import edu.pja.mas.warehouse.enums.WarehouseEmployeeType;


public record WarehouseEmployeePostDTO(
        Long warehouseId,
        WarehouseWorkerPostDTO worker,
        WarehouseOperatorPostDTO operator
) {
    public static void validate(WarehouseEmployeePostDTO dto) {
        if (dto == null) {
            throw new IllegalArgumentException("WarehouseEmployeePostDTO cannot be null");
        }
        if (dto.worker() == null && dto.operator() == null) {
            throw new IllegalArgumentException("Must assign either worker or operator role to the employee");
        }
        if (dto.worker() != null && dto.operator() != null) {
            throw new IllegalArgumentException("Cannot assign both worker and operator roles to the same employee");
        }
    }
}
