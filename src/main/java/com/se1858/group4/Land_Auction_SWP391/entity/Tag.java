package com.se1858.group4.Land_Auction_SWP391.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Tag")
public class Tag {
    @Id
    @Column(name = "tag_id")
    private int tagId;

    @Column(name = "tag_name", nullable = false)
    private String tagName;

    @Column(name = "description")
    private String description;

    public Tag() {
    }

    public Tag(int tagId, String tagName, String description) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.description = description;
    }

    public int getTagId() {
        return tagId;
    }

    public void setTagId(int tagId) {
        this.tagId = tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "tagId=" + tagId +
                ", tagName='" + tagName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}

