package com.techihub.job.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResetPassword {
    private String resetToken;
    private String newPassword;
    private String confirmPassword;
}