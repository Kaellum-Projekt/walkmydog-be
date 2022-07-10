package com.kaellum.walkmydog.provider.collections.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.kaellum.walkmydog.provider.collections.Provider;

@Repository
public interface ProviderRepository extends MongoRepository<Provider, String>{//, ProviderCustomRepository{
	
	@Query("{ 'deactivationDate' : null }")
	Page<Provider> findAllActiveProviders(Pageable pageable);
}