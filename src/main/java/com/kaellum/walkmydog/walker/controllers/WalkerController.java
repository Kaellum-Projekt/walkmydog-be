package com.kaellum.walkmydog.walker.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kaellum.walkmydog.exception.WalkMyDogException;
import com.kaellum.walkmydog.walker.dtos.WalkerDto;
import com.kaellum.walkmydog.walker.services.WalkerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/walker")
@RequiredArgsConstructor
public class WalkerController {
	
	private final WalkerService walkerService;
	
	@GetMapping("/")
	@ResponseStatus(HttpStatus.OK)
	public List<WalkerDto> getAllWalkers (){
		return walkerService.getAllWalkers();
	}
	
	@GetMapping("/getwalker")
	@ResponseStatus(HttpStatus.OK)
	public WalkerDto getWalkerById (@RequestParam String id) throws WalkMyDogException {
		return walkerService.getWalkerById(id);
	}
	
	@PostMapping("/add")
	@ResponseStatus(HttpStatus.CREATED)
	public WalkerDto addWalker (@RequestBody WalkerDto dto){		
		return walkerService.addWalker(dto);		
	}
	
	
	@PutMapping("/alter")
	@ResponseStatus(HttpStatus.OK)
	public WalkerDto updateWalker (@RequestBody WalkerDto dto) throws WalkMyDogException {
		return walkerService.updateWalker(dto);
	}
	
	@DeleteMapping
	@ResponseStatus(HttpStatus.OK)
	public boolean deleteWalker (@RequestParam String id) {
		return walkerService.deleteWalker(id);
	}

}
