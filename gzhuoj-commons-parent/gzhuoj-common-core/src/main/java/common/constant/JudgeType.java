package common.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JudgeType {
    /**
     * 普通评测
     */
    COMMON_JUDGE("/judge", "judge"),

    /**
     * SPJ
     */
    COMPILE_SPJ("/spj", "spj"),

    /**
     * 交互
     */
    COMPILE_INTERACTIVE("/interactive", "interactive");

    private final String path;

    private final String mark;
}
