package com.example.demo.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.demo.config.ApplicationConstant;
import com.example.demo.dtos.RequestData;
import com.example.demo.dtos.RequestDto;
import com.example.demo.dtos.ResponseData;
import com.example.demo.dtos.VerificationReport;
import com.example.demo.entity.Requests;
import com.example.demo.repo.RequestRepository;
import com.example.demo.repo.RequestSpecificationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RequestServiceImpl implements IRequestService {

	@Autowired
	private RequestRepository requestRepository;

	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private RequestSpecificationRepository requestSpecificationRepository;
	
	@Override
	public long createNewRequest(RequestDto requestDto) {

		String jsonRequestData = null;
		String jsonResponseData = null;

		try {
			jsonRequestData = objectMapper.writeValueAsString(requestDto.getRequestData());
			jsonResponseData = objectMapper.writeValueAsString(requestDto.getResponseData());

		} catch (JsonProcessingException e) {
			log.error("Error occure while json parsing in RequestServiceImpl ::  createNewRequest " + e.getMessage());
			if (jsonRequestData.isEmpty())
				jsonRequestData = "";
		}

		Requests request = requestRepository.save(
				Requests.builder()
							.id(new Random().nextLong())
							.copRequesterId(requestDto.getCopRequesterId())
							.registrationId(requestDto.getRegistrationId())
							.requestData(jsonRequestData)
							.responseData(jsonResponseData)
							.build()
				);
		
		return request.getId();
	}
	
	
	@Override
	public List<RequestDto> getRequestByCopRequesterId(String copRequesterid) {
		
		List<Requests> response = requestRepository.findByCopRequesterId(copRequesterid);
		
		if (response==null) {
			return null;
		}
		
		List<RequestDto> requestDtos = new ArrayList<>();
		
		for(Requests requests : response) {
			
			RequestData requestData = null;
			ResponseData responseData = null;
			try {
				requestData  = objectMapper.readValue(requests.getRequestData(), RequestData.class);
				responseData = objectMapper.readValue(requests.getResponseData(), ResponseData.class);
				
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
			
			RequestDto requestDto = RequestDto.builder()
			                  .id(requests.getId())
			                  .copRequesterId(requests.getCopRequesterId())
			                  .registrationId(requests.getRegistrationId())
			                  .responseData(responseData)
			                  .requestData(requestData)
			                  .createdAt(requests.getCreatedAt())
			                  .updatedAt(requests.getUpdatedAt())
			                  .build();
			
			
			requestDtos.add(requestDto);			
			
		}
	
		return requestDtos;
	}
	
		
	public <S, D> List<D> copyList(List<S> sourceList, Class<D> destinationType) {
		return sourceList.stream()
		.map(source -> modelMapper.map(source, destinationType))
		.collect(Collectors.toList());
}
	
	

//	@Override
//	public ByteArrayInputStream  generateExcel() {
//
//		List<Requests> requestList = requestRepository.findAll();
//
//		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
//
//			Sheet sheet = workbook.createSheet(ApplicationConstant.SHEET);
//
//			// Header
//			Row headerRow = sheet.createRow(0);
//
//			headerRow.createCell(0).setCellValue(ApplicationConstant.REQUEST_ID);
//			headerRow.createCell(1).setCellValue(ApplicationConstant.SCHEME_NAME);
//			headerRow.createCell(2).setCellValue(ApplicationConstant.LINK);
//			headerRow.createCell(3).setCellValue(ApplicationConstant.CREATE_REQUEST_TIME);
//			headerRow.createCell(4).setCellValue(ApplicationConstant.STATUS);
//			headerRow.createCell(5).setCellValue(ApplicationConstant.VERIFIED);
//			headerRow.createCell(6).setCellValue(ApplicationConstant.TIMESTAMP);
//
//			int rowIdx = 1;
//
//			for (Request request : requestList) {
//
//				Row row = sheet.createRow(rowIdx++);
//
//				// for request Data
//				String jsonRequestData = request.getRequestData();
//				RequestDto requestDto = objectMapper.readValue(jsonRequestData, RequestData.class).getRequestDto();
//				// setting Request Data
//				row.createCell(0).setCellValue(request.getRequestId());
//				row.createCell(1).setCellValue(requestDto.getSchemeName());
//				row.createCell(2).setCellValue(requestDto.getLink());
//				row.createCell(3).setCellValue(requestDto.getTimestamp());
//
//				// for response Data
//				ResponseData responseData = objectMapper.readValue(request.getResponseData(), ResponseData.class);
//				row.createCell(4).setCellValue(responseData.getStatus());
//				row.createCell(5).setCellValue(responseData.isVerified());
//				row.createCell(6).setCellValue(request.getTimestamp().toString());
//			}
//
//			workbook.write(out);
//
//			return new ByteArrayInputStream(out.toByteArray());
//			
//		} catch (Exception e) {
//			log.error("fail to import data to Excel file in RequestsServiceImpl :: generateExcel" + e.getMessage());
//			throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
//		}
//	}
//
	@Override
	public ByteArrayInputStream generateMonthExcel(String copRequesterId) {
		
		
		Specification<Requests> specification = RequestsSpecification.getByMatchedTrue(copRequesterId);
		List<Requests> requestList = requestSpecificationRepository.findAll(specification);
		
		log.info("Total Records fetched from DB :: "+requestList.size());
		
		
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {

			Sheet sheet = workbook.createSheet(ApplicationConstant.SHEET);
			
			 Font headerFont = workbook.createFont();
			 headerFont.setBold(true);
			 headerFont.setColor(IndexedColors.RED.getIndex());		 
			 
			 CellStyle headerCellStyle = workbook.createCellStyle();
			 headerCellStyle.setFont(headerFont);
			 			 

			// Header
			Row headerRow = sheet.createRow(0);

			Cell cell0 = headerRow.createCell(0);
			cell0.setCellValue(ApplicationConstant.COPREQUESTER_ID);
			cell0.setCellStyle(headerCellStyle);
			
			
			Cell cell1 = headerRow.createCell(1);
			cell1.setCellValue(ApplicationConstant.MATCHED);
			cell1.setCellStyle(headerCellStyle);
			
			Cell cell2 = headerRow.createCell(2);
			cell2.setCellValue(ApplicationConstant.COUNT);
			cell2.setCellStyle(headerCellStyle);
			
			
			Cell cell3 = headerRow.createCell(3);
			cell3.setCellValue(ApplicationConstant.REASONCODE);
			cell3.setCellStyle(headerCellStyle);
			
			// for response Data
			
			if ( requestList!=null &&  !requestList.isEmpty()) {
				
				Row row = sheet.createRow(1);
	
				Requests requests = requestList.get(0);
				ResponseData responseData = objectMapper.readValue(requests.getResponseData(), ResponseData.class);
				VerificationReport verificationReport = responseData.getVerificationData().getVerificationReport();
				row.createCell(0).setCellValue(copRequesterId);
				row.createCell(1).setCellValue(verificationReport.isMatched());
				row.createCell(2).setCellValue(requestList.size());
				row.createCell(3).setCellValue(verificationReport.getReasonCode());
				
			}
			
			
			Specification<Requests> byMatchedFalse = RequestsSpecification.getByMatchedFalse(copRequesterId);
			List<Requests> falseList = requestSpecificationRepository.findAll(byMatchedFalse);
			
			if (falseList != null && !falseList.isEmpty()) {
				Row row = sheet.createRow(2);
				Requests requests = falseList.get(0);
				ResponseData responseData = objectMapper.readValue(requests.getResponseData(), ResponseData.class);
				VerificationReport verificationReport = responseData.getVerificationData().getVerificationReport();
				row.createCell(0).setCellValue(copRequesterId);
				row.createCell(1).setCellValue(verificationReport.isMatched());
				row.createCell(2).setCellValue(falseList.size());
				row.createCell(3).setCellValue(verificationReport.getReasonCode());

			}
			
			workbook.write(out);

			return new ByteArrayInputStream(out.toByteArray());
			
		} catch (Exception e) {
			log.error("fail to import data to Excel file in RequestServiceImpl :: generateExcel" + e.getMessage());
			throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
		}
				
	}
}
