package com.gzhuoj.contest.dto.req.goJudge;

public class goJudgeSubmitReqDTO {

    /**
     * 评测ID，随机id，[0-9a-zA-Z]+
     */
    public String id;
    /**
     * 评测语言
     */
    public String lang;
    /**
     * 内存限制，使用 xxxMiB xxxMB 等格式
     */
    public String memoryLimit;
    /**
     * 题目ID
     */
    public String problem;
    /**
     * 源码内容
     */
    public String source;
    /**
     * 题目路径，测试期间请使用local://host/{评测机中绝对路径}
     */
    public String storagePath;
    /**
     * 时间限制，1000000000 为 1s
     */
    public long timeLimit;
    /**
     * 评测机分配限制
     */
    public String[] execConstraints;
}
