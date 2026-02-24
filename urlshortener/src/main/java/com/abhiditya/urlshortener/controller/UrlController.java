package com.abhiditya.urlshortener.controller;

import com.abhiditya.urlshortener.model.Url;
import com.abhiditya.urlshortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class UrlController {
    
    @Autowired
    private UrlService urlService;
    
    @PostMapping("/shorten")
    public ResponseEntity<Url> shortenUrl(@RequestBody Map<String, String> request) {
        String originalUrl = request.get("url");
        if (originalUrl == null || originalUrl.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Url url = urlService.shortenUrl(originalUrl);
        return ResponseEntity.ok(url);
    }
    
    @GetMapping("/stats/{shortCode}")
    public ResponseEntity<Url> getUrlStats(@PathVariable String shortCode) {
        Url url = urlService.getUrlStats(shortCode);
        if (url == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(url);
    }
    
    @GetMapping("/urls")
    public ResponseEntity<List<Url>> getAllUrls() {
        List<Url> urls = urlService.getAllUrls();
        return ResponseEntity.ok(urls);
    }
}
