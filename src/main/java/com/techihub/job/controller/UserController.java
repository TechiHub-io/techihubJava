package com.techihub.job.controller;

import com.techihub.job.model.*;
import com.techihub.job.repository.UserProfileRepository;
import com.techihub.job.repository.UserRepository;
import com.techihub.job.service.UserService;
import com.techihub.job.service.implementation.EmailServiceImpl;
import com.techihub.job.service.implementation.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import javax.mail.MessagingException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final EmailServiceImpl emailService;
    private final UserServiceImpl service;
    private final UserProfileRepository userProfileRepository;
    private final UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Value("${custom.validation.email.pattern}")
    private String emailPattern;

    @Value("${custom.validation.password.pattern}")
    private String passwordPattern;



    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistration userRegistration) throws MessagingException {
        if (!userRegistration.getEmail().matches(emailPattern)) {
            return ResponseEntity.badRequest().body("Invalid email format");
        }
        if (!userRegistration.getPassword().matches(passwordPattern)) {
            return ResponseEntity.badRequest().body("Password does not meet complexity requirements");
        }
        if (userService.isEmailAlreadyRegistered(userRegistration.getEmail())) {
            return ResponseEntity.badRequest().body("Email address is already registered");
        }
        String hashedPassword = passwordEncoder.encode(userRegistration.getPassword());

        UserProfile userProfile = new UserProfile();
        userProfileRepository.save(userProfile);

        User user = new User();
        user.setEmail(userRegistration.getEmail());
        user.setPassword(hashedPassword);
        user.setUserProfile(userProfile);
        userRepository.save(user);

        log.info("Confirm email address for registration");

        if (user != null) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 10);
        Date expirationTime = calendar.getTime();

        String confirmationToken = UUID.randomUUID().toString();

         userService.registerUser(user);
         log.info("Confirm email address for registration");

        userService.saveUser(user);
        log.info("User has been saved");

        String confirmationLink = "https://Techihub.io/confirm?token=" + confirmationToken;
        String emailText = "Click the link to confirm your email: " + confirmationLink + " The Link expires in 10 minutes";

        emailService.sendConfirmationEmail(userRegistration.getEmail(), "Email Confirmation", emailText);
        String loginRedirectUrl = "redirect:/login";
        return ResponseEntity.ok("User registered successfully. Redirect to login: " + loginRedirectUrl);
        }


        return ResponseEntity.ok("User registered successfully");
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
            userService.registerUser(user);
        });
        return "redirect:/login";
    }

}
