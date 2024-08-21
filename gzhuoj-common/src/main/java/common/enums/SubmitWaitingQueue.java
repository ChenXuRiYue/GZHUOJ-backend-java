package common.enums;

import lombok.Getter;

@Getter
public enum SubmitWaitingQueue {
    // 比赛评测队列
    CONTEST_JUDGE_QUEUE("gzhuoj-contest-judge:queue", "/judge"),
    // 非比赛评测队列
    COMMON_JUDGE_QUEUE("gzhuoj-common-judge:queue", "/judge"),
    // 在线自测
    ONLINE_TEST_JUDGE_QUEUE("gzhuoj-online-test-judge:queue", "/spj");

    private final String queue;

    private final String path;

    SubmitWaitingQueue(String queue, String path) {
        this.queue = queue;
        this.path = path;
    }
}
