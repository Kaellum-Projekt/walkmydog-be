package com.kaellum.walkmydog.provider.controllers;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	
	@GetMapping("/")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("@userResolverProvider.isOwner(#id)")
	public ProviderDto getProviderById (@RequestParam String id) throws WalkMyDogException {
		return providerService.getProviderById(id);
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
