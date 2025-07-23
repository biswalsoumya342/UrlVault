package com.soumya.urlshortener.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false,unique = true)
    private String username;

    private String password;

    @Column(nullable = false,unique = true)
    private String email;

    private String role;

    private LocalDateTime createdAt;

    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
    private List<ShortUrl> shorturl = new ArrayList<>();
}
