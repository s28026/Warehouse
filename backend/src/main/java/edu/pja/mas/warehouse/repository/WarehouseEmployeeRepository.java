package edu.pja.mas.warehouse.repository;

import edu.pja.mas.warehouse.entity.Employee;
import edu.pja.mas.warehouse.entity.Warehouse;
import edu.pja.mas.warehouse.entity.WarehouseEmployee;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface WarehouseEmployeeRepository extends CrudRepository<WarehouseEmployee, Long> {
    WarehouseEmployee findByEmployee(Employee employee);

    WarehouseEmployee findByEmployeePesel(String employeePesel);

    List<WarehouseEmployee> findByWarehouse_Id(Long warehouseId);

    List<WarehouseEmployee> findByWarehouse(Warehouse warehouse);
}
