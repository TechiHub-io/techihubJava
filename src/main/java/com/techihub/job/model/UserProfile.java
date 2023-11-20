package com.techihub.job.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;

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

    public String getUserId() {
        return this.userID;
    }
    /*@OneToMany(mappedBy = "userProfile")
    private List<Experience> experiences = new ArrayList<>();


     @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonProperty("documents")
    private List<Document> documents = new ArrayList<>();*/


}
