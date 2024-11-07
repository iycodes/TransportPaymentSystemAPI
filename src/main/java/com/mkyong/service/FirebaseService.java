package com.mkyong.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;

@Service
public class FirebaseService {
    private final FirebaseMessaging firebaseMessaging;

    public FirebaseService(final FirebaseMessaging firebaseMessaging_) {
        this.firebaseMessaging = firebaseMessaging_;
    }

    public String topicNotify(final String topic, final String payload) {
        Message message = Message.builder()
                .setTopic(topic)
                .putData("payload", payload)
                .build();
        try {
            return firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException("Error sending notification: " + e.getMessage(), e);
        }
    }

    public String notifyToken(final String token, final String payload) {
        Notification notification = Notification.builder().setTitle("notification title")
                .setBody("testing notification bruhh").build();
        Message message = Message.builder()
                .setToken(token)
                // .putData("body", payload)
                // .putData("title", "a notifiction")
                .setNotification(notification)
                // .putData("payload", payload)
                .build();
        try {
            return firebaseMessaging.send(message);
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException("Error sending notification: " + e.getMessage(), e);
        }
    }

    public BatchResponse notifyTokens(final ArrayList<String> tokens, final String title, final String body) {
        Notification notification = Notification.builder().setTitle(title)
                .setBody(body).build();
        MulticastMessage msg = MulticastMessage
                .builder().addAllTokens(tokens).setNotification(notification).build();
        try {
            BatchResponse response = firebaseMessaging.sendEachForMulticast(msg);
            System.err.println("batch response from multicast send is " + response);
            return response;
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException("Error sending notification: " + e.getMessage(), e);
        }
    }

}
