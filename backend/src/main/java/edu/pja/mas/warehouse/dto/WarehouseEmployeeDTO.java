package edu.pja.mas.warehouse.dto;

import edu.pja.mas.warehouse.entity.WarehouseEmployee;

import java.time.LocalDate;
import java.util.List;


public record WarehouseEmployeeDTO(
        EmployeeDTO employee,
        Long warehouseId,
        boolean isOperator,
        boolean isWorker

) {
    public static WarehouseEmployeeDTO from(WarehouseEmployee emp) {
        return new WarehouseEmployeeDTO(
                EmployeeDTO.from(emp.getEmployee()),
                emp.getWarehouse().getId(),
                emp.isWarehouseOperator(),
                emp.isWarehouseWorker()
        );
    }

    public static List<WarehouseEmployeeDTO> fromList(List<WarehouseEmployee> emps) {
        return emps.stream()
                .map(WarehouseEmployeeDTO::from)
                .toList();
    }
}
