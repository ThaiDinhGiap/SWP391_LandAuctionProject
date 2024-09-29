package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.entity.Question;
import com.se1858.group4.Land_Auction_SWP391.entity.Topic;
import com.se1858.group4.Land_Auction_SWP391.service.QuestionService;
import com.se1858.group4.Land_Auction_SWP391.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chatbot")
public class ChatBotController {

    @Autowired
    private TopicService topicService;

    @Autowired
    private QuestionService questionService;

    @GetMapping("/topics/{parentId}")
    public Map<String, Object> getSubTopicsOrQuestions(@PathVariable int parentId) {
        Map<String, Object> response = new HashMap<>();

        // Lấy danh sách các sub-topic dựa trên parentId
        List<Topic> subTopics = topicService.getSubTopics(parentId);

        if (!subTopics.isEmpty()) {
            response.put("type", "topics");
            response.put("data", subTopics);
        } else {
            // Nếu không có sub-topic, lấy các câu hỏi liên quan đến topic đó
            List<Question> questions = questionService.getQuestionsByTopic(parentId);
            response.put("type", "questions");
            response.put("data", questions);
        }

        return response;
    }

    @GetMapping("/topics")
    public List<Topic> getMainTopics() {
        return topicService.getMainTopics();
    }
}

