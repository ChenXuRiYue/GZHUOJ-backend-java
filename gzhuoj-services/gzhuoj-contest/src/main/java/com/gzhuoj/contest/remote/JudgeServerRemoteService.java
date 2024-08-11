package com.gzhuoj.contest.remote;

import com.gzhuoj.contest.model.pojo.ToJudgeDTO;
import common.convention.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.beans.Encoder;

/**
 * judgeServer远程调用接口层
 */
@FeignClient(name = "gzhuoj-judge-service",
        configuration = {JudgeServerRemoteService.MultipartSupportConfig.class})
public interface JudgeServerRemoteService {
    @PostMapping("/api/gzhuoj-judge-server/judge")
    Result<Void> judge(@RequestBody ToJudgeDTO requestParam);

    class MultipartSupportConfig {
        @Bean
        @Primary
        @Scope("prototype")
        public Encoder feignFormEncoder() {
            return new Encoder();
        }
    }
}
