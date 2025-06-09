package edu.pja.mas.warehouse.controller;

import edu.pja.mas.warehouse.dto.WarehouseDTO;
import edu.pja.mas.warehouse.dto.WarehouseEmployeeDTO;
import edu.pja.mas.warehouse.dto.WarehousePostDTO;
import edu.pja.mas.warehouse.entity.Warehouse;
import edu.pja.mas.warehouse.repository.WarehouseEmployeeRepository;
import edu.pja.mas.warehouse.service.EmployeeService;
import edu.pja.mas.warehouse.service.StorageUnitService;
import edu.pja.mas.warehouse.service.WarehouseEmployeeService;
import edu.pja.mas.warehouse.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/warehouses")
@RequiredArgsConstructor
public class WarehouseController {
    private final WarehouseService warehouseService;
    private final WarehouseEmployeeRepository warehouseEmployeeRepository;
    private final EmployeeService employeeService;
    private final WarehouseEmployeeService warehouseEmployeeService;
    private final StorageUnitService storageUnitService;

    @GetMapping("/{warehouseId}")
    public ResponseEntity<WarehouseDTO> getWarehouseById(
            @PathVariable Long warehouseId,
            @RequestParam(defaultValue = "false") boolean withEmployees
    ) {
        Warehouse warehouse = warehouseService.findById(warehouseId, withEmployees);

        if (warehouse == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        else
            return ResponseEntity.ok(WarehouseDTO.from(warehouse));
    }

    @GetMapping("/{warehouseId}/employees/")
    public ResponseEntity<List<WarehouseEmployeeDTO>> getWarehouseEmployees(
            @PathVariable Long warehouseId
    ) {
        Warehouse warehouse = warehouseService.findById(warehouseId);

        if (warehouse == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        else
            return ResponseEntity.ok(WarehouseEmployeeDTO.fromList(warehouse.getEmployees()));

    }

    @GetMapping("/")
    public ResponseEntity<List<WarehouseDTO>> getWarehouses() {
        List<Warehouse> all = warehouseService.findAll();

        return ResponseEntity.ok(WarehouseDTO.from(all));
    }

    @PostMapping("/")
    public ResponseEntity<WarehouseDTO> createWarehouse(@RequestBody WarehousePostDTO warehousePostDTO) {
        Warehouse createdWarehouse = warehouseService.save(warehousePostDTO);

        if (createdWarehouse == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(WarehouseDTO.from(createdWarehouse));
        }
    }

    @GetMapping("/{warehouseId}/stored-items/{itemId}/availability")
    public ResponseEntity<?> getStoredItemById(
            @PathVariable Long warehouseId,
            @PathVariable Long itemId
    ) {
        int availability = storageUnitService.getWarehouseItemAvailability(warehouseId, itemId);

        return ResponseEntity.ok(Map.of("availability", availability));
    }
}
