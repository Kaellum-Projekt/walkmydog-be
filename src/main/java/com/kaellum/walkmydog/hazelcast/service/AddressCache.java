package com.kaellum.walkmydog.hazelcast.service;

import java.util.List;
import java.util.Optional;

import com.kaellum.walkmydog.hazelcast.dto.AddressFullDto;

public interface AddressCache {
	
	List<AddressFullDto> getAddressSearch(Optional<String> number, Optional<String> street, Optional<String> city, Optional<String> province);
	List<AddressFullDto> getAdvAddressSearch(String addressParam);
	List<String> getAddressByCategory(Optional<String> province, Optional<String> city, Optional<String> street) throws Exception;
	
}
