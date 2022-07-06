package com.kaellum.walkmydog.config;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.kaellum.walkmydog.authentication.services.TokenService;
import com.kaellum.walkmydog.user.services.impl.UserServiceImpl;

@Component
public class SecurityAuditorAware implements AuditorAware<String> {

	@Autowired
	private TokenService tokenService;
	
	@Override
	public Optional<String> getCurrentAuditor() {
		if(!StringUtils.isBlank(tokenService.getToken()))
			return Optional.of(tokenService.getToken());
		else
			return Optional.of(UserServiceImpl.USERNAME);
	}


}
