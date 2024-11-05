package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.Topic;
import com.se1858.group4.Land_Auction_SWP391.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TopicService {

    private TopicRepository topicRepository;

    @Autowired
    public TopicService(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public List<Topic> getMainTopics() {
        return topicRepository.findByParentTopicIsNull();
    }

    public List<Topic> getSubTopics(int parentId) {
        return topicRepository.findByParentTopic_TopicId(parentId);
    }

    public Optional<Topic> getTopicById(int topicId) {
        return topicRepository.findById(topicId);
    }

    public boolean updateTopicName(int topicId, String newTopicName) {
        Optional<Topic> topicOptional = topicRepository.findById(topicId);
        if (topicOptional.isPresent()) {
            Topic topic = topicOptional.get();
            topic.setTopicName(newTopicName);
            topicRepository.save(topic);
            return true;
        }
        return false;
    }

    public void deleteTopic(int topicId) {
        topicRepository.deleteById(topicId);
    }
}

