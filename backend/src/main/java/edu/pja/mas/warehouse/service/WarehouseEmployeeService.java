package edu.pja.mas.warehouse.service;

import edu.pja.mas.warehouse.dto.WarehouseEmployeePostDTO;
import edu.pja.mas.warehouse.dto.WarehouseOperatorPostDTO;
import edu.pja.mas.warehouse.dto.WarehouseWorkerPostDTO;
import edu.pja.mas.warehouse.entity.*;
import edu.pja.mas.warehouse.repository.WarehouseEmployeeRepository;
import edu.pja.mas.warehouse.repository.WarehouseOperatorRepository;
import edu.pja.mas.warehouse.repository.WarehouseWorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class WarehouseEmployeeService {
    private final WarehouseService warehouseService;
    private final WarehouseEmployeeRepository warehouseEmployeeRepository;
    private final WarehouseWorkerRepository warehouseWorkerRepository;
    private final WarehouseOperatorRepository warehouseOperatorRepository;

    public WarehouseEmployee save(Employee employee, WarehouseEmployeePostDTO dto) {
        Warehouse warehouse = warehouseService.findById(dto.warehouseId());

        WarehouseEmployee warehouseEmployee = WarehouseEmployee.builder()
                .employee(employee)
                .warehouse(warehouse)
                .build();

        employee.setWarehouseEmployee(warehouseEmployee);
        warehouseEmployee = warehouseEmployeeRepository.save(warehouseEmployee);

        if (dto.worker() != null)
            changeToWorker(warehouseEmployee, dto.worker());
        else if (dto.operator() != null)
            changeToOperator(warehouseEmployee, dto.operator());

        return warehouseEmployee;
    }

    public WarehouseEmployee changeToWorker(WarehouseEmployee warehouseEmployee, WarehouseWorkerPostDTO dto) {
        if (warehouseEmployee.isWarehouseWorker())
            throw new IllegalArgumentException("Employee is already a warehouse worker");

        WarehouseWorker w = warehouseWorkerRepository.findByWarehouseEmployee_Id(warehouseEmployee.getId());
        if (w != null) {
            warehouseEmployee.setWarehouseWorker(w);
            return warehouseEmployeeRepository.save(warehouseEmployee);
        }

        WarehouseWorker warehouseWorker = WarehouseWorker.builder()
                .warehouseEmployee(warehouseEmployee)
                .capacity(dto.capacity())
                .build();
        warehouseWorker = warehouseWorkerRepository.save(warehouseWorker);

        warehouseEmployee.setWarehouseOperator(null);
        warehouseEmployee.setWarehouseWorker(warehouseWorker);

        return warehouseEmployeeRepository.save(warehouseEmployee);
    }

    public WarehouseEmployee changeToOperator(WarehouseEmployee warehouseEmployee, WarehouseOperatorPostDTO dto) {
        if (warehouseEmployee.isWarehouseOperator())
            throw new IllegalArgumentException("Employee is already a warehouse operator");

        WarehouseOperator op = warehouseOperatorRepository.findByWarehouseEmployee_Id(warehouseEmployee.getId());
        if (op != null) {
            warehouseEmployee.setWarehouseOperator(op);
            return warehouseEmployeeRepository.save(warehouseEmployee);
        }

        WarehouseOperator warehouseOperator = WarehouseOperator.builder()
                .warehouseEmployee(warehouseEmployee)
                .driverLicense(dto.driverLicense())
                .driverLicenseValidUntil(dto.driverLicenseValidUntil())
                .vehicleType(dto.vehicleType())
                .build();
        warehouseOperator.validateDriverLicense();
        warehouseOperator = warehouseOperatorRepository.save(warehouseOperator);

        warehouseEmployee.setWarehouseWorker(null);
        warehouseEmployee.setWarehouseOperator(warehouseOperator);

        return warehouseEmployeeRepository.save(warehouseEmployee);
    }

    public WarehouseEmployee findById(Long id) {
        return warehouseEmployeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse employee not found"));
    }

    public WarehouseEmployee findByEmployeePesel(String employeePesel) {
        return warehouseEmployeeRepository.findByEmployeePesel(employeePesel);
    }
}
