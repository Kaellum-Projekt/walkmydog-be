package com.kaellum.walkmydog.walker.mappers;

import org.modelmapper.PropertyMap;

import com.kaellum.walkmydog.walker.collections.Walker;
import com.kaellum.walkmydog.walker.dtos.WalkerDto;

public class WalkerDtoToWalker extends PropertyMap<WalkerDto, Walker>{

	@Override
	protected void configure() {
		skip(destination.getCreatedBy());
		skip(destination.getCreatedDate());
		skip(destination.getLastModifiedBy());
		skip(destination.getLastModifiedDate());		
	}

}
