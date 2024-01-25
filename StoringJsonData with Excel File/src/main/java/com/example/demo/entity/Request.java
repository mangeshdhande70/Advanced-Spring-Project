package com.example.demo.entity;

import java.io.Serializable;
import java.security.Timestamp;
import java.sql.Date;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "request")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Request implements Serializable {
	

	private static final long serialVersionUID = 1L;

	@Id
	private String requestId;
	
	@Column(name = "request_data")
	private String requestData;
	
	@Column(name = "response_data")
	private String responseData;
	
	@Column(name = "created_time")
	private Date timestamp;
	
	
	

}
