package com.se1858.group4.Land_Auction_SWP391.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Tag_for_news")
public class TagForNews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private int tagId;

    @Column(name = "tag_name", nullable = false)
    private String tagName;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<News> newsList;

    public TagForNews() {
    }

    public TagForNews(String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }

    public TagForNews(String tagName, List<News> newsList) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.newsList = newsList;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    @Override
    public String toString() {
        return "TagForNews{" +
                "tagId=" + tagId +
                ", tagName='" + tagName + '\'' +
                '}';
    }

    public void addNews(News news) {
        if(this.newsList == null){
            this.newsList = new ArrayList<>();
        }
        this.newsList.add(news);
    }
}
