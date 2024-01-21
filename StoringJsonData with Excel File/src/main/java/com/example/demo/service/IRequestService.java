package com.example.demo.service;

import java.io.ByteArrayInputStream;

import com.example.demo.dtos.RequestData;
import com.example.demo.dtos.ResponseData;

public interface IRequestService {
	
	public String createNewRequest(RequestData requestData, ResponseData responseData);
	
	public ByteArrayInputStream generateExcel();
	
	public ByteArrayInputStream generateMonthExcel();

}
