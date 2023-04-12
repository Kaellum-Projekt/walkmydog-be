package com.kaellum.walkmydog.hazelcast.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class BoundariesDto implements Serializable{
	private static final long serialVersionUID = 4963234434619023985L;

	@JsonIgnore
	private String city;
	private Double maxLon;
	private Double minLon;
	private Double MaxLat;
	private Double MinLat;
	@JsonIgnore
	private String province;

}
