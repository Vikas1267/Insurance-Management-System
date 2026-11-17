package com.indla.insurance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.indla.insurance.model.User;
import com.indla.insurance.service.UserService;

@CrossOrigin(origins = "*")
@RestController
public class LoginController {

	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity<User> loginUser(@RequestBody User user) throws Exception {
		String username = user.getUsername();
		String password = user.getUserpwd();
		user = userService.loginUser(username, password);
		return ResponseEntity.ok().body(user);
	}
}
