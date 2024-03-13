package com.techihub.job.service;

import com.techihub.job.model.Experience;
import com.techihub.job.model.Feedback;
import org.springframework.http.ResponseEntity;

public interface ExperienceService {
    ResponseEntity<Feedback> createExperience(String userProfileId, Experience experience);
    ResponseEntity<Feedback> updateExperience(String userProfileId, Long experienceId, Experience experience);
    ResponseEntity<Feedback> deleteExperience(String userProfileId, Long experienceId);
}
