-- 似乎是Navicat的原因使得不加这一句建的表会出现中文乱码
SET NAMES 'utf8';

-- 创建数据库，指定字符集和排序规则
CREATE DATABASE IF NOT EXISTS `gzhuoj-contest`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

-- 使用刚创建的数据库
USE `gzhuoj-contest`;

CREATE TABLE `contest`
(
    `contest_num`     int                                                           NOT NULL AUTO_INCREMENT COMMENT '比赛编号',
    `title`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '比赛标题',
    `start_time`     datetime                                                               DEFAULT NULL COMMENT '开始时间',
    `end_time`       datetime                                                               DEFAULT NULL COMMENT '结束时间',
    `contest_status` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci      NOT NULL DEFAULT 'N' COMMENT '比赛开放状态 0 -> 不开放， 1 -> 开放',
    `access`         tinyint                                                       NOT NULL DEFAULT '0' COMMENT '比赛访问权限 0 -> private 1 -> public 2 -> protect(需要密码)',
    `language_mask`  int unsigned                                                  NOT NULL DEFAULT '0' COMMENT '比赛可用语言的二进制编码rn0 -> c，rn1 -> c，rn2 -> java，rn3 -> python，rn4 -> go',
    `password`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT '' COMMENT '比赛密码',
    `attach`         varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '比赛描述附件',
    `topteam`        int                                                           NOT NULL DEFAULT '1' COMMENT '每个学校或组织显示排名最高的k支队r',
    `award_ratio`    varchar(255)                                                  NOT NULL DEFAULT '20015010' COMMENT '获奖比例',
    `frozen_minute`  int                                                           NOT NULL DEFAULT '-1' COMMENT '封榜分钟数',
    `frozen_after`   int                                                           NOT NULL DEFAULT '-1' COMMENT '结束后持续封榜分钟数',
    `create_time`    datetime                                                               DEFAULT NULL COMMENT '创建时间',
    `update_time`    datetime                                                               DEFAULT NULL COMMENT '更新时间',
    `delete_flag`    tinyint(1)                                                             DEFAULT NULL COMMENT '标记账号是否已经被删除 0 -> 未删除， 1 -> 已删除',
    PRIMARY KEY (`contest_num` DESC) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 99
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `contest_balloon`
(
    `contest_num`     int                                                           NOT NULL COMMENT '比赛编号',
    `problem_num`     int                                                           NOT NULL COMMENT '题目编号',
    `team_account`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '队伍账号',
    `room`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '比赛室号',
    `ac_time`        int                                                           NOT NULL COMMENT '距离比赛开始的时间',
    `pst`            tinyint                                                       NOT NULL COMMENT 'problem status，2 ac、3 fb',
    `bst`            tinyint                                                       NOT NULL COMMENT 'balloon status， 4分配，5已发',
    `balloon_sender` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '气球配分员',
    PRIMARY KEY (`contest_num`, `problem_num`, `team_account`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci COMMENT ='比赛的气球任务管理表';

CREATE TABLE `contest_description`
(
    `contest_num`  int NOT NULL COMMENT '比赛编号',
    `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '比赛描述',
    PRIMARY KEY (`contest_num`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `contest_problem`
(
    `problem_num`    int                                                           NOT NULL DEFAULT '0' COMMENT '题目集中的编号',
    `contest_num`    int                                                                    DEFAULT NULL COMMENT '比赛编号',
    `problem_color` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '题目对应气球颜色rgb十六进制编号',
    `problem_letter_index`    int                                                           NOT NULL DEFAULT '0' COMMENT '题目在比赛中的编号',
    KEY `Index_contest_num` (`contest_num`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `judge_server`
(
    `id`              int          NOT NULL AUTO_INCREMENT,
    `name`            varchar(255)          DEFAULT NULL COMMENT '判题服务名字',
    `ip`              varchar(255) NOT NULL COMMENT '判题机ip',
    `port`            int          NOT NULL COMMENT '判题机端口号',
    `url`             varchar(255)          DEFAULT NULL COMMENT 'ip:port',
    `cpu_core`        int                   DEFAULT '0' COMMENT '判题机所在服务器cpu核心数',
    `task_number`     int          NOT NULL DEFAULT '0' COMMENT '当前判题数',
    `max_task_number` int          NOT NULL COMMENT '判题并发最大数',
    `status`          int                   DEFAULT '0' COMMENT '0可用，1不可用',
    PRIMARY KEY (`id`),
    KEY `index_judge_url` (`url`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 223
  DEFAULT CHARSET = utf8mb3;

CREATE TABLE `solution`
(
    `solution_id` int                                                       NOT NULL AUTO_INCREMENT,
    `problem_num`  int                                                       NOT NULL DEFAULT '0',
    `user_id`     char(48) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `nick`        char(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
    `time`        int                                                       NOT NULL DEFAULT '0',
    `memory`      int                                                       NOT NULL DEFAULT '0',
    `in_date`     datetime                                                  NOT NULL DEFAULT '2016-05-13 19:24:00',
    `result`      smallint                                                  NOT NULL DEFAULT '0',
    `language`    int unsigned                                              NOT NULL DEFAULT '0',
    `ip`          char(46) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `contest_num`  int                                                                DEFAULT '0',
    `valid`       tinyint                                                   NOT NULL DEFAULT '1',
    `num`         tinyint                                                   NOT NULL DEFAULT '-1',
    `code_length` int                                                       NOT NULL DEFAULT '0',
    `judgetime`   timestamp                                                 NULL     DEFAULT CURRENT_TIMESTAMP,
    `pass_rate`   decimal(3, 2) unsigned                                    NOT NULL DEFAULT '0.00',
    `lint_error`  int unsigned                                              NOT NULL DEFAULT '0',
    `judger`      char(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'LOCAL',
    PRIMARY KEY (`solution_id`),
    KEY `uid` (`user_id`),
    KEY `pid` (`problem_num`),
    KEY `res` (`result`),
    KEY `cid` (`contest_num`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 13
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `submit`
(
    `submit_id`    int                                                           NOT NULL AUTO_INCREMENT,
    `problem_num`   int                                                           NOT NULL DEFAULT '0',
    `contest_num`   int                                                                    DEFAULT '0',
    `memory`       int                                                           NOT NULL DEFAULT '0',
    `submit_time`  datetime                                                      NOT NULL DEFAULT '2016-05-13 19:24:00',
    `status`       tinyint                                                       NOT NULL DEFAULT '0',
    `language`     int unsigned                                                  NOT NULL DEFAULT '0',
    `team_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
    `exec_time`    int                                                           NOT NULL DEFAULT '0',
    `code_size`    int                                                           NOT NULL DEFAULT '0',
    `judger`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL DEFAULT 'LOCAL',
    PRIMARY KEY (`submit_id`) USING BTREE,
    KEY `uid` (`team_account`) USING BTREE,
    KEY `pid` (`problem_num`) USING BTREE,
    KEY `res` (`status`) USING BTREE,
    KEY `cid` (`contest_num`) USING BTREE,
    KEY `sfc` (`submit_time`, `status`, `language`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1577
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `submit_code`
(
    `submit_id` int NOT NULL COMMENT '提交编号',
    `code`      text COMMENT '源代码',
    PRIMARY KEY (`submit_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;

CREATE TABLE `team`
(
    `team_account`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '队伍编号',
    `contest_num`     int                                                           NOT NULL COMMENT '比赛编号',
    `team_status`    tinyint(1)                                                    NOT NULL COMMENT '队伍参赛情况 0 -> 参赛 1 -> 不参赛',
    `password`       varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '密码',
    `team_name`      varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '队伍名',
    `team_member`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '队伍成员组成',
    `team_type`      tinyint                                                       NOT NULL DEFAULT '0' COMMENT '“常规”（0）、“女队”（1）、“打星”（2） ',
    `coach`          varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '教练',
    `school`         varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '学校',
    `room`           varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci          DEFAULT NULL COMMENT '比赛场地',
    `team_privilege` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci           DEFAULT NULL COMMENT '账号权限',
    PRIMARY KEY (`team_account`, `contest_num`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;