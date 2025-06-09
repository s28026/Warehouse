package edu.pja.mas.warehouse.dto;

import edu.pja.mas.warehouse.entity.EmployeeComplaint;


public record EmployeeComplaintDTO(
        Long id,
        String employeePesel,
        String description,
        boolean isResolved
) {
    public static EmployeeComplaintDTO from(EmployeeComplaint employeeComplaint) {
        return new EmployeeComplaintDTO(
                employeeComplaint.getId(),
                employeeComplaint.getEmployee().getPesel(),
                employeeComplaint.getDescription(),
                employeeComplaint.getResolved()
        );
    }
}
