package com.kaellum.walkmydog.authentication.controllers;


import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaellum.walkmydog.user.collections.User;
import com.kaellum.walkmydog.user.controllers.UserController;
import com.kaellum.walkmydog.user.services.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
	
	@MockBean
	UserService userService;
	
	@MockBean
	UserDetailsService userDetailsService;
	
	@MockBean
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@MockBean
	MappingMongoConverter mappingMongoConverter;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@BeforeEach
	void setup () {
		Mockito.when(userService.saveUser(any(User.class))).thenReturn(new User());
	}
	
	@AfterEach
	void cleanUp () {
		Mockito.reset(userService);
	}
	
	@DisplayName("Success - Create new user")
	@Test
	void success_CreateNewUser () throws Exception {
		mockMvc.perform(post("/api/user")
				.content(objectMapper.writeValueAsString(new User()))
				.contentType(APPLICATION_JSON)
				.accept(APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(content().string("true"));
			
	}	

}
