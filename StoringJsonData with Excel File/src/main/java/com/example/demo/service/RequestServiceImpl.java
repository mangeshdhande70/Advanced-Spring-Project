package com.example.demo.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.config.ApplicationConstant;
import com.example.demo.dtos.RequestData;
import com.example.demo.dtos.RequestDto;
import com.example.demo.dtos.ResponseData;
import com.example.demo.entity.Request;
import com.example.demo.repo.RequestRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
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
	private EntityManager entityManager;
	
	@Override
	public String createNewRequest(RequestData requestData, ResponseData responseData) {

		String jsonRequestData = null;
		String jsonResponseData = null;

		try {
			requestData.getRequestDto().setTimestamp(new Timestamp(System.currentTimeMillis()));
			jsonRequestData = objectMapper.writeValueAsString(requestData);

			Map<String, Object> responseDataMap = new HashMap<>();

			responseDataMap.put("status", responseData.getStatus());
			responseDataMap.put("verified", responseData.isVerified());

			jsonResponseData = objectMapper.writeValueAsString(responseDataMap);

		} catch (JsonProcessingException e) {
			log.error("Error occure while json parsing in RequestServiceImpl ::  createNewRequest " + e.getMessage());
			if (jsonRequestData.isEmpty())
				jsonRequestData = "";
		}

		Request request = requestRepository.save(Request.builder().requestId(UUID.randomUUID().toString())
				.timestamp(LocalDateTime.now()).requestData(jsonRequestData).responseData(jsonResponseData).build());

		return request.getRequestId();
	}

	@Override
	public ByteArrayInputStream  generateExcel() {

		List<Request> requestList = requestRepository.findAll();

		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {

			Sheet sheet = workbook.createSheet(ApplicationConstant.SHEET);

			// Header
			Row headerRow = sheet.createRow(0);

			headerRow.createCell(0).setCellValue(ApplicationConstant.REQUEST_ID);
			headerRow.createCell(1).setCellValue(ApplicationConstant.SCHEME_NAME);
			headerRow.createCell(2).setCellValue(ApplicationConstant.LINK);
			headerRow.createCell(3).setCellValue(ApplicationConstant.CREATE_REQUEST_TIME);
			headerRow.createCell(4).setCellValue(ApplicationConstant.STATUS);
			headerRow.createCell(5).setCellValue(ApplicationConstant.VERIFIED);
			headerRow.createCell(6).setCellValue(ApplicationConstant.TIMESTAMP);

			int rowIdx = 1;

			for (Request request : requestList) {

				Row row = sheet.createRow(rowIdx++);

				// for request Data
				String jsonRequestData = request.getRequestData();
				RequestDto requestDto = objectMapper.readValue(jsonRequestData, RequestData.class).getRequestDto();
				// setting Request Data
				row.createCell(0).setCellValue(request.getRequestId());
				row.createCell(1).setCellValue(requestDto.getSchemeName());
				row.createCell(2).setCellValue(requestDto.getLink());
				row.createCell(3).setCellValue(requestDto.getTimestamp());

				// for response Data
				ResponseData responseData = objectMapper.readValue(request.getResponseData(), ResponseData.class);
				row.createCell(4).setCellValue(responseData.getStatus());
				row.createCell(5).setCellValue(responseData.isVerified());
				row.createCell(6).setCellValue(request.getTimestamp());
			}

			workbook.write(out);

			return new ByteArrayInputStream(out.toByteArray());
			
		} catch (Exception e) {
			log.error("fail to import data to Excel file in RequestServiceImpl :: generateExcel" + e.getMessage());
			throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
		}
	}

	@Override
	public ByteArrayInputStream generateMonthExcel() {
		
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Request> query = criteriaBuilder.createQuery(Request.class);
		Root<Request> root = query.from(Request.class);
		
		
		Predicate predicate = criteriaBuilder
		          .and(criteriaBuilder.equal(criteriaBuilder.function("MONTH",Integer.class,root.get("timestamp")),
		        	   criteriaBuilder.function("MONTH", Integer.class, criteriaBuilder.currentDate())),
		        		  
		        	   criteriaBuilder.equal(criteriaBuilder.function("YEAR", Integer.class,root.get("timestamp")),
		   		       criteriaBuilder.function("YEAR", Integer.class, criteriaBuilder.currentDate()))
		          );
		
		
		query.select(root).where(predicate);
		
		List<Request> requestList = entityManager.createQuery(query).getResultList();
		
		
		try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {

			Sheet sheet = workbook.createSheet(ApplicationConstant.SHEET);

			// Header
			Row headerRow = sheet.createRow(0);

			headerRow.createCell(0).setCellValue(ApplicationConstant.REQUEST_ID);
			headerRow.createCell(1).setCellValue(ApplicationConstant.SCHEME_NAME);
			headerRow.createCell(2).setCellValue(ApplicationConstant.LINK);
			headerRow.createCell(3).setCellValue(ApplicationConstant.CREATE_REQUEST_TIME);
			headerRow.createCell(4).setCellValue(ApplicationConstant.STATUS);
			headerRow.createCell(5).setCellValue(ApplicationConstant.VERIFIED);
			headerRow.createCell(6).setCellValue(ApplicationConstant.TIMESTAMP);

			int rowIdx = 1;

			for (Request request : requestList) {

				Row row = sheet.createRow(rowIdx++);

				// for request Data
				String jsonRequestData = request.getRequestData();
				RequestDto requestDto = objectMapper.readValue(jsonRequestData, RequestData.class).getRequestDto();
				// setting Request Data
				row.createCell(0).setCellValue(request.getRequestId());
				row.createCell(1).setCellValue(requestDto.getSchemeName());
				row.createCell(2).setCellValue(requestDto.getLink());
				row.createCell(3).setCellValue(requestDto.getTimestamp());

				// for response Data
				ResponseData responseData = objectMapper.readValue(request.getResponseData(), ResponseData.class);
				row.createCell(4).setCellValue(responseData.getStatus());
				row.createCell(5).setCellValue(responseData.isVerified());
				row.createCell(6).setCellValue(request.getTimestamp());
			}

			workbook.write(out);

			return new ByteArrayInputStream(out.toByteArray());
			
		} catch (Exception e) {
			log.error("fail to import data to Excel file in RequestServiceImpl :: generateExcel" + e.getMessage());
			throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
		}
				
	}
}
