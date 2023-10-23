package com.techihub.job.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Application {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	 private String name;
	 private String resume; // Store resume data or file path
	 private String coverLetter;
	 @ManyToOne
	 @JoinColumn(name = "job_id")
	 private Job job;
	 @ManyToOne
	 @JoinColumn(name = "applicant_id")
	 private User applicant;
}
