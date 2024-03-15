package com.techihub.job.service;

import com.techihub.job.model.Education;
import com.techihub.job.model.UserProfile;
import com.techihub.job.repository.EducationRepository;
import com.techihub.job.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EducationServiceImpl implements EducationService {

    private final EducationRepository educationRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public List<Education> getAllEducations() {
        return educationRepository.findAll();
    }

    @Override
    public Education getEducationById(Long id) {
        Optional<Education> educationOptional = educationRepository.findById(id);
        return educationOptional.orElse(null);
    }




    @Override
    public Education addEducation(String userID, Education education) {
        UserProfile userProfile = userProfileRepository.findByUserID(userID);
        if (userProfile != null) {
            education.setUserProfile(userProfile);
            return educationRepository.save(education);
        }
        return null; // Or throw an exception indicating user not found
    }


    @Override
    public Education updateEducation(Long id, Education updatedEducation) {
        Optional<Education> educationOptional = educationRepository.findById(id);
        if (educationOptional.isPresent()) {
            Education education = educationOptional.get();
            // Update fields here
            education.setCourse(updatedEducation.getCourse());
            education.setSchoolName(updatedEducation.getSchoolName());
            education.setStartDate(updatedEducation.getStartDate());
            education.setEndDate(updatedEducation.getEndDate());
            education.setSummary(updatedEducation.getSummary());
            return educationRepository.save(education);
        }
        return null; // Or throw an exception indicating education not found
    }

    @Override
    public void deleteEducationById(Long id) {
        educationRepository.deleteById(id);
    }
}
