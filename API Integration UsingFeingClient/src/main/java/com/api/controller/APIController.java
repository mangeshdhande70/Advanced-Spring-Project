package com.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.api.dto.ResponseObject;
import com.api.service.ApiService;

@RestController
public class APIController {
	
	@Autowired
	private ApiService service;
	
	@GetMapping(value = "api/{id}")
	public ResponseObject get(@PathVariable int id) {
		return  service.getResponse(id);
	}
	

}
