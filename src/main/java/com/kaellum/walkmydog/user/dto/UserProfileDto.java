package com.kaellum.walkmydog.user.dto;

import com.kaellum.walkmydog.walker.dtos.WalkerDto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserProfileDto {
	
	private final UserDto userDto;
	private final WalkerDto walkerDto;

}
