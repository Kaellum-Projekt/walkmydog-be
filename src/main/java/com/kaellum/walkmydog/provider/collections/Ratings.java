package com.kaellum.walkmydog.provider.collections;

import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Ratings extends Audit<String>{
	
	@Id
	private String id;
	private Integer avgRate;
	private Set<Reviews> reviews;
	private String profileId;
	
	@Override
	public boolean isNew() {
		return id == null && getCreatedDate() == null;
	}

}
