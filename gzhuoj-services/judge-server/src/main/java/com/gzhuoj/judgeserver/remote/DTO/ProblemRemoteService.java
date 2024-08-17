package com.gzhuoj.judgeserver.remote.DTO;


import com.gzhuoj.judgeserver.remote.DTO.resp.ProblemRespDTO;
import common.convention.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.beans.Encoder;

/**
 * problem远程调用接口层
 */
@FeignClient(name = "gzhuoj-problem-service",
        configuration = {ProblemRemoteService.MultipartSupportConfig.class})
public interface ProblemRemoteService {
    @GetMapping("/api/gzhuoj-problem/problem/query")
    Result<ProblemRespDTO> queryProByNum(@RequestParam("num") Integer num);

    class MultipartSupportConfig {
        @Bean
        @Primary
        @Scope("prototype")
        public Encoder feignFormEncoder() {
            return new Encoder();
        }
    }
}
