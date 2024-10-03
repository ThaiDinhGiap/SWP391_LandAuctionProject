package com.se1858.group4.Land_Auction_SWP391.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Topic")
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int topicId;

    private String topicName;

    @ManyToOne
    @JoinColumn(name = "parent_topic_id")
    @JsonBackReference
    private Topic parentTopic;

    @OneToMany(mappedBy = "parentTopic")
    @JsonManagedReference
    private List<Topic> subTopics;

    // Constructors, getters, and setters

    public Topic() {
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Topic getParentTopic() {
        return parentTopic;
    }

    public void setParentTopic(Topic parentTopic) {
        this.parentTopic = parentTopic;
    }

    public List<Topic> getSubTopics() {
        return subTopics;
    }

    public void setSubTopics(List<Topic> subTopics) {
        this.subTopics = subTopics;
    }
}

