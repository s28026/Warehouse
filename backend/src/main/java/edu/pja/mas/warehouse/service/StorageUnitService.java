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

    public StorageUnit save(StorageUnitPostDTO dto, Long deliveryItemId) {
        WarehouseDeliveryItem item = warehouseDeliveryService.findItemById(deliveryItemId);

        if (dto.qrCode() == null && dto.nfcTag() == null)
            throw new IllegalArgumentException("Either QR code or NFC tag must be provided.");
        if (dto.qrCode() != null && dto.nfcTag() != null)
            throw new IllegalArgumentException("Only one of QR code or NFC tag can be provided.");

        StorageUnit s = StorageUnit.builder()
                .roomNumber(dto.roomNumber())
                .warehouseDeliveryItem(item)
                .warehouse(item.getDelivery().getWarehouse())
                .nfcTag(dto.nfcTag())
                .qrCode(dto.qrCode())
                .build();

        return storageUnitRepository.save(s);
    }

    public StorageUnit findByQrCode(String qrCode) {
        return storageUnitRepository.findByQrCode(qrCode)
                .orElseThrow(() -> new IllegalArgumentException("Storage unit with QR code not found: " + qrCode));
    }

    public StorageUnit findByNfcTag(String nfcTag) {
        return storageUnitRepository.findByNfcTag(nfcTag)
                .orElseThrow(() -> new IllegalArgumentException("Storage unit with NFC tag not found: " + nfcTag));
    }
}
