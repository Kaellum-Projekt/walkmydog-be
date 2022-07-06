package com.kaellum.walkmydog.user.services.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kaellum.walkmydog.exception.WalkMyDogException;
import com.kaellum.walkmydog.exception.enums.WalkMyDogExApiTypes;
import com.kaellum.walkmydog.provider.dtos.ProviderDto;
import com.kaellum.walkmydog.provider.services.ProviderService;
import com.kaellum.walkmydog.user.collections.User;
import com.kaellum.walkmydog.user.dto.UserPasswordUpdate;
import com.kaellum.walkmydog.user.dto.UserProfileDto;
import com.kaellum.walkmydog.user.repositories.UserRepository;
import com.kaellum.walkmydog.user.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service 
@RequiredArgsConstructor 
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ProviderService providerService;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    
    public static String USERNAME;

	@Override
	public UserProfileDto addNewUser(UserProfileDto user) throws WalkMyDogException {
		log.info("New User Dto {}", user);
		UserProfileDto dtoReturn = null;
		try {
			if(user == null)
				WalkMyDogException.buildWarningValidationFail(WalkMyDogExApiTypes.CREATE_API, 
						"User object must be provided");

			User userDoc = modelMapper.map(user, User.class);
			String password = passwordEncoder.encode(user.getPassword());
			userDoc.setPassword(password);
			USERNAME = user.getEmail();
			
			ProviderDto provider = null;
			if(user.getRole().equals("ROLE_PROVIDER")) {
				if(user.getProviderDto() == null)
					WalkMyDogException.buildWarningValidationFail(WalkMyDogExApiTypes.CREATE_API, 
							"Provider object must be provided");
				ProviderDto providerDto = user.getProviderDto();
				providerDto.setEmail(user.getEmail());
				provider = providerService.addProvider(user.getProviderDto());
				userDoc.setProviderId(provider.getId());
			}			
			
			userRepository.save(userDoc);		
			
			dtoReturn = modelMapper.map(userDoc, UserProfileDto.class);
			dtoReturn.setProviderDto(provider);
			
		} catch (WalkMyDogException we) {
			log.error(we.getMessage(), we);
			throw we;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			WalkMyDogException.buildCriticalRuntime(WalkMyDogExApiTypes.CREATE_API, e, e.getMessage());
		}
		return dtoReturn;
	}

	@Override
	public Boolean deactivateUser(String id) throws WalkMyDogException {
		log.info("Delete User {}", id);
		try {
			Optional<User> userOpt = userRepository.findById(id);
			
			if(userOpt.isEmpty()) 
				WalkMyDogException.buildWarningNotFound(WalkMyDogExApiTypes.UPDATE_API, "User id does not exist");

			User user = userOpt.get();
			user.setDeactivationDate(LocalDateTime.now());
			if(userRepository.save(user) != null)
				return true;		
		} catch (WalkMyDogException we) {
			log.error(we.getMessage(), we);
			throw we;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			WalkMyDogException.buildCriticalRuntime(WalkMyDogExApiTypes.CREATE_API, e, e.getMessage());
		}
		return false;
	}

	@Override
	public Boolean passwordUpdate(UserPasswordUpdate userPasswordUpdate, String userId) throws WalkMyDogException {
		try {
			if(StringUtils.isBlank(userPasswordUpdate.getCurrentPassword()) ||
					StringUtils.isBlank(userPasswordUpdate.getNewPassword()) ||
					StringUtils.isBlank(userId))
				WalkMyDogException.buildWarningValidationFail(WalkMyDogExApiTypes.UPDATE_API, "All three parameters are mandatory");
			
			Optional<User> userOpt = userRepository.findById(userId);
			
			if(userOpt.isEmpty()) 
				WalkMyDogException.buildWarningNotFound(WalkMyDogExApiTypes.UPDATE_API, "User id does not exist");

			User user = userOpt.get();
			
			//Validates current password
			String passwordProvided = passwordEncoder.encode(userPasswordUpdate.getCurrentPassword());
			if(!passwordProvided.equals(user.getPassword()))
				WalkMyDogException.buildWarningValidationFail(WalkMyDogExApiTypes.UPDATE_API, 
						"Current Password is not Valid.<br>Please, Try it again!");
			
			String newPassword = passwordEncoder.encode(userPasswordUpdate.getNewPassword());
			user.setPassword(newPassword);			
			if(userRepository.save(user) != null)
				return true;
		} catch (WalkMyDogException we) {
			log.error(we.getMessage(), we);
			throw we;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			WalkMyDogException.buildCriticalRuntime(WalkMyDogExApiTypes.CREATE_API, e, e.getMessage());
		}
		return false;
	}
}
