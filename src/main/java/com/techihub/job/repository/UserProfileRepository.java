package com.techihub.job.repository;

import com.techihub.job.model.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserProfileRepository extends JpaRepository <UserProfile, Long> {
	UserProfile findByUsername(String username);
	UserProfile findByEmail(String email);
	UserProfile findByPhoneNumber(String phoneNumber);
	//UserProfile findByUserID(String userID);
	Optional<UserProfile> findByUserID(String userID);
}
