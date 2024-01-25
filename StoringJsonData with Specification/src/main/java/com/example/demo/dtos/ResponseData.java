package com.example.demo.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
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
	

	@JsonProperty("Meta")
	private String meta;
	
	@JsonProperty("Links")
	private Links links;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("CreationDateTime")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private String creationDateTime;
	
	@JsonProperty("StatusUpdateDateTime")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
	private String statusUpdatedDateTime;
	
	@JsonProperty("Data")
	private VerificationData verificationData;
	
	
	
}
