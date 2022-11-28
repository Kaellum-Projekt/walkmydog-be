package com.kaellum.walkmydog.user.controllers;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaellum.walkmydog.exception.ConflictWalkMyDogException;
import com.kaellum.walkmydog.exception.WalkMyDogException;
import com.kaellum.walkmydog.exception.enums.WalkMyDogExApiTypes;
import com.kaellum.walkmydog.exception.enums.WalkMyDogExReasons;
import com.kaellum.walkmydog.hazelcast.dto.AddressDto;
import com.kaellum.walkmydog.hazelcast.service.AddressCache;
import com.kaellum.walkmydog.user.dto.ProviderUserFullDto;
import com.kaellum.walkmydog.user.dto.UserDto;
import com.kaellum.walkmydog.user.dto.UserPasswordUpdate;
import com.kaellum.walkmydog.user.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	private final AddressCache addressCache;
	
	@PostMapping("/signup")
	@ResponseStatus(HttpStatus.CREATED)
	public void createNewUser(@Valid @RequestBody UserDto userDto, HttpServletRequest request, HttpServletResponse response) {
		try {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpStatus.CREATED.value());
            new ObjectMapper().writeValue(response.getOutputStream(), userService.addNewUser(userDto, request.getRequestURI()));	
		} catch (WalkMyDogException e) {
			if(e.getExceptionReason().equals(WalkMyDogExReasons.DUPLICATE_RESOURCE)) {
				throw new ConflictWalkMyDogException(e);
			}else {
				throw e;
			}
		} catch (Exception e) {
			throw WalkMyDogException.buildCriticalRuntime(WalkMyDogExApiTypes.CREATE_API, e, e.getMessage());
		} 
		
	}
	
	@PutMapping("/update")
	@ResponseStatus(HttpStatus.OK)
	@PreAuthorize("@userResolverUser.isOwner(#userDto)")
	public UserDto updateUser(@Valid @RequestBody UserDto userDto) {
		return userService.updateUser(userDto);		
	}
	
	@GetMapping("/{id}")
	@ResponseStatus(HttpStatus.OK)
	public UserDto getProviderById (@PathVariable String id) throws WalkMyDogException {
		return userService.getProviderById(id);
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
	
	//**** PROVIDER ENDPOINTS ****//
	@GetMapping("/provider")
	@ResponseStatus(HttpStatus.OK)
	public <T>List<T> advancedSearch (
			@RequestParam Optional<Double> priceMin,
			@RequestParam Optional<Double> priceMax,
			@RequestParam Optional<List<Integer>> timeRange,
			@RequestParam Optional<String> province,
			@RequestParam Optional<String> city,
			@RequestParam Optional<Double> minLat,
			@RequestParam Optional<Double> maxLat,
			@RequestParam Optional<Double> minLng,
			@RequestParam Optional<Double> maxLng,
			@RequestParam Optional<Boolean> isSimple,
			@PageableDefault Pageable pageable ) throws WalkMyDogException {
		return userService.advancedSearch(
				priceMin, priceMax, timeRange, province, city, minLat, maxLat, minLng, maxLng, isSimple, pageable);
	}
	
	@GetMapping("/address")
	@ResponseStatus(HttpStatus.OK)
	public List<AddressDto> searchAddresses(
			@RequestParam Optional<String> number,
			@RequestParam Optional<String> street,
			@RequestParam Optional<String> city,
			@RequestParam Optional<String> province){
		return addressCache.getAddressSearch(number, street, city, province);
		
	}
	
}
