package com.indla.insurance.repository;

import java.io.Serializable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.indla.insurance.model.Address;
@Repository
public interface AddressRepository extends CrudRepository<Address, Serializable>{
	
}
