package com.techihub.job.service;

import com.techihub.job.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    Boolean isEmailAlreadyRegistered(String email);

    void registerUser(User user);
    Optional<User> findByEmail(String email);
    void saveUser(User user);
    Optional<User> getUserByConfirmationToken(String confirmationToken);
}
