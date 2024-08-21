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
import org.springframework.web.socket.WebSocketSession;

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

    public static void main(String[] args) {
        String s = "#include<bits/stdc++.h>\n" +
                "using namespace std;\n" +
                "vector<vector<pair<int, int>>> Char = {\n" +
                "    {\n" +
                "        {0, 0}, {0, 1}, {0, 2},\n" +
                "        {1, 0},         {1, 2},\n" +
                "        {2, 0}, {2, 1}, {2, 2},\n" +
                "        {3, 0},\n" +
                "        {4, 0}\n" +
                "    },\n" +
                "    {\n" +
                "        {0, 0}, {0, 1}, {0, 2},\n" +
                "                        {1, 2},\n" +
                "                {2, 1}, {2, 2},\n" +
                "                        {3, 2},\n" +
                "                        {4, 2}\n" +
                "    },\n" +
                "    {\n" +
                "        {0, 0}, {0, 1}, {0, 2},\n" +
                "        {1, 0},         {1, 2},\n" +
                "        {2, 0}, {2, 1}, {2, 2},\n" +
                "        {3, 0},         {3, 2},\n" +
                "        {4, 0}, {4, 1}, {4, 2}\n" +
                "    }\n" +
                "};\n" +
                "int main() \n" +
                "{\n" +
                "    ios::sync_with_stdio(false);\n" +
                "    cin.tie(nullptr);\n" +
                "    int n, m;\n" +
                "    cin >> n >> m;\n" +
                "    vector<string> s(n);\n" +
                "    for(int i = 0; i < n; i++ ){\n" +
                "    \tcin >> s[i];\n" +
                "    }\n" +
                "\n" +
                "    vector<int> ans(3);\n" +
                "    auto print = [&](int i, int j, int id){\n" +
                "        for(auto [dx, dy] : Char[id]){\n" +
                "            int x = i + dx, y = j + dy;\n" +
                "            s[x][y] = '.';\n" +
                "        }\n" +
                "        ans[id] += 1;\n" +
                "    };\n" +
                "    for(int i = 0; i < m; i++ ){\n" +
                "        for(int j = 0; j < n; j++ ){\n" +
                "            if(s[j][i] != '#'){\n" +
                "                continue;\n" +
                "            }\n" +
                "            // 先判断字符1\n" +
                "            if(s[j + 1][i] != '#'){\n" +
                "                print(j, i, 1);\n" +
                "                continue;\n" +
                "            }\n" +
                "            // 再判断字符3\n" +
                "            bool ok = true;\n" +
                "            for(auto [dx, dy] : Char[2]){\n" +
                "                int x = j + dx, y = i + dy;\n" +
                "                if(s[x][y] != '#'){\n" +
                "                    ok = false;\n" +
                "                }\n" +
                "            }\n" +
                "            print(j, i, (ok ? 2 : 0));\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    for(int i = 0; i < 3; i++ ){\n" +
                "        cout << ans[i] << \" \\n\"[i == 2];\n" +
                "    }\n" +
                "    return 0;\n" +
                "}";

    }
}
