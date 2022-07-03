package com.kaellum.walkmydog.user.dto;

import com.kaellum.walkmydog.provider.dtos.ProviderDto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserProfileDto {
	
	private final UserDto userDto;
	private final ProviderDto providerDto;

}
