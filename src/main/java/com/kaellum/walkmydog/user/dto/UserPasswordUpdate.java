package com.kaellum.walkmydog.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserPasswordUpdate {
	
	private String currentPassword;
	private String newPassword;

}
