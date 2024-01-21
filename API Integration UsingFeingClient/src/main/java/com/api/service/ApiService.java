package com.api.service;

import org.springframework.stereotype.Service;

import com.api.client.FeingClient;
import com.api.dto.ResponseObject;

import feign.Feign;
import feign.gson.GsonDecoder;

@Service
public class ApiService {

	private final String BASE_API_URL = "https://jsonplaceholder.typicode.com/posts";

	public ResponseObject getResponse(int id) {

		FeingClient api = Feign.builder().decoder(new GsonDecoder()) // Use Gson for JSON decoding
				.target(FeingClient.class, BASE_API_URL);

		// Make API calls using the defined methods in the interface
		ResponseObject response = api.getEndpointData(id);

		return response;
	}

}
