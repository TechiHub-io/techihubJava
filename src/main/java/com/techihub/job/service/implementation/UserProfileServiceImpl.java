package com.techihub.job.service.implementation;

import com.techihub.job.model.UserProfile;
import com.techihub.job.repository.UserProfileRepository;
import com.techihub.job.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileServiceImpl implements UserProfileService {
    private final UserProfileRepository userProfileRepository;

    @Override
    public UserProfile getById(Long id) {
        return userProfileRepository.findById(id).orElse(null);
    }

    @Override
    public UserProfile save(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }

    @Override
    public UserProfile getByEmail(String email) {
        return userProfileRepository.findByEmail(email);
    }

    @Override
    public void delete(Long id) {
        userProfileRepository.deleteById(id);
    }

    @Override
    public UserProfile getByPhoneNumber(String phoneNumber) {
        return userProfileRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public UserProfile getByUsername(String phoneNumber) {
        return userProfileRepository.findByUsername(phoneNumber);
    }

    @Override
    public UserProfile getByUserID(String userID) {
        return userProfileRepository.findByUserID(userID);
    }
}

