package com.se1858.group4.Land_Auction_SWP391.entity;

import jakarta.persistence.*;
import org.w3c.dom.DocumentType;

import java.time.LocalDateTime;

@Entity
@Table(name = "Document")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private int documentId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "asset_id")
    private Asset asset;

    @Column(name = "document_name")
    private String documentName;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    public Document() {
    }

    public Document(Asset asset, String documentName, String path, LocalDateTime uploadDate) {
        this.asset = asset;
        this.documentName = documentName;
        this.path = path;
        this.uploadDate = uploadDate;
    }

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
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
        return "Document{" +
                "documentId=" + documentId +
                ", asset=" + asset +
                ", documentName='" + documentName + '\'' +
                ", path='" + path + '\'' +
                ", uploadDate=" + uploadDate +
                '}';
    }
}

