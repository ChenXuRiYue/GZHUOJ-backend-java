package com.gzhuoj.contest.service.goJudge.Impl;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzhuoj.contest.config.GoUrlProperties;
import com.gzhuoj.contest.dto.req.goJudge.goJudgeStatusReqDTO;
import com.gzhuoj.contest.dto.req.goJudge.goJudgeSubmitReqDTO;
import com.gzhuoj.contest.dto.resp.goJudge.goJudgeStatusRespDTO;
import com.gzhuoj.contest.dto.resp.goJudge.goJudgeSubmitRespDTO;
import com.gzhuoj.contest.service.goJudge.goJudgeService;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.springframework.web.client.RestTemplate;
@Component
public class goJudgeServiceImpl implements goJudgeService {
    RestTemplate restTemplate;

    @Autowired
    GoUrlProperties goUrlProperties;

    public void goJudgeSubmit() {}

    @Override
    public goJudgeSubmitRespDTO Submit(goJudgeSubmitReqDTO reqDTO) {

        String url = goUrlProperties.getUrl() + "/judge/submit";
        HttpResponse<JsonNode> response = Unirest.post(url)
                .header("gzhuoj-token", "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.bnVsbA.ngHc0QXEGahdUyL-1O4DOgVTp1-7K0giruUHNt3SynBt4XZvdbbgI-vZdKeAgRtAQXIWZH-0cVycZsAgcGqjf5niheN0RDxX8c-kVi7cCn57hBe-uLJXM0xsi313oRzN7qw3hF0Sd8w-g8zUH1IOZcN0pLLGkwOHP0fvw0uWZ2G5QLUbYreq2xp25ajn9JwgC5udcORLj3DbcTYh4RnyRHmxWBVKySHg2TU-zAdfghijiGg_r7k79h4EzxTp8sZlp-_K_5IitZgK0XyLlUBMsP4cdRoFKsXYH-IhtUqZUO9DcSG8uEfvYBOHLOSUmZluDaaYMIQJ_UFPB1STnhVSKw")
                //.header("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .header("Content-Type", "application/json")
                .body("{\r\n  \"id\": \""+reqDTO.id+"\",\r\n  \"problem\": \""+reqDTO.problem+"\",\r\n  \"lang\": \""+reqDTO.lang+"\",\r\n  \"source\": \""+reqDTO.source+"\",\r\n  \"time_limit\": "+reqDTO.timeLimit+",\r\n  \"memory_limit\": \""+reqDTO.memoryLimit+"\",\r\n  \"storage_path\": \""+reqDTO.storagePath+"\",\r\n  \"exec_constraints\": [\r\n  ]\r\n}\r\n")
                .asJson();
        //JsonNode body = response.getBody();
        String jsonString = response.getBody().toString();

        // 使用 Jackson 解析 JSON
        ObjectMapper mapper = new ObjectMapper();
        goJudgeSubmitRespDTO respDTO = null;
        try {
            respDTO = mapper.readValue(jsonString, goJudgeSubmitRespDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return respDTO;
    }

    @Override
    public goJudgeStatusRespDTO Status(goJudgeStatusReqDTO reqDTO) {
        String url = goUrlProperties.getUrl() + "/judge/get_status";
        //goJudgeStatusRespDTO respDTO = new goJudgeStatusRespDTO();
        HttpResponse<JsonNode> response = Unirest.get(url+"/{status}")
                .routeParam("status", reqDTO.getSubmitId())
                .header("gzhuoj-token", "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.bnVsbA.ngHc0QXEGahdUyL-1O4DOgVTp1-7K0giruUHNt3SynBt4XZvdbbgI-vZdKeAgRtAQXIWZH-0cVycZsAgcGqjf5niheN0RDxX8c-kVi7cCn57hBe-uLJXM0xsi313oRzN7qw3hF0Sd8w-g8zUH1IOZcN0pLLGkwOHP0fvw0uWZ2G5QLUbYreq2xp25ajn9JwgC5udcORLj3DbcTYh4RnyRHmxWBVKySHg2TU-zAdfghijiGg_r7k79h4EzxTp8sZlp-_K_5IitZgK0XyLlUBMsP4cdRoFKsXYH-IhtUqZUO9DcSG8uEfvYBOHLOSUmZluDaaYMIQJ_UFPB1STnhVSKw")
                //.header("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
                .asJson();

        //JsonNode body = response.getBody();
        String jsonString = response.getBody().toString();

        // 使用 Jackson 解析 JSON
        ObjectMapper mapper = new ObjectMapper();
        goJudgeStatusRespDTO respDTO = null;
        try {
            respDTO = mapper.readValue(jsonString, goJudgeStatusRespDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respDTO;
    }

    public static void main(String[] args)  {
        goJudgeServiceImpl goJudgeService = new goJudgeServiceImpl();
        goJudgeService.goUrlProperties=new GoUrlProperties();
        goJudgeService.goUrlProperties.setUrl("http://8.138.4.17:8080");


        goJudgeSubmitReqDTO reqDTO=new goJudgeSubmitReqDTO();
        reqDTO.id="haha";
        reqDTO.problem="test";
        reqDTO.lang="lang.py.cpython.310";
        reqDTO.source="import time\\n\\nprint(sum(map(int, input().split())))";
        reqDTO.timeLimit=100000000000L;
        reqDTO.memoryLimit="256MiB";
        reqDTO.storagePath="local://host/root/gzhuoj_judger/problem/aplusb/problem";
        goJudgeSubmitRespDTO submit = goJudgeService.Submit(reqDTO);
        System.out.println(submit.getMessage());

        goJudgeStatusReqDTO goJudgeStatusReqDTO = new goJudgeStatusReqDTO();
        goJudgeStatusReqDTO.setSubmitId("haha");
        goJudgeStatusRespDTO status = goJudgeService.Status(goJudgeStatusReqDTO);
        //System.out.println(status.result.id);

    }
}
