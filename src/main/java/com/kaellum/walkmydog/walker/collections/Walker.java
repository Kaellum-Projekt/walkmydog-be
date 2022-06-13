package com.kaellum.walkmydog.walker.collections;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.kaellum.walkmydog.walker.dtos.AddressDto;
import com.kaellum.walkmydog.walker.dtos.enums.TimeRange;
import com.kaellum.walkmydog.walker.dtos.enums.WeekDays;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Document
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Walker extends Audit{
	
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

	
	
	@Override
	public boolean isNew() {
		return id == null && getCreatedDate() == null;
	}
}
