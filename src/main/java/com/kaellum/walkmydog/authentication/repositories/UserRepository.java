package com.kaellum.walkmydog.authentication.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kaellum.walkmydog.authentication.collections.User;

@Repository
public interface UserRepository extends MongoRepository<User, String>{
	
	User findByUsername(String username);

}
