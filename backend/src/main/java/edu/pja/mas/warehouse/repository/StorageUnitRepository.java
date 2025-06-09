package edu.pja.mas.warehouse.repository;

import edu.pja.mas.warehouse.entity.StorageUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface StorageUnitRepository extends JpaRepository<StorageUnit, String> {
    List<StorageUnit> findByWarehouse_IdAndWarehouseDeliveryItemStorageItem_Id(Long warehouseId, Long itemId);
}
