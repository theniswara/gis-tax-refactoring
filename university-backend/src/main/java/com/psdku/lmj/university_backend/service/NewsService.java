/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * News Service - Updated for normalized Category relationship
 */
package com.psdku.lmj.university_backend.service;

import com.psdku.lmj.university_backend.model.Category;
import com.psdku.lmj.university_backend.model.News;
import com.psdku.lmj.university_backend.repository.CategoryRepository;
import com.psdku.lmj.university_backend.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * News Service
 * 
 * Updated to work with normalized Category relationship
 */
@Service
public class NewsService {
    
    @Autowired
    private NewsRepository newsRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    /**
     * Get all news ordered by date (newest first)
     */
    public List<News> getAllNews() {
        return newsRepository.findAllByOrderByDateDesc();
    }
    
    /**
     * Get news by ID
     */
    public Optional<News> getNewsById(Long id) {
        return newsRepository.findById(id);
    }
    
    /**
     * Get news by Category object
     */
    public List<News> getNewsByCategory(Category category) {
        return newsRepository.findByCategoryOrderByDateDesc(category);
    }
    
    /**
     * Get news by category ID
     */
    public List<News> getNewsByCategoryId(Long categoryId) {
        return newsRepository.findByCategoryIdOrderByDateDesc(categoryId);
    }
    
    /**
     * Get news by category name
     */
    public List<News> getNewsByCategoryName(String categoryName) {
        return newsRepository.findByCategoryName(categoryName);
    }
    
    /**
     * Get news by category slug (URL-friendly)
     */
    public List<News> getNewsByCategorySlug(String slug) {
        return newsRepository.findByCategorySlug(slug);
    }
    
    /**
     * Get recent news (limit number of results)
     */
    public List<News> getRecentNews(int limit) {
        return newsRepository.findRecentNews(limit);
    }
    
    /**
     * Get recent news by category ID
     */
    public List<News> getRecentNewsByCategory(Long categoryId, int limit) {
        return newsRepository.findRecentNewsByCategory(categoryId, limit);
    }
    
    /**
     * Create or update news
     */
    public News saveNews(News news) {
        return newsRepository.save(news);
    }
    
    /**
     * Create news with category ID
     */
    public News createNews(News news, Long categoryId) {
        if (categoryId != null) {
            Optional<Category> category = categoryRepository.findById(categoryId);
            category.ifPresent(news::setCategory);
        }
        return newsRepository.save(news);
    }
    
    /**
     * Update news
     */
    public News updateNews(Long id, News newsDetails) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with id: " + id));
        
        if (newsDetails.getTitle() != null) {
            news.setTitle(newsDetails.getTitle());
        }
        if (newsDetails.getDate() != null) {
            news.setDate(newsDetails.getDate());
        }
        if (newsDetails.getCategory() != null) {
            news.setCategory(newsDetails.getCategory());
        }
        if (newsDetails.getAuthor() != null) {
            news.setAuthor(newsDetails.getAuthor());
        }
        if (newsDetails.getExcerpt() != null) {
            news.setExcerpt(newsDetails.getExcerpt());
        }
        if (newsDetails.getImage() != null) {
            news.setImage(newsDetails.getImage());
        }
        if (newsDetails.getContent() != null) {
            news.setContent(newsDetails.getContent());
        }
        
        return newsRepository.save(news);
    }
    
    /**
     * Delete news
     */
    public void deleteNews(Long id) {
        News news = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with id: " + id));
        newsRepository.delete(news);
    }
    
    /**
     * Increment view count
     */
    public News incrementViews(Long id) {
        Optional<News> newsOptional = newsRepository.findById(id);
        if (newsOptional.isPresent()) {
            News news = newsOptional.get();
            Integer currentViews = news.getViews();
            news.setViews(currentViews != null ? currentViews + 1 : 1);
            return newsRepository.save(news);
        }
        return null;
    }
    
    /**
     * Increment likes
     */
    public News incrementLikes(Long id) {
        Optional<News> newsOptional = newsRepository.findById(id);
        if (newsOptional.isPresent()) {
            News news = newsOptional.get();
            Integer currentLikes = news.getLikes();
            news.setLikes(currentLikes != null ? currentLikes + 1 : 1);
            return newsRepository.save(news);
        }
        return null;
    }
}
