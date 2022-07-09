package com.kaellum.walkmydog.exception;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.kaellum.walkmydog.exception.dto.WalkMyDogExceptionResponseDto;
import com.kaellum.walkmydog.exception.enums.WalkMyDogExApiTypes;
import com.kaellum.walkmydog.exception.enums.WalkMyDogExFrontendHandling;
import com.kaellum.walkmydog.exception.enums.WalkMyDogExReasons;


@RestControllerAdvice
public class ControllerErrorHandling implements AuthenticationEntryPoint{
	
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
	
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public WalkMyDogExceptionResponseDto handleValidationExceptions2(Exception e, WebRequest request) {
		return WalkMyDogExceptionResponseDto.builder()
				.code(null)
				.errorMessage(e.getMessage())
				.systemErrorMessage(e.getMessage())
				.frontendHandling(WalkMyDogExFrontendHandling.NONE.getCode())
				.exceptionReason(WalkMyDogExReasons.NONE.getCode())
				.apiType(WalkMyDogExApiTypes.NONE.getCode())
				.build();
	}

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		response.setContentType("application/json");
	    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	    response.getOutputStream().println("{ \"error\": \"" + authException.getMessage() + "\" }");
		
	}
}
