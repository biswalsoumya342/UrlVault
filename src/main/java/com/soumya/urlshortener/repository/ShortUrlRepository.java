package com.soumya.urlshortener.repository;

import com.soumya.urlshortener.model.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl,String> {
    Optional<ShortUrl> findByShorturl(String shortUrl);
}
