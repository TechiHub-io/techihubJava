package com.techihub.job.repository;

import com.techihub.job.model.Job;
import com.techihub.job.model.User;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
	Job findByEmployer(User employer);
}
