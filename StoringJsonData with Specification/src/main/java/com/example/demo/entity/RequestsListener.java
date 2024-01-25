package com.example.demo.entity;

import java.sql.Timestamp;

import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class RequestsListener {
	
	
	@PrePersist
	public void prePersitst(Requests requests) {
		
		requests.setCreatedAt(new Timestamp(System.currentTimeMillis()));
		requests.setUpdatedAt(new Timestamp(System.currentTimeMillis()));		
	}
	
	@PreUpdate
	public void preUpdated(Requests requests) {
		requests.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
	}

}
