package com.techihub.job.controller;

import com.techihub.job.model.Experience;
import com.techihub.job.model.UserProfile;
import com.techihub.job.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/user-profile")
@RequiredArgsConstructor
public class UserProfileController {
    private final UserProfileService userProfileService;

    @GetMapping("/{id}")
    @Secured("ROLE_USER")
    public ResponseEntity<UserProfile> getUserProfile(@PathVariable String userID) {
        UserProfile userProfile = userProfileService.getByUserID(userID);
        if (userProfile != null) {
            return ResponseEntity.ok(userProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/byEmail")
    @Secured("ROLE_USER")
    public ResponseEntity<UserProfile> getUserProfileByEmail(@RequestParam String email) {
        UserProfile userProfile = userProfileService.getByEmail(email);

        if (userProfile != null) {
            return ResponseEntity.ok(userProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/byPhoneNumber")
    @Secured("ROLE_USER")
    public ResponseEntity<UserProfile> getUserProfileByPhoneNumber(@RequestParam String phoneNumber) {
        UserProfile userProfile = userProfileService.getByPhoneNumber(phoneNumber);

        if (userProfile != null) {
            return ResponseEntity.ok(userProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @PostMapping("/add-experience")
    public ResponseEntity<UserProfile> addExperience(@RequestBody Experience experience, @PathVariable Long userProfileId) {
        // Retrieve the user profile by ID
        UserProfile userProfile = userProfileService.getById(userProfileId);
        if (userProfile != null) {
            // Add the experience to the user's profile
            userProfile.getExperiences().add(experience);
            // Save the updated profile
            userProfileService.save(userProfile);
            return ResponseEntity.ok(userProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/update-profile-picture/{id}")
    public ResponseEntity<UserProfile> updateProfilePicture(@PathVariable String userID, @RequestParam("file") MultipartFile file) {
        UserProfile userProfile = userProfileService.getByUserID(userID);

        if (userProfile != null) {
            try {
                userProfile.setProfilePicture(file.getBytes());
                userProfileService.save(userProfile);
                return ResponseEntity.ok(userProfile);
            } catch (IOException e) {
                return ResponseEntity.notFound().build();
                //return ResponseEntity.badRequest().body("Failed to upload the profile picture: " + e.getMessage());
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete-profile-picture/{id}")
    public ResponseEntity<UserProfile> deleteProfilePicture(@PathVariable Long id) {
        UserProfile userProfile = userProfileService.getById(id);

        if (userProfile != null) {
            // Set the profile picture to null (or delete it as needed)
            userProfile.setProfilePicture(null);
            // Save the updated profile
            userProfileService.save(userProfile);
            return ResponseEntity.ok(userProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    // Implement endpoints for updating profile picture and other profile details as needed
}

