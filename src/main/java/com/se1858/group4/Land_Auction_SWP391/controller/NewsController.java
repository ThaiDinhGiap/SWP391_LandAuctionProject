package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.dto.NewsDTO;
import com.se1858.group4.Land_Auction_SWP391.entity.*;
import com.se1858.group4.Land_Auction_SWP391.service.NewsService;
import com.se1858.group4.Land_Auction_SWP391.service.TagForNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/news")
public class NewsController {
    private NewsService newsService;
    private TagForNewsService tagForNewsService;
    @Autowired
    public NewsController(NewsService newsService, TagForNewsService tagForNewsService) {
        this.newsService = newsService;
        this.tagForNewsService = tagForNewsService;
    }
    @GetMapping("/create_news")
    public String createNews(Model model) {
        model.addAttribute("newsDTO",new NewsDTO());
        model.addAttribute("listTag",tagForNewsService.getAllTagsForNews());
        return "news/CreateNews";
    }
    @PostMapping("/add")
    public String addNews(@ModelAttribute("newsDTO") NewsDTO newsDTO,
                                @RequestParam("selectedTags") List<Integer> selectedTags,
                                RedirectAttributes redirectAttributes) {
        News news = newsDTO.getNews();
        for(Integer tagId: selectedTags){
            TagForNews tag = tagForNewsService.getTagForNewsById(tagId);
            news.addTag(tag);
            tag.addNews(news);
        }
        newsService.save(news);
        redirectAttributes.addAttribute("message","The article was created successfully");
        return "redirect:/news/create_news";
    }

    @GetMapping("/list")
    public String getAllNews(Model model) {
        List<News> list=newsService.getAllNews();
        model.addAttribute("listNews",list);
        return "news/ListNews";
    }

    @GetMapping("/viewDetail")
    public String getNewsById(@RequestParam("newsId") int newsId, Model model) {
        News news=newsService.getNewsById(newsId);
        model.addAttribute("news",news);
        return "news/NewsDetail";
    }

    @GetMapping("/delete")
    public String deleteNews(@RequestParam("newsId") int newsId) {
        newsService.deleteNewsById(newsId);
        return "redirect:/news/list";
    }
}
