package com.se1858.group4.Land_Auction_SWP391.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "Image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private int imageId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "asset_id")
    private Asset asset;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    public Image() {
    }

    public Image(int imageId, Asset asset, String path, LocalDateTime uploadDate) {
        this.imageId = imageId;
        this.asset = asset;
        this.path = path;
        this.uploadDate = uploadDate;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    @Override
    public String toString() {
        return "Image{" +
                "imageId=" + imageId +
                ", asset=" + asset +
                ", path='" + path + '\'' +
                ", uploadDate=" + uploadDate +
                '}';
    }
}

