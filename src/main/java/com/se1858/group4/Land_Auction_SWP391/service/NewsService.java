package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.Account;
import com.se1858.group4.Land_Auction_SWP391.entity.Asset;
import com.se1858.group4.Land_Auction_SWP391.entity.Image;
import com.se1858.group4.Land_Auction_SWP391.entity.News;
import com.se1858.group4.Land_Auction_SWP391.repository.NewsRepository;
import com.se1858.group4.Land_Auction_SWP391.security.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {
    private NewsRepository newsRepository;
    private AccountService accountService;
    private ImageService imageService;
    private UserDetailsService userDetailsService;

    @Autowired
    public NewsService(NewsRepository newsRepository, AccountService accountService, ImageService imageService,
                       UserDetailsService userDetailsService) {
        this.newsRepository = newsRepository;
        this.accountService = accountService;
        this.imageService = imageService;
        this.userDetailsService = userDetailsService;
    }
    public News save(News news) {
        news.setCreatedDate(LocalDateTime.now());
        if(news.getCover_photo()==null){
            Image img = imageService.findImageById(2); //tim den Image mac dinh auction_hammer.jpg
            news.setCover_photo(img);
        }
        Account this_news_writer = userDetailsService.accountAuthenticated();
        news.setStaff(this_news_writer);
        return newsRepository.save(news);
    }
    public List<News> getAllNews(){
        return newsRepository.findAll();
    }
    public List<News> getAllNewsByAuthorId(int authorId){
        return newsRepository.findByStaff_AccountId(authorId);
    }
    public News getNewsById(int id) {
        Optional<News> news = newsRepository.findById(id);
        if(news.isPresent()){
            return news.get();
        }
        else return null;
    }
    public void deleteNewsById(int id) {
        newsRepository.deleteById(id);
    }
    public List<News> getTop3LatestNews(){
        return newsRepository.findTop3ByOrderByCreatedDateDesc();
    }
    public Page<News> filterNews(List<Integer> tagIds, String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 4);
        return newsRepository.filterNews(tagIds, keyword, pageable);
    }


    public Page<News> getNews(int page) {
        Pageable pageable = PageRequest.of(page, 4);
        return newsRepository.findAll(pageable);
    }
}
