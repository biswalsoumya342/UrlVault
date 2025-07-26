package com.soumya.urlshortener.repository;

import com.soumya.urlshortener.model.UrlAnalytics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlAnalyticsRepository extends JpaRepository<UrlAnalytics,String> {
}
