package com.kaellum.walkmydog.exception.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum WalkMyDogExFrontendHandling {

    NONE("0"),
    RETRY_REQUEST("1"),
    REQUEST_ADDITIONAL_INFO("2"),
    WARNING_INFO("3"),
    CRITICAL_ALERT("4");

    @Getter 
    private final String code;
}
