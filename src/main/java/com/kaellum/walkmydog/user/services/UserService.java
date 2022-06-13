package com.kaellum.walkmydog.user.services;


import com.kaellum.walkmydog.exception.WalkMyDogException;
import com.kaellum.walkmydog.user.dto.UserDto;
import com.kaellum.walkmydog.user.dto.UserProfileDto;


public interface UserService {
    Boolean addNewUser(UserProfileDto userProfileDto) throws WalkMyDogException;
    Boolean deleteUser (String id) throws WalkMyDogException;
	Boolean updateUser(UserDto userProfileDto) throws WalkMyDogException;    
}
