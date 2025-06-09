package edu.pja.mas.warehouse.repository;

import edu.pja.mas.warehouse.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;


public interface EmployeeRepository extends JpaRepository<Employee, String> {
}
