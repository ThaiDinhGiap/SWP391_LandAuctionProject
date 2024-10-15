package com.se1858.group4.Land_Auction_SWP391.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "News")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private int newsId;

    @Column(name = "title", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String content;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.REMOVE})
    @JoinColumn(name = "cover_photo_id", referencedColumnName = "image_id")
    private Image cover_photo;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "staff_id")
    private Account staff;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(
            name = "News_Tag_for_news",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<TagForNews> tags;

    public News() {
    }

    public News(String title, String content, List<TagForNews> tags, Account staff, LocalDateTime createdDate) {
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.staff = staff;
        this.createdDate = createdDate;
    }

    public News(String title, String content, Image cover_photo, List<TagForNews> tags, LocalDateTime createdDate, Account staff) {
        this.title = title;
        this.content = content;
        this.cover_photo = cover_photo;
        this.tags = tags;
        this.createdDate = createdDate;
        this.staff = staff;
    }

    public News(String title, String content, Account staff, LocalDateTime createdDate) {
        this.title = title;
        this.content = content;
        this.staff = staff;
        this.createdDate = createdDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }

    public Account getStaff() {
        return staff;
    }

    public void setStaff(Account staff) {
        this.staff = staff;
    }

    public List<TagForNews> getTags() {
        return tags;
    }

    public void setTags(List<TagForNews> tags) {
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Image getCover_photo() {
        return cover_photo;
    }

    public void setCover_photo(Image cover_photo) {
        this.cover_photo = cover_photo;
    }

    @Override
    public String toString() {
        return "News{" +
                "content='" + content + '\'' +
                ", newsId=" + newsId +
                ", title='" + title + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }

    public void addTag(TagForNews tag) {
        if(this.tags == null){
            this.tags = new ArrayList<>();
        }
        this.tags.add(tag);
    }
}