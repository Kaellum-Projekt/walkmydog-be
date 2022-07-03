package com.kaellum.walkmydog.provider.collections;

import java.time.LocalDate;
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
	private String firstName;
	private String lastName;
	private LocalDate dob;
	private String email;
	private String phone;
	private Double price;
	private Set<WeekDays> weekDays;
	private Set<TimeRange> timeRange;
	private Set<AddressDto> addresses;
	private String userId;

	
	
	@Override
	public boolean isNew() {
		return id == null && getCreatedDate() == null;
	}
}
