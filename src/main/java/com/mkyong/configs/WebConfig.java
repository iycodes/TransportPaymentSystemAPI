package com.mkyong.configs;

import java.net.http.HttpHeaders;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {

    @Bean
    public WebClient webClient() {

        WebClient webClient = WebClient.builder()
                .baseUrl("https://mail.iycodes.com")
                // .defaultCookie("cookie-name", "cookie-value")
                .defaultHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", "https://mail.iycodes.com"))
                .build();
        return webClient;
    }
}