package com.se1858.group4.Land_Auction_SWP391.service;

import com.se1858.group4.Land_Auction_SWP391.entity.Question;
import com.se1858.group4.Land_Auction_SWP391.entity.Topic;
import com.se1858.group4.Land_Auction_SWP391.repository.QuestionRepository;
import com.se1858.group4.Land_Auction_SWP391.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    private QuestionRepository questionRepository;
    private TopicRepository topicRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, TopicRepository topicRepository) {
        this.questionRepository = questionRepository;
        this.topicRepository = topicRepository;
    }

    public List<Question> getQuestionsByTopic(int topicId) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid topic ID: " + topicId));
        return questionRepository.findByTopic(topic);
    }

    public boolean updateQuestion(int questionId, String newQuestion, String newAnswer) {
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if (questionOptional.isPresent()) {
            Question question = questionOptional.get();
            question.setQuestion(newQuestion);
            question.setAnswer(newAnswer);
            questionRepository.save(question);
            return true;
        }
        return false;
    }

    public boolean saveQuestion(int topicId, String question, String answer) {
        try {
            Question questionEntity = new Question();
            Topic topic = topicRepository.findById(topicId).get();
            questionEntity.setTopic(topic);
            questionEntity.setQuestion(question);
            questionEntity.setAnswer(answer);
            questionRepository.save(questionEntity);
            topic.addQuestion(questionEntity);
            topicRepository.save(topic);
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteQuestion(int questionId) {
        questionRepository.deleteById(questionId);
    }
}

