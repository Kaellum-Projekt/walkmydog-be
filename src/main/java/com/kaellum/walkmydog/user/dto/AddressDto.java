package com.kaellum.walkmydog.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto{
	
	//private String id;
	private String street;
	private String street2;
	private String city;
	private String province;
	private String country;
	private String postalCode;
	private String latitude;
	private String longitude;
}
