package com.gzhuoj.judgeserver.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class LanguageConfig {

    /**
     * 语言名称
     */
    private String language;

    /**
     * 源代码文件名称
     */
    private String srcName;

    /**
     * 源代码的可执行文件名称
     */
    private String exeName;

    /**
     * 编译最大cpu运行时间 s
     */
    private Long maxCpuTime;

    /**
     * 编译最大真实运行时间 s
     */
    private Long maxRealTime;

    /**
     * 编译最大运行空间 b
     */
    private Long maxMemory;

    /**
     * 编译命令
     */
    private String compileCommand;

    /**
     * 运行命令
     */
    private String runCommand;

    /**
     * 编译运行环境
     */
    private List<String> compileEnvs;

    /**
     * 执行程序环境
     */
    private List<String> runEnvs;

}