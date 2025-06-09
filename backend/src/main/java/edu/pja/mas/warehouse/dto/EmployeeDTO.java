package edu.pja.mas.warehouse.dto;

import edu.pja.mas.warehouse.entity.Employee;

import java.time.LocalDate;
import java.util.List;


public record EmployeeDTO(
        PersonDTO person,
        String phoneNumber,
        List<Integer> breaksTakenToday,
        LocalDate employmentDate,
        LocalDate terminationDate,
        LocalDate shiftStart,
        LocalDate shiftEnd,
        int salary
) {
    public static EmployeeDTO from(Employee emp) {
        return new EmployeeDTO(
                new PersonDTO(
                        emp.getPesel(),
                        emp.getFirstName(),
                        emp.getLastName(),
                        emp.getDateOfBirth(),
                        emp.getFullName()
                ),
                emp.getPhoneNumber(),
                emp.getBreaksTakenToday(),
                emp.getEmploymentDate(),
                emp.getTerminationDate(),
                emp.getShiftStart(),
                emp.getShiftEnd(),
                emp.getSalary()
        );
    }

    public static List<EmployeeDTO> from(List<Employee> employees) {
        return employees.stream()
                .map(EmployeeDTO::from)
                .toList();
    }
}
