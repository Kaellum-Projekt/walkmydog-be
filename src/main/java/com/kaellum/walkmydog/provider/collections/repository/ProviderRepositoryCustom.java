package com.kaellum.walkmydog.provider.collections.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Query;

import com.kaellum.walkmydog.exception.WalkMyDogException;
import com.kaellum.walkmydog.provider.collections.Provider;

public interface ProviderRepositoryCustom {
	
	@Query
	List<Provider> findBy(
			Optional<String> firstName, 
			Optional<String> lastName,
			Optional<Double> price,
			Optional<List<Integer>> timeRange,
			Optional<String> province,
			String city,
			Pageable pageable) throws WalkMyDogException;
	
//	List<Provider> findBy(Optional<String> name, Optional<String> ln);

}
