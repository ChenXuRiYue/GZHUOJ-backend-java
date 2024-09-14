package com.gzhuoj.contest.judge;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gzhuoj.contest.model.entity.JudgeServerDO;
import com.gzhuoj.contest.model.entity.SubmitDO;
import com.gzhuacm.sdk.contest.model.dto.ToJudgeDTO;
import com.gzhuoj.contest.service.judge.JudgeServerService;
import com.gzhuoj.contest.service.judge.SubmitService;
import com.gzhuoj.contest.util.ChooseInstanceUtils;
import common.constant.JudgeType;
import org.gzhuoj.common.sdk.convention.result.Result;
import common.enums.SubmissionStatus;
import common.exception.ClientException;
import common.utils.GenerateRandStrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.gzhuoj.contest.constant.PathConstant.JUDGE_SERVER_JUDGE_PATH;
import static org.gzhuoj.common.sdk.convention.errorcode.BaseErrorCode.JUDGE_TYPE_ERROR;

/**
 * 任务调度
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class Dispatcher {
    private final ChooseInstanceUtils chooseInstanceUtils;
    private final SubmitService submitService;
    private final JudgeServerService judgeServerService;
    private final RestTemplate restTemplate;

    private final static Integer MAX_TRY_NUM = 300;
    private final static Integer MAX_TRY_AGAIN_NUM = 10;

    // 定时调度任务线程池
    private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(20);

    // 定时调度任务Map集合
    private final static Map<String, Future> futureTaskMap = new ConcurrentHashMap<>(20);

    public void dispatch(JudgeType judgeType, Object data) {
        switch (judgeType) {
            case COMMON_JUDGE:
                defaultJudge((ToJudgeDTO) data, judgeType.getPath());
                break;
            case COMPILE_SPJ:
            case COMPILE_INTERACTIVE:

            default:
                throw new ClientException(JUDGE_TYPE_ERROR);
        }
    }

    private void defaultJudge(ToJudgeDTO data, String path) {
        Integer submitId = data.getSubmitDTO().getSubmitId();
        // 原子类防止多线程混乱(似乎不需要
        AtomicInteger count = new AtomicInteger(0);
        // 任务ConcurrentHashMap中的key
        String taskKey = GenerateRandStrUtil.getRandStr(20) + submitId;
        Runnable getResultTask = () -> {
            if (count.get() > MAX_TRY_NUM) {
                checkResul(null, submitId);
                releaseTaskThread(taskKey);
            }
            count.incrementAndGet();
            // 获取评测服务
            JudgeServerDO judgeServerDO = chooseInstanceUtils.chooseServer();
            if (judgeServerDO != null) { // 有评测机能提供服务
                Result judgeResult = null;
                try {
                    data.setJudgeServerIp(judgeServerDO.getIp());
                    data.setJudgeServerPort(judgeServerDO.getPort());
                    judgeResult = restTemplate.postForObject("http://" + judgeServerDO.getUrl() + JUDGE_SERVER_JUDGE_PATH, data, Result.class);
                } catch (Exception e) {
                    log.error("[Self Judge] Request the judge server [" + judgeServerDO.getUrl() + "] error -------------->", e);
                } finally {
                    checkResul(judgeResult, submitId);
                    releaseJudgeServer(judgeServerDO.getId());
                    releaseTaskThread(taskKey);
                }
            }
        };

        // 每两秒执行一次定时任务
        ScheduledFuture<?> scheduledFuture = scheduler.scheduleWithFixedDelay(getResultTask, 0, 2, TimeUnit.SECONDS);
        futureTaskMap.put(taskKey, scheduledFuture);
    }

    /**
     * 释放评测机资源
     */
    private void releaseJudgeServer(Integer JudgeServerId) {
        LambdaUpdateWrapper<JudgeServerDO> updateWrapper = Wrappers.lambdaUpdate(JudgeServerDO.class)
                .eq(JudgeServerDO::getId, JudgeServerId)
                .setSql("task_number = task_number - 1");
        tryAgainUpdateJudge(updateWrapper);
    }

    /**
     * 释放JudgeServer失败, 进行重试
     *
     * @param updateWrapper JudgeServer更新包装实体
     */
    private void tryAgainUpdateJudge(LambdaUpdateWrapper<JudgeServerDO> updateWrapper) {
        CompletableFuture.runAsync(() -> {
            int count = 0;
            boolean ok;
            do {
                ok = judgeServerService.update(updateWrapper);
                if (ok) return;
                count++;
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    log.error("Thread interrupted during retry", e);
                }
            } while (count < MAX_TRY_AGAIN_NUM);
        });
    }

    /**
     * 取消任务, 释放调用线程资源
     */
    private void releaseTaskThread(String taskKey) {
        Future future = futureTaskMap.get(taskKey);
        if (future != null) {
            boolean hasCancel = future.cancel(true);
            if (hasCancel) {
                futureTaskMap.remove(taskKey);
            }
        }
    }

    /**
     * 检测返回结果，用于判断是否调用错误或提交失败
     */
    private void checkResul(Result<Void> result, Integer submitId) {
        LambdaUpdateWrapper<SubmitDO> updateWrapper = Wrappers.lambdaUpdate(SubmitDO.class)
                .eq(SubmitDO::getSubmitId, submitId);
        if (result == null) {
            // 提交失败
            updateWrapper.set(SubmitDO::getStatus, SubmissionStatus.STATUS_SUBMITTED_FAILED.getCode());
            submitService.update(updateWrapper);
        } else {
            if (!Objects.equals(Result.SUCCESS_CODE, result.getCode())) {
                // 调用失败
                updateWrapper.set(SubmitDO::getStatus, SubmissionStatus.STATUS_SYSTEM_ERROR.getCode());
                submitService.update(updateWrapper);
            }
        }
    }
}
