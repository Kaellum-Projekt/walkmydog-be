package com.kaellum.walkmydog.config.securityfilter;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaellum.walkmydog.exception.WalkMyDogException;
import com.kaellum.walkmydog.exception.dto.WalkMyDogExceptionResponseDto;
import com.kaellum.walkmydog.exception.enums.WalkMyDogExApiTypes;
import com.kaellum.walkmydog.exception.enums.WalkMyDogExFrontendHandling;
import com.kaellum.walkmydog.exception.enums.WalkMyDogExReasons;
import com.kaellum.walkmydog.user.dto.UserDto;
import com.kaellum.walkmydog.user.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * @author Raphael Cremasco
 */
@Log4j2
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        log.info("Username is: {}", username); log.info("Password is: {}", password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
    	User user = (User)authentication.getPrincipal();
		UserDto userDto = userService.getUserByEmail(user.getUsername());
    	Algorithm algorithm = Algorithm.HMAC256(System.getenv("WMD_SECRET_KEY").getBytes());
        String access_token = JWT.create()
                .withSubject(user.getUsername())
                .withClaim("firstName" , userDto.getProviderDto().getFirstName())
                .withClaim("lastName" , userDto.getProviderDto().getLastName())
                .withClaim("id", userDto.getId())
                .withClaim("isVerified", userDto.getIsVerified())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        String refresh_token = JWT.create()
                .withSubject(user.getUsername())
                .withClaim("firstName" , userDto.getProviderDto().getFirstName())
                .withClaim("lastName" , userDto.getProviderDto().getLastName())
                .withClaim("id", userDto.getId())
                .withClaim("isVerified", userDto.getIsVerified())
                .withExpiresAt(new Date(System.currentTimeMillis() + 6000 * 60 * 1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        /*response.setHeader("access_token", access_token);
        response.setHeader("refresh_token", refresh_token);*/
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
    
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
    	response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        WalkMyDogException e = WalkMyDogException.buildWarningValidationFail(WalkMyDogExApiTypes.READ_API, failed.getMessage());        
        WalkMyDogExceptionResponseDto res = WalkMyDogExceptionResponseDto.builder()
		.code(e.getCode())
		.errorMessage(e.getErrorMessage())
		.systemErrorMessage(e.getSystemErrorMessage())
		.frontendHandling(e.getFrontendHandling() != null ? e.getFrontendHandling().getCode() : WalkMyDogExFrontendHandling.NONE.getCode())
		.exceptionReason(e.getExceptionReason() != null ? e.getExceptionReason().getCode() : WalkMyDogExReasons.NONE.getCode())
		.apiType(e.getApiType() != null ? e.getApiType().getCode() : WalkMyDogExApiTypes.NONE.getCode())
		.build();
        new ObjectMapper().writeValue(response.getOutputStream(), res);
    }
}
