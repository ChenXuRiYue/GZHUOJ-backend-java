package com.gzhuoj.judgeserver.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class JudgeParam {

    /**
     * 题目编号
     */
    private Integer problemNum;

    /**
     * 编译文件唯一标识符
     */
    private String fileId;

    /**
     * 判题沙盒评测程序的最大实际时间，一般为题目最大限制时间+200ms
     */
    private Long testTime;

    /**
     * 当前题目评测的最大时间限制 ms
     */
    private Long maxTime;

    /**
     * 当前题目评测的最大空间限制 mb
     */
    private Long maxMemory;

    /**
     * 当前题目评测的最大栈空间限制 mb
     */
    private Integer maxStack;

    /**
     * 普通评测的命令配置
     */
    private LanguageConfig runConfig;

}
