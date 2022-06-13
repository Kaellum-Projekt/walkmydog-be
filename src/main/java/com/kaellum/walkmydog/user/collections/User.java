package com.kaellum.walkmydog.user.collections;


import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
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
	private String profileId;
	@CreatedDate
	private LocalDateTime creationDate;
	@LastModifiedDate
	private LocalDateTime lastUpdateDate;
	private LocalDateTime deactivationDate;
}
