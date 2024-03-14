package com.techihub.job.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techihub.job.enums.Role;
import com.techihub.job.exception.InvalidPasswordException;
import com.techihub.job.exception.UserNotFoundException;
import com.techihub.job.model.*;
import com.techihub.job.model.UserProfile;
import com.techihub.job.repository.TokenRepository;
import com.techihub.job.repository.UserProfileRepository;
import com.techihub.job.repository.UserRepository;
import com.techihub.job.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
        private final UserRepository userRepository;
        private final TokenRepository tokenRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtTokenProvider jwtTokenProvider;
        private final AuthenticationManager authenticationManager;
        private final UserProfileRepository userProfileRepository;
        private final EmailServiceImpl emailService;

        @Value("${custom.validation.email.pattern}")
        private String emailPattern;

        @Value("${custom.validation.password.pattern}")
        private String passwordPattern;


        public ResponseEntity<Feedback> registerUser(UserRegistration userRegistration,Role role) throws MessagingException {
                        if (!userRegistration.getEmail().matches(emailPattern)) {
                                Feedback feedback = Feedback.builder()
                                        .timeStamp(LocalDateTime.now())
                                        .statusCode(HttpStatus.BAD_REQUEST.value())
                                        .status(HttpStatus.BAD_REQUEST)
                                        .reason("Bad Request")
                                        .message("Invalid email format")
                                        .data(null)
                                        .build();
                                return ResponseEntity.badRequest().body(feedback);
                        }

                        if (!userRegistration.getPassword().matches(passwordPattern)) {
                                Feedback feedback = Feedback.builder()
                                        .timeStamp(LocalDateTime.now())
                                        .statusCode(HttpStatus.BAD_REQUEST.value())
                                        .status(HttpStatus.BAD_REQUEST)
                                        .reason("Bad Request")
                                        .message("Password does not meet complexity requirements")
                                        .data(null)
                                        .build();
                                return ResponseEntity.badRequest().body(feedback);
                        }

                        if (!userRegistration.getPassword().equals(userRegistration.getConfirmPassword())) {
                                Feedback feedback = Feedback.builder()
                                        .timeStamp(LocalDateTime.now())
                                        .statusCode(HttpStatus.BAD_REQUEST.value())
                                        .status(HttpStatus.BAD_REQUEST)
                                        .reason("Bad Request")
                                        .message("Password and confirmation do not match")
                                        .data(null)
                                        .build();
                                return ResponseEntity.badRequest().body(feedback);
                        }

                        if (isEmailAlreadyRegistered(userRegistration.getEmail())) {
                                Feedback feedback = Feedback.builder()
                                        .timeStamp(LocalDateTime.now())
                                        .statusCode(HttpStatus.BAD_REQUEST.value())
                                        .status(HttpStatus.BAD_REQUEST)
                                        .reason("Bad Request")
                                        .message("Email address is already registered")
                                        .data(null)
                                        .build();
                                return ResponseEntity.badRequest().body(feedback);
                        }

                        String hashedPassword = passwordEncoder.encode(userRegistration.getPassword());
                        String uniqueUserID = UUID.randomUUID().toString();

                        UserProfile userProfile = new UserProfile();
                        userProfile.setUserID(uniqueUserID);
                        userProfileRepository.save(userProfile);

                        User user = new User();
                        user.setEmail(userRegistration.getEmail());
                        user.setPassword(hashedPassword);
                        user.setRole(Role.CANDIDATE);
                        user.setUserProfile(userProfile);

                        userRepository.save(user);

                        log.info("Confirm email address for registration");

                        if (user != null) {
                                LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);
                                String confirmationToken = UUID.randomUUID().toString();

                                user.setConfirmationToken(confirmationToken);
                                user.setTokenExpiration(expirationTime);

                                registerUser(userRegistration, Role.CANDIDATE);
                                log.info("Confirm email address for registration");

                                saveUser(user);
                                log.info("User has been saved");

                                String confirmationLink = "https://Techihub.io/confirm?token=" + confirmationToken;
                                String emailText = "Click the link to confirm your email: " + confirmationLink + " The Link expires in 10 minutes";
                                String emailSubject = "Techihub Email Confirmation";

                                emailService.sendConfirmationEmail(userRegistration.getEmail(),  emailSubject, emailText);
                                String loginRedirectUrl = "redirect:/login";
                                Feedback feedback = Feedback.builder()
                                        .timeStamp(LocalDateTime.now())
                                        .statusCode(HttpStatus.OK.value())
                                        .status(HttpStatus.OK)
                                        .reason("Success")
                                        .message("User registered successfully. Redirect to login: " + loginRedirectUrl)
                                        .data(null)
                                        .build();
                                return ResponseEntity.ok(feedback);
                        }

                        Feedback feedback = Feedback.builder()
                                .timeStamp(LocalDateTime.now())
                                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .reason("Internal Server Error")
                                .message("Failed to register user")
                                .data(null)
                                .build();

                        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(feedback);
                }

        public ResponseEntity<Feedback> registerEmployer(UserRegistration userRegistration, Role employer) throws MessagingException {
                if (!userRegistration.getEmail().matches(emailPattern)) {
                        Feedback feedback = Feedback.builder()
                                .timeStamp(LocalDateTime.now())
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .status(HttpStatus.BAD_REQUEST)
                                .reason("Bad Request")
                                .message("Invalid email format")
                                .data(null)
                                .build();
                        return ResponseEntity.badRequest().body(feedback);
                }

                if (!userRegistration.getPassword().matches(passwordPattern)) {
                        Feedback feedback = Feedback.builder()
                                .timeStamp(LocalDateTime.now())
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .status(HttpStatus.BAD_REQUEST)
                                .reason("Bad Request")
                                .message("Password does not meet complexity requirements")
                                .data(null)
                                .build();
                        return ResponseEntity.badRequest().body(feedback);
                }

                if (!userRegistration.getPassword().equals(userRegistration.getConfirmPassword())) {
                        Feedback feedback = Feedback.builder()
                                .timeStamp(LocalDateTime.now())
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .status(HttpStatus.BAD_REQUEST)
                                .reason("Bad Request")
                                .message("Password and confirmation do not match")
                                .data(null)
                                .build();
                        return ResponseEntity.badRequest().body(feedback);
                }

                if (isEmailAlreadyRegistered(userRegistration.getEmail())) {
                        Feedback feedback = Feedback.builder()
                                .timeStamp(LocalDateTime.now())
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .status(HttpStatus.BAD_REQUEST)
                                .reason("Bad Request")
                                .message("Email address is already registered")
                                .data(null)
                                .build();
                        return ResponseEntity.badRequest().body(feedback);
                }

                String hashedPassword = passwordEncoder.encode(userRegistration.getPassword());
                String uniqueUserID = UUID.randomUUID().toString();

                UserProfile userProfile = new UserProfile();
                userProfile.setUserID(uniqueUserID);
                userProfileRepository.save(userProfile);

                User user = new User();
                user.setEmail(userRegistration.getEmail());
                user.setPassword(hashedPassword);
                user.setUserProfile(userProfile);
                user.setRole(Role.EMPLOYER);
                userRepository.save(user);

                log.info("Confirm email address for registration");

                if (user != null) {
                        LocalDateTime expirationTime = LocalDateTime.now().plusMinutes(10);
                        String confirmationToken = UUID.randomUUID().toString();

                        user.setConfirmationToken(confirmationToken);
                        user.setTokenExpiration(expirationTime);

                        registerUser(userRegistration, Role.EMPLOYER);
                        log.info("Confirm email address for registration");

                        saveUser(user);
                        log.info("User has been saved");

                        String confirmationLink = "https://Techihub.io/confirm?token=" + confirmationToken;
                        String emailText = "Click the link to confirm your email: " + confirmationLink + " The Link expires in 10 minutes";
                        String emailSubject = "Techihub Email Confirmation";

                        emailService.sendConfirmationEmail(userRegistration.getEmail(), emailSubject, emailText);
                        String loginRedirectUrl = "redirect:/login";
                        Feedback feedback = Feedback.builder()
                                .timeStamp(LocalDateTime.now())
                                .statusCode(HttpStatus.OK.value())
                                .status(HttpStatus.OK)
                                .reason("Success")
                                .message("User registered successfully. Redirect to login: " + loginRedirectUrl)
                                .data(null)
                                .build();
                        return ResponseEntity.ok(feedback);
                }

                Feedback feedback = Feedback.builder()
                        .timeStamp(LocalDateTime.now())
                        .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .reason("Internal Server Error")
                        .message("Failed to register user")
                        .data(null)
                        .build();

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(feedback);
        }

        public LoginResponse login(LoginRequest request) {
                Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
                if (userOptional.isPresent()) {
                        User user = userOptional.get();
                        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                                UserProfile userProfile = user.getUserProfile();
                                String jwtToken = jwtTokenProvider.generateToken(user);
                                String refreshToken = jwtTokenProvider.generateRefreshToken(user);
                                saveUserToken(user, jwtToken);

                                return LoginResponse.builder()
                                        .accessToken(jwtToken)
                                        .refreshToken(refreshToken)
                                        .userId(userProfile.getUserId())
                                        .build();
                        } else {
                                throw new InvalidPasswordException("Invalid Username or password");
                        }
                } else {
                        throw new UserNotFoundException("User not found");
                }
        }



        public LoginResponse authenticate(LoginRequest request) {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );
                var user = userRepository.findByEmail(request.getEmail())
                        .orElseThrow();
                var jwtToken = jwtTokenProvider.generateToken(user);
                var refreshToken = jwtTokenProvider.generateRefreshToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, jwtToken);
                return LoginResponse.builder()
                        .accessToken(jwtToken)
                        .refreshToken(refreshToken)
                        .build();
        }

        private void saveUserToken(User user, String jwtToken) {
                var token = Token.builder()
                        .user(user)
                        .token(jwtToken)
                        .tokenType(TokenType.BEARER)
                        .expired(false)
                        .revoked(false)
                        .build();
                tokenRepository.save(token);
        }

        private void revokeAllUserTokens(User user) {
                var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
                if (validUserTokens.isEmpty())
                        return;
                validUserTokens.forEach(token -> {
                        token.setExpired(true);
                        token.setRevoked(true);
                });
                tokenRepository.saveAll(validUserTokens);
        }

        public void refreshToken(
                HttpServletRequest request,
                HttpServletResponse response
        ) throws IOException {
                final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
                final String refreshToken;
                final String userEmail;
                if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
                        return;
                }
                refreshToken = authHeader.substring(7);
                userEmail = jwtTokenProvider.extractUsername(refreshToken);
                if (userEmail != null) {
                        var user = this.userRepository.findByEmail(userEmail)
                                .orElseThrow();
                        if (jwtTokenProvider.isTokenValid(refreshToken, user)) {
                                var accessToken = jwtTokenProvider.generateToken(user);
                                revokeAllUserTokens(user);
                                saveUserToken(user, accessToken);
                                var authResponse = LoginResponse.builder()
                                        .accessToken(accessToken)
                                        .refreshToken(refreshToken)
                                        .build();
                                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                        }
                }
        }
        @Override
        public Boolean isEmailAlreadyRegistered(String email) {
                return userRepository.existsByEmail(email);
        }


        @Override
        public Optional<User> findByEmail(String email) {
                return userRepository.findByEmail(email);
        }

        @Override
        public void saveUser(User user) {
                userRepository.save(user);

        }
        @Override
        public ResponseEntity<Feedback> confirmEmail(String confirmationToken) {
                Optional<User> optionalUser = userRepository.findByConfirmationToken(confirmationToken);
                if (optionalUser.isPresent()) {
                        User user = optionalUser.get();

                        if (user != null && user.getEmailConfirmed() == null &&
                                confirmationToken.equals(user.getConfirmationToken()) && user.getTokenExpiration() != null &&
                                user.getTokenExpiration().isAfter(LocalDateTime.now())) {
                                user.setEmailConfirmed(true);

                        userRepository.save(user);
                        log.info("Email confirmed for user: " + user.getEmail());

                        Feedback feedback = Feedback.builder()
                                .timeStamp(LocalDateTime.now())
                                .statusCode(HttpStatus.OK.value())
                                .status(HttpStatus.OK)
                                .reason("Success")
                                .message("Email confirmed successfully")
                                .data(null)
                                .build();
                        return ResponseEntity.ok(feedback);

                } else {
                        log.error("Invalid or expired confirmation token");
                        Feedback feedback = Feedback.builder()
                                .timeStamp(LocalDateTime.now())
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .status(HttpStatus.BAD_REQUEST)
                                .reason("Bad Request")
                                .message("Invalid or expired confirmation token")
                                .data(null)
                                .build();
                        return ResponseEntity.badRequest().body(feedback);
                }
                } else {
                        log.error("User not found for confirmation token");
                        Feedback feedback = Feedback.builder()
                                .timeStamp(LocalDateTime.now())
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .status(HttpStatus.NOT_FOUND)
                                .reason("Not Found")
                                .message("User not found for confirmation token")
                                .data(null)
                                .build();
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(feedback);
                }
        }

        public ResponseEntity<Feedback> forgotPassword(String email) throws MessagingException {
                Optional<User> optionalUser = userRepository.findByEmail(email);
                if (optionalUser.isPresent()) {
                        User user = optionalUser.get();
                        String resetToken = UUID.randomUUID().toString().replaceAll("[^a-zA-Z0-9]", "").substring(0, 8);
                        resetToken = resetToken.replaceAll("\\s+", "");
                        user.setResetToken(resetToken);
                        userRepository.save(user);

                        String resetLink = "https://Techihub.io/reset-password";
                        String emailText = "Use the generated token to create a new Password: "  + resetToken + " Click the link provided " + resetLink;
                        String emailSubject = "Techihub Password Reset";

                        emailService.sendConfirmationEmail(email, emailSubject, emailText);

                        log.info("Password reset link sent to user: {}", email);

                        Feedback feedback = Feedback.builder()
                                .timeStamp(LocalDateTime.now())
                                .statusCode(HttpStatus.OK.value())
                                .status(HttpStatus.OK)
                                .reason("Success")
                                .message("Password reset link sent to email: " + email)
                                .data(null)
                                .build();
                        return ResponseEntity.ok(feedback);
                } else {
                        log.error("User not found for email: {}", email);
                        Feedback feedback = Feedback.builder()
                                .timeStamp(LocalDateTime.now())
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .status(HttpStatus.NOT_FOUND)
                                .reason("Not Found")
                                .message("User not found for email: " + email)
                                .data(null)
                                .build();
                        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(feedback);
                }
        }

        public ResponseEntity<Feedback> resetPassword(String resetToken, String newPassword) {
                Optional<User> optionalUser = userRepository.findByResetToken(resetToken);
                if (optionalUser.isPresent()) {
                        User user = optionalUser.get();
                        String hashedPassword = passwordEncoder.encode(newPassword);
                        user.setPassword(hashedPassword);
                        user.setResetToken(null);
                        userRepository.save(user);

                        log.info("Password reset successfully for user: {}", user.getEmail());

                        Feedback feedback = Feedback.builder()
                                .timeStamp(LocalDateTime.now())
                                .statusCode(HttpStatus.OK.value())
                                .status(HttpStatus.OK)
                                .reason("Success")
                                .message("Password reset successfully")
                                .data(null)
                                .build();
                        return ResponseEntity.ok(feedback);
                } else {
                        log.error("Invalid or expired reset token");
                        Feedback feedback = Feedback.builder()
                                .timeStamp(LocalDateTime.now())
                                .statusCode(HttpStatus.BAD_REQUEST.value())
                                .status(HttpStatus.BAD_REQUEST)
                                .reason("Bad Request")
                                .message("Invalid or expired reset token")
                                .data(null)
                                .build();
                        return ResponseEntity.badRequest().body(feedback);
                }
        }
        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return null;
        }
}
