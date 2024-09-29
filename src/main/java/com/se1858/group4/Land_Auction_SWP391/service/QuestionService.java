package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.Question;
import com.se1858.group4.Land_Auction_SWP391.entity.Topic;
import com.se1858.group4.Land_Auction_SWP391.repository.QuestionRepository;
import com.se1858.group4.Land_Auction_SWP391.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TopicRepository topicRepository;

    public List<Question> getQuestionsByTopic(int topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid topic ID: " + topicId));
        return questionRepository.findByTopic(topic);
    }
}

