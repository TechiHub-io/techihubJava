package com.techihub.job.controller;

import com.techihub.job.model.Experience;
import com.techihub.job.model.Feedback;
import com.techihub.job.model.UserProfile;
import com.techihub.job.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/api/user-profile")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;

    @GetMapping("/{userID}")
    public ResponseEntity<Feedback> getUserProfile(@PathVariable String userID) {
        Optional<UserProfile> userProfile = userProfileService.getByUserID(userID);
        Feedback feedback = Feedback.builder().timeStamp(LocalDateTime.now()).build();

        if (userProfile != null) {
            feedback.setStatus(HttpStatus.OK);
            feedback.setStatusCode(HttpStatus.OK.value());
            feedback.setMessage("User profile found");
            feedback.setData(Map.of("userProfile", userProfile));
        } else {
            feedback.setStatus(HttpStatus.NOT_FOUND);
            feedback.setStatusCode(HttpStatus.NOT_FOUND.value());
            feedback.setMessage("User profile not found");
        }

        return ResponseEntity.status(feedback.getStatus()).body(feedback);
    }



    @GetMapping("/byEmail")
    public ResponseEntity<Feedback> getUserProfileByEmail(@RequestParam String email) {
        UserProfile userProfile = userProfileService.getByEmail(email);
        Feedback.FeedbackBuilder feedbackBuilder = Feedback.builder().timeStamp(LocalDateTime.now());

        if (userProfile != null) {
            feedbackBuilder.status(HttpStatus.OK);
            feedbackBuilder.statusCode(HttpStatus.OK.value());
            feedbackBuilder.message("User profile found");
            feedbackBuilder.data(Map.of("userProfile", userProfile));
        } else {
            feedbackBuilder.status(HttpStatus.NOT_FOUND);
            feedbackBuilder.statusCode(HttpStatus.NOT_FOUND.value());
            feedbackBuilder.message("User profile not found");
        }

        Feedback feedback = feedbackBuilder.build();

        return ResponseEntity.status(feedback.getStatus()).body(feedback);
    }


    @GetMapping("/byPhoneNumber")
    public ResponseEntity<UserProfile> getUserProfileByPhoneNumber(@RequestParam String phoneNumber) {
        UserProfile userProfile = userProfileService.getByPhoneNumber(phoneNumber);

        if (userProfile != null) {
            return ResponseEntity.ok(userProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/update-profile-picture/{userID}")
    public ResponseEntity<UserProfile> updateProfilePicture(@PathVariable String userID, MultipartFile file) {
        Optional<UserProfile> userProfileOptional = userProfileService.getByUserID(userID);
        if (userProfileOptional.isPresent()) {
            UserProfile userProfile = userProfileOptional.get();
            try {
                userProfile.setProfilePicture(file.getBytes());
                userProfileService.save(userProfile);
                return ResponseEntity.ok(userProfile);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userProfile);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/delete-profile-picture/{userID}")
    public ResponseEntity<UserProfile> deleteProfilePicture(@PathVariable String userID) {
        Optional<UserProfile> userProfileOptional = userProfileService.getByUserID(userID);
        if (userProfileOptional.isPresent()) {
            UserProfile userProfile = userProfileOptional.get();
            userProfile.setProfilePicture(null);
            userProfileService.save(userProfile);
            return ResponseEntity.ok(userProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PutMapping("/update-name-role/{userID}")
    public ResponseEntity<UserProfile> updateNameAndRole(
            @PathVariable String userID,
            @RequestBody UserProfile updatedUserProfile
    ) {
        Optional<UserProfile> userProfileOptional = userProfileService.getByUserID(userID);
        if (userProfileOptional.isPresent()) {
            UserProfile userProfile = userProfileOptional.get();
            userProfile.setFirstName(updatedUserProfile.getFirstName());
            userProfile.setLastName(updatedUserProfile.getLastName());
            userProfile.setUsername(updatedUserProfile.getUsername());
            userProfile.setAddress(updatedUserProfile.getAddress());
            userProfile.setEmail(updatedUserProfile.getEmail());
            userProfile.setPhoneNumber(updatedUserProfile.getPhoneNumber());
            userProfile.setRoleName(updatedUserProfile.getRoleName());
            userProfile.setGithubUrl(updatedUserProfile.getGithubUrl());
            userProfile.setLinkedinUrl(updatedUserProfile.getLinkedinUrl());
            userProfile.setAbout(updatedUserProfile.getAbout());
            userProfileService.save(userProfile);
            return ResponseEntity.ok(userProfile);
        } else {
            UserProfile errorUserProfile = new UserProfile();
            errorUserProfile.setFirstName("User profile not found for ID: " + userID);
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(errorUserProfile);
        }
    }
}
