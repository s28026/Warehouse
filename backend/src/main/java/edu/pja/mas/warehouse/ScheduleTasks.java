package edu.pja.mas.warehouse;

import edu.pja.mas.warehouse.repository.WarehouseRepository;
import edu.pja.mas.warehouse.service.WarehouseDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ScheduleTasks {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseDeliveryService warehouseDeliveryService;

    @Scheduled(cron = "0 0 0 * * *") // runs every day at midnight
    public void runDaily() {
        warehouseDeliveryService.destroyExpiredDeliveries();
    }
}
