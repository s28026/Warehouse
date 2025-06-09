package edu.pja.mas.warehouse.repository;

import edu.pja.mas.warehouse.entity.WarehouseDeliveryItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface WarehouseDeliveryItemRepository extends JpaRepository<WarehouseDeliveryItem, Long> {
}
