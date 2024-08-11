package com.gzhuoj.contest.constant.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JudgeType {
    /**
     * 普通评测
     */
    COMMON_JUDGE("/judge"),

    /**
     * SPJ
     */
    COMPILE_SPJ("/spj"),

    /**
     * 交互
     */
    COMPILE_INTERACTIVE("/interactive");

    private final String path;
}
