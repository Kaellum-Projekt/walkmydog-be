package com.kaellum.walkmydog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
//@EnableWebSecurity
public class WebSecurityConfigDeprecated extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {
	
	private final String basicAuthUsername;
	private final String basicAuthPassword;

    public WebSecurityConfigDeprecated(@Value("TEST") String basicAuthUsername, 
    		                 @Value("1234") String basicAuthPassword) {
		super();
		this.basicAuthUsername = basicAuthUsername;
		this.basicAuthPassword = basicAuthPassword;
	}

	@Override
    protected void configure(HttpSecurity http) throws Exception 
    {
        http
         .csrf().disable()
         .authorizeRequests().anyRequest().authenticated()
         .and()
         .httpBasic();
    }
  
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) 
            throws Exception 
    {
        auth.inMemoryAuthentication()
            .withUser(basicAuthUsername)
            .password("{noop}"+basicAuthPassword)
            .roles("USER");
    }

}
