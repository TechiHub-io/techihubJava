package com.techihub.job.service;

import com.techihub.job.model.Experience;
import com.techihub.job.model.UserProfile;

import java.util.List;

public interface ExperienceService {
    List<Experience>getAllExperiences();
    Experience getExperienceById(Long id);
    Experience addExperience(String userID,Experience experience);
    Experience updateExperience(Long id,Experience updatedExperience);
    void deleteExperienceById(Long id);

}