package com.kaellum.walkmydog.user.utils;

import java.lang.reflect.Method;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kaellum.walkmydog.authentication.services.TokenService;
import com.kaellum.walkmydog.user.repositories.UserRepository;

@Component("userResolverUser")
public class SecurityUserResolverUser {
	
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UserRepository userRepository;
	
	public <T> boolean isOwner(final T id) {
	
		String userRecord = "";
		String userToken = tokenService.getToken();
		
		if(id instanceof String) {
			userRecord = userRepository.findById((String)id).get().getCreatedBy();
		}else{
			try {
				Method method = id.getClass().getMethod("getId");
				String idValue = (String) method.invoke(id);
				userRecord = userRepository.findById(idValue).get().getCreatedBy();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		return userRecord.equals(userToken);
	}

}
