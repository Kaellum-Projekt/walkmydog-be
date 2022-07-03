package com.kaellum.walkmydog.provider.collections;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
public class Address{
	
	@Id
	private String id;
	private String address;
	private String city;
	private String province;
	private String zipCode;
}
