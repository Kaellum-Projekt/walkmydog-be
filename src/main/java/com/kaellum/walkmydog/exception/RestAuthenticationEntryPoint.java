package com.kaellum.walkmydog.exception;


import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaellum.walkmydog.exception.dto.WalkMyDogExceptionResponseDto;
import com.kaellum.walkmydog.exception.enums.WalkMyDogExApiTypes;
import com.kaellum.walkmydog.exception.enums.WalkMyDogExFrontendHandling;
import com.kaellum.walkmydog.exception.enums.WalkMyDogExReasons;

//https://codehunter.cc/a/spring/errorhandling-in-spring-security-for-preauthorize-accessdeniedexception-returns-500-rather-then-401
@Component
@ControllerAdvice
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {  
	
	@Override  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException ) throws IOException, ServletException {        
		response.setContentType(APPLICATION_JSON_VALUE);    
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);    
		response.getOutputStream().println("{ \"error\": \"" + authException.getMessage() + "\" }");  
		}  
	
	@ExceptionHandler(value = { AccessDeniedException.class })  
	public void commence(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex ) throws IOException {    
		response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        WalkMyDogException e = WalkMyDogException.buildWarningValidationFail(WalkMyDogExApiTypes.READ_API, "User not allowed to change this information");        
        WalkMyDogExceptionResponseDto res = WalkMyDogExceptionResponseDto.builder()
		.code(e.getCode())
		.errorMessage(e.getErrorMessage())
		.systemErrorMessage(e.getSystemErrorMessage())
		.frontendHandling(e.getFrontendHandling() != null ? e.getFrontendHandling().getCode() : WalkMyDogExFrontendHandling.NONE.getCode())
		.exceptionReason(e.getExceptionReason() != null ? e.getExceptionReason().getCode() : WalkMyDogExReasons.NONE.getCode())
		.apiType(e.getApiType() != null ? e.getApiType().getCode() : WalkMyDogExApiTypes.NONE.getCode())
		.build();
        new ObjectMapper().writeValue(response.getOutputStream(), res);  
		}
	}
