package edu.pja.mas.warehouse.controller;

import edu.pja.mas.warehouse.dto.*;
import edu.pja.mas.warehouse.entity.DeliveryDriver;
import edu.pja.mas.warehouse.entity.Employee;
import edu.pja.mas.warehouse.entity.EmployeeComplaint;
import edu.pja.mas.warehouse.entity.WarehouseEmployee;
import edu.pja.mas.warehouse.repository.EmployeeComplaintRepository;
import edu.pja.mas.warehouse.service.DeliveryDriverService;
import edu.pja.mas.warehouse.service.EmployeeComplaintService;
import edu.pja.mas.warehouse.service.EmployeeService;
import edu.pja.mas.warehouse.service.WarehouseEmployeeService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/employees")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;
    private final WarehouseEmployeeService warehouseEmployeeService;
    private final DeliveryDriverService deliveryDriverService;
    private final EmployeeComplaintRepository employeeComplaintRepository;
    private final EmployeeComplaintService employeeComplaintService;

    @PostMapping("/")
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeePostDTO dto) {
        Employee emp = employeeService.save(dto);
        return ResponseEntity.ok(EmployeeDTO.from(emp));
    }

    @PostMapping("/{employeePesel}/complaint")
    public ResponseEntity<EmployeeComplaintDTO> createComplaint(
            @PathVariable String employeePesel,
            @RequestBody EmployeeComplaintPostDTO dto
    ) {
        EmployeeComplaint employeeComplaint = employeeComplaintService.save(employeePesel, dto);
        return ResponseEntity.ok(EmployeeComplaintDTO.from(employeeComplaint));
    }

    @PostMapping("/{employeePesel}/complaint/{complaintId}")
    public ResponseEntity<?> resolveComplaint(
            @PathVariable Long complaintId,
            @RequestBody EmployeeComplaintResolveDTO dto
    ) {
        employeeComplaintService.resolveComplaint(complaintId, dto);
        return ResponseEntity.ok(Map.of("message", "Complaint resolved successfully."));
    }

    @PostMapping("/{employeePesel}/operator")
    public ResponseEntity<WarehouseEmployeeDTO> assignAsWarehouseOperator(
            @PathVariable String employeePesel,
            @RequestBody WarehouseOperatorPostDTO dto
    ) {
        WarehouseEmployee warehouseEmployee = warehouseEmployeeService.findByEmployeePesel(employeePesel);
        warehouseEmployee = warehouseEmployeeService.changeToOperator(warehouseEmployee, dto);
        return ResponseEntity.ok(WarehouseEmployeeDTO.from(warehouseEmployee));
    }

    @PostMapping("/{employeePesel}/worker")
    public ResponseEntity<WarehouseEmployeeDTO> assignAsWarehouseWorker(
            @PathVariable String employeePesel,
            @RequestBody WarehouseWorkerPostDTO dto
    ) {
        WarehouseEmployee warehouseEmployee = warehouseEmployeeService.findByEmployeePesel(employeePesel);
        warehouseEmployee = warehouseEmployeeService.changeToWorker(warehouseEmployee, dto);
        return ResponseEntity.ok(WarehouseEmployeeDTO.from(warehouseEmployee));
    }

    @GetMapping("/{employeePesel}/deliveries")
    public ResponseEntity<EmployeeDeliveriesDTO> getEmployeeDeliveries(
            @PathVariable String employeePesel
    ) {
        DeliveryDriver driver = deliveryDriverService.findByEmployedPesel(employeePesel);

        return ResponseEntity.ok(EmployeeDeliveriesDTO.from(driver));
    }
}
