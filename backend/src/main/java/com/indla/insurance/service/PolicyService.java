package com.indla.insurance.service;

import com.indla.insurance.model.Policy;

public interface PolicyService {
	
	public Iterable<Policy> getAllPolicy();
	public Policy savePolicy(Policy policy);
	public Policy getPolicyById(Integer Id);
	public void deletePolicy(Integer id);
	
}
