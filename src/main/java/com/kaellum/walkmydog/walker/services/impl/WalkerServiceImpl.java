
package com.kaellum.walkmydog.walker.services.impl;

import static com.kaellum.walkmydog.exception.enums.WalkMyDogExApiTypes.CREATE_API;
import static com.kaellum.walkmydog.exception.enums.WalkMyDogExApiTypes.READ_API;
import static com.kaellum.walkmydog.exception.enums.WalkMyDogExApiTypes.UPDATE_API;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kaellum.walkmydog.exception.WalkMyDogException;
import com.kaellum.walkmydog.walker.collections.Walker;
import com.kaellum.walkmydog.walker.collections.repository.WalkerRepository;
import com.kaellum.walkmydog.walker.dtos.WalkerDto;
import com.kaellum.walkmydog.walker.services.WalkerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class WalkerServiceImpl implements WalkerService{

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private WalkerRepository walkerRepository;
		
	@Override
	public WalkerDto addWalker(WalkerDto dto) {

		WalkerDto resp = null;
		try {
			Walker entity = modelMapper.map(dto, Walker.class);
			
			resp = modelMapper.map(walkerRepository.save(entity), WalkerDto.class);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw WalkMyDogException.buildCriticalRuntime(CREATE_API, e, "Error creating the following walker : " + dto.toString());
		}		
		return resp;
	}

	@Override
	public List<WalkerDto> getAllWalkers() {
		List<WalkerDto> resp = null;
		try {
			List<Walker> entities = walkerRepository.findAll();
			
			resp = modelMapper.map(entities, new TypeToken<List<WalkerDto>>() {}.getType());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw WalkMyDogException.buildCriticalRuntime(READ_API, e);
		}		
		return resp;
	}

	@Override
	public WalkerDto getWalkerById(String id) throws WalkMyDogException {
		log.info("String Id {}", id);
		if(id.equalsIgnoreCase(null) || id.isBlank())
			throw WalkMyDogException.buildWarningNotFound(READ_API, String.format("Walker id not provided"));
		WalkerDto resp = null;
		try {
			
			Walker dto = walkerRepository.findById(id)
					.orElseThrow(() -> WalkMyDogException.buildWarningNotFound(READ_API, String.format("Walker %s not found", id)));
			
			resp = modelMapper.map(dto, WalkerDto.class);
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
	public WalkerDto updateWalker(WalkerDto dto) throws WalkMyDogException {
		if(dto.getId().equalsIgnoreCase(null))
			throw WalkMyDogException.buildWarningValidationFail(UPDATE_API, "Walker id must be provided");
		
		WalkerDto resp = null;
		try {
			Walker entity = walkerRepository.findById(dto.getId()).get();
			modelMapper.map(dto, entity);
			
			resp = modelMapper.map(walkerRepository.save(entity), WalkerDto.class);
		} catch (WalkMyDogException e) {
			throw e;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw WalkMyDogException.buildCriticalRuntime(UPDATE_API, e);
		}
		return resp;		
	}

	@Override
	public boolean deleteWalker(String id) {
		try {
			walkerRepository.deleteById(id);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

}