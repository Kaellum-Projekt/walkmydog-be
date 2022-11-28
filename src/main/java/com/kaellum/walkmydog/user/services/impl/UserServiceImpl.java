package com.kaellum.walkmydog.user.services.impl;

import static com.kaellum.walkmydog.exception.enums.WalkMyDogExApiTypes.CREATE_API;
import static com.kaellum.walkmydog.exception.enums.WalkMyDogExApiTypes.READ_API;
import static com.kaellum.walkmydog.exception.enums.WalkMyDogExApiTypes.UPDATE_API;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.kaellum.walkmydog.authentication.services.TokenService;
import com.kaellum.walkmydog.emailsender.EmailDetailsDtos;
import com.kaellum.walkmydog.emailsender.EmailSenderEventPublisher;
import com.kaellum.walkmydog.emailsender.EmailType;
import com.kaellum.walkmydog.exception.WalkMyDogException;
import com.kaellum.walkmydog.exception.enums.WalkMyDogExApiTypes;
import com.kaellum.walkmydog.user.collections.Provider;
import com.kaellum.walkmydog.user.collections.User;
import com.kaellum.walkmydog.user.dto.ProviderUserFullDto;
import com.kaellum.walkmydog.user.dto.ProviderUserSimpleDto;
import com.kaellum.walkmydog.user.dto.UserDto;
import com.kaellum.walkmydog.user.dto.UserPasswordUpdate;
import com.kaellum.walkmydog.user.repositories.UserRepository;
import com.kaellum.walkmydog.user.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service 
@RequiredArgsConstructor(onConstructor=@__({@Lazy})) 
@Log4j2
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final EmailSenderEventPublisher emailSenderEventPublisher;
    private final MongoTemplate mongoTemplate;
    private final @Lazy TokenService tokenService;
    
    public static String USERNAME;

	@Override
	public Map<String, String> addNewUser(UserDto userDto, String httpRequestUrl) throws WalkMyDogException {
		log.info("New User Dto {}", userDto);
		//UserDto dtoReturn = null;
		try {
			if(userDto == null)
				throw WalkMyDogException.buildWarningValidationFail(WalkMyDogExApiTypes.CREATE_API, 
						"User object must be provided");
			
			if(!isPassStrong(userDto.getPassword()))
				throw WalkMyDogException.buildWarningValidationFail(WalkMyDogExApiTypes.CREATE_API, 
						"Password is not strong enough");
			
			User example = new User();
			example.setEmail(userDto.getEmail());			
			if(userRepository.findOne(Example.of(example)).isPresent())
				throw WalkMyDogException.buildWarningDuplicate(CREATE_API, "The email address is already in use");

			User userDoc = modelMapper.map(userDto, User.class);
			String password = passwordEncoder.encode(userDto.getPassword());
			userDoc.setPassword(password);
			USERNAME = userDto.getEmail();
			
			//ProviderDto provider = null;
			if(!userDto.getProviderDto().getRole().equals("ROLE_PROVIDER") && !userDto.getProviderDto().getRole().equals("ROLE_CLIENT") )
				throw WalkMyDogException.buildWarningValidationFail(WalkMyDogExApiTypes.CREATE_API, 
						"Not valid Role");
			
			//First creates a random activation string and sets the user as non-activated
			String activationCode = generateActivationCode();
			userDoc.setUserTempCode(activationCode);
			userDoc.setIsVerified(false); 
			
			UserDto savedUser = modelMapper.map(userRepository.save(userDoc), UserDto.class);

			//Send email for activation
		    EmailDetailsDtos emDtos = new EmailDetailsDtos(savedUser.getProviderDto().getFirstName(), savedUser.getEmail(), activationCode, EmailType.ACTIVATION);
			emailSenderEventPublisher.publishEmailSenderEvent(emDtos);
			
			Map<String, String> tokens = new HashMap<>();
			String access_token = tokenService.getAcessToken(savedUser, httpRequestUrl, List.of(savedUser.getProviderDto().getRole()), false);
			String refresh_token = tokenService.getAcessToken(savedUser, httpRequestUrl, List.of(savedUser.getProviderDto().getRole()), true);
			tokens.put("access_token", access_token);
            tokens.put("refresh_token", refresh_token);
            return tokens;			
		} catch (WalkMyDogException we) {
			log.error(we.getMessage(), we);
			throw we;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw WalkMyDogException.buildCriticalRuntime(WalkMyDogExApiTypes.CREATE_API, e, e.getMessage());
		}
	}
	
	//https://stackoverflow.com/questions/41377883/how-to-perform-partial-update-with-spring-data-mongodbmongooperations
	//https://stackoverflow.com/questions/20355261/how-to-deserialize-json-into-flat-map-like-structure
	public UserDto updateUser (UserDto userDto) throws WalkMyDogException {
		log.info("Update User Dto {}", userDto);
		try {
			if(userDto == null)
				throw WalkMyDogException.buildWarningValidationFail(WalkMyDogExApiTypes.CREATE_API, 
						"User object must be provided");
			
			User user = modelMapper.map(userDto, User.class);
			Document updateDoc = new Document();
			Update update = new Update();
			mongoTemplate.getConverter().write(user, updateDoc);
			updateDoc.values().removeIf(Objects::isNull);
			updateDoc.forEach(update::set);
		
			mongoTemplate.findAndModify(
					Query.query(Criteria.where("id").is(user.getId())), update, User.class);		
			
			return modelMapper.map(userRepository.findById(user.getId()).get(), UserDto.class);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
		
	}
	
	@Override
	public Boolean deactivateUser(String id) throws WalkMyDogException {
		log.info("Delete User {}", id);
		try {
			Optional<User> userOpt = userRepository.findById(id);
			
			if(userOpt.isEmpty()) 
				throw WalkMyDogException.buildWarningNotFound(WalkMyDogExApiTypes.UPDATE_API, "User id does not exist");

			User user = userOpt.get();
			user.setDeactivationDate(LocalDateTime.now());
			if(userRepository.save(user) != null)
				return true;		
		} catch (WalkMyDogException we) {
			log.error(we.getMessage(), we);
			throw we;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw WalkMyDogException.buildCriticalRuntime(WalkMyDogExApiTypes.CREATE_API, e, e.getMessage());
		}
		return false;
	}

	@Override
	public Boolean passwordUpdate(UserPasswordUpdate userPasswordUpdate, String userId) throws WalkMyDogException {
		try {
			if(StringUtils.isBlank(userPasswordUpdate.getCurrentPassword()) ||
					StringUtils.isBlank(userPasswordUpdate.getNewPassword()) ||
					StringUtils.isBlank(userId))
				throw WalkMyDogException.buildWarningValidationFail(WalkMyDogExApiTypes.UPDATE_API, "All three parameters are mandatory");
			
			if(!isPassStrong(userPasswordUpdate.getNewPassword()))
				throw WalkMyDogException.buildWarningValidationFail(WalkMyDogExApiTypes.CREATE_API, 
						"Password is not strong enough");
			
			Optional<User> userOpt = userRepository.findById(userId);
			
			if(userOpt.isEmpty()) 
				throw WalkMyDogException.buildWarningNotFound(WalkMyDogExApiTypes.UPDATE_API, "User id does not exist");

			User user = userOpt.get();
			
			//Validates current password
			if(!passwordEncoder.matches(userPasswordUpdate.getCurrentPassword(), user.getPassword()))
				throw WalkMyDogException.buildWarningValidationFail(WalkMyDogExApiTypes.UPDATE_API, 
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
			throw WalkMyDogException.buildCriticalRuntime(WalkMyDogExApiTypes.CREATE_API, e, e.getMessage());
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
			
			if(!user.getUserTempCode().equals(activationCode))
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
			user.setUserTempCode(activationCode);
			
			userRepository.save(user);
			
			EmailDetailsDtos emDtos = new EmailDetailsDtos(user.getProvider().getFirstName(), user.getEmail(), activationCode, EmailType.ACTIVATION);
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
	
	@Override
	public void resetForgotPassword(String email) throws WalkMyDogException {
		try {
			Optional<User> userOpt = userRepository.findUserByEmailAndVerified(email);
			if(!userOpt.isPresent())
				throw WalkMyDogException.buildWarningNotFound(WalkMyDogExApiTypes.UPDATE_API, "User not found");
			
			USERNAME = email;
			
			User user = userOpt.get();
			
			String newCode = generateActivationCode();
			user.setUserTempCode(newCode);
			
			userRepository.save(user);
			
			EmailDetailsDtos emDtos = new EmailDetailsDtos(user.getProvider().getFirstName(), user.getEmail(), newCode, EmailType.RESETPASS);
			emailSenderEventPublisher.publishEmailSenderEvent(emDtos);
		} catch (WalkMyDogException we) {
			log.error(we.getErrorMessage(), we);
			throw we;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw WalkMyDogException.buildCriticalRuntime(WalkMyDogExApiTypes.UPDATE_API, e);
		}	
	}
	
	@Override
	public Boolean passwordReset(UserPasswordUpdate userPasswordUpdate, String code) throws WalkMyDogException {
		try {
			if(StringUtils.isBlank(userPasswordUpdate.getNewPassword()) || StringUtils.isBlank(code))
				WalkMyDogException.buildWarningValidationFail(WalkMyDogExApiTypes.UPDATE_API, "All parameters are mandatory");
			
			if(!isPassStrong(userPasswordUpdate.getNewPassword()))
				throw WalkMyDogException.buildWarningValidationFail(WalkMyDogExApiTypes.CREATE_API, 
						"Password is not strong enough");
			
			Optional<User> userOpt = userRepository.findByUserTempCode(code);
			
			if(userOpt.isEmpty()) 
				throw WalkMyDogException.buildWarningNotFound(WalkMyDogExApiTypes.UPDATE_API, "Reset passoword link invalid!");
			
			User user = userOpt.get();
			if(isValidationCodeExpired(user.getUserTempCode()))
				throw WalkMyDogException.buildWarningValidationFail(WalkMyDogExApiTypes.UPDATE_API, "Reset password link is expired!");
			
			USERNAME = user.getEmail();
			
			String newPassword = passwordEncoder.encode(userPasswordUpdate.getNewPassword());
			user.setPassword(newPassword);			
			if(userRepository.save(user) != null)
				return true;
		} catch (WalkMyDogException we) {
			log.error(we.getMessage(), we);
			throw we;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw WalkMyDogException.buildCriticalRuntime(WalkMyDogExApiTypes.CREATE_API, e, e.getMessage());
		}
		return false;
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
		actCode.append(RandomStringUtils.random(6, true, true));
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
	
	/**
	 * 6 characters length
	 * 1 letters in Upper Case
	 * 1 Special Character (!@#$&*)
	 * 1 numerals (0-9)
	 * 1 letters in Lower Case
	 */
	private boolean isPassStrong(String password){
	    return password.matches("^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*\\d)(?=.*[a-z]).{6,}$");

	  }

	@Override
	public UserDto getUserByEmail(String email) {
		return modelMapper.map(userRepository.findUserByEmail(email), UserDto.class);
	}

	//**** PROVIDER ENDPOINTS ****//
	@SuppressWarnings("unchecked")
	@Override
	public <T>List<T> advancedSearch(
			Optional<Double> priceMin,
			Optional<Double> priceMax,
			Optional<List<Integer>> timeRange,
			Optional<String> province,
			Optional<String> city,
			Optional<Double> minLat, 
			Optional<Double> maxLat, 
			Optional<Double> minLng, 
			Optional<Double> maxLng, 
			Optional<Boolean> isSimple,
			Pageable pageable) throws WalkMyDogException {
		
		try {
			List<User> users = null;
			boolean isSimp = false; 
			final List<Criteria> criteria = new ArrayList<>();
			
			if(minLat.isPresent() || maxLat.isPresent() || minLng.isPresent() || maxLng.isPresent())
				if (!(minLat.isPresent() && maxLat.isPresent() && minLng.isPresent() && maxLng.isPresent() && isSimple.isPresent())) {
					throw WalkMyDogException.buildWarningNotFound(READ_API, String.format("All parameters of location coordinates & query flag should be provided"));
				} else {
					isSimp = isSimple.get();
					criteria.add(Criteria.where("provider.addresses").elemMatch(Criteria.where("latitude").lte(maxLat.get())));
					criteria.add(Criteria.where("provider.addresses").elemMatch(Criteria.where("latitude").gte(minLat.get())));
					criteria.add(Criteria.where("provider.addresses").elemMatch(Criteria.where("longitude").lte(maxLng.get())));
					criteria.add(Criteria.where("provider.addresses").elemMatch(Criteria.where("longitude").gte(minLng.get())));
				}					
			
			final Query query = isSimp?new Query().with(Pageable.unpaged()):new Query().with(pageable);		
			
			criteria.add(Criteria.where("provider.role").in("ROLE_PROVIDER"));						

			if(city.isPresent())	
				criteria.add(Criteria.where("provider.addresses").elemMatch(Criteria.where("city").is(city.get())));		
			
			if(priceMin.isPresent())
				criteria.add(Criteria.where("provider.price").gte(priceMin.get()));
			
			if(priceMax.isPresent())
				criteria.add(Criteria.where("provider.price").lte(priceMax.get()));
			
			if(timeRange.isPresent())
				criteria.add(Criteria.where("provider.timeRanges").in(timeRange.get()));
			
			if(province.isPresent())
				criteria.add(Criteria.where("provider.addresses").elemMatch(Criteria.where("province").is(province.get())));	
				
			if (!criteria.isEmpty())
				query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
			
			users = mongoTemplate.find(query, User.class);
					
			if(isSimp) {
				List<ProviderUserSimpleDto> dtosReturn = new ArrayList<>();
				users.stream()
				.forEach(x -> {
					ProviderUserSimpleDto dto = modelMapper.map(x.getProvider(), ProviderUserSimpleDto.class);
					dto.setId(x.getId());
					dtosReturn.add(dto);
				});
				
				return (List<T>) dtosReturn;
			}else {
				List<ProviderUserFullDto> dtosReturn = new ArrayList<>();
				users.stream()
				.forEach(x -> {
					ProviderUserFullDto dto = modelMapper.map(x.getProvider(), ProviderUserFullDto.class);
					dto.setId(x.getId());
					dtosReturn.add(dto);
				});
				
				return (List<T>) dtosReturn;
			}


		} catch (WalkMyDogException e) {
			log.error(e.getErrorMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw WalkMyDogException.buildCriticalRuntime(UPDATE_API, e);
		}
	}
	
	@Override
	public UserDto getProviderById(String id) throws WalkMyDogException {
		log.info("String Id {}", id);
		if(id.equalsIgnoreCase(null) || id.isBlank())
			throw WalkMyDogException.buildWarningNotFound(READ_API, String.format("User id not provided"));
		try {
			
			User user = userRepository.findById(id)
					.orElseThrow(() -> WalkMyDogException.buildWarningNotFound(READ_API, String.format("User %s not found", id)));

			return modelMapper.map(user, UserDto.class);
		} catch (WalkMyDogException e) {
			log.error(e.getErrorMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw WalkMyDogException.buildCriticalRuntime(READ_API, e);
		}
	}
}
	
