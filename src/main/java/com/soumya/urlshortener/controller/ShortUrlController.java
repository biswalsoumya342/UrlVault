package com.soumya.urlshortener.controller;

import com.soumya.urlshortener.payload.ApiResponse;
import com.soumya.urlshortener.payload.shorturl.PasswordDto;
import com.soumya.urlshortener.payload.shorturl.ShortUrlDto;
import com.soumya.urlshortener.service.ShortUrlService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdk.jfr.Frequency;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/short")
public class ShortUrlController {

    private ShortUrlService shortUrlService;

    public ShortUrlController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @PostMapping("/url")
    public ResponseEntity<?> generateShortUrl(@RequestBody ShortUrlDto shortUrlDto, HttpServletRequest request){
        String shortUrl = shortUrlService.generateShortUrl(shortUrlDto,request);
         return new ResponseEntity<>(
                new ApiResponse(
                        LocalDateTime.now(),
                        shortUrl,
                        HttpStatus.OK.value()
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable String id){
        boolean status = shortUrlService.changePrivacy(id);
        if (status) return new ResponseEntity<>(
                new ApiResponse(
                        LocalDateTime.now(),
                        "Your Link Is Now Public",
                        HttpStatus.OK.value()
                ),
                HttpStatus.OK
        );
        else return new ResponseEntity<>(
                new ApiResponse(
                        LocalDateTime.now(),
                        "Your Link Is Now Private",
                        HttpStatus.OK.value()
                ),
                HttpStatus.OK
        );
    }

    @GetMapping("/show")
    public ResponseEntity<?> findAll(){
        return new ResponseEntity<>(
                shortUrlService.findAllShortUrl(),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUrl(@PathVariable String id){
        shortUrlService.deleteShortUrl(id);
        return new ResponseEntity<>(
                new ApiResponse(
                        LocalDateTime.now(),
                        "Url Deleted Successful",
                        HttpStatus.OK.value()
                ),
                HttpStatus.OK
        );
    }
}
