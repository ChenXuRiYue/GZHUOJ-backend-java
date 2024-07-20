package com.gzhuoj.usr.remote;

import com.gzhuoj.usr.remote.dto.req.UpdateProblemReqDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "gzhuoj-problem-service")
public interface AdminRemoteService {

    @PostMapping("/gzhuoj/problem/update")
    void updateProblem(@RequestBody UpdateProblemReqDTO requestParam);
}
