package com.soumya.urlshortener.service.impl;

import com.soumya.urlshortener.exception.BadRequestException;
import com.soumya.urlshortener.exception.ResourceNotFoundException;
import com.soumya.urlshortener.exception.UnauthorizeAccessException;
import com.soumya.urlshortener.model.DeviceInformation;
import com.soumya.urlshortener.model.ShortUrl;
import com.soumya.urlshortener.model.UrlAnalytics;
import com.soumya.urlshortener.repository.ShortUrlRepository;
import com.soumya.urlshortener.repository.UrlAnalyticsRepository;
import com.soumya.urlshortener.service.UrlAnalyticsService;
import com.soumya.urlshortener.util.UserAgentUtil;
import eu.bitwalker.useragentutils.UserAgent;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UrlAnalyticsServiceImpl implements UrlAnalyticsService {


    private final UrlAnalyticsRepository analyticsRepository;
    private final ShortUrlRepository shortUrlRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UrlAnalyticsServiceImpl(UrlAnalyticsRepository analyticsRepository, ShortUrlRepository shortUrlRepository, BCryptPasswordEncoder passwordEncoder) {
        this.analyticsRepository = analyticsRepository;
        this.shortUrlRepository = shortUrlRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String urlEndpoint(String url, String password, HttpServletRequest request) {

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()+"/";

        String userAgent = request.getHeader("User-Agent");

        UserAgent ua = UserAgentUtil.getUserAgent(userAgent);

        UrlAnalytics urlAnalytics = analyticsRepository.findByShorturl_Shorturl(baseUrl+url).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "UrlAnalytic",
                        "Url",
                        baseUrl+url
                )
        );
        if(urlAnalytics.getClickCount()!=null){
            urlAnalytics.setClickCount(urlAnalytics.getClickCount()+1);
        }else {
            urlAnalytics.setClickCount(1);
        }
        List<DeviceInformation> deviceInformationList = new ArrayList<>();
        DeviceInformation deviceInformation = new DeviceInformation();
        deviceInformation.setTime(LocalDateTime.now());
        deviceInformation.setDevice(UserAgentUtil.extractDevice(ua));
        deviceInformation.setOperatingSystem(UserAgentUtil.extractOperatingSystem(ua));
        deviceInformation.setBrowser(UserAgentUtil.extractBrowser(ua));
        deviceInformationList.add(deviceInformation);
        urlAnalytics.setDeviceinfo(deviceInformationList);
        ShortUrl shortUrl = shortUrlRepository.findByShorturl(baseUrl+url).orElseThrow(
                ()-> new ResourceNotFoundException(
                        "URL",
                        "Url",
                        url
                )
        );

        if (shortUrl.getPassword()!=null){

            if (password==null){
                throw new UnauthorizeAccessException("Link Is Password Protected, Please Enter Password");
            }
            if (passwordEncoder.matches(password,shortUrl.getPassword())){
                analyticsRepository.save(urlAnalytics);
                return shortUrl.getOriginalurl();
            }else {
                throw new UnauthorizeAccessException("Please Enter Correct Password");
            }

        }

        if (shortUrl.getExpiredAt().isBefore(LocalDateTime.now())){
            throw new BadRequestException("Requested Url Is Expired");
        }

        if (!shortUrl.getIsPublic()){
            throw new UnauthorizeAccessException("The Requested Link Is Expired");
        }

        analyticsRepository.save(urlAnalytics);
        return shortUrl.getOriginalurl();
    }
}
