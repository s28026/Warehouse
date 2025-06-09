package edu.pja.mas.warehouse.controller;

import edu.pja.mas.warehouse.dto.*;
import edu.pja.mas.warehouse.entity.StorageUnit;
import edu.pja.mas.warehouse.entity.WarehouseDelivery;
import edu.pja.mas.warehouse.service.DeliveryDriverService;
import edu.pja.mas.warehouse.service.StorageUnitService;
import edu.pja.mas.warehouse.service.WarehouseDeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/deliveries")
@RequiredArgsConstructor
public class DeliveryController {
    private final DeliveryDriverService deliveryDriverService;
    private final WarehouseDeliveryService warehouseDeliveryService;
    private final StorageUnitService storageUnitService;

    @GetMapping("/{deliveryId}")
    public ResponseEntity<?> getDeliveryById(@PathVariable Long deliveryId) {
        WarehouseDelivery delivery = warehouseDeliveryService.findById(deliveryId);
        return ResponseEntity.ok(WarehouseDeliveryDTO.from(delivery));
    }

    @GetMapping("/{deliveryId}/available-delivery-drivers")
    public ResponseEntity<?> getAvailableDeliveryDrivers(@PathVariable Long deliveryId) {
        return ResponseEntity.ok(
                warehouseDeliveryService.findAllAvailableDeliveryDriversWithShift(deliveryId)
        );
    }

    @PostMapping("/")
    public ResponseEntity<WarehouseDeliveryDTO> createDelivery(
            @RequestBody WarehouseDeliveryPostDTO dto
    ) {
        return ResponseEntity.ok(WarehouseDeliveryDTO.from(warehouseDeliveryService.save(dto)));
    }

    @PostMapping("/{deliveryId}/pickup-address/")
    public ResponseEntity<?> setPickupAddress(
            @PathVariable Long deliveryId,
            @RequestBody PickupAddressPostDTO dto
    ) {
        warehouseDeliveryService.setPickupAddress(deliveryId, dto.address());
        return ResponseEntity.ok(Map.of("message", "Pickup address set successfully."));
    }

    @PostMapping("/{deliveryId}/sms-confirmation/{driverId}")
    public ResponseEntity<?> sendConfirmationSMS(@PathVariable Long deliveryId, @PathVariable Long driverId) {
        warehouseDeliveryService.mockConfirmationSMS(deliveryId, driverId);
        return ResponseEntity.ok(Map.of("message", "SMS confirmation sent successfully."));
    }

    @PostMapping("/{deliveryId}/assigned-driver/{driverId}")
    public ResponseEntity<?> setAssignedDriver(
            @PathVariable Long deliveryId,
            @PathVariable Long driverId
    ) {
        warehouseDeliveryService.assignDeliveryDriver(deliveryId, driverId);
        return ResponseEntity.ok(Map.of("message", "Driver successfully assigned."));
    }

    @PostMapping("/{deliveryId}/unload/{deliveryItemId}")
    public ResponseEntity<?> unloadDeliveryItem(
            @PathVariable Long deliveryId,
            @PathVariable Long deliveryItemId,
            @RequestBody StorageUnitPostDTO dto
    ) {
        StorageUnit storageUnit = storageUnitService.save(dto, deliveryItemId);
        return ResponseEntity.ok(StorageUnitDTO.from(storageUnit));
    }
}
