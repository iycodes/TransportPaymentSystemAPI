package com.mkyong.configs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.api.client.util.Value;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.core.io.Resource;
import com.mkyong.properties.FirebaseProperties;

@Configuration
@EnableConfigurationProperties(FirebaseProperties.class)
public class FirebaseConfig {

    private final FirebaseProperties firebaseProperties;

    public FirebaseConfig(final FirebaseProperties firebaseProperties) {
        this.firebaseProperties = firebaseProperties;
    }

    // @Value("classpath:/firebase-service-account")
    // private Resource privateKey;

    @Bean
    GoogleCredentials googleCredentials() throws IOException {

        // if (firebaseProperties.getServiceAccount() != null) {
        try (final InputStream inputStream = firebaseProperties.getServiceAccount().getInputStream()) {
            return GoogleCredentials.fromStream(inputStream);
        }
        // InputStream credentials = new
        // ByteArrayInputStream(privateKey.getContentAsByteArray());

        // } else {
        // // Use standard credentials chain. Useful when running inside GKE
        // return GoogleCredentials.getApplicationDefault();
        // }
    }

    @Bean
    public FirebaseApp firebaseApp(GoogleCredentials credentials) {
        final FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();
        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(firebaseOptions);
        } else {
            return FirebaseApp.getInstance();
        }
    }

    @Bean
    FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }
}