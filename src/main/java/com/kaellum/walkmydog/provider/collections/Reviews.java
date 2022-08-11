package com.kaellum.walkmydog.provider.collections;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reviews {
	
	private ObjectId id = new ObjectId();
	private Integer rate;
	private String reviewerName;
	private String reviewNote;

}
