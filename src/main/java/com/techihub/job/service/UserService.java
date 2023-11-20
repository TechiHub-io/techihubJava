package com.techihub.job.service;

import com.techihub.job.model.*;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    Boolean isEmailAlreadyRegistered(String email);
    ResponseEntity<Feedback> registerUser(UserRegistration userRegistration) throws MessagingException;
    Optional<User> findByEmail(String email);
    void saveUser(User user);
    Optional<User> getUserByConfirmationToken(String confirmationToken);
}