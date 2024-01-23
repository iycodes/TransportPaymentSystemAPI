package com.mkyong;

import com.mkyong.model.Book;
import com.mkyong.model.Userr;
import com.mkyong.repository.BookRepository;
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

@SpringBootApplication
public class MainApplication {

    private static final Logger log = LoggerFactory.getLogger(MainApplication.class);
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;

    public static void main(String[] args) {
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

            Book b1 = new Book("Book A",
                    BigDecimal.valueOf(9.99)

            );
            Book b2 = new Book("Book B",
                    BigDecimal.valueOf(19.99)

            );
            Book b3 = new Book("Book C",
                    BigDecimal.valueOf(29.99)

            );
            Book b4 = new Book("Book D",
                    BigDecimal.valueOf(39.99)


            );
            Userr userr1 = new Userr(
                    "Iyanuoluwa", "iyanuoluwafanoro@gmail.com", "08142751683", "pyrex007"
            );
            Userr userr2 = new Userr("Iyanu", "iyanufanoro@gmail.com", "08142751683", "pyrex007");

            bookRepository.saveAll(List.of(b1, b2, b3, b4));
            userRepository.saveAll(List.of(userr1, userr2));

        };
    }

}