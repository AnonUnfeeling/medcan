package ua.softgroup.medreview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * @author Oleksandr Tyshkovets <sg.olexander@gmail.com>
 */
@SpringBootApplication
public class MedReviewApp {

    public static void main(String[] args) {
        SpringApplication.run(MedReviewApp.class, args);
    }
}
