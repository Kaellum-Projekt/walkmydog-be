package com.kaellum.walkmydog.walker.services;

import java.util.List;

import com.kaellum.walkmydog.exception.WalkMyDogException;
import com.kaellum.walkmydog.walker.dtos.WalkerDto;

public interface WalkerService {
	
	WalkerDto addWalker (WalkerDto dto);
	
	List<WalkerDto> getAllWalkers ();
	
	WalkerDto getWalkerById (String id) throws WalkMyDogException;
	
	WalkerDto updateWalker (WalkerDto dto) throws WalkMyDogException;
	
	boolean deleteWalker (String id);

}
