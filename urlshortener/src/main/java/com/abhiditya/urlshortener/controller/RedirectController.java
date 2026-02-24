package com.abhiditya.urlshortener.controller;

import com.abhiditya.urlshortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class RedirectController {
    
    @Autowired
    private UrlService urlService;
    
    @GetMapping("/")
    public String home() {
        return "forward:/index.html";
    }

    @GetMapping("/{shortCode:[a-zA-Z0-9]+}")
    public RedirectView redirect(@PathVariable String shortCode) {
        var url = urlService.getOriginalUrl(shortCode);
        if (url == null) {
            throw new UrlNotFoundException("Short URL not found: " + shortCode);
        }
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(url.getOriginalUrl());
        return redirectView;
    }
    
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class UrlNotFoundException extends RuntimeException {
        public UrlNotFoundException(String message) {
            super(message);
        }
    }
}
