-- 似乎是Navicat的原因使得不加这一句建的表会出现中文乱码
SET NAMES 'utf8';

-- 创建数据库，指定字符集和排序规则
CREATE
DATABASE IF NOT EXISTS `gzhuoj-problem`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

-- 使用刚创建的数据库
USE
`gzhuoj-problem`;

CREATE TABLE `problem`
(
    `id`             bigint       NOT NULL AUTO_INCREMENT,
    `problem_name`   varchar(128) NOT NULL,
    `time_limit`     int          NOT NULL COMMENT '单位(s)',
    `memory_limit`   int          NOT NULL COMMENT '空间存储限制单位 (mb)',
    `accepted`       int          NOT NULL DEFAULT '0',
    `solved`         int          NOT NULL DEFAULT '0',
    `submit`         int          NOT NULL DEFAULT '0',
    `author`         varchar(128)          DEFAULT NULL,
    `problem_num`    int          NOT NULL,
    `problem_status` tinyint      NOT NULL,
    `spj`            tinyint      NOT NULL DEFAULT '0',
    `attach`         varchar(128)          DEFAULT NULL,
    `create_time`    date         NOT NULL,
    `update_time`    date         NOT NULL,
    `delete_flag`    tinyint      NOT NULL DEFAULT '0',
    `problem_type`   tinyint      NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `problem_description`
(
    `id`                      bigint NOT NULL AUTO_INCREMENT,
    `problem_num`             int    NOT NULL,
    `description`             text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
    `description_html`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
    `input_description`       text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
    `input_description_html`  text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
    `output_description`      text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
    `output_description_html` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
    `explanation`             text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
    `explanation_html`        text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `test_example`
(
    `test_example_num` int  NOT NULL,
    `input`            text NOT NULL,
    `output`           text,
    `problem_num`      int  NOT NULL,
    `create_time`      date NOT NULL,
    `update_time`      date NOT NULL,
    `delete_flag`      tinyint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `gzhuoj-problem`.problem_judge_resources
(
    problem_num   INTEGER      NOT NULL COMMENT 'problem 在题库中的序号',
    id           INTEGER auto_increment NOT NULL COMMENT '表内主键',
    file_name    varchar(128) NOT NULL,
    file_content LONGTEXT NULL COMMENT '文件内容',
    create_time  date         NOT NULL,
    update_time  date         NOT NULL,
    delete_flag  tinyint NULL,
    CONSTRAINT problem_judge_resources_pk PRIMARY KEY (id)
) ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci
COMMENT='保存了评测的测试资源根据评测类型，数据格式包括了  in, out, spj 或其他扩展的格式';
