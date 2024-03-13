package com.techihub.job.service.implementation;

import com.techihub.job.model.Education;
import com.techihub.job.model.UserProfile;
import com.techihub.job.repository.EducationRepository;
import com.techihub.job.repository.UserProfileRepository;
import com.techihub.job.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public Education createEducation(String userProfileId, Education education) {
        Optional<UserProfile> optionalUserProfile = userProfileRepository.findByUserID(userProfileId);
        if (optionalUserProfile.isPresent()) {
            UserProfile userProfile = optionalUserProfile.get();
            education.setUserProfile(userProfile);
            return educationRepository.save(education);
        } else {
            throw new IllegalArgumentException("User profile with ID " + userProfileId + " does not exist.");
        }
    }

    @Override
    public Education updateEducation(String userProfileId, Long educationId, Education education) {
        Optional<Education> optionalEducation = educationRepository.findById(educationId);
        if (optionalEducation.isPresent()) {
            Education existingEducation = optionalEducation.get();
            if (existingEducation.getUserProfile().getUserId().equals(userProfileId)) {
                education.setId(educationId);
                education.setUserProfile(existingEducation.getUserProfile());
                return educationRepository.save(education);
            } else {
                throw new IllegalArgumentException("Education with ID " + educationId + " does not belong to the user profile with ID " + userProfileId);
            }
        } else {
            throw new IllegalArgumentException("Education with ID " + educationId + " does not exist.");
        }
    }

    @Override
    public void deleteEducation(String userProfileId, Long educationId) {
        Optional<Education> optionalEducation = educationRepository.findById(educationId);
        if (optionalEducation.isPresent()) {
            Education education = optionalEducation.get();
            if (education.getUserProfile().getUserId().equals(userProfileId)) {
                educationRepository.delete(education);
            } else {
                throw new IllegalArgumentException("Education with ID " + educationId + " does not belong to the user profile with ID " + userProfileId);
            }
        } else {
            throw new IllegalArgumentException("Education with ID " + educationId + " does not exist.");
        }
    }
}
