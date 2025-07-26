package com.soumya.urlshortener.controller;

import com.soumya.urlshortener.service.UrlAnalyticsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class UrlAnalyticsController {

    private final UrlAnalyticsService service;

    public UrlAnalyticsController(UrlAnalyticsService service) {
        this.service = service;
    }

    @GetMapping("/{url}")
    public void urlEndpoint(@PathVariable String url, @RequestParam(required = false) String password, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String originalUrl = service.urlEndpoint(url,password,request);
        response.sendRedirect(originalUrl);
    }
}
