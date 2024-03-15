package com.techihub.job.service.implementation;

import com.techihub.job.model.Job;
import com.techihub.job.repository.JobRepository;
import com.techihub.job.service.JobService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.lang.Boolean.TRUE;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class JobServiceImpl implements JobService {
	private final  JobRepository jobRepository;

    @Override
    public Job create(Job job) {
        log.info("Saving new job: {}",  job.getTitle());
        job.setLogoUpload(setServerImageUrl());
        return jobRepository.save(job);
    }
    @Override
    public Job get(Long id) {
        log.info("Fetching Job by ID: {}", id);
        Optional<Job> jobOptional = jobRepository.findById(id);
        if (jobOptional.isPresent()) {
            return jobOptional.get();
        } else {
            // Handle the case where the Optional is empty
            throw new NoSuchElementException("Job not found for ID: " + id);
        }
    }


    @Override
    public Collection<Job> list(int limit) {
        log.info("Fetching all jobs");
        return jobRepository.findAll(PageRequest.of(0,limit)).toList();
    }

    @Override
    public Job update(Job job) {
        log.info("Updating job: {}",  job.getId());
        return jobRepository.save(job);
    }

    @Override
    public Boolean delete(Long id) {
        log.info("Deleting Job by ID: {}", id);
        jobRepository.deleteById(id);
        return TRUE ;
    }

    private String setServerImageUrl() {
        return null;
    }
}
