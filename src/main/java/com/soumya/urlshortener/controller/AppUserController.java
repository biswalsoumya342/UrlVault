package com.soumya.urlshortener.controller;

import com.soumya.urlshortener.payload.ApiErrorResponse;
import com.soumya.urlshortener.payload.ApiResponse;
import com.soumya.urlshortener.payload.AppUserLoginDto;
import com.soumya.urlshortener.payload.AppUserSignupDto;
import com.soumya.urlshortener.service.AppUserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AppUserController {


    private final AppUserService service;

    public AppUserController(AppUserService service) {
        this.service = service;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> userSignup(@RequestBody AppUserSignupDto signupDto){
        boolean status = service.signup(signupDto);
        if (status) return new ResponseEntity<>(
                new ApiResponse(
                        LocalDateTime.now(),
                        "Signup Successful, Please Signin",
                        HttpStatus.OK.value()
                ),
                HttpStatus.OK
        );
        else return new ResponseEntity<>(
                new ApiErrorResponse(
                        LocalDateTime.now(),
                        "Unexpected Error occured, Try Again!",
                        HttpStatus.INTERNAL_SERVER_ERROR.value()
                ),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @PostMapping("/login")
    public String userLogin(@RequestBody AppUserLoginDto loginDto){
        log.debug("Request Hit To Login Enpoint User: {}",loginDto.getUsername());
        return  service.login(loginDto);
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(){
        return new ResponseEntity<>(
                new ApiResponse(
                        LocalDateTime.now(),
                        "Application Security And Everything Working Fine",
                        HttpStatus.OK.value()
                ),
                HttpStatus.OK
        );
    }
}
