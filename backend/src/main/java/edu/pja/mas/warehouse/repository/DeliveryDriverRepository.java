package edu.pja.mas.warehouse.repository;

import edu.pja.mas.warehouse.entity.DeliveryDriver;
import edu.pja.mas.warehouse.entity.Employee;
import edu.pja.mas.warehouse.entity.WarehouseDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface DeliveryDriverRepository extends JpaRepository<DeliveryDriver, Long> {
    DeliveryDriver findByEmployee(Employee employee);

    @Query("""
            FROM DeliveryDriver d
            LEFT JOIN Employee e ON d.employee.pesel = e.pesel
            WHERE e.terminationDate IS NULL
            AND d.status = 'AVAILABLE'
            """)
    List<DeliveryDriver> findAllAvailable();

    @Query("""

""")
    DeliveryDriver findAvailableById(Long deliveryId);

    @Query("""
            FROM WarehouseDelivery wd
            WHERE wd.assignedDriver = :driver
                AND wd.status = 'COMPLETED'
                AND wd.deliveredAt IS NOT NULL
                AND wd.deliveredAt < CURRENT_TIMESTAMP
            """)
    List<WarehouseDelivery> findAllTodayDeliveriesByDriver(DeliveryDriver driver);

    @Query("""
            FROM DeliveryDriver d
            WHERE d.status = 'AVAILABLE'
                AND NOT EXISTS(
                    SELECT 1 FROM DeclinedDelivery dd
                    WHERE dd.driver = d
                        AND dd.delivery.id = :deliveryId
                )
            """)
    List<DeliveryDriver> findAllAvailableForWarehouseDelivery(Long deliveryId);

    Optional<DeliveryDriver> findByEmployeePesel(String employeePesel);
}
