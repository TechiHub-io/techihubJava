package com.techihub.job.controller;

import com.techihub.job.model.*;
import com.techihub.job.service.UserService;
import com.techihub.job.service.implementation.UserServiceImpl;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final UserServiceImpl service;

    @PostMapping("/register/user")
    public ResponseEntity<Feedback> registerUser(@Valid @RequestBody UserRegistration userRegistration) throws MessagingException {
        return service.registerUser(userRegistration);
    }
    @PostMapping("/register/employer")
    public ResponseEntity<Feedback> registerEmployer(@Valid @RequestBody UserRegistration userRegistration) throws MessagingException {
        return service.registerEmployer(userRegistration);
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
    public String confirmEmail(@RequestParam("token") String confirmationToken) {
        userService.getUserByConfirmationToken(confirmationToken).ifPresent(user -> {
            user.setEmailConfirmed(true);
            //service.registerUser(user);
        });
        return "redirect:/login";
    }

}
