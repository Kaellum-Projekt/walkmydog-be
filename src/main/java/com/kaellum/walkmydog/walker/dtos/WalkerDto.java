package com.kaellum.walkmydog.walker.dtos;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

//import javax.validation.constraints.Email;
//import javax.validation.constraints.NotNull;

import com.kaellum.walkmydog.walker.dtos.enums.TimeRange;
import com.kaellum.walkmydog.walker.dtos.enums.WeekDays;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalkerDto {
	
	private String id;
	@NotNull
	private String firstName;
	@NotNull
	private String lastName;
	@NotNull
	private LocalDate dob;
	@Email @NotNull
	private String email;
	@NotNull
	private String phone;
	private Double price;
	private Set<WeekDays> weekDays;
	private Set<TimeRange> timeRange;
	@NotNull
	private Set<AddressDto> addresses;

}
