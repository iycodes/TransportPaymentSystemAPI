package com.mkyong;

import com.mkyong.model.Book;
import com.mkyong.model.TransactionEntity;
import com.mkyong.model.UserEntity;
import com.mkyong.model.UserEntity;
import com.mkyong.model.UserEntity.UserRole;
import com.mkyong.model.enums.TxStatus;
import com.mkyong.model.enums.TxType;
import com.mkyong.repository.TransactionRepository;
import com.mkyong.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

@SpringBootApplication
public class MainApplication {

    private static final Logger log = LoggerFactory.getLogger(MainApplication.class);
    @Autowired
    UserRepository userRepository;
    @Autowired
    TransactionRepository transactionRepository;

    public static void main(String[] args) {

        Properties properties = new Properties();
        properties.setProperty("autosave", "always");
        properties.setProperty("cleanupSavepoints", "true");
        SpringApplication.run(MainApplication.class, args);
    }
    // Run this if app.db.init.enabled = true

    @Bean
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

    @Bean
    @ConditionalOnProperty(prefix = "app", name = "db.init.enabled", havingValue = "true")
    public CommandLineRunner demoCommandLineRunner() {

        return args -> {

            System.out.println("Running.....");
            // TransactionEntity mockTx = new TransactionEntity("1-2-17149979931", "Ride
            // Payment", 1, 2,
            // BigDecimal.valueOf(60),
            // TxStatus.success, TxType.ridePayment);
            UserEntity userr1 = new UserEntity(
                    "Iyanuoluwa", "iyanuoluwafanoro@gmail.com", "08142751683",
                    "$2a$12$XG609EMUTEXEQGFpYp76JOe.wX1MTz5WHWxJtEXOSqLF4AidbcOvu",
                    UserRole.user);
            UserEntity userr2 = new UserEntity("Iyanu", "fanoroiyanu@gmail.com",
                    "08142751683",
                    "$2a$12$XG609EMUTEXEQGFpYp76JOe.wX1MTz5WHWxJtEXOSqLF4AidbcOvu", UserRole.user);
            UserEntity driver = new UserEntity("Sogo", "sogo113@gmail.com", "0813378499",
                    "sogo123", UserRole.driver);
            // transactionRepository.save(mockTx);
            // bookRepository.saveAll(List.of(b1, b2, b3, b4));
            // userRepository.saveAll(List.of(userr1, driver, userr2));

        };
    }

}