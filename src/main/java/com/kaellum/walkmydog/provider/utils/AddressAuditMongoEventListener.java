package com.kaellum.walkmydog.provider.utils;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;

import com.kaellum.walkmydog.authentication.services.TokenService;
import com.kaellum.walkmydog.provider.collections.Address;
import com.kaellum.walkmydog.user.services.impl.UserServiceImpl;

public class AddressAuditMongoEventListener extends AbstractMongoEventListener<Address>{
	
	@Autowired
	private MongoOperations mongoOperations;
	
	@Autowired
	private TokenService tokenService;
	
	@Override
	public void onBeforeSave(BeforeSaveEvent<Address> event) {
		Address address = event.getSource();
		
		if(StringUtils.isBlank(tokenService.getToken())) {
			address.setCreatedBy(UserServiceImpl.USERNAME);
			address.setCreatedDate(LocalDateTime.now());			
		}
		address.setLastModifiedDate(LocalDateTime.now());
		
		mongoOperations.save(address);	
	}

}
