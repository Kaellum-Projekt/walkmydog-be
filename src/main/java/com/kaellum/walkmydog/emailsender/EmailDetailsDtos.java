package com.kaellum.walkmydog.emailsender;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailDetailsDtos {
	
	private String userFullName;
	private String email;
	private String activationCode;

}
