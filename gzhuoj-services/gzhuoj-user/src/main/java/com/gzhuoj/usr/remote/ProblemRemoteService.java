package com.gzhuoj.usr.remote;

import com.gzhuoj.usr.remote.dto.req.UpdateProblemReqDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.beans.Encoder;

@FeignClient(name = "gzhuoj-problem-service",
        configuration = {ProblemRemoteService.MultipartSupportConfig.class})
public interface ProblemRemoteService {

    @PostMapping("/gzhuoj/problem/update")
    void updateProblem(@RequestBody UpdateProblemReqDTO requestParam);

    class MultipartSupportConfig {
        @Bean
        @Primary
        @Scope("prototype")
        public Encoder feignFormEncoder() {
            return new Encoder();
        }
    }
}
