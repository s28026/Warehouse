package edu.pja.mas.warehouse.repository;

import edu.pja.mas.warehouse.entity.StorageItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StorageItemRepository extends JpaRepository<StorageItem, Long> {
}
