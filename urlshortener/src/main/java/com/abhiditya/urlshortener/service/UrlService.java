package com.abhiditya.urlshortener.service;

import com.abhiditya.urlshortener.model.Url;
import com.abhiditya.urlshortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UrlService {
    
    @Autowired
    private UrlRepository urlRepository;
    
    private final AtomicLong counter = new AtomicLong(1);
    
    public Url shortenUrl(String originalUrl) {
        String shortCode = generateUniqueShortCode();
        String shortUrl = "http://localhost:8080/" + shortCode;
        
        Url url = new Url(originalUrl, shortUrl);
        url.setShortCode(shortCode);
        
        return urlRepository.save(url);
    }
    
    public Url getOriginalUrl(String shortCode) {
        Url url = urlRepository.findByShortCode(shortCode).orElse(null);
        if (url != null) {
            url.setClickCount(url.getClickCount() + 1);
            urlRepository.save(url);
        }
        return url;
    }
    
    public Url getUrlStats(String shortCode) {
        return urlRepository.findByShortCode(shortCode).orElse(null);
    }
    
    public List<Url> getAllUrls() {
        return urlRepository.findAll();
    }
    
    private String generateUniqueShortCode() {
        String shortCode;
        do {
            shortCode = encodeToBase62(counter.getAndIncrement());
        } while (urlRepository.existsByShortCode(shortCode));
        return shortCode;
    }
    
    private String encodeToBase62(long num) {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        
        while (num > 0) {
            sb.append(chars.charAt((int)(num % 62)));
            num /= 62;
        }
        
        return sb.reverse().toString();
    }
}
