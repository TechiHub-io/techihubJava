package com.techihub.job.controller;

import com.techihub.job.model.Education;
import com.techihub.job.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-profiles/{userProfileId}/educations")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService educationService;

    @PostMapping
    public Education createEducation(@PathVariable String userProfileId, @RequestBody Education education) {
        return educationService.createEducation(userProfileId, education);
    }

    @PutMapping("/{educationId}")
    public Education updateEducation(@PathVariable String userProfileId, @PathVariable Long educationId, @RequestBody Education education) {
        return educationService.updateEducation(userProfileId, educationId, education);
    }

    @DeleteMapping("/{educationId}")
    public void deleteEducation(@PathVariable String userProfileId, @PathVariable Long educationId) {
        educationService.deleteEducation(userProfileId, educationId);
    }
}
