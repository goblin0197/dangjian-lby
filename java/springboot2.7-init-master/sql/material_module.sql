-- 材料管理模块数据库表设计
-- 创建日期：2026-04-11

-- 材料模板表
CREATE TABLE IF NOT EXISTS `material_template` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '模板 ID',
    `name` varchar(255) NOT NULL COMMENT '模板名称',
    `stage` varchar(50) NOT NULL COMMENT '所属发展阶段',
    `type` varchar(20) NOT NULL COMMENT '材料类型',
    `status` varchar(20) NOT NULL DEFAULT 'enable' COMMENT '模板状态：enable-启用/disable-停用',
    `file_url` varchar(500) NOT NULL COMMENT '模板文件路径',
    `file_size` bigint DEFAULT NULL COMMENT '文件大小（字节）',
    `upload_user_id` bigint NOT NULL COMMENT '上传用户 ID',
    `upload_user_name` varchar(100) NOT NULL COMMENT '上传用户姓名',
    `remark` varchar(500) DEFAULT NULL COMMENT '模板说明',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_stage` (`stage`),
    KEY `idx_type` (`type`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='材料模板表';

-- 材料提交表
CREATE TABLE IF NOT EXISTS `material_submission` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '提交 ID',
    `user_id` bigint NOT NULL COMMENT '用户 ID',
    `user_name` varchar(100) NOT NULL COMMENT '用户姓名',
    `org_level` varchar(50) NOT NULL COMMENT '组织层级',
    `stage` varchar(50) NOT NULL COMMENT '发展阶段',
    `material_name` varchar(255) NOT NULL COMMENT '材料名称',
    `submit_status` varchar(20) NOT NULL DEFAULT 'unsubmitted' COMMENT '提交状态：unsubmitted-未提交/submitted-已提交/approved-审核通过/final_approved-终审通过/rejected-退回/archived-已归档',
    `audit_status` varchar(20) NOT NULL DEFAULT 'pending' COMMENT '审核状态：pending-待审核/auditing-审核中/approved-通过/rejected-退回',
    `file_url` varchar(500) DEFAULT NULL COMMENT '材料文件路径',
    `file_size` bigint DEFAULT NULL COMMENT '文件大小（字节）',
    `upload_time` datetime DEFAULT NULL COMMENT '上传时间',
    `auditor` varchar(100) DEFAULT NULL COMMENT '审核人',
    `auditor_id` bigint DEFAULT NULL COMMENT '审核人 ID',
    `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
    `audit_opinion` varchar(500) DEFAULT NULL COMMENT '审核意见',
    `final_auditor` varchar(100) DEFAULT NULL COMMENT '终审人',
    `final_auditor_id` bigint DEFAULT NULL COMMENT '终审人 ID',
    `final_audit_time` datetime DEFAULT NULL COMMENT '终审时间',
    `final_audit_opinion` varchar(500) DEFAULT NULL COMMENT '终审意见',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_stage` (`stage`),
    KEY `idx_submit_status` (`submit_status`),
    KEY `idx_audit_status` (`audit_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='材料提交表';

-- 材料归档表
CREATE TABLE IF NOT EXISTS `material_archive` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '归档 ID',
    `submission_id` bigint NOT NULL COMMENT '提交 ID',
    `user_id` bigint NOT NULL COMMENT '用户 ID',
    `user_name` varchar(100) NOT NULL COMMENT '用户姓名',
    `org_level` varchar(50) NOT NULL COMMENT '组织层级',
    `stage` varchar(50) NOT NULL COMMENT '发展阶段',
    `material_name` varchar(255) NOT NULL COMMENT '材料名称',
    `file_url` varchar(500) NOT NULL COMMENT '归档文件路径',
    `file_size` bigint DEFAULT NULL COMMENT '文件大小（字节）',
    `upload_time` datetime NOT NULL COMMENT '上传时间',
    `auditor` varchar(100) NOT NULL COMMENT '审核人',
    `audit_time` datetime NOT NULL COMMENT '审核时间',
    `archive_user` varchar(100) NOT NULL COMMENT '归档人',
    `archive_user_id` bigint NOT NULL COMMENT '归档人 ID',
    `archive_time` datetime NOT NULL COMMENT '归档时间',
    `archive_remark` varchar(500) DEFAULT NULL COMMENT '归档备注',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_stage` (`stage`),
    KEY `idx_archive_time` (`archive_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='材料归档表';

-- 材料审核日志表
CREATE TABLE IF NOT EXISTS `material_audit_log` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志 ID',
    `submission_id` bigint NOT NULL COMMENT '提交 ID',
    `audit_user_id` bigint NOT NULL COMMENT '审核用户 ID',
    `audit_user_name` varchar(100) NOT NULL COMMENT '审核用户姓名',
    `audit_type` varchar(20) NOT NULL COMMENT '审核类型：audit-初审/final_audit-终审',
    `audit_result` varchar(20) NOT NULL COMMENT '审核结果：approved-通过/rejected-退回',
    `audit_opinion` varchar(500) DEFAULT NULL COMMENT '审核意见',
    `audit_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '审核时间',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_submission_id` (`submission_id`),
    KEY `idx_audit_user_id` (`audit_user_id`),
    KEY `idx_audit_time` (`audit_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='材料审核日志表';
