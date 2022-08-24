package com.kaellum.walkmydog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
	
	//Not ideal solution for auditing the document Address, but while I don't find a proper way to save that information this will do

//	 @Bean public AddressAuditMongoEventListener userCascadingMongoEventListener()
//	 { return new AddressAuditMongoEventListener(); }
	 
  
//	@Bean
//	CommandLineRunner run(UserService userService) {
//		return args -> {
//			userService.saveUser(new User(null, "raphael", "mmc1234", "ROLE_USER"));
//			userService.saveUser(new User(null, "jeferson", "abc123", "ROLE_USER"));
//		};
//	}

}
