package com.techihub.job.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String address;
    private String email;
    @OneToOne(mappedBy = "userProfile")
    private User user;
    private String phoneNumber;
    @Lob
    private byte[] profilePicture;
    private String userID;

    public String getUserId() {
        return this.userID;
    }

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL)
    private List<Experience> experiences = new ArrayList<>();

    @OneToMany(mappedBy = "userProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents;

    @PostLoad
    private void generateUUIDUserID() {
        if (userID == null) {
            this.userID = UUID.randomUUID().toString().replaceAll("-", "");
        }
    }

}
