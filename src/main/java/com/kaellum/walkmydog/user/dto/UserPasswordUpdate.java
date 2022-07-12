package com.kaellum.walkmydog.user.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordUpdate {
	
	@NotNull
	private String currentPassword;
	@NotNull
	private String newPassword;

}
