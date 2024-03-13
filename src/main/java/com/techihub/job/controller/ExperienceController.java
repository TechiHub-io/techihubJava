package com.techihub.job.controller;

import com.techihub.job.model.Experience;
import com.techihub.job.model.Feedback;
import com.techihub.job.service.ExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-profiles/{userProfileId}/experiences")
@RequiredArgsConstructor
public class ExperienceController {

    private final ExperienceService experienceService;


    @PostMapping
    public ResponseEntity<Feedback> createExperience(@PathVariable String userProfileId, @RequestBody Experience experience) {
        return experienceService.createExperience(userProfileId, experience);
    }

    @PutMapping("/{experienceId}")
    public ResponseEntity<Feedback>  updateExperience(@PathVariable String userProfileId, @PathVariable Long experienceId, @RequestBody Experience experience) {
        return experienceService.updateExperience(userProfileId, experienceId, experience);
    }

    @DeleteMapping("/{experienceId}")
    public ResponseEntity<Feedback> deleteExperience(@PathVariable String userProfileId, @PathVariable Long experienceId) {
       return experienceService.deleteExperience(userProfileId, experienceId);
    }
}
