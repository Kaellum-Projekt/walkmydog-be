
package com.kaellum.walkmydog.provider.collections.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kaellum.walkmydog.provider.collections.Provider;

@Repository
public interface ProviderRepository extends MongoRepository<Provider, String>{
}