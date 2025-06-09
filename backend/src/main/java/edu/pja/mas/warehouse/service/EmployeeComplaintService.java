package edu.pja.mas.warehouse.service;

import edu.pja.mas.warehouse.dto.EmployeeComplaintPostDTO;
import edu.pja.mas.warehouse.dto.EmployeeComplaintResolveDTO;
import edu.pja.mas.warehouse.entity.Employee;
import edu.pja.mas.warehouse.entity.EmployeeComplaint;
import edu.pja.mas.warehouse.repository.EmployeeComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class EmployeeComplaintService {
    private final EmployeeService employeeService;
    private final EmployeeComplaintRepository employeeComplaintRepository;

    public EmployeeComplaint save(String employeePesel, EmployeeComplaintPostDTO dto) {
        Employee employee = employeeService.findById(employeePesel);

        EmployeeComplaint employeeComplaint = EmployeeComplaint.builder()
                .employee(employee)
                .description(dto.description())
                .build();

        return employeeComplaintRepository.save(employeeComplaint);

    }

    public EmployeeComplaint findById(Long id) {
        return employeeComplaintRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Employee complaint not found with id: " + id));
    }

    public void resolveComplaint(Long id, EmployeeComplaintResolveDTO dto) {
        EmployeeComplaint employeeComplaint = findById(id);
        employeeComplaint.setResolved(true);
        employeeComplaint.setActionTaken(dto.actionTaken());
        employeeComplaintRepository.save(employeeComplaint);
    }
}
