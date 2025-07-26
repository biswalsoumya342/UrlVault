package com.soumya.urlshortener.payload.shorturl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShortUrlDto {

    private String id;

    @NotBlank(message = "Enter Your Url")
    private String originalurl;

    private Boolean isPublic;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#?!@]).{8,}$",message = "Password Must Be Combination Of 8 Character and digit,uppercase,lowercase,special character")
    private String password;

    private LocalDateTime createdAt;

    private Integer expiredAt;
}
