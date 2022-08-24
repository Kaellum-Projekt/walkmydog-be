package com.kaellum.walkmydog.user.collections;


import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User extends Audit<String>{	
	@Id
	private String id;
	private String email;
	private String salt;
	private String password;
	private Boolean isVerified;
	private String userTempCode;
	private LocalDateTime deactivationDate;
	private Provider provider;
	
	
	@Override	
	public boolean isNew() {
		return id == null && getCreatedDate() == null;
	}
}
