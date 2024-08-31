-- 似乎是Navicat的原因使得不加这一句建的表会出现中文乱码
SET NAMES 'utf8';

-- 创建数据库，指定字符集和排序规则
CREATE DATABASE IF NOT EXISTS `gzhuoj-user`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

-- 使用刚创建的数据库
USE `gzhuoj-user`;

CREATE TABLE `user` (
                        `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
                        `username` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户名',
                        `password` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户密码',
                        `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户邮箱',
                        `organization` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '用户组织',
                        `role` tinyint(1) DEFAULT NULL COMMENT '用户角色',
                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                        `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                        `user_account` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户账号',
                        `delete_flag` tinyint(1) DEFAULT NULL COMMENT '标记账号是否已经被删除 0 -> 未删除， 1 -> 已删除',
                        PRIMARY KEY (`id`) USING BTREE,
                        UNIQUE KEY `user_unique` (`user_account`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;