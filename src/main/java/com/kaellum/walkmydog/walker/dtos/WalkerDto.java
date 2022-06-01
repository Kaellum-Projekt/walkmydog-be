package com.kaellum.walkmydog.walker.dtos;

import java.time.LocalDate;
import java.util.Set;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalkerDto {
	
	private String id;
	private String firstName;
	private String lastName;
	private LocalDate dob;
	private String username;
	private String email;
	private Set<AddressDto> addresses;

}
