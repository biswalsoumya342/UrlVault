package com.soumya.urlshortener.service.impl;

import com.soumya.urlshortener.exception.BadRequestException;
import com.soumya.urlshortener.exception.ResourceNotFoundException;
import com.soumya.urlshortener.exception.UnauthorizeAccessException;
import com.soumya.urlshortener.model.AppUser;
import com.soumya.urlshortener.model.ShortUrl;
import com.soumya.urlshortener.model.UrlAnalytics;
import com.soumya.urlshortener.payload.shorturl.PasswordDto;
import com.soumya.urlshortener.payload.shorturl.ShortUrlDisplayDto;
import com.soumya.urlshortener.payload.shorturl.ShortUrlDto;
import com.soumya.urlshortener.repository.AppUserRepository;
import com.soumya.urlshortener.repository.ShortUrlRepository;
import com.soumya.urlshortener.repository.UrlAnalyticsRepository;
import com.soumya.urlshortener.service.ShortUrlService;
import com.soumya.urlshortener.util.RandomUrlUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShortUrlServiceImpl implements ShortUrlService {

    private ShortUrlRepository shortUrlRepository;
    private AppUserRepository userRepository;
    private ModelMapper mapper;
    private BCryptPasswordEncoder passwordEncoder;
    private UrlAnalyticsRepository analyticsRepository;

    public ShortUrlServiceImpl(ShortUrlRepository shortUrlRepository, UrlAnalyticsRepository analyticsRepository, BCryptPasswordEncoder passwordEncoder, ModelMapper mapper, AppUserRepository userRepository) {
        this.shortUrlRepository = shortUrlRepository;
        this.analyticsRepository = analyticsRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    @Override
    public String generateShortUrl(ShortUrlDto shortUrlDto, HttpServletRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        AppUser user = userRepository.findByUsername(username).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "User",
                        "Username",
                        username
                )
        );
        ShortUrl shortUrl = mapper.map(shortUrlDto, ShortUrl.class);
        if (shortUrlDto.getPassword()!=null) shortUrl.setPassword(passwordEncoder.encode(shortUrlDto.getPassword()));
        if (shortUrlDto.getExpiredAt()!=null) shortUrl.setExpiredAt(LocalDateTime.now().plusMinutes(shortUrlDto.getExpiredAt()));
        shortUrl.setShorturl(RandomUrlUtil.randomUrl(request));
        shortUrl.setCreatedAt(LocalDateTime.now());
        shortUrl.setUser(user);
        ShortUrl savedUrl = shortUrlRepository.save(shortUrl);
        UrlAnalytics urlAnalytics = new UrlAnalytics();
        urlAnalytics.setShorturl(shortUrl);
        urlAnalytics.setClickCount(0);
        analyticsRepository.save(urlAnalytics);
        return savedUrl.getShorturl();
    }

    @Override
    public void deleteShortUrl(String id) {
        shortUrlRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "Short Url",
                        "Id",
                        id
                )
        );
        shortUrlRepository.deleteById(id);
    }

    @Override
    public boolean changePrivacy(String id) {
        ShortUrl shortUrl = shortUrlRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "Short Url",
                        "Id",
                        id
                )
        );

        if (shortUrl.getIsPublic()) {
            shortUrl.setIsPublic(false);
            return false;
        }else {
            shortUrl.setIsPublic(true);
            return true;
        }
    }

    @Override
    public List<ShortUrlDisplayDto> findAllShortUrl() {
        List<ShortUrl> shortUrls = shortUrlRepository.findAll();
        return shortUrls.stream()
                .map(list->mapper.map(list, ShortUrlDisplayDto.class))
                .collect(Collectors.toList());
    }
}
