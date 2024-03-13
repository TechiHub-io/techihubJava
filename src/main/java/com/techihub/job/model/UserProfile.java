package com.techihub.job.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "user_profile")
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

    @OneToOne(mappedBy = "userProfile", cascade = CascadeType.ALL)
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

    @JsonProperty("about")
    @Size(max = 1000, message = "About must be 10000 characters or less")
    private String about;

   @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL)
    @JsonProperty("experience")
    private List<Experience> experienceList = new ArrayList<>();

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL)
    @JsonProperty("education")
    private List<Education> educationList = new ArrayList<>();

    public String getUserId() {
        return this.userID;
    }
}
