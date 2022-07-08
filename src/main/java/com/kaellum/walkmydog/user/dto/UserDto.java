package com.kaellum.walkmydog.user.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UserDto {
	
	private String id;
	@NotNull
	private String email;
	@NotNull
	private String passwordHash;
	@NotNull
	private String role;
	private String profileId;
	private LocalDateTime creationDate;
	private LocalDateTime lastUpdateDate;
}
