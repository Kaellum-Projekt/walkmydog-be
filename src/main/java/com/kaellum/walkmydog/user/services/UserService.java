package com.kaellum.walkmydog.user.services;


import com.kaellum.walkmydog.exception.WalkMyDogException;
import com.kaellum.walkmydog.user.dto.UserPasswordUpdate;
import com.kaellum.walkmydog.user.dto.UserProfileDto;


public interface UserService {
	UserProfileDto addNewUser(UserProfileDto userProfileDto) throws WalkMyDogException;
    Boolean passwordUpdate(UserPasswordUpdate userPasswordUpdate, String userId) throws WalkMyDogException;
    Boolean deactivateUser (String id) throws WalkMyDogException;
	boolean activateUser(String email, String activationCode)  throws WalkMyDogException;   
}
