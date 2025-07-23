package com.soumya.urlshortener.service.impl;

import com.soumya.urlshortener.model.AppUser;
import com.soumya.urlshortener.payload.AppUserLoginDto;
import com.soumya.urlshortener.payload.AppUserSignupDto;
import com.soumya.urlshortener.repository.AppUserRepository;
import com.soumya.urlshortener.security.JwtService;
import com.soumya.urlshortener.service.AppUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository repo;
    private final ModelMapper mapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AppUserServiceImpl(AppUserRepository repo, ModelMapper mapper, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.repo = repo;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @Override
    public boolean signup(AppUserSignupDto signupDto) {
        AppUser user = mapper.map(signupDto,AppUser.class);
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());
        repo.save(user);
        return true;
    }

    @Override
    public String login(AppUserLoginDto loginDto) {

        log.debug("Login Service Invoked And Start Authentication");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );
        log.debug("Authentication Successful");
        return jwtService.generateToken(authentication.getName());
    }
}
