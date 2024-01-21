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
public class ResponseData {
	
//	{"verified":"false","status":"true"}
	
	@JsonProperty("verified")
	private boolean verified;
	
	@JsonProperty("status")
	private String status;

}
