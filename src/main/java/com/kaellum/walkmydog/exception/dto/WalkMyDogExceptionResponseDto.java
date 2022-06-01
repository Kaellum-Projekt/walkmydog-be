package com.kaellum.walkmydog.exception.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WalkMyDogExceptionResponseDto {
	
	private String code;
    private String errorMessage;
    private String systemErrorMessage;
    private String frontendHandling;
    private String exceptionReason;
    private String apiType;
}
