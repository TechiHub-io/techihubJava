package com.techihub.job.service.implementation;


import com.techihub.job.model.Application;
import com.techihub.job.repository.ApplicationRepository;
import com.techihub.job.service.ApplicationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
@Transactional
public class ApplicationServiceImpl implements ApplicationService {
    private final ApplicationRepository applicationRepository;
    @Override
    public Application create(Application application) {
        //log.info("Making job Application: {}",  application.getjob());
        return applicationRepository.save(application);
    }
}
