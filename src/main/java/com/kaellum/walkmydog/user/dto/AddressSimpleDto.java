package com.kaellum.walkmydog.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressSimpleDto {
	private Double latitude;
	private Double longitude;
}
