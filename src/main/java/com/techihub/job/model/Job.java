package com.techihub.job.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Job {
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;
	  private String title;
	  @Size(max = 1000, message = "Description must be 10000 characters or less")
	  private String description;
	  private String location;
	  private double salary;
	  private String imageUrl;
	  private String companyName;
	  private String companyUrl;
	  @Size(max = 1000, message = "About must be 10000 characters or less")
	  private String about;
	  @Size(max = 1000, message = "Requirements must be 10000 characters or less")
	  private String requirements;
	  @Size(max = 1000, message = "Desirable must be 10000 characters or less")
	  private String desirable;
	  @Size(max = 1000, message = "Benefits must be 10000 characters or less")
	  private String benefits;
	  private Boolean jobType;
	  @Size(max = 1000, message = "Experience must be 10000 characters or less")
	  private String experience;
	  private LocalDateTime expirationDate;


	  @ManyToOne
	  @JoinColumn(name = "employer_id")
	  private User employer;
}
