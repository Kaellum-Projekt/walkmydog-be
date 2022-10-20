package com.kaellum.walkmydog.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AddressDto extends AddressSimpleDto{
	
	//private String id;
	private String street;
	private String street2;
	private String city;
	private String province;
	private String country;
	private String postalCode;
}
