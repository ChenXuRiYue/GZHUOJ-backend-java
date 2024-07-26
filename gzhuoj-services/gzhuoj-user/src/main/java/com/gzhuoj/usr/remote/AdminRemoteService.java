package com.gzhuoj.usr.remote;

import com.gzhuoj.usr.remote.dto.req.UpdateProblemReqDTO;
import common.convention.result.Result;
import feign.form.spring.SpringFormEncoder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.beans.Encoder;
import java.util.List;

@FeignClient(name = "gzhuoj-problem-service",
        configuration = {AdminRemoteService.MultipartSupportConfig.class})
public interface AdminRemoteService {

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
