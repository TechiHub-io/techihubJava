package com.techihub.job.service;

import com.techihub.job.model.Education;

public interface EducationService {
    Education createEducation(String userProfileId, Education education);
    Education updateEducation(String userProfileId, Long educationId, Education education);
    void deleteEducation(String userProfileId, Long educationId);
}
