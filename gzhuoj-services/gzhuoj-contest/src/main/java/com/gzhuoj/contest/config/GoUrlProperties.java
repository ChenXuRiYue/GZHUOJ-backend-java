package com.gzhuoj.contest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "gzhuoj.go-url")

public class GoUrlProperties {
    String Url;
}
