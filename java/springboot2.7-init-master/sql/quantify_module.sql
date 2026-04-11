-- 量化管理模块数据表
-- 创建日期：2026-04-11

-- 量化指标表
CREATE TABLE IF NOT EXISTS `quantify_indicator` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '指标ID',
    `name` varchar(256) NOT NULL COMMENT '指标名称',
    `rule` varchar(512) NOT NULL COMMENT '统计规则',
    `dimension` varchar(64) NOT NULL COMMENT '统计维度：organization-组织/personal-个人/both-两者',
    `org_level` varchar(512) NOT NULL COMMENT '适用组织层级，JSON格式存储',
    `status` varchar(32) DEFAULT 'enable' NOT NULL COMMENT '状态：enable-启用/disable-停用',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete` tinyint DEFAULT 0 NOT NULL COMMENT '是否删除',
    INDEX `idx_dimension` (`dimension`),
    INDEX `idx_status` (`status`)
) COMMENT='量化指标表' COLLATE=utf8mb4_unicode_ci;

-- 量化数据记录表
CREATE TABLE IF NOT EXISTS `quantify_data` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '数据ID',
    `indicator_id` bigint NOT NULL COMMENT '指标ID',
    `target_id` bigint NOT NULL COMMENT '统计对象ID（组织ID或用户ID）',
    `target_type` varchar(32) NOT NULL COMMENT '统计对象类型：organization-组织/user-用户',
    `period` varchar(32) NOT NULL COMMENT '统计周期（如：2025-01, 2025-Q1）',
    `value` decimal(10,2) NOT NULL COMMENT '统计值',
    `activity_rate` decimal(5,2) DEFAULT 0 COMMENT '活动参与率',
    `sign_rate` decimal(5,2) DEFAULT 0 COMMENT '签到率',
    `material_rate` decimal(5,2) DEFAULT 0 COMMENT '材料完成率',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_indicator_id` (`indicator_id`),
    INDEX `idx_target` (`target_id`, `target_type`),
    INDEX `idx_period` (`period`)
) COMMENT='量化数据记录表' COLLATE=utf8mb4_unicode_ci;
