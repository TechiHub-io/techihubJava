package com.techihub.job.service;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;


import java.util.Collections;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Implement your authentication logic here
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // Replace this logic with your actual authentication logic
        if (username.equals("employee") && password.equals("password")) {
            return new UsernamePasswordAuthenticationToken(username, password, Collections.singleton(new SimpleGrantedAuthority("EMPLOYEE")));
        } else if (username.equals("employer") && password.equals("password")) {
            return new UsernamePasswordAuthenticationToken(username, password, Collections.singleton(new SimpleGrantedAuthority("EMPLOYER")));
        }

        // If authentication fails, throw an exception
       // throw new CustomAuthenticationException("Authentication failed");
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

