package com.example.demo.dtos;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RequestDto {
	
	private long id;

	@JsonProperty("Data")
	private RequestData requestData;
	
	@JsonProperty("ResponseData")
	private ResponseData responseData;
	
	private Date createdAt;

	private Date updatedAt;

	private String copRequesterId;

	private String registrationId;
	
	
	
	

}
