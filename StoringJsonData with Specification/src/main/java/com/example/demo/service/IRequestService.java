package com.example.demo.service;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.example.demo.dtos.RequestDto;

public interface IRequestService {
	
	public long createNewRequest(RequestDto requestDto);
	
	public List<RequestDto> getRequestByCopRequesterId(String copRequesterid);
	
//	public ByteArrayInputStream generateExcel();
	
	public ByteArrayInputStream generateMonthExcel(String copRequesterId);

}
