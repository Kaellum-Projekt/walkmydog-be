package com.kaellum.walkmydog.authentication.collections;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User{	
	@Id
	private String id;
	private String username;
	private String password;
	private String role;
}
