package com.techihub.job.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.techihub.job.exception.InvalidPasswordException;
import com.techihub.job.exception.UserNotFoundException;
import com.techihub.job.model.*;
import com.techihub.job.model.UserProfile;
import com.techihub.job.repository.TokenRepository;
import com.techihub.job.repository.UserProfileRepository;
import com.techihub.job.repository.UserRepository;
import com.techihub.job.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
        private final UserRepository userRepository;
        private final TokenRepository tokenRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtTokenProvider jwtTokenProvider;
        private final AuthenticationManager authenticationManager;
        private final UserProfileRepository userProfileRepository;

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
        public void registerUser(User user) {
                userRepository.save(user);
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
        public Optional<User> getUserByConfirmationToken(String confirmationToken) {
                return userRepository.findByConfirmationToken(confirmationToken);
        }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return null;
        }
}
