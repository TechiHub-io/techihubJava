package com.techihub.job.service;

public interface EmailService  {
    String sendConfirmationEmail(String to,String subject, String text);
}
