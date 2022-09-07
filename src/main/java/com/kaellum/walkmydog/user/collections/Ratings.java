package com.kaellum.walkmydog.user.collections;

import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ratings{
	
	private Integer avgRate;
	private Set<Reviews> reviews;


}
