package com.techihub.job.controller;

import com.techihub.job.model.Education;
import com.techihub.job.service.EducationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/educations")
public class EducationController {

    private final EducationService educationService;

    @GetMapping
    public ResponseEntity<List<Education>> getAllEducations() {
        List<Education> educations = educationService.getAllEducations();
        return new ResponseEntity<>(educations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Education> getEducationById(@PathVariable("id") Long id) {
        Education education = educationService.getEducationById(id);
        if (education != null) {
            return new ResponseEntity<>(education, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Education> addEducation(@PathVariable("userId") String userId,
                                                  @RequestBody Education education) {
        Education addedEducation = educationService.addEducation(userId, education);
        if (addedEducation != null) {
            return new ResponseEntity<>(addedEducation, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Education> updateEducation(@PathVariable("id") Long id,
                                                     @RequestBody Education updatedEducation) {
        Education education = educationService.updateEducation(id, updatedEducation);
        if (education != null) {
            return new ResponseEntity<>(education, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEducationById(@PathVariable("id") Long id) {
        educationService.deleteEducationById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
