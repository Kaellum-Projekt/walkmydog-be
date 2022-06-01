
package com.kaellum.walkmydog.walker.collections.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kaellum.walkmydog.walker.collections.Walker;

@Repository
public interface WalkerRepository extends MongoRepository<Walker, String>{
}