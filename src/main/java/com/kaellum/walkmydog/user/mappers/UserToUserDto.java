package com.kaellum.walkmydog.user.mappers;

import org.modelmapper.PropertyMap;

import com.kaellum.walkmydog.user.collections.User;
import com.kaellum.walkmydog.user.dto.UserDto;


public class UserToUserDto extends PropertyMap<UserDto, User>{

	@Override
	protected void configure() {
		map(source.getProviderDto(), destination.getProvider());		
	}

}
