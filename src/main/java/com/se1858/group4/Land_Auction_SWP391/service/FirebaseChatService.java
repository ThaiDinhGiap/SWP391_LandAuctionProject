package com.se1858.group4.Land_Auction_SWP391.service;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class FirebaseChatService {

    private final FirebaseDatabase firebaseDatabase;

    public FirebaseChatService() {
        this.firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void saveChatMessage(String sessionId, String sender, String content) {
        DatabaseReference chatRef = firebaseDatabase.getReference("chats").child(sessionId).child("messages");

        String messageId = chatRef.push().getKey();
        Map<String, Object> message = new HashMap<>();
        message.put("sender", sender);
        message.put("content", content);
        message.put("timestamp", System.currentTimeMillis());

        assert messageId != null;
        chatRef.child(messageId).setValueAsync(message);
    }

    public void createChatSession(String sessionId, Integer clientId, Integer staffId) {
        DatabaseReference sessionRef = firebaseDatabase.getReference("chats").child(sessionId);

        Map<String, Object> sessionData = new HashMap<>();
        sessionData.put("clientId", clientId);
        sessionData.put("staffId", staffId);
        sessionData.put("created_at", System.currentTimeMillis());

        sessionRef.setValueAsync(sessionData);
    }
}

