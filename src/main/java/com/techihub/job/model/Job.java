package com.techihub.job.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	  private String description;
	  private String location;
	  private double salary;
	  private String imageUrl;
	  private String companyName;
	  private String companyUrl;
	  private String about;
	  private String position;

	  @ManyToOne
	  @JoinColumn(name = "employer_id")
	  private User employer;
}
