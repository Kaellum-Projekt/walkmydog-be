package com.kaellum.walkmydog.hazelcast.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper=false)
public class AddressFullDto extends AddressDto {

	private static final long serialVersionUID = 1L;
	private BoundariesDto boundaries;
}
