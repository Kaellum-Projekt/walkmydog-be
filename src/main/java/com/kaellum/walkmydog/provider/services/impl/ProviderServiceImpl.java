
package com.kaellum.walkmydog.provider.services.impl;

import static com.kaellum.walkmydog.exception.enums.WalkMyDogExApiTypes.CREATE_API;
import static com.kaellum.walkmydog.exception.enums.WalkMyDogExApiTypes.READ_API;
import static com.kaellum.walkmydog.exception.enums.WalkMyDogExApiTypes.UPDATE_API;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.kaellum.walkmydog.exception.WalkMyDogException;
import com.kaellum.walkmydog.provider.collections.Provider;
import com.kaellum.walkmydog.provider.collections.repository.ProviderRepository;
import com.kaellum.walkmydog.provider.dtos.ProviderDto;
import com.kaellum.walkmydog.provider.services.ProviderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProviderServiceImpl implements ProviderService{

	private final ModelMapper modelMapper;
	private final ProviderRepository providerRepository;
	private final MongoTemplate mongoTemplate;
	
		
	@Override
	public ProviderDto addProvider(ProviderDto dto, String email) {

		ProviderDto resp = null;
		try {
			
			Provider entity = modelMapper.map(dto, Provider.class);
			entity.setCreatedBy(email);
			//entity.setDeactivationDate(LocalDateTime.now());// REMOVE THAT CRAP LATER
			
			resp = modelMapper.map(providerRepository.save(entity), ProviderDto.class);
		} catch (WalkMyDogException we) {
			log.error(we.getAdditionalData(), we);
			throw we;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw WalkMyDogException.buildCriticalRuntime(CREATE_API, e, "Error creating the following provider : " + dto.toString());
		}		
		return resp;
	}

	@Override
	public List<ProviderDto> getAllProviders(Pageable page) {
		
		List<ProviderDto> resp = null;
		try {		
			Page<Provider> entities = providerRepository.findAllActiveProviders(page);
			
			resp = modelMapper.map(entities.getContent(), new TypeToken<List<ProviderDto>>() {}.getType());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw WalkMyDogException.buildCriticalRuntime(READ_API, e);
		}		
		return resp;
	}

	@Override
	public ProviderDto getProviderById(String id) throws WalkMyDogException {
		log.info("String Id {}", id);
		if(id.equalsIgnoreCase(null) || id.isBlank())
			throw WalkMyDogException.buildWarningNotFound(READ_API, String.format("Provider id not provided"));
		ProviderDto resp = null;
		try {
			
			Provider dto = providerRepository.findById(id)
					.orElseThrow(() -> WalkMyDogException.buildWarningNotFound(READ_API, String.format("Provider %s not found", id)));
			
			resp = modelMapper.map(dto, ProviderDto.class);
		} catch (WalkMyDogException e) {
			log.error(e.getErrorMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw WalkMyDogException.buildCriticalRuntime(READ_API, e);
		}
		return resp;
	}

	@Override
	public ProviderDto updateProvider(ProviderDto dto) throws WalkMyDogException {
		if(dto.getId().equalsIgnoreCase(null))
			throw WalkMyDogException.buildWarningValidationFail(UPDATE_API, "Provider id must be provided");
		
		ProviderDto resp = null;
		try {
			Provider entity = providerRepository.findById(dto.getId()).get();
			modelMapper.map(dto, entity);
			
			resp = modelMapper.map(providerRepository.save(entity), ProviderDto.class);
		} catch (WalkMyDogException e) {
			log.error(e.getErrorMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw WalkMyDogException.buildCriticalRuntime(UPDATE_API, e);
		}
		return resp;		
	}

	@Override
	public boolean deleteProvider(String id) {
		try {
			providerRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public List<ProviderDto> advancedSearch(Optional<String> firstName, Optional<String> lastName,
			Optional<Double> price, Optional<List<Integer>> timeRange, Optional<String> province, Optional<String> city,
			Pageable pageable) throws WalkMyDogException {
		
		try {
			List<Provider> dtoReturn = null;
			
			final Query query = new Query().with(pageable);
			final List<Criteria> criteria = new ArrayList<>();
			
			if(city.isPresent())
				criteria.add(Criteria.where("address").elemMatch(Criteria.where("city").is(city.get())));				
			
//			if(firstName.isPresent())
//				criteria.add(Criteria.where("firstName").is(firstName.get()));
//			
//			if(lastName.isPresent())
//				criteria.add(Criteria.where("lastName").is(lastName.get()));
			
			if(price.isPresent())
				criteria.add(Criteria.where("price").is(price.get()));
			
			if(timeRange.isPresent())
				criteria.add(Criteria.where("timeRanges").in(timeRange.get()));
			
			if(province.isPresent())
				criteria.add(Criteria.where("address").elemMatch(Criteria.where("province").is(province.get())));	
				
			if (!criteria.isEmpty())
				query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
			
			dtoReturn = mongoTemplate.find(query, Provider.class);	
			
			return modelMapper.map(dtoReturn, new TypeToken<List<ProviderDto>>() {}.getType());

		} catch (WalkMyDogException e) {
			log.error(e.getErrorMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw WalkMyDogException.buildCriticalRuntime(UPDATE_API, e);
		}
	}		

}