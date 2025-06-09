package edu.pja.mas.warehouse.controller;

import edu.pja.mas.warehouse.dto.*;
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
    public ResponseEntity<?> createComplaint(
            @PathVariable String employeePesel,
            @RequestBody EmployeeComplaintPostDTO dto
    ) {
        try {
            EmployeeComplaint employeeComplaint = employeeComplaintService.save(employeePesel, dto);

            return ResponseEntity.ok(EmployeeComplaintDTO.from(employeeComplaint));
        } catch (IllegalArgumentException | ValidationException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{employeePesel}/complaint/{complaintId}")
    public ResponseEntity<?> resolveComplaint(
            @PathVariable Long complaintId,
            @RequestBody EmployeeComplaintResolveDTO dto
    ) {
        try {
            employeeComplaintService.resolveComplaint(complaintId, dto);

            return ResponseEntity.ok(Map.of("message", "Complaint resolved successfully."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/{employeePesel}/operator")
    public ResponseEntity<?> assignAsWarehouseOperator(
            @PathVariable String employeePesel,
            @RequestBody WarehouseOperatorPostDTO dto
    ) {
        WarehouseEmployee warehouseEmployee = warehouseEmployeeService.findByEmployeePesel(employeePesel);

        return ResponseEntity.ok(warehouseEmployeeService.changeToOperator(warehouseEmployee, dto));
    }

    @PostMapping("/{employeePesel}/worker")
    public ResponseEntity<?> assignAsWarehouseWorker(
            @PathVariable String employeePesel,
            @RequestBody WarehouseWorkerPostDTO dto
    ) {
        WarehouseEmployee warehouseEmployee = warehouseEmployeeService.findByEmployeePesel(employeePesel);

        return ResponseEntity.ok(warehouseEmployeeService.changeToWorker(warehouseEmployee, dto));
    }
}
