package com.kaellum.walkmydog.authentication.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kaellum.walkmydog.authentication.collections.User;
import com.kaellum.walkmydog.authentication.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public boolean createNewUser(@RequestBody User user) {
		User response = userService.saveUser(user);
		return (response != null ? true : false);		
	}
	
	@GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		userService.refreshToken(request, response);
	}

}
