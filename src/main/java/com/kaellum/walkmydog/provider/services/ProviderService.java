package com.kaellum.walkmydog.provider.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.kaellum.walkmydog.exception.WalkMyDogException;
import com.kaellum.walkmydog.provider.dtos.ProviderDto;

public interface ProviderService {
	
	ProviderDto addProvider (ProviderDto dto, String email);
	
	List<ProviderDto> getAllProviders (Pageable page);
	
	ProviderDto getProviderById (String id) throws WalkMyDogException;
	
	ProviderDto updateProvider (ProviderDto dto) throws WalkMyDogException;
	
	boolean deleteProvider (String id);
	
	List<ProviderDto> advancedSearch(
			Optional<String> firstName, 
			Optional<String> lastName,
			Optional<Double> price,
			Optional<List<Integer>> timeRange,
			Optional<String> province,
			String city,
			Pageable pageable) throws WalkMyDogException;

}
