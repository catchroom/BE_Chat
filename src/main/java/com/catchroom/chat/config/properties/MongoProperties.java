package com.catchroom.chat.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "spring.data.mongodb")
public class MongoProperties {
    String uri;
    String username;
    String password;
    String database;
}