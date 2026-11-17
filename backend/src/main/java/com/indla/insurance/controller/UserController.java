package com.indla.insurance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.indla.insurance.model.User;
import com.indla.insurance.service.InsuranceTypeService;
import com.indla.insurance.service.UserService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/save")
	public ResponseEntity<User> createUser(@RequestBody User user) throws Exception {
		User users = userService.saveUser(user);
		return ResponseEntity.ok().body(users);
	}

	@PostMapping("/savepolicy")
	public ResponseEntity<User> createUserPolicy(@RequestBody User user) throws Exception {
		User users = userService.saveUser(user);
		return ResponseEntity.ok().body(users);
	}

	@GetMapping("/getcity/{city}")
	public ResponseEntity<List<User>> getUserByCity(@PathVariable("city") String city) {
		List<User> user = userService.getListByCity(city);
		return ResponseEntity.ok().body(user);
	}
}
