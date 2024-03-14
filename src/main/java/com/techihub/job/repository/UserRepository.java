package com.techihub.job.repository;

import com.techihub.job.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
    Optional<User> findByConfirmationToken(String confirmationToken);
    Optional<User> findByResetToken(String resetToken);
}