package com.kaellum.walkmydog.user.dto;

import java.time.LocalDateTime;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UserDto {
	
	private String id;
	@NotNull @NotEmpty @Email
	private String email;
	@NotNull @NotEmpty
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	private Boolean isVerified;
	private LocalDateTime deactivationDate;
	@JsonProperty(value = "profile")
	@Valid
	private ProviderDto providerDto;
	private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;

}
