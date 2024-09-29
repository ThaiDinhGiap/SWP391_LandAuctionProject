package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.News;
import com.se1858.group4.Land_Auction_SWP391.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NewsService {
    private NewsRepository newsRepository;
    private AccountService accountService;

    @Autowired
    public NewsService(NewsRepository newsRepository, AccountService accountService) {
        this.newsRepository = newsRepository;
        this.accountService = accountService;
    }
    public News save(News news) {
        news.setCreatedDate(LocalDateTime.now());
        news.setStaff(accountService.findAccountById(5));
        return newsRepository.save(news);
    }
    public List<News> getAllNews(){
        return newsRepository.findAll();
    }
    public News getNewsById(int id) {
        return newsRepository.findById(id).get();
    }
    public void deleteNewsById(int id) {
        newsRepository.deleteById(id);
    }
}
