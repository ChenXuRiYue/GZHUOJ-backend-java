package com.gzhuoj.contest.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubmissionLanguage {
    C(0),
    CPlusPlus(1),
    Java(2),
    Python(3),
    Go(4);

    /**
     * 提交语言编号
     */
    private final Integer code;

}
