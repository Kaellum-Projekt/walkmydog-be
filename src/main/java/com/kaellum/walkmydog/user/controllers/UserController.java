package com.kaellum.walkmydog.user.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kaellum.walkmydog.user.dto.UserDto;
import com.kaellum.walkmydog.user.dto.UserProfileDto;
import com.kaellum.walkmydog.user.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@PostMapping("/add")
	@ResponseStatus(HttpStatus.CREATED)
	public boolean createNewUser(@RequestBody UserProfileDto userProfile) {
		return userService.addNewUser(userProfile);	
	}
	
	@DeleteMapping("/del")
	@ResponseStatus(HttpStatus.OK)
	public boolean deleteUser(@RequestParam String id) {
		return userService.deleteUser(id);	
	}
	
	@PutMapping("/upd")
	@ResponseStatus(HttpStatus.OK)
	public boolean updatewUser(@RequestBody UserDto user) {
		return userService.updateUser(user);	
	}
	


}
