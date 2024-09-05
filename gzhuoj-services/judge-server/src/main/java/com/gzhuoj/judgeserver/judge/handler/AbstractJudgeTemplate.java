package com.gzhuoj.judgeserver.judge.handler;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.gzhuoj.judgeserver.model.pojo.JudgeParam;
import com.gzhuoj.judgeserver.model.pojo.TestCaseParam;
import common.designpattern.strategy.AbstractExecuteStrategy;
import common.enums.SubmissionStatus;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.gzhuoj.judgeserver.constant.PatternConstant.REMOVE_END_SPACE_PATTERN;


public abstract class AbstractJudgeTemplate implements AbstractExecuteStrategy<TestCaseParam, JudgeParam, JSONObject> {
    /**
     * 进行判题
     *
     * @param testCaseParam 测试数据参数
     * @param judgeParam    Judge参数
     * @return 结果实体
     */
    protected abstract JSONArray judge(TestCaseParam testCaseParam, JudgeParam judgeParam);

    /**
     * 评测结果转换
     *
     * @param testCaseResult 评测结果
     * @return 转换结果响应
     */
    protected abstract JSONObject converResult(JSONObject testCaseResult, TestCaseParam testCaseParam, JudgeParam judgeParam);

    /**
     * 执行评测并返回结果
     */
    public JSONObject executeResp(TestCaseParam testCaseParam, JudgeParam judgeParam) {
        JSONArray result = judge(testCaseParam, judgeParam);
        // 对返回结果进行转化，处理std和error信息
        return converResult((JSONObject) result.get(0), testCaseParam, judgeParam);
    }

    /**
     * 去除行末空格后对比输出
     */
    public Integer compareOutput(String stdout, String testCaseOutput) {
        // 获取UTF—8的std
        byte[] stdByte = removeEndSpace(stdout).getBytes(StandardCharsets.UTF_8);
        String md5 = DigestUtils.md5DigestAsHex(stdByte);
        if (Objects.equals(md5, testCaseOutput)) {
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


