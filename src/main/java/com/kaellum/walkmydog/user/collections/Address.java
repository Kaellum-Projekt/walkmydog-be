package com.kaellum.walkmydog.user.collections;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
	
//	@Id
//	private ObjectId id = new ObjectId();
	private String street;
	private String street2;
	private String city;
	private String province;
	private String country;
	private String postalCode;
	private Double latitude;
	private Double longitude;
}
