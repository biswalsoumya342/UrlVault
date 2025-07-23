package com.soumya.urlshortener.security.impl;

import com.soumya.urlshortener.exception.ResourceNotFoundException;
import com.soumya.urlshortener.model.AppUser;
import com.soumya.urlshortener.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserRepository repo;

    public UserDetailsServiceImpl(AppUserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("LoadByUsername Invoked In UserDetailsService");
        AppUser user = repo.findByUsername(username)
                .orElseThrow(
                        ()-> new ResourceNotFoundException(
                                "User",
                                "Username",
                                username
                        )
                );
        log.debug("UserFound And Send To UserDetails");
        return new UserDetailsImpl(user);
    }
}
