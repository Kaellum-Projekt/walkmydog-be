package com.kaellum.walkmydog.user.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonPropertyOrder({"id"})
public class ProviderUserSimpleDto{
	
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private String id;
	private Set<AddressSimpleDto> addresses;
}
