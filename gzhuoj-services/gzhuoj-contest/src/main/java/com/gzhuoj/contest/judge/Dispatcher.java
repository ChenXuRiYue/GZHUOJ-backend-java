package com.gzhuoj.contest.judge;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gzhuoj.contest.constant.enums.JudgeType;
import com.gzhuoj.contest.constant.enums.SubmissionStatus;
import com.gzhuoj.contest.model.entity.JudgeServerDO;
import com.gzhuoj.contest.model.entity.SubmitDO;
import com.gzhuoj.contest.model.pojo.ToJudgeDTO;
import com.gzhuoj.contest.remote.JudgeServerRemoteService;
import com.gzhuoj.contest.service.judge.JudgeServerService;
import com.gzhuoj.contest.service.SubmitService;
import com.gzhuoj.contest.util.ChooseInstanceUtils;
import common.convention.result.Result;
import common.exception.ClientException;
import common.toolkit.GenerateRandStrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static common.convention.errorcode.BaseErrorCode.JUDGE_TYPE_ERROR;

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
    private final JudgeServerRemoteService judgeServerRemoteService;

    private final static Integer MAX_TRY_NUM = 300;
    private final static Integer MAX_TRY_AGAIN_NUM = 10;

    // 定时调度任务线程池
    private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(20);

    // 定时调度任务Map集合
    private final static Map<String, Future> futureTaskMap = new ConcurrentHashMap<>(20);

    public void dispatch(JudgeType judgeType,  Object data) {
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
        Integer submitId = data.getSubmitDO().getSubmitId();
        // 原子类防止多线程混乱(似乎不需要
        AtomicInteger count = new AtomicInteger(0);
        // 任务ConcurrentHashMap中的key
        String taskKey = GenerateRandStrUtil.getRandStr(20) + submitId;
        Runnable getResultTask = () -> {
            if(count.get() > MAX_TRY_NUM){
                checkResul(null, submitId);
                releaseTaskThread(taskKey);
            }
            count.incrementAndGet();
            // 获取评测服务
            JudgeServerDO judgeServerDO = chooseInstanceUtils.chooseServer();
            if(judgeServerDO != null){ // 有评测机能提供服务
                Result<Void> judgeResult = null;
                try{
                    judgeResult = judgeServerRemoteService.judge(data);
                } catch (Exception e){
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
        boolean hasUpdate = judgeServerService.update(updateWrapper);
        if(!hasUpdate){
            tryAgainUpdateJudge(updateWrapper);
        }
    }

    /**
     * 释放JudgeServer失败, 进行重试
     * @param updateWrapper JudgeServer更新包装实体
     */
    private void tryAgainUpdateJudge(LambdaUpdateWrapper<JudgeServerDO> updateWrapper) {
        boolean ok = false;
        int count = 0;
        do{
            ok = judgeServerService.update(updateWrapper);
            if(ok){
               return;
            } else {
                count += 1;
                try {
                    // 睡眠200ms来缓解数据库重试压力
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }while(count < MAX_TRY_AGAIN_NUM);
    }

    /**
     * 取消任务, 释放调用线程资源
     */
    private void releaseTaskThread(String taskKey) {
        Future future = futureTaskMap.get(taskKey);
        if(future != null){
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
        LambdaUpdateWrapper<SubmitDO> updateWrapper = Wrappers.lambdaUpdate(SubmitDO.class);
        if(result == null){
            // 提交失败
            updateWrapper.set(SubmitDO::getSubmitId, submitId);
            updateWrapper.set(SubmitDO::getStatus, SubmissionStatus.STATUS_SUBMITTED_FAILED.getCode());
            submitService.update(updateWrapper);
        } else {
            if(!Objects.equals(Result.SUCCESS_CODE, result.getCode())){
                // 调用失败
                updateWrapper.set(SubmitDO::getStatus, SubmissionStatus.STATUS_SYSTEM_ERROR.getCode());
                submitService.update(updateWrapper);
            }
        }
    }
}
