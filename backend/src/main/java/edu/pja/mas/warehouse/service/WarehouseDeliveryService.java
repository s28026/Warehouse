package edu.pja.mas.warehouse.service;

import edu.pja.mas.warehouse.dto.DeliveryDriverDTO;
import edu.pja.mas.warehouse.dto.DeliveryDriverShiftDTO;
import edu.pja.mas.warehouse.dto.WarehouseDeliveryItemPostDTO;
import edu.pja.mas.warehouse.dto.WarehouseDeliveryPostDTO;
import edu.pja.mas.warehouse.entity.*;
import edu.pja.mas.warehouse.enums.DriverStatus;
import edu.pja.mas.warehouse.enums.SmsStatus;
import edu.pja.mas.warehouse.enums.WarehouseDeliveryStatus;
import edu.pja.mas.warehouse.repository.DeliveryDriverRepository;
import edu.pja.mas.warehouse.repository.WarehouseDeliveryItemRepository;
import edu.pja.mas.warehouse.repository.WarehouseDeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
public class WarehouseDeliveryService {
    private final WarehouseDeliveryRepository warehouseDeliveryRepository;
    private final DeliveryDriverService deliveryDriverService;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final DeliveryDriverRepository deliveryDriverRepository;
    private final WarehouseDeliveryItemRepository warehouseDeliveryItemRepository;
    private final StorageItemService storageItemService;
    private final WarehouseService warehouseService;
    private final EmployeeService employeeService;

    public WarehouseDelivery save(WarehouseDeliveryPostDTO dto) {
        Warehouse warehouse = warehouseService.findById(dto.warehouseId());

        WarehouseDelivery warehouseDelivery = WarehouseDelivery.builder()
                .warehouse(warehouse)
                .build();

        return warehouseDeliveryRepository.save(warehouseDelivery);
    }

    public WarehouseDelivery findById(Long id) {
        return warehouseDeliveryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Delivery driver not found with id: " + id));
    }

    public WarehouseDeliveryItem findItemById(Long id) {
        return warehouseDeliveryItemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Warehouse delivery item not found with id: " + id));
    }

    private void validatePickupAddress(String pickupAddress) {
        if (pickupAddress == null || pickupAddress.isBlank())
            throw new IllegalArgumentException("Pickup address cannot be null or blank");

        String[] parts = pickupAddress.split(",");

        if (parts.length != 4)
            throw new IllegalArgumentException("Address must have 4 parts: street, number, city, zip");

        String street = parts[0].trim();
        String number = parts[1].trim();
        String city = parts[2].trim();
        String zip = parts[3].trim();

        if (street.isEmpty() || number.isEmpty() || city.isEmpty() || zip.isEmpty())
            throw new IllegalArgumentException("None of the address parts can be empty");

        if (!zip.matches("\\d{2}-\\d{3}"))
            throw new IllegalArgumentException("ZIP code must be in format XX-XXX");
    }

    // This method is a placeholder for distance validation logic.
    // In a real application, you would integrate with a mapping service to calculate the distance.
    // For this example, we will simulate it with a random number.
    private boolean checkDistance(String pickupAddress) {
        Random random = new Random();
        int distance = random.nextInt(550); // Simulating distance in km

        return distance > 275; // 50%
    }

    public void setPickupAddress(Long deliveryId, String pickupAddress) {
        validatePickupAddress(pickupAddress);

        WarehouseDelivery wd = findById(deliveryId);

        if (wd.getStatus() != WarehouseDeliveryStatus.AWAITING_PICKUP_ADDRESS &&
                wd.getStatus() != WarehouseDeliveryStatus.INVALID_ADDRESS)
            throw new IllegalArgumentException("Delivery is not in a state that allows setting pickup address");

        try {
            if (!checkDistance(pickupAddress)) {
                wd.setStatus(WarehouseDeliveryStatus.INVALID_ADDRESS);
                throw new IllegalArgumentException("Distance exceeds maximum limit of 500 km");
            } else {
                wd.setStatus(WarehouseDeliveryStatus.AWAITING_DRIVER);
            }
        } finally {
            wd.setPickupAddress(pickupAddress);
            warehouseDeliveryRepository.save(wd);
        }
    }

