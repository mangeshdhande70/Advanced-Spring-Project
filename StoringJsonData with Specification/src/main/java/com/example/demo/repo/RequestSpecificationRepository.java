package com.example.demo.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Requests;

@Repository
public interface RequestSpecificationRepository extends JpaRepository<Requests, String>, JpaSpecificationExecutor<Requests> {
}