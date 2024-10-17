package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.entity.Asset;
import com.se1858.group4.Land_Auction_SWP391.entity.News;
import com.se1858.group4.Land_Auction_SWP391.entity.Tag;
import com.se1858.group4.Land_Auction_SWP391.entity.TagForNews;
import com.se1858.group4.Land_Auction_SWP391.service.AssetService;
import com.se1858.group4.Land_Auction_SWP391.service.NewsService;
import com.se1858.group4.Land_Auction_SWP391.service.TagForNewsService;
import com.se1858.group4.Land_Auction_SWP391.service.TagService;
import com.se1858.group4.Land_Auction_SWP391.utility.GetSrcInGoogleMapEmbededURLUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
@RequestMapping("/customer")
public class CustomerController {
    private NewsService newsService;
    private TagForNewsService tagForNewsService;
    private AssetService assetService;
    private TagService tagService;
    @Autowired
    public CustomerController(NewsService newsService, TagForNewsService tagForNewsService, AssetService assetService, TagService tagService) {
        this.newsService = newsService;
        this.tagForNewsService = tagForNewsService;
        this.assetService = assetService;
        this.tagService = tagService;
    }
    @GetMapping("/get_all_asset")
    public String getAllAsset(Model model) {
        List<Asset> newsList=assetService.getAllAssetWithStatus("Waiting for Auction Scheduling");
        model.addAttribute("listAsset",newsList);
//        lay danh sach cac tag
        List<Tag> tagList=tagService.getAllTag();
        model.addAttribute("listTag",tagList);
        return "customer/assetList";
    }
    @GetMapping("/get_all_news")
    public String getAllNews(Model model) {
        List<News> newsList=newsService.getAllNews();
        model.addAttribute("listNews",newsList);
//        lay ra 3 bai viet moi nhat
        List<News> top3News=newsService.getTop3LatestNews();
        model.addAttribute("top3LatestNews",top3News);
//        lay danh sach cac tag
        List<TagForNews> tagList=tagForNewsService.getAllTagsForNews();
        model.addAttribute("listTag",tagList);
        return "customer/newsList";
    }
    @GetMapping("/viewNewsDetail")
    public String getNewsById(@RequestParam("newsId") int newsId, Model model) {
        News news=newsService.getNewsById(newsId);
        model.addAttribute("news",news);
//        lay ra 3 bai viet moi nhat
        List<News> top3News=newsService.getTop3LatestNews();
        model.addAttribute("top3LatestNews",top3News);
//        lay danh sach cac tag
        List<TagForNews> tagList=tagForNewsService.getAllTagsForNews();
        model.addAttribute("listTag",tagList);
        return "customer/newsDetail";
    }
    @GetMapping("/viewAssetDetail")
    public String getAssetById(@RequestParam("assetId") int assetId, Model model) {
        Asset asset=assetService.getAssetById(assetId);
        String embedUrl = GetSrcInGoogleMapEmbededURLUtil.extractSrcFromIframe(asset.getCoordinatesOnMap());
        model.addAttribute("embedUrl", embedUrl);
        model.addAttribute("asset",asset);
        return "customer/assetDetail";
    }
}
