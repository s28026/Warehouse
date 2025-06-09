package edu.pja.mas.warehouse.repository;

import edu.pja.mas.warehouse.entity.WarehouseDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface WarehouseDeliveryRepository extends JpaRepository<WarehouseDelivery, Long> {
    @Query("""
            FROM WarehouseDelivery wd
            WHERE wd.status IN ('AWAITING_PICKUP_ADDRESS', 'INVALID_ADDRESS')
            AND wd.registeredAt < :threshold
            """)
    List<WarehouseDelivery> findByDeliveriesMarkedForDestruction(@Param("threshold") LocalDate threshold);
}
