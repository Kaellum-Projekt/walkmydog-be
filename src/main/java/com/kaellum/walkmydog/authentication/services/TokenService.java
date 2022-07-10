package com.kaellum.walkmydog.authentication.services;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface TokenService extends UserDetailsService {
	void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
	
	String getToken();
}
