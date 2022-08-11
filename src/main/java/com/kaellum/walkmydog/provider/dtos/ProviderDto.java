package com.kaellum.walkmydog.provider.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.kaellum.walkmydog.provider.collections.Ratings;
import com.kaellum.walkmydog.provider.dtos.enums.TimeRange;
import com.kaellum.walkmydog.provider.dtos.enums.WeekDays;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProviderDto {
	
	private String id;
	private LocalDate dob;
	@NotNull @NotEmpty
	private String phone;
	private Double price;
	private Set<WeekDays> days;
	private Set<TimeRange> hours;
	private String geoLocation;
	@NotNull @NotEmpty
	private Set<AddressDto> addresses;
	private Ratings ratings;
	private String createdBy;
    private LocalDateTime createdDate;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedDate;
}
