package com.techihub.job.controller;

import com.techihub.job.enums.Role;
import com.techihub.job.model.*;
import com.techihub.job.repository.UserRepository;
import com.techihub.job.service.UserService;
import com.techihub.job.service.implementation.UserServiceImpl;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserServiceImpl service;
    private final UserRepository userRepository;

    @PostMapping("/register/user")
    public ResponseEntity<Feedback> registerUser(@Valid @RequestBody UserRegistration userRegistration) throws MessagingException {
        return service.registerUser(userRegistration, Role.CANDIDATE);
    }
    @PostMapping("/register/employer")
    public ResponseEntity<Feedback> registerEmployer(@Valid @RequestBody UserRegistration userRegistration) throws MessagingException {
        return service.registerEmployer(userRegistration,Role.EMPLOYER);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> register(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(service.login(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponse> authenticate(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/confirm")
    public ResponseEntity<Feedback> confirmEmail(@RequestParam("token") String confirmationToken) {
        return service.confirmEmail(confirmationToken);
    }

    @PostMapping("/update-role/{userId}")
    public ResponseEntity<Feedback> updateRole(@PathVariable Long userId, @RequestParam Role newRole) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setRole(newRole);
            userRepository.save(user);

            Feedback feedback = Feedback.builder()
                    .timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.OK.value())
                    .status(HttpStatus.OK)
                    .reason("Success")
                    .message("User role updated successfully")
                    .data(null)
                    .build();
            return ResponseEntity.ok(feedback);
        } else {
            Feedback feedback = Feedback.builder()
                    .timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .status(HttpStatus.NOT_FOUND)
                    .reason("Not Found")
                    .message("User not found")
                    .data(null)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(feedback);
        }
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<Feedback> forgotPassword(@RequestParam String email) throws MessagingException {
        return service.forgotPassword(email);
    }
    @PostMapping("/reset-password")
    public ResponseEntity<Feedback> resetPassword(@RequestBody ResetPassword resetPassword) {
        String resetToken = resetPassword.getResetToken();
        String newPassword = resetPassword.getNewPassword();
        String confirmPassword = resetPassword.getConfirmPassword();
        if (!newPassword.equals(confirmPassword)) {
            Feedback feedback = Feedback.builder()
                    .timeStamp(LocalDateTime.now())
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .status(HttpStatus.BAD_REQUEST)
                    .reason("Bad Request")
                    .message("New password and confirm password do not match")
                    .data(null)
                    .build();
            return ResponseEntity.badRequest().body(feedback);
        }
        return service.resetPassword(resetToken, newPassword);
    }
}