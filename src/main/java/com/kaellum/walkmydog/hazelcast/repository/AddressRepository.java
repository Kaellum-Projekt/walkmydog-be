package com.kaellum.walkmydog.hazelcast.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kaellum.walkmydog.hazelcast.collection.Addresses;


@Repository
public interface AddressRepository extends MongoRepository<Addresses, String>{

}
