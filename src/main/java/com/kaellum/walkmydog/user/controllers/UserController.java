package com.kaellum.walkmydog.user.controllers;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kaellum.walkmydog.user.dto.UserPasswordUpdate;
import com.kaellum.walkmydog.user.dto.UserProfileDto;
import com.kaellum.walkmydog.user.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.CREATED)
	public UserProfileDto createNewUser(@Valid @RequestBody UserProfileDto userProfile) {
		return userService.addNewUser(userProfile);	
	}
	
	@PutMapping("/update-password")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("@userResolverProvider.isOwner(#userPasswordUpdate)")
	public boolean passwordUpdate(@Valid @RequestBody UserPasswordUpdate userPasswordUpdate, @RequestParam String userId) {
		return userService.passwordUpdate(userPasswordUpdate, userId);	
	}
	
	@DeleteMapping("/deactivate")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("@userResolverProvider.isOwner(#userPasswordUpdate)")
	public boolean deactivateUser(@RequestParam String id) {
		return userService.deactivateUser(id);	
	}
	
	@PutMapping(value = "/activation/{email}/{activationCode}")
	@ResponseStatus(HttpStatus.OK)
	public boolean activateUser(@PathVariable final String email, @PathVariable final  String activationCode) {
		return userService.activateUser(email, activationCode);
	}


}
