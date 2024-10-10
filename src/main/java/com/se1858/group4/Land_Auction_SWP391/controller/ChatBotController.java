package com.se1858.group4.Land_Auction_SWP391.controller;

import com.se1858.group4.Land_Auction_SWP391.dto.StaffDTO;
import com.se1858.group4.Land_Auction_SWP391.entity.Question;
import com.se1858.group4.Land_Auction_SWP391.entity.Staff;
import com.se1858.group4.Land_Auction_SWP391.entity.Topic;
import com.se1858.group4.Land_Auction_SWP391.repository.StaffRepository;
import com.se1858.group4.Land_Auction_SWP391.service.QuestionService;
import com.se1858.group4.Land_Auction_SWP391.service.StaffService;
import com.se1858.group4.Land_Auction_SWP391.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/chatbot")
public class ChatBotController {

    private final TopicService topicService;
    private final QuestionService questionService;
    private final StaffService staffService;

    @Autowired
    public ChatBotController(TopicService topicService, QuestionService questionService, StaffService staffService) {
        this.topicService = topicService;
        this.questionService = questionService;
        this.staffService = staffService;
    }

    @GetMapping("/topics/{parentId}")
    public Map<String, Object> getSubTopicsOrQuestions(@PathVariable int parentId) {
        Map<String, Object> response = new HashMap<>();

        Optional<Topic> optionalTopic = topicService.getTopicById(parentId);
        Topic topic = optionalTopic.get();

        if (topic.getTopicName().equals("Direct Support")) {
            List<StaffDTO> staffList = staffService.getCustomerCareStaffDTOs();
            response.put("type", "Direct Support");
            response.put("data", staffList);
        } else {
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
        }
        return response;
    }

    @GetMapping("/topics")
    public List<Topic> getMainTopics() {
        return topicService.getMainTopics();
    }
}

