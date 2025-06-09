package edu.pja.mas.warehouse.service;

import edu.pja.mas.warehouse.dto.StorageUnitPostDTO;
import edu.pja.mas.warehouse.entity.StorageUnit;
import edu.pja.mas.warehouse.entity.WarehouseDelivery;
import edu.pja.mas.warehouse.entity.WarehouseDeliveryItem;
import edu.pja.mas.warehouse.repository.StorageUnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class StorageUnitService {
    private final StorageUnitRepository storageUnitRepository;
    private final WarehouseDeliveryService warehouseDeliveryService;

    public int getWarehouseItemAvailability(Long warehouseId, Long itemId) {
        List<StorageUnit> storageUnits = storageUnitRepository.findByWarehouse_IdAndWarehouseDeliveryItemStorageItem_Id(warehouseId, itemId);

        return storageUnits.stream()
                .mapToInt(StorageUnit::getItemQuantity)
                .sum();
    }

    public StorageUnit save(StorageUnitPostDTO storageUnit, Long deliveryItemId) {
        WarehouseDeliveryItem item = warehouseDeliveryService.findItemById(deliveryItemId);

        StorageUnit s = StorageUnit.builder()
                .roomNumber(storageUnit.roomNumber())
                .warehouseDeliveryItem(item)
                .warehouse(item.getDelivery().getWarehouse())
                .build();

        return storageUnitRepository.save(s);
    }
}
