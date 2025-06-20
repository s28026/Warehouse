package edu.pja.mas.warehouse.service;

import edu.pja.mas.warehouse.dto.DeliveryDriverDTO;
import edu.pja.mas.warehouse.dto.DeliveryDriverPostDTO;
import edu.pja.mas.warehouse.dto.EmployeeDeliveriesDTO;
import edu.pja.mas.warehouse.entity.DeclinedDelivery;
import edu.pja.mas.warehouse.entity.DeliveryDriver;
import edu.pja.mas.warehouse.entity.Employee;
import edu.pja.mas.warehouse.entity.WarehouseDelivery;
import edu.pja.mas.warehouse.enums.DriverStatus;
import edu.pja.mas.warehouse.enums.WarehouseDeliveryStatus;
import edu.pja.mas.warehouse.repository.DeclinedDeliveryRepository;
import edu.pja.mas.warehouse.repository.DeliveryDriverRepository;
import edu.pja.mas.warehouse.repository.WarehouseDeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class DeliveryDriverService {
    private final DeliveryDriverRepository deliveryDriverRepository;
    private final DeclinedDeliveryRepository declinedDeliveryRepository;

    public DeliveryDriver findById(Long id) {
        return deliveryDriverRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Delivery driver not found with id: " + id));
    }

    public DeliveryDriver findByEmployedPesel(String pesel) {
        return deliveryDriverRepository.findByEmployeePesel(pesel)
                .orElseThrow(() -> new IllegalArgumentException("Delivery driver not found with pesel: " + pesel));
    }

    public DeliveryDriver save(Employee employee, DeliveryDriverPostDTO dto) {
        DeliveryDriver driver = DeliveryDriver.builder()
                .driverLicense(dto.driverLicense())
                .driverLicenseValidUntil(dto.driverLicenseValidUntil())
                .employee(employee)
                .status(DriverStatus.AVAILABLE)
                .build();

        driver.validateDriverLicense();

        employee.setDeliveryDriver(driver);
        return deliveryDriverRepository.save(driver);
    }

    public List<DeliveryDriverDTO> findAllAvailable() {
        return DeliveryDriverDTO.from(deliveryDriverRepository.findAllAvailable());
    }

    public List<DeliveryDriverDTO> findAll() {
        return DeliveryDriverDTO.from(deliveryDriverRepository.findAll());
    }

    public void acceptDelivery(WarehouseDelivery delivery) {
        DeliveryDriver driver = delivery.getAssignedDriver();
        delivery.setStatus(WarehouseDeliveryStatus.IN_TRANSIT);
        delivery.setConfirmedAt(LocalDateTime.now());
        driver.setStatus(DriverStatus.RESERVED);
        deliveryDriverRepository.save(driver);
    }

    public void declineDelivery(WarehouseDelivery delivery, WarehouseDeliveryStatus status) {
        DeliveryDriver driver = delivery.getAssignedDriver();

        DeclinedDelivery declinedDelivery = DeclinedDelivery.builder()
                .delivery(delivery)
                .driver(driver)
                .build();
        declinedDeliveryRepository.save(declinedDelivery);

        delivery.setStatus(status);
        delivery.setAssignedDriver(null);
        driver.setStatus(DriverStatus.AVAILABLE);

        deliveryDriverRepository.save(driver);
    }
}
