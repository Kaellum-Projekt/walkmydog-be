package com.kaellum.walkmydog.provider.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.kaellum.walkmydog.provider.collections.Ratings;
import com.kaellum.walkmydog.provider.dtos.enums.TimeRange;
import com.kaellum.walkmydog.provider.dtos.enums.WeekDays;
import com.kaellum.walkmydog.user.utils.CustomObjectValidation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@CustomObjectValidation(
        conditionalProperty = "role", values = {"ROLE_PROVIDER"},
        requiredProperties = {"phone", "price", "days", "hours", "geoLocation", "addresses"})
public class ProviderDto {
	
	private String id;
	@NotNull @NotEmpty
	private String firstName;
	@NotNull @NotEmpty
	private String lastName;
	private LocalDate dob;
	private String phone;
	private Double price;
	private Set<WeekDays> days;
	private Set<TimeRange> hours;
	private String geoLocation;
	private Set<AddressDto> addresses;
	private Ratings ratings;
	@NotNull @NotEmpty
	private String role;
	private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;
}
