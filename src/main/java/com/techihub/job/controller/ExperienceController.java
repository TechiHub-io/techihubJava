package com.techihub.job.controller;

import com.techihub.job.model.UserProfile;
import com.techihub.job.service.ExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.techihub.job.model.Experience;

import java.util.List;

@RestController
@RequestMapping("/api/experiences")
@RequiredArgsConstructor
public class ExperienceController {
    private final ExperienceService experienceService;

    @GetMapping("/{id}")
    public ResponseEntity<Experience> getExperience(@PathVariable Long id) {
        Experience experience = experienceService.getById(id);
        if (experience != null) {
            return ResponseEntity.ok(experience);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Experience>> getAllExperiences() {
        List<Experience> experiences = experienceService.getAllExperiences();
        return ResponseEntity.ok(experiences);
    }

    @PostMapping("/create")
    public ResponseEntity<Experience> createExperience(@RequestBody Experience experience) {
        experienceService.save(experience);
        return ResponseEntity.ok(experience);
    }

    /*@PutMapping("/update/{id}")
    public ResponseEntity<Experience> updateExperience(@PathVariable Long id, @RequestBody Experience updatedExperience) {
        Experience experience = experienceService.getById(id);
        if (experience != null) {
            experience.setTitle(updatedExperience.getTitle());
            experience.setCompany(updatedExperience.getCompany());
            experience.setStartDate(updatedExperience.getStartDate());
            experience.setEndDate(updatedExperience.getEndDate());
            experience.setWorkSummary(updatedExperience.getWorkSummary());
            experienceService.save(experience);
            return ResponseEntity.ok(experience);
        } else {
            return ResponseEntity.notFound().build();
        }

    }*/

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteExperience(@PathVariable Long id) {
        Experience experience = experienceService.getById(id);
        if (experience != null) {
            experienceService.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

