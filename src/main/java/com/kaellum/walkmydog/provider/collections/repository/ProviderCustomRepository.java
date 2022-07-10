package com.kaellum.walkmydog.provider.collections.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.kaellum.walkmydog.exception.WalkMyDogException;
import com.kaellum.walkmydog.provider.collections.Provider;

public interface ProviderCustomRepository {
	
	List<Provider> findProviderByParams(
			Optional<String> firstName, 
			Optional<String> lastName,
			Optional<Double> price,
			Optional<List<Integer>> timeRange,
			Optional<String> province,
			String city,
			Pageable pageable) throws WalkMyDogException;

}
