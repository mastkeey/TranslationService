package ru.mastkey.translater.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "yandex")
@Data
public class YandexClientProperties {
    private String apiKey;
    private String url;
    private String languagesUrl;
}
