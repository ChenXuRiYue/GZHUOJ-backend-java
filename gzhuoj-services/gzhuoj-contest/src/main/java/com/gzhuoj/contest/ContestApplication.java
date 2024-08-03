package com.gzhuoj.contest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.gzhuoj.contest.mapper")
@EnableDiscoveryClient
@EnableTransactionManagement
@EnableFeignClients("com.gzhuoj.contest.remote")
public class ContestApplication {
    public static void main(String[] args) {
        SpringApplication.run(ContestApplication.class, args);
    }
}
