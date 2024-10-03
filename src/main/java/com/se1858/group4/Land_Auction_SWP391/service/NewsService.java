package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.Image;
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
    private ImageService imageService;

    @Autowired
    public NewsService(NewsRepository newsRepository, AccountService accountService, ImageService imageService) {
        this.newsRepository = newsRepository;
        this.accountService = accountService;
        this.imageService = imageService;
    }
    public News save(News news) {
        news.setCreatedDate(LocalDateTime.now());
        if(news.getCover_photo()==null){
            Image img = imageService.findImageById(2); //tim den Image mac dinh auction_hammer.jpg
            news.setCover_photo(img);
        }
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
