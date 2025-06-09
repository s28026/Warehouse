package edu.pja.mas.warehouse.repository;

import edu.pja.mas.warehouse.entity.EmployeeComplaint;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EmployeeComplaintRepository extends JpaRepository<EmployeeComplaint, Long> {
}
