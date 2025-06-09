package edu.pja.mas.warehouse.repository;

import edu.pja.mas.warehouse.entity.WarehouseOperator;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface WarehouseOperatorRepository extends CrudRepository<WarehouseOperator, Long> {
    WarehouseOperator findByWarehouseEmployee_Id(Long warehouseEmployeeId);
}
