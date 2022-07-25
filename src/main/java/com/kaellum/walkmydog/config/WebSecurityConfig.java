package com.kaellum.walkmydog.config;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.kaellum.walkmydog.config.securityfilter.CustomAuthenticationFilter;
import com.kaellum.walkmydog.config.securityfilter.CustomAuthorizationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {
	
	private final UserDetailsService userDetailsService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(customAuthProvider());
    }
	
	@Override
    protected void configure(HttpSecurity http) throws Exception 
    {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeRequests().antMatchers("/api/auth/**","/api/user/signup", "/api/user/activation/**", 
        		"/api/user/new-activation/**", "/api/provider/all", "/api/user/forgot-password/**", "/api/user/**/reset-password").permitAll();
//        http.authorizeRequests().antMatchers(GET, "/api/provider/**","/api/user/**").hasAnyAuthority("ROLE_PROVIDER", "ROLE_CLIENT");
//        http.authorizeRequests().antMatchers(POST, "/api/provider/**","/api/user/**").hasAnyAuthority("ROLE_PROVIDER", "ROLE_CLIENT");
//        http.authorizeRequests().antMatchers(PUT, "/api/provider/**","/api/user/**").hasAnyAuthority("ROLE_PROVIDER", "ROLE_CLIENT");
//        http.authorizeRequests().antMatchers(DELETE, "/api/provider/**","/api/user/**").hasAnyAuthority("ROLE_PROVIDER", "ROLE_CLIENT");
        http.authorizeRequests().antMatchers("/api/provider/**").hasAnyAuthority("ROLE_PROVIDER");
        http.authorizeRequests().antMatchers("/api/user/**").hasAnyAuthority("ROLE_CLIENT");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);

    }
  
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    //https://stackoverflow.com/questions/36775663/how-to-load-a-custom-daoauthenticationprovider-into-the-spring-context
    @Bean
    public AuthenticationProvider customAuthProvider() throws Exception {
    	DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setHideUserNotFoundExceptions(false);
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }   
}
