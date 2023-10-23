package com.techihub.job.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import com.techihub.job.enums.Role;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistration {
        private String firstname;
        private String lastname;
        private String email;
        private String password;
        private Role role;

}
