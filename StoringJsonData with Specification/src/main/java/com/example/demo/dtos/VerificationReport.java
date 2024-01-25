package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerificationReport {
	
	@JsonProperty("Matched")
	private boolean matched;
	
	@JsonProperty("ReasonCode")
	private String reasonCode;

}
