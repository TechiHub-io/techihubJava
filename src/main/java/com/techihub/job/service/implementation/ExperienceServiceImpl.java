package com.techihub.job.service.implementation;

import com.techihub.job.model.Experience;
import com.techihub.job.repository.ExperienceRepository;
import com.techihub.job.service.ExperienceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class ExperienceServiceImpl implements ExperienceService {
    private final ExperienceRepository experienceRepository;
    @Override
    public Experience getById(Long id) {
        return experienceRepository.findById(id).orElse(null);
    }

    @Override
    public List<Experience> getAllExperiences() {
        return experienceRepository.findAll();
    }

    @Override
    public Experience save(Experience experience) {
        experienceRepository.save(experience);
        return experience;
    }

    @Override
    public Experience delete(Long id) {
        experienceRepository.deleteById(id);
        return null;
    }

}
