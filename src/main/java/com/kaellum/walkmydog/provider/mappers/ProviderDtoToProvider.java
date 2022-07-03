package com.kaellum.walkmydog.provider.mappers;

import org.modelmapper.PropertyMap;

import com.kaellum.walkmydog.provider.collections.Provider;
import com.kaellum.walkmydog.provider.dtos.ProviderDto;

public class ProviderDtoToProvider extends PropertyMap<ProviderDto, Provider>{

	@Override
	protected void configure() {
		skip(destination.getCreatedBy());
		skip(destination.getCreatedDate());
		skip(destination.getLastModifiedBy());
		skip(destination.getLastModifiedDate());		
	}

}
