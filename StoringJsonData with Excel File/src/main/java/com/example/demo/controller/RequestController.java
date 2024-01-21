package com.example.demo.controller;

import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.RequestData;
import com.example.demo.dtos.ResponseData;
import com.example.demo.service.IRequestService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/api")
@Slf4j
public class RequestController {

	@Autowired
	private IRequestService requestService;

	@PostMapping(path = "/create")
	public ResponseEntity<?> createRequest(@RequestBody RequestData requestData) {

		log.info("Inside RequestController :: createRequest");

		ResponseData responseData = ResponseData.builder().verified(false).status("completed").build();

		String response = requestService.createNewRequest(requestData, responseData);

		return new ResponseEntity<>(response, HttpStatus.CREATED);

	}

	@GetMapping(path = "/download-excel")
	public ResponseEntity<Resource> downloadExcel() {

		String filename = "Request_Data.xlsx";

		InputStreamResource file = new InputStreamResource(requestService.generateExcel());
		
		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);

	}
	
	@GetMapping(path = "/download-excel/month")
	public ResponseEntity<Resource> downloadCurrentMonthExcel() {

		String filename = "Request_Data_Monthly.xlsx";
		InputStreamResource file = new InputStreamResource(requestService.generateMonthExcel());

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);

	}

}
