package com.api.client;

import com.api.dto.ResponseObject;

import feign.Param;
import feign.RequestLine;

public interface FeingClient {

	 @RequestLine("GET /{id}")
	 ResponseObject getEndpointData(@Param("id") int id);

}
