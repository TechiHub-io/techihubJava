package com.techihub.job.service.implementation;

import com.techihub.job.model.Experience;
import com.techihub.job.model.Feedback;
import com.techihub.job.model.UserProfile;
import com.techihub.job.repository.ExperienceRepository;
import com.techihub.job.repository.UserProfileRepository;
import com.techihub.job.service.ExperienceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Slf4j
public class ExperienceServiceImpl implements ExperienceService {
    private final ExperienceRepository experienceRepository;
    private final UserProfileRepository userProfileRepository;

    @Override
    public ResponseEntity<Feedback> createExperience(String userProfileId, Experience experience) {
        try {
            Optional<UserProfile> optionalUserProfile = userProfileRepository.findByUserID(userProfileId);
            if (optionalUserProfile.isPresent()) {
                UserProfile userProfile = optionalUserProfile.get();
                experience.setUserProfile(userProfile);
                Experience savedExperience = experienceRepository.save(experience);
                Feedback feedback = Feedback.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.CREATED.value())
                        .status(HttpStatus.CREATED)
                        .reason("Created")
                        .message("Experience created successfully")
                        .data((Map<?, ?>) savedExperience)
                        .build();
                return ResponseEntity.status(HttpStatus.CREATED).body(feedback);
            } else {
                throw new IllegalArgumentException("User profile with ID " + userProfileId + " does not exist.");
            }
        } catch (Exception e) {
            Feedback feedback = Feedback.builder()
                    .timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .reason("Internal Server Error")
                    .message("Failed to create experience: " + e.getMessage())
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(feedback);
        }
    }




    @Override
    public ResponseEntity<Feedback> updateExperience(String userProfileId, Long experienceId, Experience experience) {
        try {
            Optional<Experience> optionalExperience = experienceRepository.findById(experienceId);
            if (optionalExperience.isPresent()) {
                Experience existingExperience = optionalExperience.get();
                // Ensure that the experience belongs to the given user profile
                if (existingExperience.getUserProfile().getUserId().equals(userProfileId)) {
                    experience.setId(experienceId);
                    experience.setUserProfile(existingExperience.getUserProfile());
                    Experience updatedExperience = experienceRepository.save(experience);
                    Feedback feedback = Feedback.builder()
                            .timeStamp(LocalDateTime.now())
                            .statusCode(HttpStatus.OK.value())
                            .status(HttpStatus.OK)
                            .reason("OK")
                            .message("Experience updated successfully")
                            .data((Map<?, ?>) updatedExperience)
                            .build();
                    return ResponseEntity.ok().body(feedback);
                } else {
                    // Handle if experience doesn't belong to the user profile
                    throw new IllegalArgumentException("Experience with ID " + experienceId + " does not belong to the user profile with ID " + userProfileId);
                }
            } else {
                // Handle if experience doesn't exist
                throw new IllegalArgumentException("Experience with ID " + experienceId + " does not exist.");
            }
        } catch (Exception e) {
            Feedback feedback = Feedback.builder()
                    .timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .reason("Internal Server Error")
                    .message("Failed to update experience: " + e.getMessage())
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(feedback);
        }
    }

    @Override
    public ResponseEntity<Feedback> deleteExperience(String userProfileId, Long experienceId) {
        try {
            Optional<Experience> optionalExperience = experienceRepository.findById(experienceId);
            if (optionalExperience.isPresent()) {
                Experience experience = optionalExperience.get();
                if (experience.getUserProfile().getUserId().equals(userProfileId)) {
                    experienceRepository.delete(experience);
                    Feedback feedback = Feedback.builder()
                            .timeStamp(LocalDateTime.now())
                            .statusCode(HttpStatus.NO_CONTENT.value())
                            .status(HttpStatus.NO_CONTENT)
                            .reason("No Content")
                            .message("Experience deleted successfully")
                            .data(null)
                            .build();
                    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(feedback);
                } else {
                    throw new IllegalArgumentException("Experience with ID " + experienceId + " does not belong to the user profile with ID " + userProfileId);
                }
            } else {
                // Handle if experience doesn't exist
                throw new IllegalArgumentException("Experience with ID " + experienceId + " does not exist.");
            }
        } catch (Exception e) {
            Feedback feedback = Feedback.builder()
                    .timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .reason("Internal Server Error")
                    .message("Failed to delete experience: " + e.getMessage())
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(feedback);
        }
    }
}
