package com.kaellum.walkmydog.user.services;


import com.kaellum.walkmydog.exception.WalkMyDogException;
import com.kaellum.walkmydog.user.dto.UserPasswordUpdate;
import com.kaellum.walkmydog.user.dto.UserDto;


public interface UserService {
	UserDto addNewUser(UserDto userDto) throws WalkMyDogException;
    Boolean passwordUpdate(UserPasswordUpdate userPasswordUpdate, String userId) throws WalkMyDogException;
    Boolean deactivateUser (String id) throws WalkMyDogException;
	boolean activateUser(String email, String activationCode)  throws WalkMyDogException;
	boolean resendActivationCode(String email) throws WalkMyDogException;
	void resetForgotPassword(String email) throws WalkMyDogException;
	Boolean passwordReset(UserPasswordUpdate userPasswordUpdate, String code) throws WalkMyDogException;
	UserDto getUserByEmail(String email);
}
