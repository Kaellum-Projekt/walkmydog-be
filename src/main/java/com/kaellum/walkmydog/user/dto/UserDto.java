package com.kaellum.walkmydog.user.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class UserDto {
	
	private String id;
	private String username;
	private String password;
	private String role;
	private String profileId;
	private LocalDateTime creationDate;
	private LocalDateTime lastUpdateDate;
}
