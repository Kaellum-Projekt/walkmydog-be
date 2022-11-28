package com.kaellum.walkmydog.hazelcast.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import com.kaellum.walkmydog.hazelcast.collection.Addresses;
import com.kaellum.walkmydog.hazelcast.dto.AddressDto;
import com.kaellum.walkmydog.hazelcast.repository.AddressRepository;
import com.kaellum.walkmydog.hazelcast.service.AddressCache;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Service
@CacheConfig(cacheNames = "addresses")
@Log4j2
public class AddressCacheImpl implements AddressCache{
	
	private final AddressRepository addressRepository;
	private final HazelcastInstance hzInstance;
	
	@PostConstruct
	private void postConstruct() throws Exception {
		this.loadAll();
	}

	private void loadAll() throws Exception {
		try {
			
			IMap<String, AddressDto> hzMap = hzInstance.getMap("addresses");
			
			if(hzMap.isEmpty()) {			
				List<Addresses> addresses = addressRepository.findAll();
				for(Addresses ad : addresses) {
					hzMap.put(ad.getId(), new AddressDto(
							ad.getNumber(), 
							ad.getStreet(), 
							ad.getCity(), 
							Double.valueOf(ad.getLat()), 
							Double.valueOf(ad.getLon())));
				}
			}			
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		
	}

	@Override
	public List<AddressDto> getAddressSearch(Optional<String> number, Optional<String> street, Optional<String> city, Optional<String> province) {
		IMap<String, AddressDto> hzMap = hzInstance.getMap("addresses");
		
		List<Predicate<String, AddressDto>> predicates = new ArrayList<>();
		
		constructPredicate(predicates, number, street, city, province);		
		
		return List.copyOf(hzMap.values(Predicates.and(predicates.toArray(new Predicate[predicates.size()]))));
	}

	@SafeVarargs
	private void constructPredicate(List<Predicate<String, AddressDto>> predicates, Optional<String>... parameters ) {
		String[] paramName = {"number", "street", "city", "province"};
		for(int index = 0 ; index < 4 ; index++) {
			if(parameters[index].isPresent() && !StringUtils.isBlank(parameters[index].get())) {
				Predicate<String, AddressDto> PredNumber = Predicates.like(paramName[index], parameters[index].get() + "%");
				predicates.add(PredNumber);
			}
			
		}

	}
	
	

}
