package com.gzhuoj.contest.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubmissionStatus {
    ACCEPTED(0),

    PRESENTATION_ERROR(1),

    WRONG_ANSWER(2),

    TIME_LIMIT_EXCEED(3),

    MEMORY_LIMIT_EXCEED(4),

    OUTPUT_LIMIT_EXCEED(5),

    RUNTIME_ERROR(6),

    COMPILE_ERROR(7),

    PENDING(8);

    /**
     * 提交状态编号
     */
    private final Integer code;

}
