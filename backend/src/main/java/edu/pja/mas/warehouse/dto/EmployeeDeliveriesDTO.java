package edu.pja.mas.warehouse.dto;

import edu.pja.mas.warehouse.entity.DeliveryDriver;
import edu.pja.mas.warehouse.entity.WarehouseDelivery;
import edu.pja.mas.warehouse.entity.WarehouseDeliveryItem;
import edu.pja.mas.warehouse.enums.WarehouseDeliveryStatus;

import java.util.ArrayList;
import java.util.List;


public record EmployeeDeliveriesDTO(
        WarehouseDeliveryDTO current,
        List<WarehouseDeliveryDTO> previousDeliveries
) {
    public static EmployeeDeliveriesDTO from(DeliveryDriver driver) {

        List<WarehouseDelivery> deliveries = driver.getAssignedDeliveries();

        WarehouseDelivery currentDelivery = null;
        List<WarehouseDelivery> previousDeliveries = new ArrayList<>();
        for (WarehouseDelivery delivery : deliveries) {
            if (delivery.getStatus() == WarehouseDeliveryStatus.IN_TRANSIT)
                currentDelivery = delivery;
            else
                previousDeliveries.add(delivery);
        }

        return new EmployeeDeliveriesDTO(
                currentDelivery != null ? WarehouseDeliveryDTO.from(currentDelivery) : null,
                WarehouseDeliveryDTO.from(previousDeliveries)
        );
    }
}
