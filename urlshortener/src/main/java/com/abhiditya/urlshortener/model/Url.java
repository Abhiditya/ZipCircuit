package com.abhiditya.urlshortener.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "urls")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Url {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String shortCode;
    
    @Column(nullable = false)
    private String originalUrl;
    
    @Column(nullable = false)
    private String shortUrl;
    
    @Column(nullable = false)
    private int clickCount;
    
    @Column(nullable = false)
    private long createdAt;
    
    public Url(String originalUrl, String shortUrl) {
        this.originalUrl = originalUrl;
        this.shortUrl = shortUrl;
        this.clickCount = 0;
        this.createdAt = System.currentTimeMillis();
    }
}
