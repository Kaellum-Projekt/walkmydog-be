package com.kaellum.walkmydog.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonPropertyOrder({"id"})
public class ProviderUserIDDto extends ProviderDto{
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String id;
}
