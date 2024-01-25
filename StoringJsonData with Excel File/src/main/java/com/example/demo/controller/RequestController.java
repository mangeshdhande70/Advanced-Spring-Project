package com.example.demo.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.domain.Specification;
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
import com.example.demo.entity.Request;
import com.example.demo.repo.RequestSpecificationRepository;
import com.example.demo.service.IRequestService;
import com.example.demo.service.RequestsSpecification;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/api")
@Slf4j
public class RequestController {

	@Autowired
	private IRequestService requestService;
	
	@Autowired
	private RequestSpecificationRepository requestSpecificationRepository;

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
	
	
	@GetMapping(path = "/saveFile")
	public String saveFile() {
		
		String filename = "Request_Data_.xlsx";
		String filePath = "D:/test/"+LocalDate.now()+"_"+filename;
		InputStream inputStream = requestService.generateExcel();
		
//		Timestamp t = new Timestamp(Instant.now().toEpochMilli());
//		System.out.println(t);
		
		try(FileOutputStream fileOutputStream= new FileOutputStream(new File(filePath))){
		
			byte[] buffer = new byte[1024];
			int bytesRead;
			
			while((bytesRead = inputStream.read(buffer))!=-1) {
				fileOutputStream.write(buffer,0,bytesRead);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "File Saved in path :: "+filePath;
		
	}
	
	
	@GetMapping(path = "/get")
	public ResponseEntity<List<Request>> findByScheme(){
		
		System.out.println("Timestamp :: "+ new Timestamp(System.currentTimeMillis()));
		
		Specification<Request> specification = RequestsSpecification.getByScheme("saving");
		
		List<Request> all = requestSpecificationRepository.findAll(specification);
		
		log.info("Size :: "+all.size());
		
		return ResponseEntity.ok(all);
		
	}
	
	@GetMapping(path = "/getmatchedtrue")
	public ResponseEntity<List<Request>> findByMatched(){
				
		Specification<Request> specification = RequestsSpecification.getByMatchedTrue();
		List<Request> all = requestSpecificationRepository.findAll(specification);
		log.info("Size :: "+all.size());		
		return ResponseEntity.ok(all);
		
	}
	
	@GetMapping(path = "/getmatchedfalse")
	public ResponseEntity<List<Request>> findByMatchedFalse(){
				
		Specification<Request> specification = RequestsSpecification.getByMatchedFalse();
		List<Request> all = requestSpecificationRepository.findAll(specification);
		log.info("Size :: "+all.size());
		
		return ResponseEntity.ok(all);
		
	}
	
	

}
