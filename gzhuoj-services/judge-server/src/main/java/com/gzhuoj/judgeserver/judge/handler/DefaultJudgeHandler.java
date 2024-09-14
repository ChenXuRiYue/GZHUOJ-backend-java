package com.gzhuoj.judgeserver.judge.handler;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.gzhuoj.judgeserver.judge.SandboxRun;
import com.gzhuoj.judgeserver.model.pojo.JudgeParam;
import com.gzhuoj.judgeserver.model.pojo.LanguageConfig;
import com.gzhuoj.judgeserver.model.pojo.TestCaseParam;
import com.gzhuoj.judgeserver.util.JudgeUtils;
import common.constant.JudgeType;
import common.enums.SubmissionStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * 普通评测处理器
 */
@Component
public class DefaultJudgeHandler extends AbstractJudgeTemplate{

    @Override
    public String mark(){
        return JudgeType.COMMON_JUDGE.getMark();
    }

    @Override
    protected JSONArray judge(TestCaseParam testCaseParam, JudgeParam judgeParam) {
        LanguageConfig runConfig = judgeParam.getRunConfig();
        // 评测机返回结果
        return SandboxRun.testCase(
                JudgeUtils.translateCommandline(runConfig.getRunCommand()),
                runConfig.getRunEnvs(),
                runConfig.getExeName(),
                testCaseParam.getTestCaseInputPath(),
                judgeParam.getTestTime(),
                judgeParam.getFileId(),
                judgeParam.getMaxStack(),
                judgeParam.getMaxMemory()
        );
    }

    @Override
    protected JSONObject converResult(JSONObject testCaseResult, TestCaseParam testCaseParam, JudgeParam judgeParam) {
        JSONObject result = new JSONObject();
        StringBuilder errorMsg = new StringBuilder();
        Integer status = testCaseResult.getInt("status");
        if (Objects.equals(SubmissionStatus.ACCEPTED.getCode(), status)) {
            // 判题无异常
            long actualTime = testCaseResult.getLong("time") / 1000000;
            long actualMemory = testCaseResult.getLong("memory") / 1024 / 1024;
            if (actualTime > judgeParam.getMaxTime()) {
                result.set("status", SubmissionStatus.TIME_LIMIT_EXCEED.getCode());
            } else if (actualMemory > judgeParam.getMaxMemory()) {
                result.set("status", SubmissionStatus.MEMORY_LIMIT_EXCEED.getCode());
            } else {
                String stdout = ((JSONObject) testCaseResult.get("files")).getStr("stdout", "");
                result.set("status", compareOutput(stdout, testCaseParam.getTestCaseOutput()));
            }
        } else if (Objects.equals(SubmissionStatus.TIME_LIMIT_EXCEED.getCode(), status)) {
            // 在沙箱中评测就T了
            result.set("status", SubmissionStatus.TIME_LIMIT_EXCEED.getCode());
        } else if (testCaseResult.getLong("exitStatus") != 0) {
            // Runtime ERROR
            result.set("status", SubmissionStatus.RUNTIME_ERROR.getCode());
            errorMsg.append(String.format("The program return exit status code: %s\n", testCaseResult.getLong("exitStatus")));
        }

        result.set("time", testCaseResult.getLong("time") / 1000000);
        result.set("memory", testCaseResult.getLong("memory") / 1024);

        if (!StringUtils.isEmpty(errorMsg.toString())) {
            String str = errorMsg.toString();
            result.set("errMsg", str.substring(0, Math.min(1024 * 1024, str.length())));
        }
        return result;
    }
}
