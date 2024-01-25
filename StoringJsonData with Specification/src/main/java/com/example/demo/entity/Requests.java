package com.example.demo.entity;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "requests")
@EntityListeners(RequestsListener.class)
public class Requests implements Serializable {
	/**
		 * 
		 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String requestData;

	private String responseData;

	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	@Column(name = "created_at")
	private Date createdAt;

	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	@Column(name = "updated_at")
	private Date updatedAt;

	private String copRequesterId;

	private String registrationId;

}
