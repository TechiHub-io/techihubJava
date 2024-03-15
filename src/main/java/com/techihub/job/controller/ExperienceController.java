package com.techihub.job.controller;

import com.techihub.job.service.ExperienceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.techihub.job.model.Experience;

import java.util.List;



import org.springframework.http.HttpStatus;


@RestController
@RequiredArgsConstructor
@RequestMapping("/experience")
public class ExperienceController {

    private final ExperienceService experienceService;

    @GetMapping
    public ResponseEntity<List<Experience>> getAllExperiences() {
        List<Experience> experiences = experienceService.getAllExperiences();
        return new ResponseEntity<>(experiences, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Experience> getEducationById(@PathVariable("id") Long id) {
        Experience experience = experienceService.getExperienceById(id);
        if (experience != null) {
            return new ResponseEntity<>(experience, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Experience> addExperience(@PathVariable("userId") String userId,
                                                  @RequestBody Experience experience) {
        Experience addedExperience = experienceService.addExperience(userId, experience);
        if (addedExperience != null) {
            return new ResponseEntity<>(addedExperience, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Experience> updateExperience(@PathVariable("id") Long id,
                                                     @RequestBody Experience updatedExperience) {
        Experience experience = experienceService.updateExperience(id, updatedExperience);
        if (experience != null) {
            return new ResponseEntity<>(experience, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExperienceById(@PathVariable("id") Long id) {
        experienceService.deleteExperienceById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}


