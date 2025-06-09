package edu.pja.mas.warehouse.repository;

import edu.pja.mas.warehouse.entity.WarehouseDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface WarehouseDeliveryRepository extends JpaRepository<WarehouseDelivery, Long> {
    @Modifying
    @Query("""
            DELETE FROM WarehouseDelivery wd
            WHERE wd.status IN ('AWAITING_PICKUP_ADDRESS', 'INVALID_ADDRESS')
            AND wd.registeredAt < :threshold
            """)
    void bulkDeleteDeliveriesMarkedForDestruction(@Param("threshold") LocalDate threshold);
}
