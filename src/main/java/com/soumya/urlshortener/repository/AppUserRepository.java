package com.soumya.urlshortener.repository;

import com.soumya.urlshortener.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser,String> {
    Optional<AppUser> findByUsername(String username);
}
