package com.kaellum.walkmydog.user.controllers;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kaellum.walkmydog.exception.ConflictWalkMyDogException;
import com.kaellum.walkmydog.exception.WalkMyDogException;
import com.kaellum.walkmydog.exception.enums.WalkMyDogExReasons;
import com.kaellum.walkmydog.user.dto.UserDto;
import com.kaellum.walkmydog.user.dto.UserPasswordUpdate;
import com.kaellum.walkmydog.user.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.CREATED)
	public UserDto createNewUser(@Valid @RequestBody UserDto userDto) {
		try {
			return userService.addNewUser(userDto);	
		} catch (WalkMyDogException e) {
			if(e.getExceptionReason().equals(WalkMyDogExReasons.DUPLICATE_RESOURCE)) {
				throw new ConflictWalkMyDogException(e);
			}else {
				throw e;
			}
		}
		
	}
	
	@PutMapping("/update")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("@userResolverUser.isOwner(#userDto)")
	public UserDto updateUser(@Valid @RequestBody UserDto userDto) {
		return userService.updateUser(userDto);		
	}
	
	@PutMapping("/update-password")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("@userResolverUser.isOwner(#userId)")
	public boolean passwordUpdate(@Valid @RequestBody UserPasswordUpdate userPasswordUpdate, @RequestParam String userId) {
		return userService.passwordUpdate(userPasswordUpdate, userId);	
	}
	
	@DeleteMapping("/deactivate")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("@userResolverUser.isOwner(#userId)")
	public boolean deactivateUser(@RequestParam String userId) {
		return userService.deactivateUser(userId);	
	}
	
	@GetMapping(value = "/activation/{email}/{activationCode}")
	@ResponseStatus(HttpStatus.OK)
	public boolean activateUser(@PathVariable final String email, @PathVariable final  String activationCode) {
		return userService.activateUser(email, activationCode);
	}
	
	@PutMapping(value = "/new-activation/{email}")
	@ResponseStatus(HttpStatus.OK)
	public boolean resendActivationCode(@PathVariable final String email) {
		return userService.resendActivationCode(email);
	}
	
	@PutMapping(value = "/forgot-password/{email}")
	@ResponseStatus(HttpStatus.OK)
	public void resetForgotPassword(@PathVariable final String email) {
		userService.resetForgotPassword(email);
	}
	
	@PutMapping(value = "/{passwordResetCode}/reset-password")
	@ResponseStatus(HttpStatus.OK)
	public void passwordReset(@Valid @RequestBody UserPasswordUpdate userPasswordUpdate, @PathVariable String passwordResetCode) {
		userService.passwordReset(userPasswordUpdate, passwordResetCode);
	}


}
