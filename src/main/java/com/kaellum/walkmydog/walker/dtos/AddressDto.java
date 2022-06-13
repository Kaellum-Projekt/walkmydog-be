package com.kaellum.walkmydog.walker.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto{
	
	private String id;
	private String address;
	private String city;
	private String province;
	private String country;
	private String zipCode;
}
