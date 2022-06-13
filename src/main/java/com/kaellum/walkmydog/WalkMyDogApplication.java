package com.kaellum.walkmydog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;


@SpringBootApplication
@EnableMongoAuditing
public class WalkMyDogApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalkMyDogApplication.class, args);
	}
	
	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
//	@Bean
//	CommandLineRunner run(UserService userService) {
//		return args -> {
//			userService.saveUser(new User(null, "raphael", "mmc1234", "ROLE_USER"));
//			userService.saveUser(new User(null, "jeferson", "abc123", "ROLE_USER"));
//		};
//	}

}
