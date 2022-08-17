package com.kaellum.walkmydog.user.mappers;

import org.modelmapper.PropertyMap;

import com.kaellum.walkmydog.user.collections.User;
import com.kaellum.walkmydog.user.dto.UserDto;


public class UserDtoToUser extends PropertyMap<User, UserDto>{

	@Override
	protected void configure() {
		map(source.getProvider(), destination.getProviderDto());
	}

}
