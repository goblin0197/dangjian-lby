-- 公告管理模块扩展数据库表设计
-- 创建日期：2026-04-11

-- 公告附件表
CREATE TABLE IF NOT EXISTS `announcement_attachment` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '附件 ID',
    `announcement_id` bigint NOT NULL COMMENT '公告 ID',
    `name` varchar(255) NOT NULL COMMENT '附件名称',
    `url` varchar(500) NOT NULL COMMENT '附件路径',
    `size` varchar(50) DEFAULT NULL COMMENT '附件大小',
    `type` varchar(50) DEFAULT NULL COMMENT '附件类型',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_announcement_id` (`announcement_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告附件表';

-- 公告阅读记录表
CREATE TABLE IF NOT EXISTS `announcement_read_record` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录 ID',
    `announcement_id` bigint NOT NULL COMMENT '公告 ID',
    `user_id` bigint NOT NULL COMMENT '用户 ID',
    `read_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '阅读时间',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_announcement_user` (`announcement_id`,`user_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_read_time` (`read_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告阅读记录表';

-- 公告分享表
CREATE TABLE IF NOT EXISTS `announcement_share` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分享 ID',
    `announcement_id` bigint NOT NULL COMMENT '公告 ID',
    `share_url` varchar(500) NOT NULL COMMENT '分享链接',
    `share_code` varchar(64) NOT NULL COMMENT '分享码',
    `share_user_id` bigint NOT NULL COMMENT '分享用户 ID',
    `share_user_name` varchar(100) NOT NULL COMMENT '分享用户姓名',
    `share_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '分享时间',
    `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
    `view_count` int NOT NULL DEFAULT '0' COMMENT '查看次数',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_announcement_id` (`announcement_id`),
    KEY `idx_share_user_id` (`share_user_id`),
    KEY `idx_share_code` (`share_code`),
    KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告分享表';
