package com.techihub.job.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("username")
    private String username;

    @JsonProperty("address")
    private String address;

    @JsonProperty("email")
    private String email;

    @JsonProperty("role_name")
    private String roleName;

    @OneToOne(mappedBy = "userProfile")
    @JsonIgnore
    private User user;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @Lob
    @JsonProperty("profile_picture")
    private byte[] profilePicture;

    @JsonProperty("user_id")
    private String userID;

    @JsonProperty("githubUrl")
    private String githubUrl;

    @JsonProperty("linkedinUrl")
    private String linkedinUrl;

    @Column(length = 1000)
    @JsonProperty("about")
    private String about;

    public String getUserId() {
        return this.userID;
    }
}
