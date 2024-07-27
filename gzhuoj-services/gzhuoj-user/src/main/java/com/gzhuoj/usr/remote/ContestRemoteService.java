package com.gzhuoj.usr.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.beans.Encoder;

@FeignClient(name = "gzhuoj-contest-service",
        configuration = {ContestRemoteService.MultipartSupportConfig.class})
public interface ContestRemoteService {
    @GetMapping("/gzhuoj/contest/status")
    void changeStatus(@RequestParam("id") Integer id, @RequestParam("status") Integer status);

    class MultipartSupportConfig {
        @Bean
        @Primary
        @Scope("prototype")
        public Encoder feignFormEncoder() {
            return new Encoder();
        }
    }
}
