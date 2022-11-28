package com.kaellum.walkmydog.hazelcast.service;

import java.util.List;
import java.util.Optional;

import com.kaellum.walkmydog.hazelcast.dto.AddressDto;

public interface AddressCache {
	
	List<AddressDto> getAddressSearch(Optional<String> number, Optional<String> street, Optional<String> city, Optional<String> province);

}
