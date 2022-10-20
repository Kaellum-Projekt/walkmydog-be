package com.kaellum.walkmydog.user.services;


import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Pageable;

import com.kaellum.walkmydog.exception.WalkMyDogException;
import com.kaellum.walkmydog.user.dto.UserDto;
import com.kaellum.walkmydog.user.dto.UserPasswordUpdate;


public interface UserService {
	Map<String, String>  addNewUser(UserDto userDto, String httpRequestUrl) throws WalkMyDogException;
	UserDto updateUser (UserDto userDto) throws WalkMyDogException;
    Boolean passwordUpdate(UserPasswordUpdate userPasswordUpdate, String userId) throws WalkMyDogException;
    Boolean deactivateUser (String id) throws WalkMyDogException;
	boolean activateUser(String email, String activationCode)  throws WalkMyDogException;
	boolean resendActivationCode(String email) throws WalkMyDogException;
	void resetForgotPassword(String email) throws WalkMyDogException;
	Boolean passwordReset(UserPasswordUpdate userPasswordUpdate, String code) throws WalkMyDogException;
	UserDto getUserByEmail(String email);
	UserDto getProviderById (String id) throws WalkMyDogException;
	<T>List<T> advancedSearch(
			Optional<Double> priceMin,
			Optional<Double> priceMax,
			Optional<List<Integer>> timeRange,
			Optional<String> province,
			Optional<String> city,
			Optional<Double> minLat, 
			Optional<Double> maxLat, 
			Optional<Double> minLng, 
			Optional<Double> maxLng, 
			Optional<Boolean> isSimple,
			Pageable pageable) throws WalkMyDogException;
}
