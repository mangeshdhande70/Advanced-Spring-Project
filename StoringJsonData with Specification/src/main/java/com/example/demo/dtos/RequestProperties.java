package com.example.demo.dtos;



import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class RequestProperties {
	
	@JsonProperty("SchemeName")
	private String schemeName;
	
	@JsonProperty("AccountType")
	private String accountType;
	
	@JsonProperty("Identification")
	private String identification;
	
	@JsonProperty("Name")
	private String name;
	
	@JsonProperty("SecondaryIdentification")
	private String secondaryIdentification;
	

}
