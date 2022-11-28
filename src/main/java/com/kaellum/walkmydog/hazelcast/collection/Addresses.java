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
public class Addresses implements Serializable {
    private static final long serialVersionUID = -3408701100386534973L;
	
	@Id
	private String id;
	private Double lon;
	private Double lat;
	private String number;
	private String street;
	private String city;

}
