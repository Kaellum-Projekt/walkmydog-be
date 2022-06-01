package com.kaellum.walkmydog.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum WalkMyDogExReasons {

    NONE("0"),
    RESOURCE_NOT_FOUND("1"),
    DUPLICATE_RESOURCE("2"),
    VALIDATION_FAIL("3"),
    RUNTIME_ERROR("R"),
    DATABASE_ERROR("D"),
    FILE_ERROR("F"),
    TIMEOUT_ERROR("T");
	
	@Getter
	private final String code;
}
