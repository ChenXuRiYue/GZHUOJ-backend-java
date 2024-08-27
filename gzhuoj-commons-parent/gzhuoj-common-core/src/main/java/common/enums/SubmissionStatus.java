package common.enums;

import lombok.Getter;

@Getter
public enum SubmissionStatus {
    ACCEPTED(0, "Accepted"),

    PRESENTATION_ERROR(1, "Presentation Error"),

    WRONG_ANSWER(2, "Wrong Answer"),

    TIME_LIMIT_EXCEED(3, "Time Limit Exceed"),

    MEMORY_LIMIT_EXCEED(4, "Memory Limit Exceed"),

    OUTPUT_LIMIT_EXCEED(5, "Output Limit Exceed"),

    RUNTIME_ERROR(6, "Runtime Error"),

    COMPILE_ERROR(7, "Compile Error"),

    PENDING(8, "Pending"),

    STATUS_SUBMITTED_FAILED(9, "Status Submitted Failed"),

    STATUS_SYSTEM_ERROR(10, "Status System Error"),

    STATUS_CANCELLED(11, "Status Cancelled"),

    STATUS_COMPILING(12, "Status Compiling"),

    NONZERO_EXIT_STATUS(13, "Nonzero Exit Status"),

    INTERNAL_ERROR(14, "Internal Error"),

    FILE_ERROR(15, "File Error"),

    SIGNALLED(16, "Signalled");


    /**
     * 提交状态编号
     */
    private final Integer code;

    /**
     * 提交状态表述
     */
    private final String status;

    SubmissionStatus(Integer code, String status) {
        this.code = code;
        this.status = status;
    }
}