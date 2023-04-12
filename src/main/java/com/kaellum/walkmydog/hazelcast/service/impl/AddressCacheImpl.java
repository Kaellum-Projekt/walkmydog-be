package com.kaellum.walkmydog.hazelcast.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.query.PagingPredicate;
import com.hazelcast.query.Predicate;
import com.hazelcast.query.Predicates;
import com.kaellum.walkmydog.hazelcast.collection.Addresses;
import com.kaellum.walkmydog.hazelcast.collection.Boundaries;
import com.kaellum.walkmydog.hazelcast.dto.AddressDto;
import com.kaellum.walkmydog.hazelcast.dto.AddressFullDto;
import com.kaellum.walkmydog.hazelcast.dto.BoundariesDto;
import com.kaellum.walkmydog.hazelcast.repository.AddressRepository;
import com.kaellum.walkmydog.hazelcast.repository.BoundariesRepository;
import com.kaellum.walkmydog.hazelcast.service.AddressCache;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Service
//@CacheConfig(cacheNames = "addresses")
@Log4j2
public class AddressCacheImpl implements AddressCache{
	
	private final AddressRepository addressRepository;
	private final BoundariesRepository boundariesRepository;
	private final HazelcastInstance hzInstance;
	private final ModelMapper modelMapper;
	
	@PostConstruct
	private void postConstruct() throws Exception {
		this.loadAll();
	}

	private void loadAll() throws Exception {
		try {
			
			IMap<String, AddressDto> hzMapAddr = hzInstance.getMap("addresses");
			IMap<String, BoundariesDto> hzMapCBoun = hzInstance.getMap("city_boundaries");
			
			if(hzMapAddr.isEmpty()) {
				List<Addresses> addresses = addressRepository.findAll();
				for(Addresses ad : addresses) {
					hzMapAddr.put(ad.getId(), new AddressDto(
							ad.getNumber(), 
							ad.getStreet(), 
							ad.getCity(),
							ad.getProvince(),
							Double.valueOf(ad.getLat()), 
							Double.valueOf(ad.getLon())));
				}
			}
			if(hzMapCBoun.isEmpty()) {
				List<Boundaries> boundaries = boundariesRepository.findAll();
				for(Boundaries bd : boundaries) {
					hzMapCBoun.put(bd.getId(), new BoundariesDto(
							bd.getCity(),
							bd.getMaxLat(),
							bd.getMinLat(),
							bd.getMaxLon(),
							bd.getMinLon(),
							bd.getProvince()));
				}
			}	
		}catch (Exception e) {
			log.error(e.getMessage(), e);
			throw e;
		}
		
	}

	@Override
	public List<AddressFullDto> getAddressSearch(Optional<String> number, Optional<String> street, Optional<String> city, Optional<String> province) {
		IMap<String, AddressDto> hzMap = hzInstance.getMap("addresses");
		
		List<Predicate<String, AddressDto>> predicates = new ArrayList<>();
		
		constructPredicate(predicates, number, street, city, province);		
		
		List<AddressDto> listAddresses = List.copyOf(hzMap.values(Predicates.and(predicates.toArray(new Predicate[predicates.size()]))));		
		
		return findCityBoundaries(listAddresses);
	}

	@SafeVarargs
	private void constructPredicate(List<Predicate<String, AddressDto>> predicates, Optional<String>... parameters ) {
		String[] paramName = {"number", "street", "city", "province"};
		for(int index = 0 ; index < 4 ; index++) {
			if(parameters[index].isPresent() && !StringUtils.isBlank(parameters[index].get())) {
				Predicate<String, AddressDto> PredNumber = Predicates.like(paramName[index], parameters[index].get().toUpperCase() + "%");
				predicates.add(PredNumber);
			}
			
		}

	}
	
	@Override
	public List<AddressFullDto> getAdvAddressSearch(String addressParam) {
		IMap<String, AddressDto> hzMap = hzInstance.getMap("addresses");
		
		List<List<Predicate<String, AddressDto>>> predicatesAnd = new ArrayList<>();
		
		List<Predicate<String, AddressDto>> predicatesOR;
		
		String[] paramName = {"number", "street", "city", "province"};
		
		String[] parameters =  addressParam.split("\\s+");
		
		for (String param : parameters) {
			predicatesOR = new ArrayList<>();
			for (String pName : paramName) {	
				Predicate<String, AddressDto> PredNumber = Predicates.ilike(pName, "%" + param.trim() + "%");
				predicatesOR.add(PredNumber);
			}
			predicatesAnd.add(predicatesOR);
		}
		
		Predicate<String, AddressDto> allPredicates = Predicates.and(
				predicatesAnd.stream()
				.map(x -> Predicates.or(x.toArray(new Predicate[x.size()])))
				.toArray(Predicate[]::new));
		
		PagingPredicate<String, AddressDto> pagingPredicate = Predicates.pagingPredicate(allPredicates, 30);
		
		List<AddressDto> listAddresses = List.copyOf(hzMap.values(pagingPredicate));

		findCityBoundaries(listAddresses);		
		
		return findCityBoundaries(listAddresses);	
	}
	
	private List<AddressFullDto> findCityBoundaries(List<AddressDto> addresses) {
		List<AddressFullDto> addressesFullDto = modelMapper.map(addresses, new TypeToken<List<AddressFullDto>>(){}.getType());
		String[] cities = addressesFullDto.stream().map(AddressFullDto::getCity).distinct().toArray(String[]::new);
		IMap<String, BoundariesDto> hzMap = hzInstance.getMap("city_boundaries");
		Collection<BoundariesDto> bound = hzMap.values(Predicates.in("city", cities));
		addressesFullDto.forEach(x -> x.setBoundaries(bound.stream().filter(y -> y.getCity().equals(x.getCity())).findFirst().orElse(null)));
		return addressesFullDto;
	}
	
	@Override
	public List<String> getAddressByCategory(Optional<String> street, Optional<String> city, Optional<String> province) throws Exception{

		if (!street.isPresent() && !city.isPresent() && !province.isPresent()){
			return List.of("AB", "BC", "MB", "NB", "NL", "NT", "NS", "NU", "ON", "PE", "QC", "SK", "YK");
		}else if (province.isPresent() && city.isPresent() && !street.isPresent()) {
			IMap<String, BoundariesDto> hzMap = hzInstance.getMap("city_boundaries");
			Collection<BoundariesDto> bound = hzMap.values(Predicates.and(Predicates.like("city", city.get().toUpperCase() + "%"), Predicates.equal("province", province.get())));
			return bound.stream().map(BoundariesDto::getCity).collect(Collectors.toList());
		}else{
			List<AddressFullDto> addresses = getAddressSearch(Optional.empty(), street, city, province);
			return addresses.stream().map(AddressFullDto::getStreet).distinct().collect(Collectors.toList());
		}
	}

	
	

}
