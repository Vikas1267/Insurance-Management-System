package com.indla.insurance.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.indla.insurance.exception.PolicyTypeException;
import com.indla.insurance.model.Policy;
import com.indla.insurance.repository.PolicyRepository;
import com.indla.insurance.service.PolicyService;

@Service
public class PolicyServiceImpl implements PolicyService{

	private static final Logger logger = LoggerFactory.getLogger(PolicyServiceImpl.class);
	
	@Autowired
	private PolicyRepository repo; //we are injecting the repo object into PolicyServiceImpl class

	@Override
	public Iterable<Policy> getAllPolicy() {
		logger.info("Policy Service Implementation : getAllPolicy() method");
		return repo.findAll(); //to get the all list
	}

	@Override
	public Policy savePolicy(Policy policy) {
		logger.info("Policy Service Implementation : savePolicy() method");
		return repo.save(policy);
	}

	@Override
	public Policy getPolicyById(Integer Id) {
		logger.info("Policy Service Implementation : getPolicyById() method");
		Policy policy=repo.findById(Id);//null
		if(policy==null) { //6==null or null==null
			throw new PolicyTypeException("Policy id "+Id+" incorrect..");
		}
		return policy;
	}

	@Override
	public void deletePolicy(Integer id) {
		logger.info("Policy Service Implementation : deletePolicy() method");
		repo.deleteById(id);
	}

}
