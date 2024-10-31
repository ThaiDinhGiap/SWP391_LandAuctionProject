package com.se1858.group4.Land_Auction_SWP391.service;

import com.google.firebase.database.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Service
public class FirebaseChatService {

    private final FirebaseDatabase firebaseDatabase;

    public FirebaseChatService(FirebaseDatabase firebaseDatabase) {
        this.firebaseDatabase = firebaseDatabase;
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

    // Hàm để lấy danh sách tin nhắn của một phiên chat
    public List<Map<String, Object>> getChatMessages(String sessionId) throws InterruptedException {
        List<Map<String, Object>> messages = new ArrayList<>();
        DatabaseReference chatRef = firebaseDatabase.getReference("chats").child(sessionId).child("messages");

        CountDownLatch latch = new CountDownLatch(1);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> message = (Map<String, Object>) messageSnapshot.getValue();
                    messages.add(message);
                }
                latch.countDown();  // Giảm count của latch xuống 0
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
                latch.countDown();  // Giảm count của latch xuống 0 để tránh deadlock
            }
        });

        latch.await();  // Đợi cho đến khi quá trình lấy dữ liệu hoàn thành
        return messages;
    }

    public List<Map<String, Object>> getChatSessionsByStaffId(Integer staffId) throws InterruptedException {
        List<Map<String, Object>> sessions = new ArrayList<>();
        DatabaseReference chatRef = firebaseDatabase.getReference("chats");
        CountDownLatch latch = new CountDownLatch(1);
        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot sessionSnapshot : dataSnapshot.getChildren()) {
                    Map<String, Object> sessionData = (Map<String, Object>) sessionSnapshot.getValue();
                    if (sessionData != null && sessionData.get("staffId") != null && sessionData.get("staffId").toString().equals(staffId.toString())) {
                        sessionData.put("sessionId", sessionSnapshot.getKey());
                        sessions.add(sessionData);
                    }
                }
                latch.countDown();  // Giảm count của latch xuống 0
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
                latch.countDown();  // Để tránh deadlock
            }
        });

        latch.await();  // Đợi cho đến khi quá trình lấy dữ liệu hoàn thành
        return sessions;
    }


}

