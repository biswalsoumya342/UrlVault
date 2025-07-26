package com.soumya.urlshortener.service.impl;

import com.soumya.urlshortener.exception.BadRequestException;
import com.soumya.urlshortener.exception.ResourceNotFoundException;
import com.soumya.urlshortener.exception.UnauthorizeAccessException;
import com.soumya.urlshortener.model.AppUser;
import com.soumya.urlshortener.model.ShortUrl;
import com.soumya.urlshortener.payload.shorturl.PasswordDto;
import com.soumya.urlshortener.payload.shorturl.ShortUrlDto;
import com.soumya.urlshortener.repository.AppUserRepository;
import com.soumya.urlshortener.repository.ShortUrlRepository;
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

    public ShortUrlServiceImpl(ShortUrlRepository shortUrlRepository, AppUserRepository userRepository, ModelMapper mapper, BCryptPasswordEncoder passwordEncoder) {
        this.shortUrlRepository = shortUrlRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
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
    public List<ShortUrlDto> findAllShortUrl() {
        List<ShortUrl> shortUrls = shortUrlRepository.findAll();
        return shortUrls.stream()
                .map(list->mapper.map(list,ShortUrlDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public String shortUrlEndPoint(String shortUrl, PasswordDto passwordDto, HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+"/api/short/r/";
        ShortUrl shorturl =  shortUrlRepository.findByShorturl(baseUrl+shortUrl).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "Short Url",
                        "Url",
                        shortUrl
                )
        );
        if (!shorturl.getIsPublic()) throw new BadRequestException(
                "The Link Is Private, Not Accessible"
        );
        if (shorturl.getPassword()!=null && passwordDto != null){
            if (passwordDto.getPassword()!=null){
                if (passwordEncoder.matches(passwordDto.getPassword(),shorturl.getPassword())){
                    return shorturl.getShorturl();
                }
                else {
                    throw new UnauthorizeAccessException(
                            "Please Enter Correct Password"
                    );
                }
            }else {
                throw new UnauthorizeAccessException(
                        "This Link Is Password Protected"
                );
            }
        }

        if (shorturl.getExpiredAt().isBefore(LocalDateTime.now())){
            throw new BadRequestException(
                    "Link Is Expired"
            );
        }
        return shorturl.getOriginalurl();
    }
}
