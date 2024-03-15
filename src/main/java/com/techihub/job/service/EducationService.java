package com.techihub.job.service;

import com.techihub.job.model.Education;

import java.util.List;

public interface EducationService {
    List<Education>getAllEducations();
    Education getEducationById(Long id);
    Education addEducation(String userID,Education education);
    Education updateEducation(Long id,Education updatedEducation);

    void deleteEducationById(Long Id);

}

