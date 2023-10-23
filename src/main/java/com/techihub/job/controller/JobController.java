package com.techihub.job.controller;

import com.techihub.job.model.Feedback;
import com.techihub.job.model.Job;
import com.techihub.job.service.implementation.JobServiceImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.time.LocalDateTime.now;
import static java.util.Map.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/techihub")
@RequiredArgsConstructor
public class JobController {
	private final JobServiceImpl jobService;
	
    @GetMapping("/list")
    public ResponseEntity<Feedback> getJobs(){
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
        	Feedback.builder()
        		    .timeStamp(now())
                    .data(of("jobs", jobService.list(30 )))
                    .message("Jobs Retrieved")
                    .status(OK)
                    .statusCode(OK.value())
                    .build()
        );
    }
    
    @PostMapping("/save")
    public ResponseEntity<Feedback> saveJob(@RequestBody Job job){
        return ResponseEntity.ok(
                Feedback.builder()
                		.timeStamp(now())
                        .data(of("job", jobService.create(job)))
                        .message("Job Created")
                        .status(OK)
                        .statusCode(OK.value ())
                        .build()
        );
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Feedback> getJobs(@PathVariable Long id) {
        return ResponseEntity.ok(
                Feedback.builder()
                        .timeStamp(now())
                        .data(of("job", jobService.get(id)))
                        .message("Job Retrieved")
                        .status(OK)
                        .statusCode(OK.value())
                        .build()
        );
    }

}
