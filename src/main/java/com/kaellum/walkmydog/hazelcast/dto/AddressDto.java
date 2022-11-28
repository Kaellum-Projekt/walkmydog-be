package com.kaellum.walkmydog.hazelcast.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class AddressDto implements Serializable{

	private static final long serialVersionUID = -8968282472788946326L;
	private String number;
	private String street;
	private String city;
	
	private Double latitude;
	private Double longitude;

}
