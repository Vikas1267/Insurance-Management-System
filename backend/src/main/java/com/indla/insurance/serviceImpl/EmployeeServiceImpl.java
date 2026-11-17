package com.indla.insurance.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.indla.insurance.model.Employee;
import com.indla.insurance.repository.EmployeeRepository;
import com.indla.insurance.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

	@Autowired
	private EmployeeRepository repo;

	@Override
	public Employee saveEmployee(Employee employee) {
		logger.info("Employee Service Implementation : savePolicy() method");
		return repo.save(employee);
	}
}
