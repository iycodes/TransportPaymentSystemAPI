package com.mkyong.configs;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mkyong.repository.UserRepository;
import com.mkyong.service.UserService;

import java.io.FileInputStream;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApplicationConfiguration {
    private final UserRepository userRepository;

    public ApplicationConfiguration(UserRepository userRepository_) {
        this.userRepository = userRepository_;
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username -> userRepository.findById(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    // @Bean
    // FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
    // return FirebaseMessaging.getInstance(firebaseApp);
    // }

    // @Bean
    // FirebaseApp firebaseApp(GoogleCredentials credentials) {
    // FirebaseOptions options = FirebaseOptions.builder()
    // .setCredentials(credentials)
    // .build();

    // return FirebaseApp.initializeApp(options);
    // }

    // @Bean
    // GoogleCredentials googleCredentials() {

    // if (firebaseProperties.getServiceAccount() != null) {
    // try (InputStream is =
    // firebaseProperties.getServiceAccount().getInputStream()) {
    // return GoogleCredentials.fromStream(is);
    // }
    // } else {
    // // Use standard credentials chain. Useful when running inside GKE
    // return GoogleCredentials.getApplicationDefault();
    // }
    // }
    // FileInputStream serviceAccount =
    // new FileInputStream("path/to/serviceAccountKey.json");

    // FirebaseOptions options = FirebaseOptions.builder()
    // .setCredentials(GoogleCredentials.fromStream(serviceAccount))
    // .build();

}
