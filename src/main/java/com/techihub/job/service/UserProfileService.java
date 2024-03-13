package com.techihub.job.service;

import com.techihub.job.model.UserProfile;

import java.util.Optional;

public interface UserProfileService {
    UserProfile getById(Long id);
    UserProfile save(UserProfile userProfile);
    UserProfile getByEmail(String email);
    void delete(Long id);

    UserProfile getByPhoneNumber(String phoneNumber);
    UserProfile getByUsername(String phoneNumber);

    Optional<UserProfile> getByUserID(String userID);

}
