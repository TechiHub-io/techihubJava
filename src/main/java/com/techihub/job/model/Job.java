package com.techihub.job.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
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
	  @JsonProperty("description")
	  @Column(columnDefinition = "TEXT", length = -1)
	  private String description;
	  private String location;
	  private double salary;
	  private String logoUpload;
	  private String companyName;
	  private String companyWebsiteLink;
	  @JsonProperty("about")
	  @Column(columnDefinition = "TEXT", length = -1)
	  private String about;
	  private String desires;
	  @JsonProperty("jobBenefits")
	  @Column(columnDefinition = "TEXT", length = -1)
	  private String jobBenefits;
	  private String jobType;
	  @JsonProperty("requirements")
	  @Column(columnDefinition = "TEXT", length = -1)
	  private String requirements;
	  private String deadline;
	  @JsonProperty("experience")
	  @Column(columnDefinition = "TEXT", length = -1)
	  private String experience;

	  @ManyToOne
	  @JoinColumn(name = "employer_id")
	  private User employer;
}
