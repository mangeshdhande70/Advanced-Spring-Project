package com.example.demo.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dtos.RequestDto;
import com.example.demo.entity.Requests;
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
	public ResponseEntity<?> createRequest(@RequestBody RequestDto requestDto) {

		log.info("Inside RequestController :: createRequest");
		long id = requestService.createNewRequest(requestDto);
		return new ResponseEntity<>(id, HttpStatus.CREATED);

	}
	
	
	
	@GetMapping(path = "/get/{copRequesterId}")
	public ResponseEntity<List<RequestDto>> getRequestsByCopRequesterId(@PathVariable String copRequesterId){
	
		System.out.println(new Timestamp(System.currentTimeMillis()) );
		List<RequestDto> list = requestService.getRequestByCopRequesterId(copRequesterId);
		return ResponseEntity.ok(list);
	}
	
	

//	@GetMapping(path = "/download-excel")
//	public ResponseEntity<Resource> downloadExcel() {
//
//		String filename = "Request_Data.xlsx";
//
//		InputStreamResource file = new InputStreamResource(requestService.generateExcel());
//		
//		
//		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
//				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);
//
//	}
//	
	@GetMapping(path = "/download-excel-month/{copRequesterId}")
	public ResponseEntity<Resource> downloadCurrentMonthExcel(@PathVariable String copRequesterId) {

		String filename = "Request_Data_Monthly.xlsx";
		InputStreamResource file = new InputStreamResource(requestService.generateMonthExcel(copRequesterId));

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename)
				.contentType(MediaType.parseMediaType("application/vnd.ms-excel")).body(file);

	}
	
	
	
	
	@GetMapping(path = "/get")
	public ResponseEntity<List<Requests>> findByScheme(){
		
		System.out.println("Timestamp :: "+ new Timestamp(System.currentTimeMillis()));
		
		Specification<Requests> specification = RequestsSpecification.getByScheme("saving");
		
		List<Requests> all = requestSpecificationRepository.findAll(specification);
		
		log.info("Size :: "+all.size());
		
		return ResponseEntity.ok(all);
		
	}
	
	@GetMapping(path = "/getmatched/{copRequesterId}")
	public ResponseEntity<List<Requests>> findByMatched(@PathVariable String copRequesterId){		
		Specification<Requests> specification = RequestsSpecification.getByMatchedTrue(copRequesterId);
		List<Requests> all = requestSpecificationRepository.findAll(specification);
		log.info("Size :: "+all.size());		
		return ResponseEntity.ok(all);
		
	}
	
	@GetMapping(path = "/getmatchedfalse/{copRequesterId}")
	public ResponseEntity<List<Requests>> findByMatchedFalse(@PathVariable String copRequesterId){
				
		Specification<Requests> specification = RequestsSpecification.getByMatchedFalse(copRequesterId);
		List<Requests> all = requestSpecificationRepository.findAll(specification);
		log.info("Size :: "+all.size());
		
		return ResponseEntity.ok(all);
		
	}
	
	

}
