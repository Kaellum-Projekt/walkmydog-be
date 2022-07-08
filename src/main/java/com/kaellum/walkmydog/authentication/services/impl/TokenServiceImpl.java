package com.kaellum.walkmydog.authentication.services.impl;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kaellum.walkmydog.authentication.services.TokenService;
import com.kaellum.walkmydog.exception.WalkMyDogException;
import com.kaellum.walkmydog.exception.enums.WalkMyDogExApiTypes;
import com.kaellum.walkmydog.user.collections.User;
import com.kaellum.walkmydog.user.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service 
@RequiredArgsConstructor 
@Log4j2
@Transactional
public class TokenServiceImpl implements TokenService, UserDetailsService {
    private final UserRepository userRepository;

	@Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException, WalkMyDogException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256(System.getenv("WMD_SECRET_KEY").getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                User user = userRepository.findUserByEmailAndVerified(username);
                String access_token = JWT.create()
                        .withSubject(user.getEmail())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withArrayClaim("roles", new String[]{user.getRole()})
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setStatus(HttpStatus.OK.value());
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }catch (Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, WalkMyDogException> error = new HashMap<>();
                error.put("error_message", WalkMyDogException.buildCriticalRuntime(WalkMyDogExApiTypes.READ_API, e));
                response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
        	response.setHeader("error", "Missing Refresh Token");
            response.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
            Map<String, WalkMyDogException> error = new HashMap<>();
            error.put("error_message", WalkMyDogException.buildCriticalRuntime(WalkMyDogExApiTypes.READ_API, "Missing Refresh Token"));
            response.setContentType(MimeTypeUtils.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), error);
        }
    }
	
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmailAndVerified(username);
        if(user == null) {
            log.error("User {} not found in the database", username);
          throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", username);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(user.getRole()));
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPasswordHash(), authorities);
        }
    }
    
    @Override
    public String getToken() {
    	String token = "";
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    	if (!auth.getPrincipal().equals("anonymousUser"))
    		token = ((UsernamePasswordAuthenticationToken) auth).getPrincipal().toString();
    
    	return token;
    }
}
