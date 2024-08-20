package com.gzhuoj.contest.remote;


import com.gzhuoj.contest.remote.Resp.ProblemContentRespDTO;
import com.gzhuoj.contest.remote.Resp.ProblemRespDTO;
import common.convention.result.Result;
import common.model.pojo.ProblemPrintDTO;
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
    ProblemRespDTO queryProByNum(@RequestParam("num") Integer num);

    @GetMapping("/api/gzhuoj-problem/problem/selectProblemById")
    ProblemPrintDTO selectProblemById(@RequestParam("problemId") Integer problemId);

    @GetMapping("/api/gzhuoj-problem/get/problem/content")
    Result<ProblemContentRespDTO> getProblemContent(Integer problemNum);

    class MultipartSupportConfig {
        @Bean
        @Primary
        @Scope("prototype")
        public Encoder feignFormEncoder() {
            return new Encoder();
        }
    }
}
