package com.techihub.job.service;

import com.techihub.job.model.Experience;
import com.techihub.job.model.UserProfile;

import java.util.List;

public interface ExperienceService {
   Experience getById(Long id);
    List<Experience>getAllExperiences();
    Experience save(Experience experience);
    Experience delete(Long id);

}
