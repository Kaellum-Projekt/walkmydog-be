package com.kaellum.walkmydog.user.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.kaellum.walkmydog.provider.collections.Provider;
import com.kaellum.walkmydog.user.collections.User;

@Repository
public interface UserRepository extends MongoRepository<User, String>{
	
	@Query("{ 'isVerified' : true, 'email' : ?0 }")
	Optional<User>  findUserByEmailAndVerified(String email);
	
	@Query("{ 'isVerified' : false, 'email' : ?0 }")
	Optional<User> findUserByEmailAndNotVerified(String email); 
	
	User findUserByEmail(String email);
	
	Optional<User> findByUserTempCode(String userTempCode);
	
	Optional<Provider> findByProviderId(String id);
	

}
