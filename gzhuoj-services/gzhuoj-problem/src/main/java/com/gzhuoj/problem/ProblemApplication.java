package com.gzhuoj.problem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.gzhuoj.problem.mapper")
@EnableDiscoveryClient
@EnableTransactionManagement
public class ProblemApplication {
    public static void main(String[] args) {
        SpringApplication.run(ProblemApplication.class, args);
    }
}
