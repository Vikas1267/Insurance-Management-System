package com.indla.insurance.service;

import java.util.List;

import com.indla.insurance.model.User;
public interface UserService {
	
	public User loginUser(String username, String password)throws Exception;

	public User saveUser(User user);
	
	public List<User> getListByCity(String city);
	
}
                                                     




