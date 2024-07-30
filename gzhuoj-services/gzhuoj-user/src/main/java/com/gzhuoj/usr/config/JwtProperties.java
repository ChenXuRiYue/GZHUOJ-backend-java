package com.gzhuoj.usr.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import java.time.Duration;

/**
 * 定义与JWT工具有关的属性
 */
@Data
@ConfigurationProperties(prefix = "gzhuoj.jwt")
public class JwtProperties {
    private Resource location;
    private String password;
    private String alias;
    // 默认三十分钟
    private Duration tokenTTL = Duration.ofHours(30);
}