    public void assignDeliveryDriver(Long deliveryId, Long driverId) {
        WarehouseDelivery wd = findById(deliveryId);
        DeliveryDriver dd = deliveryDriverService.findById(driverId);

        if (wd.getStatus() != WarehouseDeliveryStatus.AWAITING_DRIVER)
            throw new IllegalArgumentException("Delivery is not in a state that allows driver assignment");

        if (wd.getAssignedDriver() != null)
            throw new IllegalArgumentException("Driver is already assigned to this delivery");

        wd.setAssignedDriver(dd);
        wd.setStatus(WarehouseDeliveryStatus.AWAITING_DRIVER_CONFIRMATION);

        warehouseDeliveryRepository.save(wd);
    }

    private static final int CONFIRMATION_SMS_DELAY_MINUTES = 5;

    public void setAssignedDeliveryDriver(WarehouseDelivery delivery, DeliveryDriver driver) {
        if (driver.getStatus() != DriverStatus.AVAILABLE)
            throw new IllegalArgumentException("Driver is not available for assignment");

        delivery.setAssignedDriver(driver);
        driver.setStatus(DriverStatus.RESERVED);
        warehouseDeliveryRepository.save(delivery);
        deliveryDriverRepository.save(driver);
    }

    public void markAsAwaitingDriver(Long deliveryId) {
        WarehouseDelivery wd = findById(deliveryId);

        if (wd.getStatus() != WarehouseDeliveryStatus.AWAITING_PICKUP_ADDRESS &&
                wd.getStatus() != WarehouseDeliveryStatus.INVALID_ADDRESS) {
            throw new IllegalArgumentException("Delivery is not in a state that allows marking as awaiting driver");
        }

        wd.setStatus(WarehouseDeliveryStatus.AWAITING_DRIVER);
        warehouseDeliveryRepository.save(wd);
    }

    public void sendConfirmationSMS(Long deliveryId, Long driverId) {
        WarehouseDelivery wd = findById(deliveryId);
        DeliveryDriver dd = deliveryDriverService.findById(driverId);

        Set<WarehouseDeliveryStatus> allowedStatuses = Set.of(
                WarehouseDeliveryStatus.AWAITING_DRIVER,
                WarehouseDeliveryStatus.DRIVER_REJECTED,
                WarehouseDeliveryStatus.DRIVER_TIMEOUT
        );

        if (!allowedStatuses.contains(wd.getStatus()))
            throw new IllegalArgumentException("Delivery is not in a state that allows sending confirmation SMS");

        wd.setStatus(WarehouseDeliveryStatus.AWAITING_DRIVER_CONFIRMATION);
        setAssignedDeliveryDriver(wd, dd);
    }

    public void mockConfirmationSMS(Long deliveryId, Long driverId) {
        sendConfirmationSMS(deliveryId, driverId);
        Random random = new Random();

        scheduler.schedule(() -> {
            SmsStatus[] statuses = SmsStatus.values();
            handleSMSResponse(deliveryId, statuses[random.nextInt(statuses.length)]);
        }, CONFIRMATION_SMS_DELAY_MINUTES, TimeUnit.SECONDS);
    }

