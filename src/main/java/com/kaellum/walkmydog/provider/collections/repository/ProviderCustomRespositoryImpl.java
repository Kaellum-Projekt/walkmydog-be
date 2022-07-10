package com.kaellum.walkmydog.provider.collections.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.kaellum.walkmydog.exception.WalkMyDogException;
import com.kaellum.walkmydog.exception.enums.WalkMyDogExApiTypes;
import com.kaellum.walkmydog.provider.collections.Provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

//https://medium.com/javarevisited/spring-boot-mongo-template-best-get-api-filtering-with-multiple-properties-13c68de5ae02
//@Repository
@RequiredArgsConstructor
@Log4j2
public class ProviderCustomRespositoryImpl implements ProviderCustomRepository {
	
	private final MongoTemplate mongoTemplate;

	@Override
	public List<Provider> findProviderByParams(Optional<String> firstName, Optional<String> lastName,
			Optional<Double> price, Optional<List<Integer>> timeRange, Optional<String> province, String city,
			Pageable pageable) throws WalkMyDogException {
		
		List<Provider> dtoReturn = new ArrayList<>();
		
		try {
			final Query query = new Query().with(pageable);
			final List<Criteria> criteria = new ArrayList<>();
			
//			criteria.add(Criteria.where("addresses.city").is(city));
			
			if(firstName.isPresent())
				criteria.add(Criteria.where("firstName").is(firstName.get()));
			
			if(lastName.isPresent())
				criteria.add(Criteria.where("lastName").is(lastName.get()));
			
			if(price.isPresent())
				criteria.add(Criteria.where("price").is(price.get()));
			
			if(timeRange.isPresent())
				criteria.add(Criteria.where("timeRanges").in(timeRange.get()));
			
//			if(province.isPresent())
//				criteria.add(Criteria.where("addresses.province").is(province.get()));
				
			if (!criteria.isEmpty())
				query.addCriteria(new Criteria().andOperator(criteria.toArray(new Criteria[criteria.size()])));
			
			dtoReturn = mongoTemplate.find(query, Provider.class);			
			
		} catch (WalkMyDogException e) {
			log.error(e.getErrorMessage(), e);
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw WalkMyDogException.buildCriticalRuntime(WalkMyDogExApiTypes.UPDATE_API, e);
		}
		return dtoReturn;
	}

}
