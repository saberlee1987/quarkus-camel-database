package com.saber.camel.dto;

import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import lombok.Data;

@Data

public class PersonDto {
	
	private String firstName;
	private String lastName;
	private String nationalCode;
	private Integer age;
	
	@Override
	public String toString() {
		return new GsonBuilder()
				.setLenient()
				.setPrettyPrinting()
				.setNumberToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
				.setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
				.enableComplexMapKeySerialization()
				.create()
				.toJson(this, PersonDto.class);
	}
}
