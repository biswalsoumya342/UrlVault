package com.soumya.urlshortener.repository;

import com.soumya.urlshortener.model.ShortUrl;
import com.soumya.urlshortener.model.UrlAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UrlAnalyticsRepository extends JpaRepository<UrlAnalytics,String> {
    Optional<UrlAnalytics> findByShorturl_Shorturl(String shorturlValue);
}
