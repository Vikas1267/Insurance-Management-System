package com.indla.insurance.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.indla.insurance.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Serializable> {

	public User findByUsername(String username);
	
	@Query(value="select * from user d where city=?1", nativeQuery =true)
	List<User> findByCity(String city);

}
