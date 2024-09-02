package com.gzhuacm.sdk.judgeserver.api;

import com.gzhuacm.sdk.judgeserver.model.dto.req.SubmitRemoteDTO;
import org.gzhuoj.common.sdk.convention.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.beans.Encoder;
@FeignClient(name = "gzhuoj-contest-service",
        configuration = {ContestRemoteService.MultipartSupportConfig.class})
public interface ContestRemoteService {

    @PutMapping("/api/gzhuoj-contest/submit")
    Result<Boolean> submitUpdate(@RequestBody SubmitRemoteDTO requestParam);

    @GetMapping("/api/gzhuoj-contest/code")
    Result<String> getCode(@RequestParam("submitId") Integer submitId);
    class MultipartSupportConfig {
        @Bean
        @Primary
        @Scope("prototype")
        public Encoder feignFormEncoder() {
            return new Encoder();
        }
    }
}
