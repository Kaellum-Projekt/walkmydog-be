package com.kaellum.walkmydog.exception;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public class ConflictWalkMyDogException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	@NonNull
	private WalkMyDogException walkMyDogException;

}
