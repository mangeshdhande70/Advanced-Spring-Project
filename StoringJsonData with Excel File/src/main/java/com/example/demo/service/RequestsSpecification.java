package com.example.demo.service;

import java.sql.Date;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entity.Request;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class RequestsSpecification {
	
	
	public static Specification<Request> hasDepartment(String schemeName) {
        return new Specification<Request>() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public Predicate toPredicate(Root<Request> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(
                        cb.function("jsonb_extract_path_text", String.class, root.get("requestData"), cb.literal("Data.schemeName")),
                        schemeName
                );
            }
        };
    }
	
	public static Specification<Request> getByScheme(String scheme){
		System.out.println("RequestsSpecification.getByScheme()");
		return (root,query,builder)-> 
		builder.equal(
				builder.function("JSON_EXTRACT", String.class, root.get("requestData"),builder.literal("$.Data.schemeName")), scheme);
	}
	
	
//	public static Specification<Request> getByMatched(){
//		System.out.println("RequestsSpecification.getByMatched()");
//		return (root,query,builder)-> 
//		builder.isTrue(
//				builder.function("JSON_UNQUOTE", Boolean.class,
//						builder.function("JSON_EXTRACT", Boolean.class, 
//						root.get("responseData"),
//						builder.literal("$.Data.VerificationReport.Matched")))
//				);
//	}
	
	public static Specification<Request> getByMatchedTrue(){
		
		String requestId = "a82364a6-654e-4b20-81fb-92ad324bda86";
		
	
		
		System.out.println("RequestsSpecification.getByMatched()");
		return (root,query,builder)-> 
		builder.and(
		builder.like(
				builder.function("JSON_UNQUOTE", String.class,
						builder.function("JSON_EXTRACT", String.class,
						root.get("responseData"),
						builder.literal("$.Data.VerificationReport.Matched"))
						
						
						), "%true%"),
		
		        builder.equal(root.get("requestId"), requestId)
//		        ,builder.between(root.get("timestamp"), startDate, endDate)
				);
	}
	
	
	public static Specification<Request> getByMatchedFalse(){
		System.out.println("RequestsSpecification.getByMatched()");
		return (root,query,builder)-> 
		builder.like(
				builder.function("JSON_UNQUOTE", String.class,
						builder.function("JSON_EXTRACT", String.class,
						root.get("responseData"),
						builder.literal("$.Data.VerificationReport.Matched"))), "%false%");
	}


}
