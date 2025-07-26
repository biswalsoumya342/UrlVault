package com.soumya.urlshortener.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String originalurl;

    private String shorturl;

    private Boolean isPublic;

    private String password;

    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private AppUser user;

    @OneToOne(mappedBy = "shortUrl",cascade = CascadeType.ALL,orphanRemoval = true)
    private UrlAnalytics urlAnalytics;
}
