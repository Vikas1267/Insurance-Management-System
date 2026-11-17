package com.indla.insurance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.indla.insurance.model.Address;
import com.indla.insurance.model.Employee;
import com.indla.insurance.service.AddressService;
import com.indla.insurance.service.EmployeeService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private AddressService addressService;

	@PostMapping("/saveEmpAdd")
	public ResponseEntity<Employee> saveEmployeeAddress(@RequestBody Employee employee) {
		Employee employees = employeeService.saveEmployee(employee);
		List<Address> add = employees.getAddress();
		for (Address address : add) {
			addressService.saveAddress(address);
		}
		return ResponseEntity.ok().body(employee);
	}
}
