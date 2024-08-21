package com.gzhuoj.judgeserver.judge;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.gzhuoj.judgeserver.judge.JudgeType.DefaultJudge;
import com.gzhuoj.judgeserver.model.entity.SubmitDO;
import com.gzhuoj.judgeserver.model.pojo.JudgeParam;
import com.gzhuoj.judgeserver.model.pojo.TestCaseParam;
import com.gzhuoj.judgeserver.remote.DTO.resp.ProblemRespDTO;
import com.gzhuoj.judgeserver.util.ThreadPoolUtils;
import common.enums.SubmissionLanguage;
import common.enums.SubmissionStatus;
import common.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

import static common.convention.errorcode.BaseErrorCode.*;

@Component
@RequiredArgsConstructor
public class JudgeRun {
    private final DefaultJudge defaultJudge;
    private final LanguageConfigLoader languageConfigLoader;

    public List<JSONObject> MulThreadJudge(List<String> testCaseInputList,
                                           List<String> testCaseOutputList,
                                           SubmitDO submitDO,
                                           ProblemRespDTO problemRespDTO,
                                           String userFileId) throws ExecutionException, InterruptedException {
        if(CollUtil.isEmpty(testCaseInputList) || CollUtil.isEmpty(testCaseOutputList)) {
            throw new ServiceException(JUDGE_TESTCASE_NOT_EXIST_ERROR);
        }
        if(testCaseInputList.size() != testCaseOutputList.size()){
            throw new ServiceException(JUDGE_TESTCASE_NUMBER_NOT_SAME_ERROR);
        }

        // 评测时长多给200ms
        // 在实际测评过程中，通常会有一些额外的开销，比如系统启动、I/O 操作、线程切换等
        Long testTime = (long) problemRespDTO.getTimeLimit() + 200;

        // 包装评测参数对象
        JudgeParam judgeParam = JudgeParam.builder()
                .fileId(userFileId)
                .problemId(problemRespDTO.getProblemNum())
                .maxMemory((long) problemRespDTO.getMemoryLimit())
                .maxTime((long) problemRespDTO.getTimeLimit())
                .testTime(testTime)
                .runConfig(languageConfigLoader.getLanguageConfigByName(SubmissionLanguage.getLangById(submitDO.getLanguage())))
                .maxStack(1024)
                .build();
        // 顺序测, 一个点错就返回
        return defaultJudge(judgeParam, testCaseInputList, testCaseOutputList);
    }

    private List<JSONObject> defaultJudge(JudgeParam judgeParam, List<String> testCaseInputList, List<String> testCaseOutputList) throws ExecutionException, InterruptedException {
        List<FutureTask<JSONObject>> futureTaskList = new ArrayList<>();
        for(int i = 0; i < testCaseInputList.size(); i++) {
            TestCaseParam testCaseParam = TestCaseParam.builder()
                    .testCaseNum(i + 1)
                    .testCaseInputPath(testCaseInputList.get(i))
                    .testCaseOutput(testCaseOutputList.get(i))
                    .build();
            FutureTask<JSONObject> testCase = new FutureTask<>(() -> {
                JSONObject result = defaultJudge.judge(testCaseParam, judgeParam);
                result.set("testCaseID", testCaseParam.getTestCaseNum());
                return result;
            });
            futureTaskList.add(testCase);
        }
        return submitAllToThreadPool(futureTaskList);
    }

    private List<JSONObject> submitAllToThreadPool(List<FutureTask<JSONObject>> futureTaskList) throws ExecutionException, InterruptedException {
        for (FutureTask<JSONObject> futureTask : futureTaskList) {
            ThreadPoolUtils.getInstance().getThreadPool().submit(futureTask);
        }
        List<JSONObject> result = new LinkedList<>();
        while (!futureTaskList.isEmpty()) {
            Iterator<FutureTask<JSONObject>> iterable = futureTaskList.iterator();
//            ExecutorService executorService = ThreadPoolUtils.getInstance().getThreadPool();
//            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executorService;
//            System.out.println(threadPoolExecutor.getActiveCount());
            // 循环监听获取线程评测结果
            while (iterable.hasNext()) {
                FutureTask<JSONObject> future = iterable.next();
                if (future.isDone() && !future.isCancelled()) {
                    // 获取线程返回结果
                    JSONObject tmp = future.get();
                    result.add(tmp);
                    // 任务完成移除任务
                    iterable.remove();
                } else {
                    Thread.sleep(10); // 避免CPU高速运转，这里休息10毫秒
                }
            }
        }
        return result;
    }

    /**
     * 提交任务到线程池中
     * 弃用
     */
    private JSONObject SubmitTaskToThreadPool(FutureTask<JSONObject> futureTask) throws ExecutionException, InterruptedException {
        ThreadPoolUtils.getInstance().getThreadPool().submit(futureTask);
        while (true) {
            if(futureTask.isDone() && !futureTask.isCancelled()) {
                return futureTask.get();
            } else {
                // 避免CPU高速运转休息10毫秒
                Thread.sleep(10);
            }
        }
    }
}
