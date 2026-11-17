package com.indla.insurance.repository;

import java.io.Serializable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.indla.insurance.model.Policy;
@Repository
public interface PolicyRepository extends CrudRepository<Policy, Serializable>{
	
	public Policy findById(Integer Id);

}
