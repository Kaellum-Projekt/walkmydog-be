package com.kaellum.walkmydog.exception;

import com.kaellum.walkmydog.exception.enums.WalkMyDogExApiTypes;
import com.kaellum.walkmydog.exception.enums.WalkMyDogExFrontendHandling;
import com.kaellum.walkmydog.exception.enums.WalkMyDogExReasons;

import static com.kaellum.walkmydog.exception.enums.WalkMyDogExFrontendHandling.*;
import static com.kaellum.walkmydog.exception.enums.WalkMyDogExReasons.*;

import java.util.Arrays;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
@Data
@EqualsAndHashCode(callSuper = false)
public class WalkMyDogException extends RuntimeException{
	

	private static final long serialVersionUID = 1L;
	private String code;
    private String errorMessage;
    private String systemErrorMessage;
    private WalkMyDogExFrontendHandling frontendHandling;
    private @Getter WalkMyDogExReasons exceptionReason;
    private WalkMyDogExApiTypes apiType;
    private Throwable throwable;
    private Object additionalData;
    
    @Builder(builderMethodName = "builder")
    public WalkMyDogException(String code, String errorMessage, String systemErrorMessage, WalkMyDogExFrontendHandling frontendHandling,
			WalkMyDogExReasons exceptionReason, WalkMyDogExApiTypes apiType, Throwable throwable,
			Object additionalData) {
		super(errorMessage);
		this.errorMessage = errorMessage;
		this.systemErrorMessage = systemErrorMessage;
		this.frontendHandling = frontendHandling;
		this.exceptionReason = exceptionReason;
		this.apiType = apiType;
		this.throwable = throwable;
		this.additionalData = additionalData;
		this.code = createErrorCode();
	}
    

    
    public static WalkMyDogException buildWarningNotFound(WalkMyDogExApiTypes apiType, String systemErrorMessage) {

        return builder()
        		.frontendHandling(WARNING_INFO)
        		.exceptionReason(RESOURCE_NOT_FOUND)
        		.apiType(apiType)
        		.systemErrorMessage(systemErrorMessage)
        		.errorMessage(systemErrorMessage)
        		.build();
    }


    public static WalkMyDogException buildWarningDuplicate(WalkMyDogExApiTypes apiType, String systemErrorMessage) {

        return builder()
        		.frontendHandling(WARNING_INFO)
        		.exceptionReason(DUPLICATE_RESOURCE)
        		.apiType(apiType)
        		.systemErrorMessage(systemErrorMessage)
        		.errorMessage(systemErrorMessage)
        		.build();
    }


    public static WalkMyDogException buildWarningValidationFail(WalkMyDogExApiTypes apiType, String systemErrorMessage) {

        return builder()
        		.frontendHandling(WARNING_INFO)
        		.exceptionReason(VALIDATION_FAIL)
        		.apiType(apiType)
        		.systemErrorMessage(systemErrorMessage)
        		.errorMessage(systemErrorMessage)
        		.build();
    }


    public static WalkMyDogException buildCriticalRuntime(WalkMyDogExApiTypes apiType, String systemErrorMessage) {

        return builder()
        		.frontendHandling(CRITICAL_ALERT)
        		.exceptionReason(RUNTIME_ERROR)
        		.apiType(apiType)
        		.systemErrorMessage(systemErrorMessage)
        		.errorMessage(systemErrorMessage)
        		.build();
    }


    public static WalkMyDogException buildCriticalRuntime(WalkMyDogExApiTypes apiType, Throwable e) {

        return builder()
        		.frontendHandling(CRITICAL_ALERT)
        		.exceptionReason(findReason(e))
        		.apiType(apiType)
        		.throwable(e)
        		.errorMessage(e.getMessage())
        		.build();
    }



    public static WalkMyDogException buildCriticalRuntime(WalkMyDogExApiTypes apiType, Throwable e, String systemErrorMessage) {
 
        return builder()
        		.frontendHandling(CRITICAL_ALERT)
        		.exceptionReason(findReason(e))
        		.apiType(apiType)
        		.systemErrorMessage(systemErrorMessage)
        		.throwable(e)
        		.errorMessage(e.getMessage())
        		.build();
    }

    
    private static WalkMyDogExReasons findReason (Throwable e) {
    	WalkMyDogExReasons reason = RUNTIME_ERROR;
        if(e.getCause() != null && WalkMyDogException.class.isAssignableFrom(e.getCause().getClass()))
            reason = ((WalkMyDogException) e.getCause()).getExceptionReason();
        
        return reason;
    }
    
    private String createErrorCode() {
    	StackTraceElement[] errorTrace;
    	if(this.getThrowable() == null)
    		errorTrace = this.getStackTrace();
    	else
    		errorTrace = this.getThrowable().getStackTrace();
    	
    	return Arrays.stream(errorTrace)
    	.filter(x -> x.toString().contains("walkmydog") && !x.toString().contains("exception"))
    	.findFirst()
    	.map(x -> x.getFileName().split("\\.")[0].toUpperCase() + "-" + x.getLineNumber())
    	.get(); 
    }

    
}
