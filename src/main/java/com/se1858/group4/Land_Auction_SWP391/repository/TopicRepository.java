package com.se1858.group4.Land_Auction_SWP391.repository;

import com.se1858.group4.Land_Auction_SWP391.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TopicRepository extends JpaRepository<Topic, Integer> {

    // Tìm các topic chính (parent_topic_id = NULL)
    List<Topic> findByParentTopicIsNull();

    // Tìm các sub-topic theo parent_topic_id
    List<Topic> findByParentTopic_TopicId(int parentId);
}



