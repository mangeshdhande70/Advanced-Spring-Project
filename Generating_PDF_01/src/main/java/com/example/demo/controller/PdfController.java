package com.example.demo.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.fdf.FDFDocument;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class PdfController {

	@GetMapping("/generate-pdf")
	public ResponseEntity<InputStreamResource> generatePDF() throws IOException, DocumentException {

		String url = "http://localhost:9999/generate-pdf";
		Resource apiResource = new RestTemplate().getForEntity(url, Resource.class).getBody();
		
		log.info("fileName :: "+apiResource.getFilename());

//		InputStream inputStream = apiResource.getInputStream();
		InputStreamResource relatedSpecification = new InputStreamResource(apiResource.getInputStream());
		
		// Generate PDF
		ByteArrayOutputStream outputStream =new ByteArrayOutputStream();
		Document document = new Document();
		PdfWriter.getInstance(document, outputStream);
		document.open();
		document.add(new Paragraph("Hello, this is your PDF Document!"));
		document.add(new Paragraph("Hello, this is your PDF Document 2!"));
		document.close();
//		byte[] pdfBytes = outputStream.toByteArray();		
//		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(pdfBytes);
		InputStreamResource pdfView = new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
		
		
		// calling for merge two pdf
		InputStreamResource mergePdf = mergePdf(pdfView, relatedSpecification);
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
//		headers.setContentDispositionFormData("inline", "merged.pdf");
		headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=merged.pdf");
		
		
		return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(mergePdf);
	}
	
	
	private InputStreamResource mergePdf(InputStreamResource pdf1,InputStreamResource pdf2) throws IllegalStateException, IOException {
		
		PdfReader reader1 = new PdfReader(pdf1.getInputStream());
		PdfReader reader2 = new PdfReader(pdf2.getInputStream());
		
		ByteArrayOutputStream mergedStream = new ByteArrayOutputStream();
		
		Document document = new Document();
		try {
			PdfSmartCopy copy = new PdfSmartCopy(document, mergedStream);
			document.open();
			
			for(int i =1; i<=reader1.getNumberOfPages();i++) {
				copy.addPage(copy.getImportedPage(reader1, i));
			}
			
			for(int i =1; i<=reader2.getNumberOfPages();i++) {
				copy.addPage(copy.getImportedPage(reader2, i));
			}
			
			document.close();
			byte[] mergedPdfByte = mergedStream.toByteArray();
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mergedPdfByte);
			return new InputStreamResource(byteArrayInputStream);
			
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
		return null;
		
		
	}

	

}
