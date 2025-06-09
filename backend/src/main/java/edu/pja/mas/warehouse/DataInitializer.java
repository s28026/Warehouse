package edu.pja.mas.warehouse;

import edu.pja.mas.warehouse.dto.*;
import edu.pja.mas.warehouse.entity.*;
import edu.pja.mas.warehouse.enums.WarehouseDeliveryStatus;
import edu.pja.mas.warehouse.repository.*;
import edu.pja.mas.warehouse.service.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final WarehouseRepository warehouseRepository;
    private final WarehouseService warehouseService;
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;
    private final DeliveryDriverRepository deliveryDriverRepository;
    private final DeliveryDriverService deliveryDriverService;
    private final WarehouseDeliveryService warehouseDeliveryService;
    private final StorageItemService storageItemService;
    private final StorageItemRepository storageItemRepository;
    private final WarehouseDeliveryItemRepository warehouseDeliveryItemRepository;
    private final WarehouseDeliveryRepository warehouseDeliveryRepository;

    Warehouse w1, w2;

    private void mockWarehouses() {
        WarehousePostDTO warehouseDto1 = new WarehousePostDTO("Koszykowa, 86, Warszawa, 02-008");
        w1 = warehouseService.save(warehouseDto1);
        warehouseRepository.flush();

        WarehousePostDTO warehouseDto2 = new WarehousePostDTO("Miodowa, 12, Warszawa, 01-001");
        w2 = warehouseService.save(warehouseDto2);
        warehouseRepository.flush();
    }

    private void setDeliveryAsCompleted(WarehouseDelivery wd, Employee dd, Employee we) {
        LocalDate today = LocalDate.now();
        LocalDateTime time = LocalDateTime.now();

        wd.setPickupAddress("Cudna, 93, Warsaw, 03-290");
        wd.setRegisteredAt(LocalDateTime.of(today, time.minusHours(4).minusMinutes(30).toLocalTime()));
        wd.setConfirmedAt(LocalDateTime.of(today, time.minusHours(4).minusMinutes(26).toLocalTime()));
        wd.setPickupAt(LocalDateTime.of(today, time.minusHours(2).minusMinutes(22).toLocalTime()));
        wd.setDeliveredAt(LocalDateTime.of(today, LocalTime.now()));

        wd.setAssignedDriver(dd.getDeliveryDriver());
        wd.setAssignedWarehouseEmployee(we.getWarehouseEmployee());

        wd.setStatus(WarehouseDeliveryStatus.COMPLETED);

        warehouseDeliveryRepository.save(wd);
    }

    private void mockW1() {
        // Warehouse with employees
        Employee e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12;
        {
            // Create employees for warehouse w1
            // Pesel must be unique, so we use different ones for each employee
            EmployeePostDTO edto1 = new EmployeePostDTO(
                    new PersonPostDTO(
                            "84848272411",
                            "John",
                            "Doe",
                            LocalDate.of(2000, 1, 1)
                    ),
                    "812345679",
                    5000,
                    new WarehouseEmployeePostDTO(
                            w1.getId(),
                            new WarehouseWorkerPostDTO(5000),
                            null
                    ),
                    null
            );
            EmployeePostDTO edto2 = new EmployeePostDTO(
                    new PersonPostDTO(
                            "84848272412",
                            "Jane",
                            "Doe",
                            LocalDate.of(1995, 5, 15)
                    ),
                    "812345678",
                    6000,
                    new WarehouseEmployeePostDTO(
                            w1.getId(),
                            new WarehouseWorkerPostDTO(6000),
                            null
                    ),
                    null
            );
            EmployeePostDTO edto3 = new EmployeePostDTO(
                    new PersonPostDTO(
                            "84848272413",
                            "Alice",
                            "Smith",
                            LocalDate.of(1990, 3, 20)
                    ),
                    "812345677",
                    7000,
                    new WarehouseEmployeePostDTO(
                            w1.getId(),
                            null,
                            new WarehouseOperatorPostDTO(
                                    "FL-1234",
                                    LocalDate.now().plusYears(2),
                                    "Truck"
                            )
                    ),
                    new DeliveryDriverPostDTO(
                            "DL-1234567",
                            LocalDate.now().plusYears(2)
                    )
            );
            EmployeePostDTO edto4 = new EmployeePostDTO(
                    new PersonPostDTO(
                            "84848272414",
                            "Bob",
                            "Johnson",
                            LocalDate.of(1988, 8, 8)
                    ),
                    "812345676",
                    5500,
                    new WarehouseEmployeePostDTO(
                            w1.getId(),
                            new WarehouseWorkerPostDTO(5500),
                            null
                    ),
                    null
            );
            EmployeePostDTO edto5 = new EmployeePostDTO(
                    new PersonPostDTO(
                            "84848272415",
                            "Charlie",
                            "Brown",
                            LocalDate.of(1992, 12, 12)
                    ),
                    "812345675",
                    5800,
                    new WarehouseEmployeePostDTO(
                            w1.getId(),
                            new WarehouseWorkerPostDTO(5800),
                            null
                    ),
                    null
            );
            EmployeePostDTO edto6 = new EmployeePostDTO(
                    new PersonPostDTO(
                            "84848272416",
                            "David",
                            "Wilson",
                            LocalDate.of(1985, 11, 11)
                    ),
                    "812345674",
                    6200,
                    new WarehouseEmployeePostDTO(
                            w1.getId(),
                            null,
                            new WarehouseOperatorPostDTO(
                                    "FL-2357",
                                    LocalDate.now().plusYears(2),
                                    "Van"
                            )
                    ),
                    new DeliveryDriverPostDTO(
                            "DL-1354567",
                            LocalDate.now().plusYears(2)
                    )
            );
            EmployeePostDTO edto7 = new EmployeePostDTO(
                    new PersonPostDTO(
                            "84848272417",
                            "Eve",
                            "Davis",
                            LocalDate.of(1993, 4, 4)
                    ),
                    "812345673",
                    6500,
                    null,
                    new DeliveryDriverPostDTO(
                            "DL-1232567",
                            LocalDate.now().plusYears(2)
                    )
            );
            // only deliver drivers
            EmployeePostDTO edto8 = new EmployeePostDTO(
                    new PersonPostDTO(
                            "84848272418",
                            "Frank",
                            "Garcia",
                            LocalDate.of(1991, 6, 6)
                    ),
                    "812345672",
                    7000,
                    null,
                    new DeliveryDriverPostDTO(
                            "DL-1234568",
                            LocalDate.now().plusYears(2)
                    )
            );
            EmployeePostDTO edto9 = new EmployeePostDTO(
                    new PersonPostDTO(
                            "84848272419",
                            "Grace",
                            "Martinez",
                            LocalDate.of(1989, 9, 9)
                    ),
                    "812345671",
                    7200,
                    null,
                    new DeliveryDriverPostDTO(
                            "DL-1234569",
                            LocalDate.now().plusYears(2)
                    )
            );
            EmployeePostDTO edto10 = new EmployeePostDTO(
                    new PersonPostDTO(
                            "84848272420",
                            "Hank",
                            "Lopez",
                            LocalDate.of(1987, 7, 7)
                    ),
                    "812345670",
                    6800,
                    null,
                    new DeliveryDriverPostDTO(
                            "DL-1234570",
                            LocalDate.now().plusYears(2)
                    )
            );
            EmployeePostDTO edto11 = new EmployeePostDTO(
                    new PersonPostDTO(
                            "84848272421",
                            "Ivy",
                            "Gonzalez",
                            LocalDate.of(1994, 10, 10)
                    ),
                    "812345669",
                    6900,
                    null,
                    new DeliveryDriverPostDTO(
                            "DL-1234571",
                            LocalDate.now().plusYears(2)
                    )
            );
            EmployeePostDTO edto12 = new EmployeePostDTO(
                    new PersonPostDTO(
                            "84848272422",
                            "Jack",
                            "Perez",
                            LocalDate.of(1996, 2, 2)
                    ),
                    "812345668",
                    7100,
                    null,
                    new DeliveryDriverPostDTO(
                            "DL-1234572",
                            LocalDate.now().plusYears(2)
                    )
            );

            e1 = employeeService.save(edto1);
            e2 = employeeService.save(edto2);
            e3 = employeeService.save(edto3);
            e4 = employeeService.save(edto4);
            e5 = employeeService.save(edto5);
            e6 = employeeService.save(edto6);
            e7 = employeeService.save(edto7);
            e8 = employeeService.save(edto8);
            e9 = employeeService.save(edto9);
            e10 = employeeService.save(edto10);
            e11 = employeeService.save(edto11);
            e12 = employeeService.save(edto12);
            employeeRepository.flush();
        }

        // Storage Items
        StorageItem si1, si2, si3;
        {
            StorageItemPostDTO siDto1 = new StorageItemPostDTO(
                    "Item A",
                    "A123",
                    50.5
            );
            StorageItemPostDTO siDto2 = new StorageItemPostDTO(
                    "Item B",
                    "B456",
                    30.0
            );
            StorageItemPostDTO siDto3 = new StorageItemPostDTO(
                    "Item C",
                    "C789",
                    20.0
            );

            si1 = storageItemService.save(siDto1);
            si2 = storageItemService.save(siDto2);
            si3 = storageItemService.save(siDto3);
            storageItemRepository.flush();
        }

        WarehouseDelivery wd1, wd2, wd3;
        {
            WarehouseDeliveryPostDTO wd1Dto = new WarehouseDeliveryPostDTO(w1.getId());
            WarehouseDeliveryPostDTO wd2Dto = new WarehouseDeliveryPostDTO(w1.getId());
            WarehouseDeliveryPostDTO wd3Dto = new WarehouseDeliveryPostDTO(w1.getId());

            wd1 = warehouseDeliveryService.save(wd1Dto);
            wd2 = warehouseDeliveryService.save(wd2Dto);
            wd3 = warehouseDeliveryService.save(wd3Dto);
            warehouseDeliveryRepository.flush();
        }

        WarehouseDeliveryItem wdi1, wdi2, wdi3;
        {
            WarehouseDeliveryItemPostDTO wdiDto1 = new WarehouseDeliveryItemPostDTO(
                    wd1.getId(),
                    si1.getId(),
                    10
            );
            WarehouseDeliveryItemPostDTO wdiDto2 = new WarehouseDeliveryItemPostDTO(
                    wd1.getId(),
                    si2.getId(),
                    5
            );
            WarehouseDeliveryItemPostDTO wdiDto3 = new WarehouseDeliveryItemPostDTO(
                    wd2.getId(),
                    si1.getId(),
                    20
            );

            warehouseDeliveryService.addItemsToDelivery(List.of(wdiDto1, wdiDto2, wdiDto3));
            warehouseDeliveryItemRepository.flush();

            setDeliveryAsCompleted(wd1, e3, e1);
            setDeliveryAsCompleted(wd2, e6, e2);
            setDeliveryAsCompleted(wd3, e6, e4);

            warehouseDeliveryRepository.flush();
        }
    }

    private void mockW2Employees() {

    }

    @PostConstruct
    public void init() {
        if (warehouseRepository.count() != 0) return;

        mockWarehouses();

        mockW1();
    }
}
