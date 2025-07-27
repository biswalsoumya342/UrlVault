package com.soumya.urlshortener.payload.shorturl;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrlDisplayDto {

    private String id;

    private String originalurl;

    private String shorturl;

    private Boolean isPublic;

    private String password;

    private LocalDateTime createdAt;

    private LocalDateTime expiredAt;
}
