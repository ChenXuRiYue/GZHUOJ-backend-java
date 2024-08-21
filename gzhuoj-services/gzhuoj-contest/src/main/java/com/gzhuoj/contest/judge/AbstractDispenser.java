package com.gzhuoj.contest.judge;

import common.enums.SubmitWaitingQueue;

/**
 * 评测派送者抽象类
 * 鉴于以后不单有contest评测而引入
 */
public abstract class AbstractDispenser {
    // 通过不同redis队列key中获取到任务
    public void priorityTask(SubmitWaitingQueue... queues) {
        for(SubmitWaitingQueue waitingQueue : queues) {
            String task = getTaskByRedis(waitingQueue.getQueue());
            if(task != null) {
                dispenserTask(waitingQueue.getPath(), task);
                break;
            }
        }
    }

    public abstract void dispenserTask(String path, String taskStr);

    public abstract String getTaskByRedis(String queue);
}
