package edu.pja.mas.warehouse.dto;

import edu.pja.mas.warehouse.entity.Employee;
import edu.pja.mas.warehouse.entity.Warehouse;
import edu.pja.mas.warehouse.entity.WarehouseEmployee;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;


public record WarehouseDTO(
        Long id,
        String location,
        List<WarehouseEmployeeDTO> employees
) {
    public static WarehouseDTO from(Warehouse warehouse) {
        List<WarehouseEmployeeDTO> employees = null;

        if (Hibernate.isInitialized(warehouse.getEmployees()))
            employees = WarehouseEmployeeDTO.fromList(warehouse.getEmployees());

        return new WarehouseDTO(
                warehouse.getId(),
                warehouse.getLocation(),
                employees
        );
    }

    public static List<WarehouseDTO> from(List<Warehouse> warehouses) {
        return warehouses.stream()
                .map(WarehouseDTO::from)
                .toList();
    }
}
