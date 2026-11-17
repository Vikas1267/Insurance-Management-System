package com.indla.insurance.repository;

import java.io.Serializable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.indla.insurance.model.Employee;
@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Serializable>{
	
}
