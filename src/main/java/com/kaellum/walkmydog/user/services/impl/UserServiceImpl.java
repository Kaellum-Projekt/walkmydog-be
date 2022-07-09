package com.kaellum.walkmydog.user.services.impl;

import static com.kaellum.walkmydog.exception.enums.WalkMyDogExApiTypes.CREATE_API;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Example;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kaellum.walkmydog.emailsender.EmailDetailsDtos;
import com.kaellum.walkmydog.emailsender.EmailSenderEventPublisher;
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
    private final EmailSenderEventPublisher emailSenderEventPublisher;
    
    public static String USERNAME;

	@Override
	public UserProfileDto addNewUser(UserProfileDto user) throws WalkMyDogException {
		log.info("New User Dto {}", user);
		UserProfileDto dtoReturn = null;
		try {
			if(user == null)
				WalkMyDogException.buildWarningValidationFail(WalkMyDogExApiTypes.CREATE_API, 
						"User object must be provided");
			
			User example = new User();
			example.setEmail(user.getEmail());			
			if(userRepository.findOne(Example.of(example)).isPresent())
				throw WalkMyDogException.buildWarningDuplicate(CREATE_API, "The email address is already in use");

			User userDoc = modelMapper.map(user, User.class);
			String password = passwordEncoder.encode(user.getPasswordHash());
			userDoc.setPasswordHash(password);
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
			
			//First creates a random activation string and sets the user as non-activated
			String activationCode = generateActivationCode();
			userDoc.setVerificationString(activationCode);
			userDoc.setIsVerified(false);
			
			userRepository.save(userDoc);		
			
			dtoReturn = modelMapper.map(userDoc, UserProfileDto.class);
			dtoReturn.setProviderDto(provider);
			
			//Send email for activation
		    EmailDetailsDtos emDtos = new EmailDetailsDtos(user.getFirstName(), user.getEmail(), activationCode);
			emailSenderEventPublisher.publishEmailSenderEvent(emDtos);
			
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
			if(!passwordProvided.equals(user.getEmail()))
				WalkMyDogException.buildWarningValidationFail(WalkMyDogExApiTypes.UPDATE_API, 
						"Current Password is not Valid.<br>Please, Try it again!");
			
			String newPassword = passwordEncoder.encode(userPasswordUpdate.getNewPassword());
			user.setPasswordHash(newPassword);			
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
	public boolean activateUser(String email, String activationCode) throws WalkMyDogException {
		if(StringUtils.isBlank(email) || StringUtils.isBlank(activationCode))
			WalkMyDogException.buildWarningValidationFail(WalkMyDogExApiTypes.UPDATE_API, "Activation URL not valid");
		
		try {
			Optional<User> userOpt = userRepository.findUserByEmailAndNotVerified(email);
			if(!userOpt.isPresent())
				throw WalkMyDogException.buildWarningNotFound(WalkMyDogExApiTypes.UPDATE_API, "User not found or already verified");
			
			User user = userOpt.get();
			
			if(!user.getVerificationString().equals(activationCode))
				throw WalkMyDogException.buildWarningValidationFail(WalkMyDogExApiTypes.UPDATE_API, "Activation code is not valid");
			
			if(isValidationCodeExpired(activationCode))
				throw WalkMyDogException.buildWarningValidationFail(WalkMyDogExApiTypes.UPDATE_API, "Activation code is expired!");
			
			USERNAME = user.getEmail();
			user.setIsVerified(true);
			userRepository.save(user);
		} catch (WalkMyDogException we) {
			log.error(we.getErrorMessage(), we);
			throw we;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw WalkMyDogException.buildCriticalRuntime(WalkMyDogExApiTypes.UPDATE_API, e);
		}		
		return true;
	}
	
	@Override
	public boolean resendActivationCode(String email) throws WalkMyDogException {
		
		try {
			Optional<User> userOpt = userRepository.findUserByEmailAndNotVerified(email);
			if(!userOpt.isPresent())
				throw WalkMyDogException.buildWarningNotFound(WalkMyDogExApiTypes.UPDATE_API, "User not found or already verified");
			
			User user = userOpt.get();
			
			String activationCode = generateActivationCode();
			user.setVerificationString(activationCode);
			
			userRepository.save(user);
			
			EmailDetailsDtos emDtos = new EmailDetailsDtos(user.getFirstName(), user.getEmail(), activationCode);
			emailSenderEventPublisher.publishEmailSenderEvent(emDtos);
		} catch (WalkMyDogException we) {
			log.error(we.getErrorMessage(), we);
			throw we;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw WalkMyDogException.buildCriticalRuntime(WalkMyDogExApiTypes.UPDATE_API, e);
		}	
		return true;
	}
	
	private String generateActivationCode() {
		StringBuilder actCode = new StringBuilder();
		actCode.append(RandomStringUtils.random(3, true, true));
		actCode.append(StringUtils.leftPad(String.valueOf(LocalDateTime.now().getDayOfMonth()), 2, "0"));
		actCode.append(RandomStringUtils.random(3, true, true));
		actCode.append(StringUtils.leftPad(String.valueOf(LocalDateTime.now().getMonthValue()), 2, "0"));
		actCode.append(RandomStringUtils.random(3, true, true));
		actCode.append(String.valueOf(LocalDateTime.now().getYear()));
		actCode.append(RandomStringUtils.random(3, true, true));
		actCode.append(StringUtils.leftPad(String.valueOf(LocalDateTime.now().getHour()), 2, "0"));
		actCode.append(RandomStringUtils.random(3, true, true));
		actCode.append(StringUtils.leftPad(String.valueOf(LocalDateTime.now().getMinute()), 2, "0"));
		return actCode.toString();
	}

	private boolean isValidationCodeExpired(String activationCode) {
		
		int day = Integer.valueOf(activationCode.substring(3,5));
		int month = Integer.valueOf(activationCode.substring(8,10));
		int year = Integer.valueOf(activationCode.substring(13,17));
		int hour = Integer.valueOf(activationCode.substring(20,22));
		int min = Integer.valueOf(activationCode.substring(25,27));
		
		LocalDateTime dateFromCode = LocalDateTime.of(year, month, day, hour, min).plusMinutes(30);
		
		long minutes = ChronoUnit.MINUTES.between(LocalDateTime.now(), dateFromCode);
		
		if(minutes < 0)
			return true;
		return false;
	}
	
	
}
