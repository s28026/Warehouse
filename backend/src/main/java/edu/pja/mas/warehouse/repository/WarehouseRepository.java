package edu.pja.mas.warehouse.repository;

import edu.pja.mas.warehouse.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;


public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    // get employees by warehouse id
}
