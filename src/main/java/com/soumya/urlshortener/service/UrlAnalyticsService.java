package com.soumya.urlshortener.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UrlAnalyticsService {
    public String urlEndpoint(String url, String password, HttpServletRequest request);
}
