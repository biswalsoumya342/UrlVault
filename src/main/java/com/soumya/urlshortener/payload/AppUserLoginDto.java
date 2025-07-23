package com.soumya.urlshortener.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUserLoginDto {

    private String username;

    private String password;
}
