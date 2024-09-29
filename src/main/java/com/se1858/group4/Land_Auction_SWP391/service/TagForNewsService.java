package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.TagForNews;
import com.se1858.group4.Land_Auction_SWP391.repository.TagForNewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagForNewsService {
    private TagForNewsRepository tagForNewsRepository;
    @Autowired
    public TagForNewsService(TagForNewsRepository tagForNewsRepository) {
        this.tagForNewsRepository = tagForNewsRepository;
    }
    public List<TagForNews> getAllTagsForNews() {
        return tagForNewsRepository.findAll();
    }
    public TagForNews getTagForNewsById(int id) {
        return tagForNewsRepository.findById(id).get();
    }
}
