package com.kaellum.walkmydog.user.dto;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.kaellum.walkmydog.user.collections.Ratings;
import com.kaellum.walkmydog.user.dto.enums.TimeRange;
import com.kaellum.walkmydog.user.dto.enums.WeekDays;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
//@CustomObjectValidation(
//        conditionalProperty = "role", values = {"ROLE_PROVIDER"},
//        requiredProperties = {"phone", "price", "days", "hours", "geoLocation", "addresses"})
public class ProviderDto {
	
	//private String id;
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
}
