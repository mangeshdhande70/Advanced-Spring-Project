package com.example.demo.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.entity.RequestsListener;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class AppConfiguration {
	
	
	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	
	@Bean
	public RequestsListener requestsListener() {
		return new RequestsListener();
	}
	
	@Bean
	public ModelMapper mapper() {
		return new ModelMapper();
	}
	
}
