package ru.mastkey.translater;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class TranslaterApplication {

    public static void main(String[] args) {
        SpringApplication.run(TranslaterApplication.class, args);
    }

}
