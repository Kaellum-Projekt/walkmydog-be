package com.kaellum.walkmydog.config;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kaellum.walkmydog.authentication.services.TokenService;
import com.kaellum.walkmydog.provider.collections.repository.ProviderRepository;

@Component("userResolver")
public class SecurityUserResolver {
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private ProviderRepository providerRepository;
	
	public <T> boolean isOwner(final T id) {
	
		String userRecord = "";
		String userToken = tokenService.getToken();
		
		if(id instanceof String) {
			userRecord = providerRepository.findById((String)id).get().getUserId();
		}else{
			try {
				Method method = id.getClass().getMethod("getId");
				String idValue = (String) method.invoke(id);
				userRecord = providerRepository.findById(idValue).get().getUserId();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		return userRecord.equals(userToken);
	}

}
