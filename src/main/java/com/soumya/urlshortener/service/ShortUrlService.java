package com.soumya.urlshortener.service;

import com.soumya.urlshortener.payload.shorturl.PasswordDto;
import com.soumya.urlshortener.payload.shorturl.ShortUrlDisplayDto;
import com.soumya.urlshortener.payload.shorturl.ShortUrlDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface ShortUrlService {
    String generateShortUrl(ShortUrlDto shortUrlDto, HttpServletRequest request);
    void deleteShortUrl(String id);
    boolean changePrivacy(String id);
    List<ShortUrlDisplayDto> findAllShortUrl();
}
