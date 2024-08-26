package com.gzhuacm.sdk.contest.api;

import com.gzhuacm.sdk.contest.model.dto.ToJudgeDTO;
import org.gzhuoj.common.sdk.convention.result.Result;
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
        configuration = {JudgeServerApi.MultipartSupportConfig.class})
public interface JudgeServerApi {
    @PostMapping("/api/gzhuoj-judge-server/judge")
    Result<Void> judge(@RequestBody ToJudgeDTO toJudgeDTO);

    class MultipartSupportConfig {
        @Bean
        @Primary
        @Scope("prototype")
        public Encoder feignFormEncoder() {
            return new Encoder();
        }
    }
}
