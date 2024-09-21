package com.se1858.group4.Land_Auction_SWP391.Entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "Asset")
public class Asset {

    @Id
    @Column(name = "asset_id")
    private int assetId;

    @Column(name = "location", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    private String location;

    @Column(name = "length", precision = 10, scale = 2, nullable = false)
    private BigDecimal length;

    @Column(name = "width", precision = 10, scale = 2, nullable = false)
    private BigDecimal width;

    @Column(name = "area", precision = 10, scale = 2, nullable = false)
    private BigDecimal area;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "classify", columnDefinition = "NVARCHAR(MAX)")
    private String classify;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "local_authority_id", referencedColumnName = "local_authority_id")
    private LocalAuthority localAuthority;

    @Column(name = "asset_status", length = 100)
    private String assetStatus;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinTable(
            name = "Asset_Tag",
            joinColumns = @JoinColumn(name = "asset_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @OneToMany(mappedBy = "asset",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Image> images;

    @OneToMany(mappedBy = "asset",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Document> documents;

    @OneToMany(mappedBy = "asset",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private List<Auction_session> auction_sessions;

    public Asset() {
    }

    public Asset(BigDecimal area, int assetId, String assetStatus, String classify, String description, BigDecimal length, LocalAuthority localAuthority, String location, List<Tag> tags, BigDecimal width) {
        this.area = area;
        this.assetId = assetId;
        this.assetStatus = assetStatus;
        this.classify = classify;
        this.description = description;
        this.length = length;
        this.localAuthority = localAuthority;
        this.location = location;
        this.tags = tags;
        this.width = width;
    }

    public Asset(BigDecimal area, int assetId, String assetStatus, String classify, String description, List<Document> documents, List<Image> images, BigDecimal length, LocalAuthority localAuthority, String location, List<Tag> tags, BigDecimal width) {
        this.area = area;
        this.assetId = assetId;
        this.assetStatus = assetStatus;
        this.classify = classify;
        this.description = description;
        this.documents = documents;
        this.images = images;
        this.length = length;
        this.localAuthority = localAuthority;
        this.location = location;
        this.tags = tags;
        this.width = width;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public int getAssetId() {
        return assetId;
    }

    public void setAssetId(int assetId) {
        this.assetId = assetId;
    }

    public String getAssetStatus() {
        return assetStatus;
    }

    public void setAssetStatus(String assetStatus) {
        this.assetStatus = assetStatus;
    }

    public String getClassify() {
        return classify;
    }

    public void setClassify(String classify) {
        this.classify = classify;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public LocalAuthority getLocalAuthority() {
        return localAuthority;
    }

    public void setLocalAuthority(LocalAuthority localAuthority) {
        this.localAuthority = localAuthority;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public List<Auction_session> getAuction_sessions() {
        return auction_sessions;
    }

    public void setAuction_sessions(List<Auction_session> auction_sessions) {
        this.auction_sessions = auction_sessions;
    }

    @Override
    public String toString() {
        return "Asset{" +
                "area=" + area +
                ", assetId=" + assetId +
                ", location='" + location + '\'' +
                ", length=" + length +
                ", width=" + width +
                ", description='" + description + '\'' +
                ", classify='" + classify + '\'' +
                ", localAuthority=" + localAuthority +
                ", assetStatus='" + assetStatus + '\'' +
                '}';
    }
}
