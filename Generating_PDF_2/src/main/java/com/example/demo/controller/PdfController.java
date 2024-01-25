package com.example.demo.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PdfController {

@GetMapping("/generate-pdf")
public ResponseEntity<Resource> generatePDF() {
	
	String fileName = "";
//	D:/test/
	String filePath = "D:/20290396.pdf";
	Path path = Paths.get(filePath);

	// call getFileName() and get FileName path object
	Path filePathName = path.getFileName();
	fileName = filePathName.toString();
	if (fileName != null)
		fileName = fileName.replace("(temp)", "");
	HttpHeaders headers = new HttpHeaders();
	
	headers.setContentType(MediaType.APPLICATION_PDF);

	headers.setContentDisposition(
			ContentDisposition.parse("attachment; fileName=\"" + fileName + "" + "\""));
	InputStreamResource resource = null;
	try {
		resource = new InputStreamResource(new FileInputStream(filePath));

	} catch (FileNotFoundException e) {

		e.getMessage();

	}
	
	List<String> str = new ArrayList<String>();

	str.add("fileName");
	headers.add("fileName", fileName);

	headers.setAccessControlExposeHeaders(str);
	
	System.out.println(ResponseEntity.ok().headers(headers));
	System.out.println(fileName);

	return ResponseEntity.ok()

			.headers(headers).body(resource);
}
}
