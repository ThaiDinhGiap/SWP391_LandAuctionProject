package com.se1858.group4.Land_Auction_SWP391.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Asset")
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asset_id")
    private int assetId;

    @Column(name = "location", columnDefinition = "NVARCHAR(MAX)", nullable = false)
    private String location;

    @Column(name = "length", precision = 6, scale = 2, nullable = false)
    private BigDecimal length;

    @Column(name = "width", precision = 6, scale = 2, nullable = false)
    private BigDecimal width;

    @Column(name = "area", precision = 12, scale = 4, nullable = false)
    private BigDecimal area;

    @Column(name = "description", columnDefinition = "NVARCHAR(MAX)")
    private String description;

    //sua phan loai va them create_date
    @Column(name = "coordinates_on_map", columnDefinition = "NVARCHAR(MAX)")
    private String coordinatesOnMap;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "local_authority_id", referencedColumnName = "local_authority_id")
    private LocalAuthority localAuthority;

    @Column(name = "asset_status", length = 100)
    private String assetStatus;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

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
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}
    )
    private List<AuctionSession> auction_sessions;

    public Asset() {
    }

    public Asset(BigDecimal length, BigDecimal width, BigDecimal area, String description, String location, String assetStatus, LocalDateTime createdDate, LocalAuthority localAuthority, String coordinatesOnMap) {
        this.length = length;
        this.width = width;
        this.area = area;
        this.description = description;
        this.location = location;
        this.assetStatus = assetStatus;
        this.createdDate = createdDate;
        this.localAuthority = localAuthority;
        this.coordinatesOnMap = coordinatesOnMap;
    }

    public Asset(BigDecimal area, String assetStatus, List<AuctionSession> auction_sessions, String coordinatesOnMap, LocalDateTime createdDate, String description, List<Document> documents, List<Image> images, BigDecimal length, LocalAuthority localAuthority, String location, List<Tag> tags, BigDecimal width) {
        this.area = area;
        this.assetStatus = assetStatus;
        this.auction_sessions = auction_sessions;
        this.coordinatesOnMap = coordinatesOnMap;
        this.createdDate = createdDate;
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

    public String getCoordinatesOnMap() {
        return coordinatesOnMap;
    }

    public void setCoordinatesOnMap(String coordinatesOnMap) {
        this.coordinatesOnMap = coordinatesOnMap;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
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

    public List<AuctionSession> getAuction_sessions() {
        return auction_sessions;
    }

    public void setAuction_sessions(List<AuctionSession> auction_sessions) {
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
                ", coordinatesOnMap='" + coordinatesOnMap + '\'' +
                ", localAuthority=" + localAuthority +
                ", assetStatus='" + assetStatus + '\'' +
                ", createdDate=" + createdDate +
                '}';
    }

    public void addTag(Tag tag) {
        if(this.tags == null){
            this.tags = new ArrayList<>();
        }
        this.tags.add(tag);
    }
    public void addImage(Image image) {
        if(this.images == null){
            this.images = new ArrayList<>();
        }
        this.images.add(image);
    }
    public void addDocument(Document document) {
        if(this.documents == null){
            this.documents = new ArrayList<>();
        }
        this.documents.add(document);
    }
    public void addAuctionSession(AuctionSession auction) {
        if(this.auction_sessions == null){
            this.auction_sessions = new ArrayList<>();
        }
        this.auction_sessions.add(auction);
    }
}
