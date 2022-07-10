package com.kaellum.walkmydog.provider.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kaellum.walkmydog.exception.WalkMyDogException;
import com.kaellum.walkmydog.provider.dtos.ProviderDto;
import com.kaellum.walkmydog.provider.services.ProviderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/provider")
@RequiredArgsConstructor
public class ProviderController {
	
	private final ProviderService providerService;
	
	@GetMapping("/all")
	@ResponseStatus(HttpStatus.OK)
	public List<ProviderDto> getAllProvider (@PageableDefault Pageable pageable){
		return providerService.getAllProviders(pageable);
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public ProviderDto getProviderById (@PathVariable String id) throws WalkMyDogException {
		return providerService.getProviderById(id);
	}
	
	@GetMapping("/")
	@ResponseStatus(HttpStatus.OK)
	public List<ProviderDto> advancedSearch (
			@RequestParam Optional<String> firstName, 
			@RequestParam Optional<String> lastName,
			@RequestParam Optional<Double> price,
			@RequestParam Optional<List<Integer>> timeRange,
			@RequestParam Optional<String> province,
			@RequestParam String city,
			@PageableDefault Pageable pageable ) throws WalkMyDogException {
		return null;//providerService.getProviderById(id);
	}
	
	@PostMapping("/add")
	@ResponseStatus(HttpStatus.OK)
	public ProviderDto addProvider (@RequestBody ProviderDto dto) throws WalkMyDogException {
		return providerService.addProvider(dto);
	}
	
	@PutMapping("/update")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("@userResolverProvider.isOwner(#dto)")
	public ProviderDto updateProvider (@RequestBody ProviderDto dto) throws WalkMyDogException {
		return providerService.updateProvider(dto);
	}
}
