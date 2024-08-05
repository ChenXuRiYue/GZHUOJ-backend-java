CREATE TABLE `user`
(
    `id`           int NOT NULL COMMENT 'ID',
    `username`     varchar(128) DEFAULT NULL COMMENT '用户名',
    `password`     varchar(128) DEFAULT NULL COMMENT '用户密码',
    `email`        varchar(64)  DEFAULT NULL COMMENT '用户邮箱',
    `organization` varchar(128) DEFAULT NULL COMMENT '用户组织',
    `role`         tinyint(1) DEFAULT NULL COMMENT '用户角色',
    `create_time`  datetime     DEFAULT NULL COMMENT '创建时间',
    `update_time`  datetime     DEFAULT NULL COMMENT '更新时间',
    `user_account` varchar(255) DEFAULT NULL COMMENT '用户账号',
    `delete_flag`  tinyint(1) DEFAULT NULL COMMENT '标记账号是否已经被删除 0 -> 未删除， 1 -> 已删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;