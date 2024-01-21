package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Request;

public interface RequestRepository extends JpaRepository<Request,String> {

}
