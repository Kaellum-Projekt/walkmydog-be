package com.kaellum.walkmydog.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum WalkMyDogExApiTypes {

    NONE("0"),
    CREATE_API("1"),
    READ_API("2"),
    UPDATE_API("3"),
    DELETE_API("4");
	
	@Getter
	private final String code;
}
