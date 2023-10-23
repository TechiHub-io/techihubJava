package com.techihub.job.controller;


import com.techihub.job.model.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techihub.job.model.Application;
import com.techihub.job.service.ApplicationService;

import static java.time.LocalDateTime.now;
import static java.util.Map.of;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {
	 private final ApplicationService applicationService;

    @PostMapping("/apply/{job}")
    public ResponseEntity<Feedback> saveJob(@RequestBody Application applicationRequest, @PathVariable String job){
        return ResponseEntity.ok(
                Feedback.builder()
                        .timeStamp(now())
                        .data(of("Apply", applicationService.create(applicationRequest)))
                        .message("Application Sent")
                        .status(OK)
                        .statusCode(OK.value ())
                        .build()
        );
    }
    //standard and premium
}
