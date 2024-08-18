package com.gzhuoj.judgeserver.util;

import java.util.concurrent.*;

/**
 * 线程池工具类
 */
public class ThreadPoolUtils {
    private static ExecutorService executorService;

    // 可用cpu数
    private static final int cpuNum = Runtime.getRuntime().availableProcessors();

    private ThreadPoolUtils() {
        executorService = new ThreadPoolExecutor(
                cpuNum, // 核心线程数
                cpuNum, // 最大线程数， 最大并发数
                3, // 无任务的非核心线程销毁时间
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(200 * cpuNum), //阻塞队列，可支持最大等待数
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy() //队列满了选择丢弃最早的任务，防止抛出异常
        );
    }

    // 静态内部类单例模式
    // java中的静态内部类的成员只有在第一次被显示访问时才会初始化，实现了延迟加载
    // 静态成员全局共享实现单例，不需要synchronized
    private static class PluginConfigHolder{
        private final static ThreadPoolUtils INSTANCE = new ThreadPoolUtils();
    }

    public static ThreadPoolUtils getInstance(){
        return PluginConfigHolder.INSTANCE;
    }

    public ExecutorService getThreadPool() {
        return executorService;
    }
}
