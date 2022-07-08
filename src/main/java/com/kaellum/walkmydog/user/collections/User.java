package com.kaellum.walkmydog.user.collections;


import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.kaellum.walkmydog.provider.collections.Audit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User extends Audit{	
	@Id
	private String id;
	private String email;
	private String firstName;
	private String lastName;
	private String salt;
	private String passwordHash;
	private String role;
	private Boolean isVerified;
	private String verificationString;
	private String passwordResetCode;
	private String providerId;
	private LocalDateTime deactivationDate;
	
	@Override	
	public boolean isNew() {
		return id == null && getCreatedDate() == null;
	}
}
