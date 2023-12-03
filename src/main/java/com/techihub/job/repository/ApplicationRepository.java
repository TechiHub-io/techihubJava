package com.techihub.job.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techihub.job.model.Application;
import com.techihub.job.model.Job;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository <Application, Long> {
	 List<Application> findByJob(Job job);
}