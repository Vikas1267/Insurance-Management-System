package com.indla.insurance.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.indla.insurance.exception.LifeInsuranceException;
import com.indla.insurance.model.User;
import com.indla.insurance.repository.UserRepository;
import com.indla.insurance.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public User loginUser(String username, String password) {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new LifeInsuranceException("You entered incorrect username.");
		} else {
			if (user.getUserpwd().equals(password)) {
				return user;
			}
			throw new LifeInsuranceException("You entered incorrect password.");
		}
	}

	@Override
	@Transactional
	public User saveUser(User user) {
		return userRepository.save(user);
	}

	@Override
	public List<User> getListByCity(String city) {
		List<User> user = userRepository.findByCity(city);
		return user;
	}
}
