package com.kaellum.walkmydog.walker.dtos.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TimeRange {
	SIX_TO_ELEVEN("0"), ELEVEN_TO_FIFTEEN("1"), FIFTEEN_TO_TWENTY_TWO("2");
	
	@JsonValue
	private final String timeRangeCode; 
}
