package edu.pja.mas.warehouse.service;

import edu.pja.mas.warehouse.dto.DeliveryDriverDTO;
import edu.pja.mas.warehouse.dto.DeliveryDriverPostDTO;
import edu.pja.mas.warehouse.dto.EmployeePostDTO;
import edu.pja.mas.warehouse.dto.WarehouseEmployeePostDTO;
import edu.pja.mas.warehouse.entity.DeliveryDriver;
import edu.pja.mas.warehouse.entity.Employee;
import edu.pja.mas.warehouse.entity.WarehouseEmployee;
import edu.pja.mas.warehouse.enums.EmployeeType;
import edu.pja.mas.warehouse.repository.DeliveryDriverRepository;
import edu.pja.mas.warehouse.repository.EmployeeRepository;
import edu.pja.mas.warehouse.repository.WarehouseEmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final DeliveryDriverRepository deliveryDriverRepository;
    private final WarehouseEmployeeRepository warehouseEmployeeRepository;

    private final DeliveryDriverService deliveryDriverService;
    private final @Lazy WarehouseEmployeeService warehouseEmployeeService;

    public Employee findById(String pesel) {
        return employeeRepository.findById(pesel)
                .orElseThrow(() -> new IllegalArgumentException("Employee with given PESEL not found: " + pesel));
    }

    public Employee save(EmployeePostDTO dto) {
        Employee employee = Employee.builder()
                .firstName(dto.person().firstName())
                .lastName(dto.person().lastName())
                .pesel(dto.person().pesel())
                .dateOfBirth(dto.person().dateOfBirth())
                .phoneNumber(dto.phoneNumber())
                .salary(dto.salary())
                .build();

        employee = employeeRepository.save(employee);

        if (dto.deliveryDriver() != null)
            employAsDeliveryDriver(employee, dto.deliveryDriver());
        if (dto.warehouse() != null)
            employAsWarehouseEmployee(employee, dto.warehouse());

        return employee;
    }

    public List<Employee> saveAll(List<EmployeePostDTO> dtos) {
        return dtos.stream()
                .map(this::save)
                .toList();
    }

    public void employAsDeliveryDriver(Employee employee, DeliveryDriverPostDTO dto) {
        if (employee.getEmployeeTypes().contains(EmployeeType.DELIVERY_DRIVER))
            throw new IllegalArgumentException("Employee is already employed as a delivery driver.");

        DeliveryDriver deliveryDriver = deliveryDriverRepository.findByEmployee(employee);

        if (deliveryDriver == null)
            deliveryDriverService.save(employee, dto);
        else
            employee.setDeliveryDriver(deliveryDriver);

        employee.getEmployeeTypes().add(EmployeeType.DELIVERY_DRIVER);
        employeeRepository.save(employee);
    }

    public void employAsWarehouseEmployee(Employee employee, WarehouseEmployeePostDTO dto) {
        if (employee.getEmployeeTypes().contains(EmployeeType.WAREHOUSE_EMPLOYEE))
            throw new IllegalArgumentException("Employee is already employed as a delivery driver.");

        WarehouseEmployee warehouseEmployee = warehouseEmployeeRepository.findByEmployee(employee);

        if (warehouseEmployee == null)
            warehouseEmployeeService.save(employee, dto);
        else
            employee.setWarehouseEmployee(warehouseEmployee);

        employee.getEmployeeTypes().add(EmployeeType.WAREHOUSE_EMPLOYEE);
        employeeRepository.save(employee);
    }

    public void startShift(String pesel) {
        Employee employee = findById(pesel);

        if (employee.getShiftStart() != null)
            throw new IllegalArgumentException("Shift has already been started for employee: " + pesel);

        employee.setShiftStart(LocalDate.now());
        employeeRepository.save(employee);
    }

    public void endShift(String pesel) {
        Employee employee = findById(pesel);

        if (employee.getShiftStart() == null)
            throw new IllegalArgumentException("Shift has not been started for employee: " + pesel);

        employee.setShiftEnd(LocalDate.now());
        employeeRepository.save(employee);
    }

    public void takeBreak(String pesel, int duration) {
        Employee employee = findById(pesel);

        if (!employee.isOnShift())
            throw new IllegalArgumentException("Employee is not currently on shift: " + pesel);

        if (!employee.canTakeBreak(duration))
            throw new IllegalArgumentException("Employee cannot take a break of " + duration + " minutes: " + pesel);

        employee.getBreaksTakenToday().add(duration);
        employeeRepository.save(employee);
    }
}
