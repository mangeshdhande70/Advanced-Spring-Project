package com.example.demo.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Requests;


public interface RequestRepository extends JpaRepository<Requests,Long> {
	
	public List<Requests> findByCopRequesterId(String copRequesterId);

}
