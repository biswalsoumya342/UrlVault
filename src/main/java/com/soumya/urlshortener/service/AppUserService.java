package com.soumya.urlshortener.service;

import com.soumya.urlshortener.payload.AppUserLoginDto;
import com.soumya.urlshortener.payload.AppUserSignupDto;

public interface AppUserService {
    public boolean signup(AppUserSignupDto signupDto);
    public String login(AppUserLoginDto loginDto);
}
