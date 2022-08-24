package com.kaellum.walkmydog.user.dto.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum WeekDays {
	MONDAY("0"), TUESDAY("1"), WEDNESDAY("2"), THURSDAY("3"), FRIDAY("4"), SATURDAY("5"), SUNDAY("6");

	@JsonValue
	private final String dayCode;
}
