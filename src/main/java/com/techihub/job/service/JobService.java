package com.techihub.job.service;

import com.techihub.job.model.Job;

import java.util.Collection;

public interface JobService {

    Job create(Job job);
    Job get(Long id);
    Collection<Job> list(int limit);
    Job update(Job job);
    Boolean delete(Long id);
}
