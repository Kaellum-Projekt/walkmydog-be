package com.kaellum.walkmydog.provider.collections;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
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
public class Address extends Audit<ObjectId>{
	
	@Id
	private ObjectId id = new ObjectId();
	private String street;
	private String street2;
	private String city;
	private String province;
	private String country;
	private String postalCode;
	
	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}
}
