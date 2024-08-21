package com.gzhuoj.judgeserver.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzhuoj.judgeserver.model.entity.SubmitDO;
import com.gzhuoj.judgeserver.service.JudgeServerService;
import common.convention.result.Result;
import common.convention.result.Results;
import lombok.RequiredArgsConstructor;
import com.gzhuoj.judgeserver.dto.req.ToJudgeReqDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import static common.convention.errorcode.BaseErrorCode.JUDGE_PARAM_NOT_FOUND_ERROR;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/gzhuoj-judge-server")
public class JudgeController {
    private final JudgeServerService judgeServerService;

    private Map<String, CountDownLatch> latchMap = new ConcurrentHashMap<>();
    private Map<String, String> responseMap = new ConcurrentHashMap<>();
    @PostMapping("/test")
    public Result<Void> test(@RequestBody ToJudgeReqDTO requestParam){

        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        // 创建请求数据
        RunRequest runRequest = new RunRequest();
        List<RunRequest.Cmd> cmdList = new ArrayList<>();
        RunRequest.Cmd cmd = new RunRequest.Cmd();

        cmd.setArgs(List.of("C:\\mingw64\\bin\\g++", "a.cc", "-o", "a"));
        cmd.setEnv(List.of("PATH=C:\\mingw64\\bin;"));

        List<RunRequest.Cmd.File> files = new ArrayList<>();
        RunRequest.Cmd.File file1 = new RunRequest.Cmd.File();
        file1.setContent("");
        files.add(file1);

        RunRequest.Cmd.File file2 = new RunRequest.Cmd.File();
        file2.setName("stdout");
        file2.setMax(10240);
        files.add(file2);

        RunRequest.Cmd.File file3 = new RunRequest.Cmd.File();
        file3.setName("stderr");
        file3.setMax(10240);
        files.add(file3);
        cmd.setFiles(files);

        cmd.setCpuLimit(10000000000L);
        cmd.setMemoryLimit(104857600L);
        cmd.setProcLimit(50);

        Map<String, RunRequest.Cmd.FileContent> copyIn = new HashMap<>();
        RunRequest.Cmd.FileContent fileContent = new RunRequest.Cmd.FileContent();
        fileContent.setContent("#include <iostream>\n#include <signal.h>\n#include <unistd.h>\nusing namespace std;\nint main() {\nint a, b;\ncin >> a >> b;\ncout << a + b << endl;\n}");
        copyIn.put("a.cc", fileContent);
        cmd.setCopyIn(copyIn);

        cmd.setCopyOutCached(List.of("a.exe"));

        cmdList.add(cmd);
        runRequest.setCmd(cmdList);

        // 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);

        // 封装请求体
        HttpEntity<RunRequest> requestEntity = new HttpEntity<>(runRequest, headers);

        // 发起 POST 请求
        String url = "http://localhost:5050/run";
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, requestEntity, String.class);

        // 处理响应
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            String responseBody = responseEntity.getBody();
            System.out.println("Response: " + responseBody);
        } else {
            System.out.println("Request failed: " + responseEntity.getStatusCode());
        }

        return Results.success();
    }

    @PostMapping("judge")
    public Result<Void> judge(@RequestBody ToJudgeReqDTO requestParam){
        SubmitDO submitDO = requestParam.getSubmitDO();
        if(submitDO == null || requestParam.getJudgeServerIp() == null || requestParam.getJudgeServerPort() == null){
            // 应该告诉contest这边再次检测到参数不符合
            return Results.failure(JUDGE_PARAM_NOT_FOUND_ERROR.code(), JUDGE_PARAM_NOT_FOUND_ERROR.message());
        }
        judgeServerService.judge(submitDO);
        return Results.success().setMessage("成功发送！");
    }
}
