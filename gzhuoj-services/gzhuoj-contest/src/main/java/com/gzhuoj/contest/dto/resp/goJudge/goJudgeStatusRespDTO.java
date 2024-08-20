package com.gzhuoj.contest.dto.resp.goJudge;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class goJudgeStatusRespDTO {

    /**
     * 容器状态，状态列表参见 https://github.com/moby/moby/blob/v27.1.2/api/types/swarm/task.go
     */
    @JsonProperty("container_status")
    public String containerStatus;
    @JsonProperty("container_status_msg")
    public String containerStatusMsg;
        /**
     * 当前执行进度
     */
    public long progress;
    /**
     * 评测结果，null则暂时无结果
     */
    public Result result;
    /**
     * 总评测数，不包含pretest
     */
    public long total;
    /**
     * 如果失败，则只有message信息
     */
    public String message;
    static public class Result{


        public String id;


        public boolean success;


        public Compile compile;


        public Pretest pretest;



        public List<List<TestCase>> testcases;
        static public class Compile {


            public String stdout;


            public String stderr;


            public boolean success;
        }
        static public class Pretest {

            public long time;

            public long memory;

            public String status;

            public String message;

            public String stdout;

            public String stderr;
        }
        static public class TestCase {

            public long time;

            public long memory;

            public String status;

            public String message;
        }
    }
}
