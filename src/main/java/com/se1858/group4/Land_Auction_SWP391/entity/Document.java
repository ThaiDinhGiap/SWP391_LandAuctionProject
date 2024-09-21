package com.se1858.group4.Land_Auction_SWP391.entity;

import jakarta.persistence.*;
import org.w3c.dom.DocumentType;

import java.time.LocalDateTime;

@Entity
@Table(name = "Document")
public class Document {
    @Id
    @Column(name = "document_id")
    private int documentId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "asset_id")
    private Asset asset;

    @Column(name = "document_name")
    private String documentName;

    @Column(name = "path", nullable = false)
    private String path;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    @JoinColumn(name = "type_id")
    private Document_type type;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    public Document() {
    }

    public Document(int documentId, Asset asset, String documentName, String path, Document_type type, LocalDateTime uploadDate) {
        this.documentId = documentId;
        this.asset = asset;
        this.documentName = documentName;
        this.path = path;
        this.type = type;
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

    public Document_type getType() {
        return type;
    }

    public void setType(Document_type type) {
        this.type = type;
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
                ", type=" + type +
                ", uploadDate=" + uploadDate +
                '}';
    }
}

