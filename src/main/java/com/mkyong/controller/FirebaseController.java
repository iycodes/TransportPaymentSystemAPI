package com.mkyong.controller;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.internal.FirebaseService;
import com.google.firebase.messaging.BatchResponse;

@RestController
@RequestMapping("/firebase")
public class FirebaseController {

    private final com.mkyong.service.FirebaseService firebaseService;

    public FirebaseController(final com.mkyong.service.FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }

    @PostMapping("/topic/{topic}")
    public ResponseEntity<String> firebaseNotify(@PathVariable("topic") final String topic,
            @RequestBody final String payload) {
        String messageId = firebaseService.topicNotify(topic, payload);
        return new ResponseEntity<>(messageId, HttpStatus.ACCEPTED);
    }

    @PostMapping("/token/{token}")
    public ResponseEntity<String> tokenNotify(@PathVariable("token") final String token,
            @RequestBody final String body) {
        System.out.println("body string is " + body);
        String messageId = firebaseService.notifyToken(token, body);
        return new ResponseEntity<>(messageId, HttpStatus.ACCEPTED);
    }

    @GetMapping("/notifyTokens/test")
    public ResponseEntity<Object> notifyTokens() {
        ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(
                "eccOJUP5SEqombLOaYYV05:APA91bGSXnH7vueB6VJZWlFaFiMNymdTvWzNWsXO3hsOmC1ixDfvz7TvT0145ML1RIUcn4nhypBw5FwecKuPKD3OdxY-cYs03SKNjC54TjPXN3r2ZMVf3YkgDnMd410Vhz86nUosbhhZ"));
        BatchResponse response = firebaseService.notifyTokens(tokens, "title testing", "Testing body");
        return ResponseEntity.ok(response);
    }

}
