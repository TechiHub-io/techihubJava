package com.techihub.job.service.implementation;

import com.techihub.job.model.Experience;
import com.techihub.job.repository.ExperienceRepository;
import com.techihub.job.repository.UserProfileRepository;
import com.techihub.job.service.ExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import com.techihub.job.model.UserProfile;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepository experienceRepository;
    private final UserProfileRepository userProfileRepository;


    @Override
    public List<Experience> getAllExperiences() {
        return experienceRepository.findAll();
    }

    @Override
    public Experience getExperienceById(Long id) {
        Optional<Experience> experienceOptional = experienceRepository.findById(id);
        return experienceOptional.orElse(null);
    }


    @Override
    public Experience addExperience(String userID, Experience experience) {
        UserProfile userProfile = userProfileRepository.findByUserID(userID);
        if (userProfile != null) {
            experience.setUserProfile(userProfile);
            return experienceRepository.save(experience);
        }
        return null; // Or throw an exception indicating user not found
    }


    @Override
    public Experience updateExperience(Long id, Experience updatedExperience) {
        Optional<Experience> experienceOptional = experienceRepository.findById(id);
        if (experienceOptional.isPresent()) {
            Experience experience = experienceOptional.get();
            // Update fields here
            experience.setCompany(updatedExperience.getCompany());
            experience.setTitle(updatedExperience.getTitle());
            experience.setStartDate(updatedExperience.getStartDate());
            experience.setEndDate(updatedExperience.getEndDate());
            experience.setWorkSummary(updatedExperience.getWorkSummary());
            return experienceRepository.save(experience);
        }
        return null; // Or throw an exception indicating education not found
    }

    @Override
    public void deleteExperienceById(Long id) {
        experienceRepository.deleteById(id);
    }
}

