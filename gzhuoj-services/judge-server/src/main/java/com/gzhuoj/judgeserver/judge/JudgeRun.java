package com.gzhuoj.judgeserver.judge;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.gzhuacm.sdk.problem.model.dto.ProblemRespDTO;
import com.gzhuoj.judgeserver.model.entity.SubmitDO;
import com.gzhuoj.judgeserver.model.pojo.JudgeParam;
import com.gzhuoj.judgeserver.model.pojo.TestCaseParam;
import com.gzhuoj.judgeserver.util.ThreadPoolUtils;
import common.constant.JudgeType;
import common.designpattern.strategy.AbstractStrategyChoose;
import common.enums.Language;
import common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode.JUDGE_TESTCASE_NOT_EXIST_ERROR;
import static org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode.JUDGE_TESTCASE_NUMBER_NOT_SAME_ERROR;

@Component
@RequiredArgsConstructor
public class JudgeRun {
    private final LanguageConfigLoader languageConfigLoader;
    private final AbstractStrategyChoose abstractStrategyChoose;

    public List<JSONObject> MulThreadJudge(List<String> testCaseInputList,
                                           List<String> testCaseOutputList,
                                           SubmitDO submitDO,
                                           ProblemRespDTO problemRespDTO,
                                           String userFileId) throws ExecutionException, InterruptedException {
        if (CollUtil.isEmpty(testCaseInputList) || CollUtil.isEmpty(testCaseOutputList)) {
            throw new ServiceException(JUDGE_TESTCASE_NOT_EXIST_ERROR);
        }
        if (testCaseInputList.size() != testCaseOutputList.size()) {
            throw new ServiceException(JUDGE_TESTCASE_NUMBER_NOT_SAME_ERROR);
        }

        // 评测时长多给200ms
        // 在实际测评过程中，通常会有一些额外的开销，比如系统启动、I/O 操作、线程切换等
        Long testTime = (long) problemRespDTO.getTimeLimit() + 200;

        // 包装评测参数对象
        JudgeParam judgeParam = JudgeParam.builder()
                .fileId(userFileId)
                .problemNum(problemRespDTO.getProblemNum())
                .maxMemory((long) problemRespDTO.getMemoryLimit())
                .maxTime((long) problemRespDTO.getTimeLimit())
                .testTime(testTime)
                .runConfig(languageConfigLoader.getLanguageConfigByName(Language.getLangById(submitDO.getLanguage())))
                .maxStack(1024)
                .build();
        return createJudgeFutureTask(judgeParam, testCaseInputList, testCaseOutputList, JudgeType.COMMON_JUDGE.getMark());
    }

    private List<JSONObject> createJudgeFutureTask(JudgeParam judgeParam, List<String> testCaseInputList, List<String> testCaseOutputList, String mark) {
        ExecutorService executorService = ThreadPoolUtils.getInstance().getThreadPool();

        List<CompletableFuture<JSONObject>> futureList = new ArrayList<>();

        for (int i = 0; i < testCaseInputList.size(); i++) {
            TestCaseParam testCaseParam = TestCaseParam.builder()
                    .testCaseNum(i + 1)
                    .testCaseInputPath(testCaseInputList.get(i))
                    .testCaseOutput(testCaseOutputList.get(i))
                    .build();

            CompletableFuture<JSONObject> future = CompletableFuture.supplyAsync(() -> {
                // 调用抽象策略执行
                JSONObject result = abstractStrategyChoose.chooseAndExecuteResp(mark, testCaseParam, judgeParam);
                result.set("testCaseID", testCaseParam.getTestCaseNum());
                return result;
            }, executorService).exceptionally(ex -> {
                // 捕获异常并记录
                JSONObject errorResult = new JSONObject();
                errorResult.set("error", ex.getMessage());
                errorResult.set("testCaseID", testCaseParam.getTestCaseNum());
                return errorResult;
            });

            futureList.add(future);
        }

        return collectResults(futureList);
    }

    private List<JSONObject> collectResults(List<CompletableFuture<JSONObject>> futureList) {
        return futureList.stream()
                .map(CompletableFuture::join) // 使用 join 获取结果，不抛出受检异常
                .collect(Collectors.toList());
    }


}
