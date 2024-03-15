package com.techihub.job.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonProperty("company")
    private String company;
    @JsonProperty("title")
    private String title;
    @JsonProperty("startDate")
    private LocalDate startDate;
    @JsonProperty("endDate")
    private LocalDate endDate;
    @JsonProperty("workSummary")
    @Size(max = 1000, message = "Experience must be 10000 characters or less")
    private String workSummary;

    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    @JsonIgnore
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private UserProfile userProfile;
}