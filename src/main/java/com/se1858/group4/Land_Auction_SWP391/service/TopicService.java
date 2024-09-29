package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.Topic;
import com.se1858.group4.Land_Auction_SWP391.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TopicService {

    @Autowired
    private TopicRepository topicRepository;

    public List<Topic> getMainTopics() {
        return topicRepository.findByParentTopicIsNull();
    }

    public List<Topic> getSubTopics(int parentId) {
        return topicRepository.findByParentTopic_TopicId(parentId);
    }

    public Optional<Topic> getTopicById(int topicId) {
        return topicRepository.findById(topicId);
    }
}

