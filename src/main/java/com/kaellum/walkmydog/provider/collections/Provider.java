package com.kaellum.walkmydog.provider.collections;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import com.kaellum.walkmydog.provider.dtos.enums.TimeRange;
import com.kaellum.walkmydog.provider.dtos.enums.WeekDays;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Provider extends Audit<ObjectId>{
	
	private ObjectId id = new ObjectId();
	private LocalDate dob;
	private String phone;
	private Double price;
	private Object geoLocation;
	private Set<WeekDays> days;
	private Set<TimeRange> hours;
	private Set<Address> addresses;
	private String userId;
	private LocalDateTime deactivationDate;
	private Set<Reviews> ratings;

	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}
}
