package com.kaellum.walkmydog.hazelcast.dto;

import java.io.Serializable;
import java.util.Comparator;

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
public class AddressDto implements Serializable, Comparable<AddressDto>{

	private static final long serialVersionUID = -8968282472788946326L;
	private String number;
	private String street;
	private String city;
	private String province;
	
	private Double latitude;
	private Double longitude;
		
	@Override
	public int compareTo(AddressDto o) {
		return Comparator.comparing(AddressDto::getLatitude)
	              .thenComparing(AddressDto::getLongitude)
	              .compare(this, o);
	}

}
