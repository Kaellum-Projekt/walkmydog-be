package com.kaellum.walkmydog.provider.collections;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.kaellum.walkmydog.provider.dtos.AddressDto;
import com.kaellum.walkmydog.provider.dtos.enums.TimeRange;
import com.kaellum.walkmydog.provider.dtos.enums.WeekDays;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Provider extends Audit{
	
	@Id
	private String id;
	private LocalDate dob;
	private String phone;
	private Double price;
	private Object geoLocation;
	private Set<WeekDays> days;
	private Set<TimeRange> hours;
	private Set<AddressDto> address;
	private String userId;
	private LocalDateTime deactivationDate;
	private Set<Reviews> ratings;
	
	@Override
	public boolean isNew() {
		return id == null && getCreatedDate() == null;
	}
}
