package com.example.demo.service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.YearMonth;
import java.time.ZoneOffset;

import org.springframework.data.jpa.domain.Specification;

import com.example.demo.entity.Requests;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class RequestsSpecification {
	
	
	public static Specification<Requests> hasDepartment(String schemeName) {
        return new Specification<Requests>() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public Predicate toPredicate(Root<Requests> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(
                        cb.function("jsonb_extract_path_text", String.class, root.get("requestData"), cb.literal("Data.schemeName")),
                        schemeName
                );
            }
        };
    }
	
	public static Specification<Requests> getByScheme(String scheme){
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
	
	public static Specification<Requests> getByMatchedTrue(String copRequesterId) {

		YearMonth lastMonth = YearMonth.now().minusMonths(1);
		Timestamp startOfMonth = Timestamp.from(lastMonth.atDay(1).atStartOfDay().toInstant(ZoneOffset.UTC));
		Timestamp endOfMonth = Timestamp.from(lastMonth.atEndOfMonth().atTime(23, 59, 59).toInstant(ZoneOffset.UTC));

		System.out.println("RequestsSpecification.getByMatched()");
		return (root, query, builder) -> builder.and(
				builder.like(builder.function("JSON_UNQUOTE", String.class,
						builder.function("JSON_EXTRACT", String.class, root.get("responseData"),
								builder.literal("$.Data.VerificationReport.Matched"))),
						"%true%"),

				builder.equal(root.get("copRequesterId"), copRequesterId),
				builder.between(root.get("createdAt"), startOfMonth, endOfMonth));
	}
	
	public static Specification<Requests> getByMatchedFalse(String copRequesterId) {

		YearMonth lastMonth = YearMonth.now().minusMonths(1);
		Timestamp startOfMonth = Timestamp.from(lastMonth.atDay(1).atStartOfDay().toInstant(ZoneOffset.UTC));
		Timestamp endOfMonth = Timestamp.from(lastMonth.atEndOfMonth().atTime(23, 59, 59).toInstant(ZoneOffset.UTC));

		System.out.println("RequestsSpecification.getByMatchedFalse()");
		return (root, query, builder) -> builder.and(
				builder.like(builder.function("JSON_UNQUOTE", String.class,
						builder.function("JSON_EXTRACT", String.class, root.get("responseData"),
								builder.literal("$.Data.VerificationReport.Matched"))),
						"%false%"),

				builder.equal(root.get("copRequesterId"), copRequesterId),
				builder.between(root.get("createdAt"), startOfMonth, endOfMonth));
	}

}
