package com.kaellum.walkmydog.authentication.services;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface TokenService {
	void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