    public void handleSMSResponse(Long deliveryId, SmsStatus status) {
        WarehouseDelivery wd = findById(deliveryId);

        if (wd.getStatus() != WarehouseDeliveryStatus.AWAITING_DRIVER_CONFIRMATION)
            throw new IllegalArgumentException("Delivery is not in a state that allows handling SMS response");

        try {
            if (status == SmsStatus.ACCEPTED)
                deliveryDriverService.acceptDelivery(wd);
            else if (status == SmsStatus.REJECTED)
                deliveryDriverService.declineDelivery(wd, WarehouseDeliveryStatus.DRIVER_REJECTED);
            else if (status == SmsStatus.TIMEOUT)
                deliveryDriverService.declineDelivery(wd, WarehouseDeliveryStatus.DRIVER_TIMEOUT);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            warehouseDeliveryRepository.save(wd);
        }
    }

    public List<DeliveryDriverShiftDTO> findAllAvailableDeliveryDriversWithShift(Long deliveryId) {
        List<DeliveryDriver> availableDrivers = deliveryDriverRepository.findAllAvailableForWarehouseDelivery(deliveryId);

        return availableDrivers.stream().map(d -> {
            List<WarehouseDelivery> deliveries = deliveryDriverRepository.findAllTodayDeliveriesByDriver(d);

            return new DeliveryDriverShiftDTO(
                    DeliveryDriverDTO.from(d),
                    deliveries.size(),
                    deliveries.stream().mapToDouble(WarehouseDelivery::getDuration).sum()
            );
        }).toList();
    }

    public WarehouseDeliveryItem addItemToDelivery(WarehouseDeliveryItemPostDTO dto) {
        WarehouseDelivery delivery = findById(dto.deliveryId());
        StorageItem storageItem = storageItemService.findById(dto.itemId());

        WarehouseDeliveryItem item = WarehouseDeliveryItem.builder()
                .delivery(delivery)
                .storageItem(storageItem)
                .quantity(dto.quantity())
                .build();

        return warehouseDeliveryItemRepository.save(item);
    }

    public List<WarehouseDeliveryItem> addItemsToDelivery(List<WarehouseDeliveryItemPostDTO> dtos) {
        return dtos.stream()
                .map(this::addItemToDelivery)
                .toList();
    }

    public void destroyExpiredDeliveries() {
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);
        warehouseDeliveryRepository.bulkDeleteDeliveriesMarkedForDestruction(sevenDaysAgo.atStartOfDay());
    }

    public void markForUnload(Long deliveryId) {
        WarehouseDelivery wd = findById(deliveryId);

        if (wd.getStatus() != WarehouseDeliveryStatus.IN_TRANSIT)
            throw new IllegalArgumentException("Delivery is not in a state that allows marking for unload");

        DeliveryDriver driver = wd.getAssignedDriver();
        driver.setStatus(DriverStatus.AVAILABLE);
        deliveryDriverRepository.save(driver);

        wd.setDeliveredAt(LocalDateTime.now());
        wd.setStatus(WarehouseDeliveryStatus.AWAITING_UNLOAD);
        warehouseDeliveryRepository.save(wd);
    }

    public void unload(Long deliveryId, String employeePesel) {
        WarehouseDelivery wd = findById(deliveryId);

        if (wd.getStatus() != WarehouseDeliveryStatus.AWAITING_UNLOAD)
            throw new IllegalArgumentException("Delivery is not in a state that allows assigning employee to unload");

        Employee emp = employeeService.findById(employeePesel);
        if (!emp.isWarehouseEmployee())
            throw new IllegalArgumentException("Employee is not a warehouse employee");

        wd.setStatus(WarehouseDeliveryStatus.UNLOADING);
        wd.setAssignedWarehouseEmployee(emp.getWarehouseEmployee());
        warehouseDeliveryRepository.save(wd);
    }

    public void markAsCompleted(Long deliveryId) {
        WarehouseDelivery wd = findById(deliveryId);

        if (wd.getStatus() != WarehouseDeliveryStatus.UNLOADING)
            throw new IllegalArgumentException("Delivery is not in a state that allows marking as completed");

        wd.setStatus(WarehouseDeliveryStatus.COMPLETED);
        warehouseDeliveryRepository.save(wd);
    }
}
