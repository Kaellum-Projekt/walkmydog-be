package com.kaellum.walkmydog.authentication.services;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.auth0.jwt.algorithms.Algorithm;
import com.kaellum.walkmydog.user.dto.UserDto;

public interface TokenService extends UserDetailsService {
	void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
	String getAcessToken(UserDto userDto, String requestUrl, List<String> roles, boolean isRefreshToken);	
	String getToken();
}
