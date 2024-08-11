package com.gzhuoj.contest.judge;

/**
 * 评测派送者抽象类
 * 鉴于以后不单有contest评测而引入
 */
public abstract class AbstractDispenser {
    // 通过不同redis队列key中获取到任务
    public void priorityTask(String... queues) {
        for(String queue : queues) {
            String task = getTaskByRedis(queue);
            if(task != null) {
                dispenserTask(task);
            }
        }
    }

    public abstract void dispenserTask(String taskStr);

    public abstract String getTaskByRedis(String queue);
}
