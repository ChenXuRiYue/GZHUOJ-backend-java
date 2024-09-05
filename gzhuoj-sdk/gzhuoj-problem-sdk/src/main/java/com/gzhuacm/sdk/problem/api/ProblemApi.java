package com.gzhuacm.sdk.problem.api;

import com.gzhuacm.sdk.problem.model.dto.*;
import org.gzhuoj.common.sdk.convention.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.beans.Encoder;
import java.util.List;

@FeignClient(name = "gzhuoj-problem-service",
        configuration = {ProblemApi.MultipartSupportConfig.class})
public interface ProblemApi {
    @GetMapping("/api/gzhuoj-problem/problem/query")
    Result<ProblemRespDTO> queryProByNum(@RequestParam("problemNum") Integer problemNum);

    @GetMapping("/api/gzhuoj-problem/problem/selectProblemById")
    ProblemPrintDTO selectProblemById(@RequestParam("problemNum") Integer problemNum);

    @PostMapping("/api/gzhuoj-problem/problem/get/contents")
    Result<ProblemContentRespDTO> getProblemContent(@RequestBody ProblemReqDTO problemReqDTO);


    @GetMapping("/api/gzhuoj-problem/judge/testcase/upload")
    Result<List<ProblemJudgeResourcesRespDTO>> upload(@RequestParam("problemNum") Integer problemNum);

    class MultipartSupportConfig {
        @Bean
        @Primary
        @Scope("prototype")
        public Encoder feignFormEncoder() {
            return new Encoder();
        }
    }
}
