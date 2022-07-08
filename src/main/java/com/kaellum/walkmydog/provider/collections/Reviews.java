package com.kaellum.walkmydog.provider.collections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reviews {
	
	private Integer rate;
	private String reviewerName;
	private String reviewNote;

}
