package com.kaellum.walkmydog.user.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.kaellum.walkmydog.exception.WalkMyDogException;
import com.kaellum.walkmydog.exception.enums.WalkMyDogExApiTypes;
import com.kaellum.walkmydog.provider.dtos.ProviderDto;
import com.kaellum.walkmydog.provider.services.ProviderService;
import com.kaellum.walkmydog.user.collections.User;
import com.kaellum.walkmydog.user.dto.UserDto;
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
    //private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

	@Override
	public Boolean addNewUser(UserProfileDto user) throws WalkMyDogException {
		log.info("New User Dto {}", user);
		try {
			UserDto userDto = user.getUserDto();
			ProviderDto providerDto = user.getProviderDto();
			
			if(userDto == null || providerDto == null) {
				WalkMyDogException.buildWarningValidationFail(WalkMyDogExApiTypes.CREATE_API, 
						"User and Provider object must be provided");
			}
			
			User userDoc = modelMapper.map(userDto, User.class);
			
			ProviderDto walker = providerService.addProvider(providerDto);
			userDoc.setProfileId(walker.getId());
			userRepository.save(userDoc);		
			
		} catch (WalkMyDogException we) {
			log.error(we.getMessage(), we);
			throw we;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			WalkMyDogException.buildCriticalRuntime(WalkMyDogExApiTypes.CREATE_API, e, e.getMessage());
		}
		return true;
	}

	@Override
	public Boolean deleteUser(String id) throws WalkMyDogException {
		log.info("Delete User {}", id);
		try {
			User user = userRepository.findById(id).get();
			providerService.deleteProvider(user.getProfileId());
			userRepository.deleteById(id);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			WalkMyDogException.buildCriticalRuntime(WalkMyDogExApiTypes.CREATE_API, e, e.getMessage());
		}
		return true;
	}

	@Override
	public Boolean updateUser(UserDto userProfileDto) throws WalkMyDogException {
		log.info("New User Dto {}", userProfileDto);
		try {			
			if(userProfileDto == null){
				WalkMyDogException.buildWarningValidationFail(WalkMyDogExApiTypes.CREATE_API, 
						"User object should not be null");			
			}
			User userDoc = modelMapper.map(userProfileDto, User.class);			
			userRepository.save(userDoc);				
		} catch (WalkMyDogException we) {
			log.error(we.getMessage(), we);
			throw we;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			WalkMyDogException.buildCriticalRuntime(WalkMyDogExApiTypes.CREATE_API, e, e.getMessage());
		}
		return true;
	}
}
