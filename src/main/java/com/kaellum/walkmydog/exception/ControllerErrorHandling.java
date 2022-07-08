package com.kaellum.walkmydog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.kaellum.walkmydog.exception.dto.WalkMyDogExceptionResponseDto;
import com.kaellum.walkmydog.exception.enums.WalkMyDogExApiTypes;
import com.kaellum.walkmydog.exception.enums.WalkMyDogExFrontendHandling;
import com.kaellum.walkmydog.exception.enums.WalkMyDogExReasons;


@RestControllerAdvice
public class ControllerErrorHandling {
	
	@ExceptionHandler(WalkMyDogException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public WalkMyDogExceptionResponseDto controllerErrorHandling (WalkMyDogException e) {
		return WalkMyDogExceptionResponseDto.builder()
				.code(e.getCode())
				.errorMessage(e.getErrorMessage())
				.systemErrorMessage(e.getSystemErrorMessage())
				.frontendHandling(e.getFrontendHandling() != null ? e.getFrontendHandling().getCode() : WalkMyDogExFrontendHandling.NONE.getCode())
				.exceptionReason(e.getExceptionReason() != null ? e.getExceptionReason().getCode() : WalkMyDogExReasons.NONE.getCode())
				.apiType(e.getApiType() != null ? e.getApiType().getCode() : WalkMyDogExApiTypes.NONE.getCode())
				.build();
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public WalkMyDogExceptionResponseDto handleValidationExceptions(MethodArgumentNotValidException ex) {
	    StringBuilder strBuilder = new StringBuilder();
	    strBuilder.append("The following field(s) must be valid -> ");
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        strBuilder.append(((FieldError) error).getField() + ": " +  error.getDefaultMessage() + " | ");
	    });	    
	    return WalkMyDogExceptionResponseDto.builder()

				.errorMessage(null)
				.systemErrorMessage(strBuilder.toString())
				.frontendHandling(WalkMyDogExFrontendHandling.NONE.getCode())
				.exceptionReason(WalkMyDogExReasons.NONE.getCode())
				.apiType(WalkMyDogExApiTypes.NONE.getCode())
				.build();
	}
}
