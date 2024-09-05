package com.gzhuoj.judgeserver.judge.JudgeType;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.gzhuoj.judgeserver.judge.SandboxRun;
import com.gzhuoj.judgeserver.model.pojo.JudgeParam;
import com.gzhuoj.judgeserver.model.pojo.LanguageConfig;
import com.gzhuoj.judgeserver.model.pojo.TestCaseParam;
import com.gzhuoj.judgeserver.util.JudgeUtils;
import common.enums.SubmissionStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Component
public class DefaultJudge {
    // [^\\S\\n]  -- 所有空白字符但不包括换行符
    // + 匹配多次
    // (?=\\n) -- 正向肯定断言，只有出现换行符时才执行匹配
    private final static Pattern REMOVE_END_SPACE_PATTERN = Pattern.compile("[^\\S\\n]+(?=\\n)");

    public JSONObject judge(TestCaseParam testCaseParam, JudgeParam judgeParam) {
        LanguageConfig runConfig = judgeParam.getRunConfig();
        // 评测机返回结果
        JSONArray result = SandboxRun.testCase(
                JudgeUtils.translateCommandline(runConfig.getRunCommand()),
                runConfig.getRunEnvs(),
                runConfig.getExeName(),
                testCaseParam.getTestCaseInputPath(),
                judgeParam.getTestTime(),
                judgeParam.getFileId(),
                judgeParam.getMaxStack(),
                judgeParam.getMaxMemory()
        );
        // 对返回结果进行转化，处理std和error信息
        return converResult((JSONObject) result.get(0), testCaseParam, judgeParam);
    }

    private JSONObject converResult(JSONObject testCaseResult, TestCaseParam testCaseParam, JudgeParam judgeParam) {
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

    /**
     * 去除行末空格后对比输出
     */
    private Integer compareOutput(String stdout, String testCaseOutput) {
        // 获取UTF—8的std
        byte[] stdByte = removeEndSpace(stdout).getBytes(StandardCharsets.UTF_8);
        String md5 = DigestUtils.md5DigestAsHex(stdByte);
        if(Objects.equals(md5, testCaseOutput)){
            return SubmissionStatus.ACCEPTED.getCode();
        }
        return SubmissionStatus.WRONG_ANSWER.getCode();
    }

    public static String removeEndSpace(String input) {
        if (input == null) {
            return null;
        }
        return REMOVE_END_SPACE_PATTERN.matcher(input).replaceAll("");
    }
}
