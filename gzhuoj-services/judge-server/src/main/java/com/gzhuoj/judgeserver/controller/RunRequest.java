package com.gzhuoj.judgeserver.controller;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class RunRequest {
    private List<Cmd> cmd;

    // Getters and setters
    @Data
    public static class Cmd {
        private List<String> args;
        private List<String> env;
        private List<File> files;
        private long cpuLimit;
        private long memoryLimit;
        private int procLimit;
        private Map<String, FileContent> copyIn;
        private List<String> copyOutCached;

        // Getters and setters
        @Data
        public static class File {
            private String name;
            private int max;
            private String content;

            // Getters and setters
        }
        @Data
        public static class FileContent {
            private String content;

            // Getters and setters
        }
    }
}
