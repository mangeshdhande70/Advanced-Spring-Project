package com.example.demo.dtos;



import java.sql.Timestamp;

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
public class RequestDto {
	
//	{"link":"https://localhost:8080/api/create","schemeName":"saving","create_request_time":1705657821971}
	
	@JsonProperty("link")
	private String link;
	
	@JsonProperty("schemeName")
	private String schemeName;
	
	@JsonProperty("create_request_time")
	private Timestamp timestamp;
	

}
