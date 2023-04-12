package com.kaellum.walkmydog.hazelcast.collection;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Boundaries implements Serializable {
	private static final long serialVersionUID = -7202622706067606213L;
	
	@Id
	private String id;
	private String city;
	private Double maxLon;
	private Double minLon;
	private Double MaxLat;
	private Double MinLat;
	private String province;
}
