package com.techihub.job.repository;

import com.techihub.job.model.User;
import com.techihub.job.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository <UserProfile, Long> {
	UserProfile findByUsername(String username);
	UserProfile findByEmail(String email);
	UserProfile findByPhoneNumber(String phoneNumber);

    UserProfile findByUser(User user);

	UserProfile findByUserID(String userID);
}
