package com.kaellum.walkmydog.user.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kaellum.walkmydog.provider.dtos.ProviderDto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserProfileDto {
	
	private String id;
	@NotNull @NotEmpty
	private String firstName;
	@NotNull @NotEmpty
	private String lastName;
	@NotNull @NotEmpty @Email
	private String email;
	@NotNull @NotEmpty
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String passwordHash;
	@NotNull @NotEmpty
	private String role;
	@JsonProperty(value = "profile")
	private ProviderDto providerDto;
	private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;


}
