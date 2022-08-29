package com.kaellum.walkmydog.user.collections;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import com.kaellum.walkmydog.user.dto.enums.TimeRange;
import com.kaellum.walkmydog.user.dto.enums.WeekDays;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Provider{
	
	private ObjectId id = new ObjectId();
	private String firstName;
	private String lastName;
	private LocalDate dob;
	private String phone;
	private Double price;
	private Set<WeekDays> days;
	private Set<TimeRange> hours;
	private Set<Address> addresses;
	private String userId;
	private LocalDateTime deactivationDate;
	private Set<Reviews> ratings;
	private String role;
}
