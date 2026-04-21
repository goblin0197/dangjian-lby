# 智慧党建系统数据库表设计
# 创建库
create database if not exists party_building;

# 切换库
use party_building;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for activity
-- ----------------------------
DROP TABLE IF EXISTS `activity`;
CREATE TABLE `activity`  (
                             `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                             `activityName` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '活动名称',
                             `orgId` bigint NOT NULL COMMENT '所属党组织ID',
                             `userId` bigint NOT NULL COMMENT '创建人ID',
                             `activityType` int NOT NULL COMMENT '活动类型:1.会议/2.志愿活动/3.学习/4.其他',
                             `activityContent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '活动描述',
                             `enrollDeadline` datetime NOT NULL COMMENT '报名截止时间',
                             `startTime` datetime NOT NULL COMMENT '开始时间',
                             `endTime` datetime NOT NULL COMMENT '结束时间',
                             `location` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '活动地点',
                             `maxNum` int NOT NULL DEFAULT 1 COMMENT '最大参与人数',
                             `currentNum` int NOT NULL DEFAULT 0 COMMENT '当前参与人数',
                             `totalParticipant` int NOT NULL DEFAULT 0 COMMENT '总报名人数（已报名人数）',
                             `actualParticipant` int NOT NULL DEFAULT 0 COMMENT '实际参与人数（已报名且签到人数）',
                             `signRate` double NOT NULL DEFAULT 0 COMMENT '签到率（实际参与人数/总参与人数）',
                             `status` int NOT NULL COMMENT '活动状态:1.待发布/2.已发布/3.进行中/4.已结束',
                             `signInType` int NULL DEFAULT NULL COMMENT '签到方式:1.扫码签到/2.手动签到',
                             `QRCodeUrl` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '签到二维码URL(系统生成)',
                             `reviewContent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '活动总结',
                             `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                             PRIMARY KEY (`id`) USING BTREE,
                             INDEX `idx_orgId`(`orgId` ASC) USING BTREE,
                             INDEX `idx_creatorId`(`userId` ASC) USING BTREE,
                             INDEX `idx_activityType`(`activityType` ASC) USING BTREE,
                             INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2041935759308615683 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '活动表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for activity_enroll
-- ----------------------------
DROP TABLE IF EXISTS `activity_enroll`;
CREATE TABLE `activity_enroll`  (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                    `activityId` bigint NOT NULL COMMENT '活动ID',
                                    `userId` bigint NOT NULL COMMENT '用户ID',
                                    `enrollTime` datetime NOT NULL COMMENT '报名时间',
                                    `participantStatus` int NOT NULL COMMENT '参与状态:1.已报名/2.已取消',
                                    `isSign` tinyint NOT NULL DEFAULT 0 COMMENT '是否签到:0.未签到/1.已签到',
                                    `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `opUserId` bigint NOT NULL COMMENT '操作人ID',
                                    `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    INDEX `idx_activityId`(`activityId` ASC) USING BTREE,
                                    INDEX `idx_userId`(`userId` ASC) USING BTREE,
                                    INDEX `idx_participantStatus`(`participantStatus` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '活动报名记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for announcement
-- ----------------------------
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement`  (
                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '公告ID',
                                 `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告标题',
                                 `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告类型',
                                 `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告内容',
                                 `publisher` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '发布人',
                                 `publisherId` bigint NOT NULL COMMENT '发布人ID',
                                 `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告状态',
                                 `isTop` tinyint NOT NULL DEFAULT 0 COMMENT '是否置顶',
                                 `publishTime` datetime NULL DEFAULT NULL COMMENT '发布时间',
                                 `validStart` datetime NULL DEFAULT NULL COMMENT '生效开始时间',
                                 `validEnd` datetime NULL DEFAULT NULL COMMENT '生效结束时间',
                                 `readCount` int NOT NULL DEFAULT 0 COMMENT '阅读量',
                                 `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 INDEX `idx_type`(`type` ASC) USING BTREE,
                                 INDEX `idx_status`(`status` ASC) USING BTREE,
                                 INDEX `idx_publishTime`(`publishTime` ASC) USING BTREE,
                                 INDEX `idx_isTop`(`isTop` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for announcement_attachment
-- ----------------------------
DROP TABLE IF EXISTS `announcement_attachment`;
CREATE TABLE `announcement_attachment`  (
                                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT '附件ID',
                                            `announcementId` bigint NOT NULL COMMENT '公告ID',
                                            `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '附件名称',
                                            `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '附件路径',
                                            `size` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '附件大小',
                                            `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '附件类型',
                                            `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                            PRIMARY KEY (`id`) USING BTREE,
                                            INDEX `idx_announcementId`(`announcementId` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '公告附件表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for announcement_read_record
-- ----------------------------
DROP TABLE IF EXISTS `announcement_read_record`;
CREATE TABLE `announcement_read_record`  (
                                             `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
                                             `announcementId` bigint NOT NULL COMMENT '公告ID',
                                             `userId` bigint NOT NULL COMMENT '用户ID',
                                             `readTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '阅读时间',
                                             PRIMARY KEY (`id`) USING BTREE,
                                             UNIQUE INDEX `uk_announcement_user`(`announcementId` ASC, `userId` ASC) USING BTREE,
                                             INDEX `idx_userId`(`userId` ASC) USING BTREE,
                                             INDEX `idx_readTime`(`readTime` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '公告阅读记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for announcement_share
-- ----------------------------
DROP TABLE IF EXISTS `announcement_share`;
CREATE TABLE `announcement_share`  (
                                       `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分享ID',
                                       `announcementId` bigint NOT NULL COMMENT '公告ID',
                                       `shareUrl` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '分享链接',
                                       `shareUserId` bigint NOT NULL COMMENT '分享用户ID',
                                       `shareTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '分享时间',
                                       `expireTime` datetime NULL DEFAULT NULL COMMENT '过期时间',
                                       `viewCount` int NOT NULL DEFAULT 0 COMMENT '查看次数',
                                       PRIMARY KEY (`id`) USING BTREE,
                                       INDEX `idx_announcementId`(`announcementId` ASC) USING BTREE,
                                       INDEX `idx_shareUserId`(`shareUserId` ASC) USING BTREE,
                                       INDEX `idx_expireTime`(`expireTime` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '公告分享表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for development_stage
-- ----------------------------
DROP TABLE IF EXISTS `development_stage`;
CREATE TABLE `development_stage`  (
                                      `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                      `userId` bigint NOT NULL COMMENT '用户ID',
                                      `trainerId` bigint NOT NULL COMMENT '培养人ID',
                                      `stageName` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '阶段名称：积极分子/发展对象/预备党员/正式党员',
                                      `stageStartTime` date NOT NULL COMMENT '阶段开始时间',
                                      `stageEndTime` date NULL DEFAULT NULL COMMENT '阶段结束时间',
                                      `stageStatus` tinyint NOT NULL DEFAULT 0 COMMENT '阶段状态：0进行中/1已完成/2已终止',
                                      `assessmentContent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '考察内容',
                                      `assessmentResult` tinyint NULL DEFAULT NULL COMMENT '考察结果：1合格/0不合格/2未审核',
                                      `auditUserId` bigint NULL DEFAULT NULL COMMENT '审核人员ID',
                                      `auditTime` datetime NULL DEFAULT NULL COMMENT '审核时间',
                                      `auditRemark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '审核意见',
                                      `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                      PRIMARY KEY (`id`) USING BTREE,
                                      INDEX `idx_userId`(`userId` ASC) USING BTREE,
                                      INDEX `idx_trainerId`(`trainerId` ASC) USING BTREE,
                                      INDEX `idx_stageName`(`stageName` ASC) USING BTREE,
                                      INDEX `idx_stageStatus`(`stageStatus` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '发展阶段表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for development_stage_log
-- ----------------------------
DROP TABLE IF EXISTS `development_stage_log`;
CREATE TABLE `development_stage_log`  (
                                          `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                          `stageId` bigint NOT NULL COMMENT '发展阶段记录ID',
                                          `userId` bigint NOT NULL COMMENT '用户ID',
                                          `operationType` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作类型：创建/更新/提交审核/审核/删除',
                                          `operatorId` bigint NOT NULL COMMENT '操作人ID',
                                          `operatorName` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作人姓名',
                                          `beforeData` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '变更前数据（JSON格式）',
                                          `afterData` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '变更后数据（JSON格式）',
                                          `remark` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '备注说明',
                                          `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                          PRIMARY KEY (`id`) USING BTREE,
                                          INDEX `idx_stageId`(`stageId` ASC) USING BTREE,
                                          INDEX `idx_userId`(`userId` ASC) USING BTREE,
                                          INDEX `idx_operationType`(`operationType` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '发展阶段变更日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for file
-- ----------------------------
DROP TABLE IF EXISTS `file`;
CREATE TABLE `file`  (
                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                         `fileName` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件名称',
                         `originFileName` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '原始文件名',
                         `orgId` bigint NULL DEFAULT NULL COMMENT '所属党组织ID',
                         `userId` bigint NULL DEFAULT NULL COMMENT '上传用户ID',
                         `stageId` bigint NULL DEFAULT NULL COMMENT '所属发展阶段ID',
                         `fileUrl` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件URL',
                         `fileSize` int NOT NULL COMMENT '文件大小（字节）',
                         `isTemplate` tinyint NOT NULL DEFAULT 0 COMMENT '是否为模板：0-否，1-是',
                         `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                         PRIMARY KEY (`id`) USING BTREE,
                         INDEX `idx_org_id`(`orgId` ASC) USING BTREE,
                         INDEX `idx_user_id`(`userId` ASC) USING BTREE,
                         INDEX `idx_is_template`(`isTemplate` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文件表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for material_archive
-- ----------------------------
DROP TABLE IF EXISTS `material_archive`;
CREATE TABLE `material_archive`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT '归档ID',
                                     `submissionId` bigint NOT NULL COMMENT '提交ID',
                                     `userId` bigint NOT NULL COMMENT '用户ID',
                                     `userName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户姓名',
                                     `orgLevel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '组织层级',
                                     `stage` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '发展阶段',
                                     `materialName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '材料名称',
                                     `fileUrl` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '归档文件路径',
                                     `uploadTime` datetime NOT NULL COMMENT '上传时间',
                                     `auditor` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '审核人',
                                     `auditTime` datetime NOT NULL COMMENT '审核时间',
                                     `archiveUser` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '归档人',
                                     `archiveTime` datetime NOT NULL COMMENT '归档时间',
                                     `archiveRemark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '归档备注',
                                     `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                     `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                     PRIMARY KEY (`id`) USING BTREE,
                                     INDEX `idx_userId`(`userId` ASC) USING BTREE,
                                     INDEX `idx_stage`(`stage` ASC) USING BTREE,
                                     INDEX `idx_archiveTime`(`archiveTime` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '材料归档表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for material_auditlog
-- ----------------------------
DROP TABLE IF EXISTS `material_auditlog`;
CREATE TABLE `material_auditlog`  (
                                      `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
                                      `submissionId` bigint NOT NULL COMMENT '提交ID',
                                      `auditUserId` bigint NOT NULL COMMENT '审核用户ID',
                                      `auditUserName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '审核用户姓名',
                                      `auditType` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '审核类型',
                                      `auditResult` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '审核结果',
                                      `auditOpinion` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '审核意见',
                                      `auditTime` datetime NOT NULL COMMENT '审核时间',
                                      `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      PRIMARY KEY (`id`) USING BTREE,
                                      INDEX `idx_submissionId`(`submissionId` ASC) USING BTREE,
                                      INDEX `idx_auditUserId`(`auditUserId` ASC) USING BTREE,
                                      INDEX `idx_auditTime`(`auditTime` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '材料审核日志表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for material_submission
-- ----------------------------
DROP TABLE IF EXISTS `material_submission`;
CREATE TABLE `material_submission`  (
                                        `id` bigint NOT NULL AUTO_INCREMENT COMMENT '提交ID',
                                        `userId` bigint NOT NULL COMMENT '用户ID',
                                        `userName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户姓名',
                                        `orgLevel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '组织层级',
                                        `stage` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '发展阶段',
                                        `materialName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '材料名称',
                                        `submitStatus` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '提交状态',
                                        `auditStatus` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '审核状态',
                                        `fileUrl` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '材料文件路径',
                                        `uploadTime` datetime NULL DEFAULT NULL COMMENT '上传时间',
                                        `auditor` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '审核人',
                                        `auditTime` datetime NULL DEFAULT NULL COMMENT '审核时间',
                                        `auditOpinion` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '审核意见',
                                        `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                        `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                        PRIMARY KEY (`id`) USING BTREE,
                                        INDEX `idx_userId`(`userId` ASC) USING BTREE,
                                        INDEX `idx_stage`(`stage` ASC) USING BTREE,
                                        INDEX `idx_submitStatus`(`submitStatus` ASC) USING BTREE,
                                        INDEX `idx_auditStatus`(`auditStatus` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '材料提交表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for material_template
-- ----------------------------
DROP TABLE IF EXISTS `material_template`;
CREATE TABLE `material_template`  (
                                      `id` bigint NOT NULL AUTO_INCREMENT COMMENT '模板ID',
                                      `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '模板名称',
                                      `stage` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '所属发展阶段',
                                      `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '材料类型',
                                      `status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '模板状态',
                                      `fileUrl` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '模板文件路径',
                                      `uploadTime` datetime NOT NULL COMMENT '上传时间',
                                      `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '模板说明',
                                      `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                      PRIMARY KEY (`id`) USING BTREE,
                                      INDEX `idx_stage`(`stage` ASC) USING BTREE,
                                      INDEX `idx_type`(`type` ASC) USING BTREE,
                                      INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '材料模板表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for notice
-- ----------------------------
DROP TABLE IF EXISTS `notice`;
CREATE TABLE `notice`  (
                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                           `title` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告标题',
                           `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告内容',
                           `publisherId` bigint NOT NULL COMMENT '发布人ID',
                           `orgId` bigint NULL DEFAULT NULL COMMENT '所属党组织ID(null表示系统公告)',
                           `publishTime` datetime NULL DEFAULT NULL COMMENT '首次发布时间',
                           `expireTime` datetime NULL DEFAULT NULL COMMENT '过期时间',
                           `isTop` tinyint NOT NULL DEFAULT 0 COMMENT '是否置顶:0-否,1-是',
                           `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态:0-草稿/1-已发布',
                           `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                           PRIMARY KEY (`id`) USING BTREE,
                           INDEX `idx_orgId`(`orgId` ASC) USING BTREE,
                           INDEX `idx_publisherId`(`publisherId` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '公告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for org_quantify
-- ----------------------------
DROP TABLE IF EXISTS `org_quantify`;
CREATE TABLE `org_quantify`  (
                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                 `orgId` bigint NOT NULL COMMENT '组织ID',
                                 `statDate` date NOT NULL COMMENT '统计日期',
                                 `activityCount` int NOT NULL DEFAULT 0 COMMENT '活动次数',
                                 `totalParticipant` int NOT NULL DEFAULT 0 COMMENT '总报名人数',
                                 `totalSign` int NOT NULL DEFAULT 0 COMMENT '总签到人数',
                                 `avgParticipant` double NOT NULL DEFAULT 0 COMMENT '平均每次活动报名人数',
                                 `avgSign` double NOT NULL DEFAULT 0 COMMENT '平均每次活动签到人数',
                                 `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 UNIQUE INDEX `uk_orgId_statDate`(`orgId` ASC, `statDate` ASC) USING BTREE,
                                 INDEX `idx_orgId`(`orgId` ASC) USING BTREE,
                                 INDEX `idx_statDate`(`statDate` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2041924029463523331 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '组织量化统计表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for org_relation_transfer
-- ----------------------------
DROP TABLE IF EXISTS `org_relation_transfer`;
CREATE TABLE `org_relation_transfer`  (
                                          `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                          `userId` bigint NOT NULL COMMENT '用户ID（党员ID）',
                                          `fromOrgId` bigint NOT NULL COMMENT '原党组织ID',
                                          `fromOrgName` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '原党组织名称',
                                          `toOrgId` bigint NOT NULL COMMENT '目标党组织ID',
                                          `toOrgName` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '目标党组织名称',
                                          `transferReason` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '转移原因',
                                          `transferTime` datetime NOT NULL COMMENT '转移时间',
                                          `approveStatus` int NOT NULL COMMENT '审批状态：1-待审批/2-已通过/3-已拒绝',
                                          `approveUserId` bigint NULL DEFAULT NULL COMMENT '审批人ID',
                                          `approveUserName` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '审批人姓名',
                                          `approveTime` datetime NULL DEFAULT NULL COMMENT '审批时间',
                                          `approveComment` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '审批意见',
                                          `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                          `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                          `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                          PRIMARY KEY (`id`) USING BTREE,
                                          INDEX `idx_userId`(`userId` ASC) USING BTREE,
                                          INDEX `idx_fromOrgId`(`fromOrgId` ASC) USING BTREE,
                                          INDEX `idx_toOrgId`(`toOrgId` ASC) USING BTREE,
                                          INDEX `idx_approveStatus`(`approveStatus` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '组织关系转移表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for organization
-- ----------------------------
DROP TABLE IF EXISTS `organization`;
CREATE TABLE `organization`  (
                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                 `orgName` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '组织名称',
                                 `orgCode` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '组织编码',
                                 `parentId` bigint NULL DEFAULT NULL COMMENT '父组织ID',
                                 `orgType` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '组织类型：党委/党总支/党支部',
                                 `orgLevel` int NOT NULL COMMENT '组织级别',
                                 `leaderId` bigint NULL DEFAULT NULL COMMENT '负责人ID',
                                 `address` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '地址',
                                 `description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '组织描述',
                                 `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 UNIQUE INDEX `uk_orgCode`(`orgCode` ASC) USING BTREE,
                                 INDEX `idx_parentId`(`parentId` ASC) USING BTREE,
                                 INDEX `idx_orgType`(`orgType` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '党组织表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for quantify_data
-- ----------------------------
DROP TABLE IF EXISTS `quantify_data`;
CREATE TABLE `quantify_data`  (
                                  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                  `indicatorId` bigint NOT NULL COMMENT '指标ID',
                                  `targetId` bigint NOT NULL COMMENT '统计对象ID（组织ID或用户ID）',
                                  `targetType` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '统计对象类型：organization-组织/user-用户',
                                  `period` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '统计周期（如：2025.01-03）',
                                  `value` decimal(10, 2) NOT NULL COMMENT '统计值',
                                  `activityRate` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '活动参与率',
                                  `signRate` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '签到率',
                                  `materialRate` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '材料完成率',
                                  `activityCount` int NOT NULL DEFAULT 0 COMMENT '参与活动数',
                                  `unfinishedMaterial` int NOT NULL DEFAULT 0 COMMENT '未完成材料数',
                                  `personCount` int NOT NULL DEFAULT 0 COMMENT '支部人数（仅组织维度）',
                                  `averageRate` decimal(10, 2) NOT NULL DEFAULT 0.00 COMMENT '平均参与率（仅组织维度）',
                                  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `idx_indicatorId`(`indicatorId` ASC) USING BTREE,
                                  INDEX `idx_targetId`(`targetId` ASC) USING BTREE,
                                  INDEX `idx_targetType`(`targetType` ASC) USING BTREE,
                                  INDEX `idx_period`(`period` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '量化数据记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for quantify_indicator
-- ----------------------------
DROP TABLE IF EXISTS `quantify_indicator`;
CREATE TABLE `quantify_indicator`  (
                                       `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                       `name` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '指标名称',
                                       `rule` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '统计规则',
                                       `dimension` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '统计维度：organization-组织/personal-个人/both-两者',
                                       `orgLevel` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '适用组织层级，JSON格式存储',
                                       `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'enable' COMMENT '状态：enable-启用/disable-停用',
                                       `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                       `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                       `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                       PRIMARY KEY (`id`) USING BTREE,
                                       INDEX `idx_dimension`(`dimension` ASC) USING BTREE,
                                       INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '量化指标表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for relation_transfer
-- ----------------------------
DROP TABLE IF EXISTS `relation_transfer`;
CREATE TABLE `relation_transfer`  (
                                      `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                      `userId` bigint NOT NULL COMMENT '用户ID（党员ID）',
                                      `fromOrgId` bigint NOT NULL COMMENT '原党组织ID',
                                      `fromOrgName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '原党组织名称',
                                      `toOrgId` bigint NOT NULL COMMENT '目标党组织ID',
                                      `toOrgName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '目标党组织名称',
                                      `transferReason` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '转移原因',
                                      `transferTime` datetime NOT NULL COMMENT '转移时间',
                                      `approveStatus` int NOT NULL COMMENT '审批状态：待审批/已通过/已拒绝',
                                      `approveUserId` bigint NULL DEFAULT NULL COMMENT '审批人ID',
                                      `approveUserName` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '审批人姓名',
                                      `approveTime` datetime NULL DEFAULT NULL COMMENT '审批时间',
                                      `approveComment` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '审批意见',
                                      `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                      PRIMARY KEY (`id`) USING BTREE,
                                      INDEX `idx_userId`(`userId` ASC) USING BTREE,
                                      INDEX `idx_fromPartyId`(`fromOrgId` ASC) USING BTREE,
                                      INDEX `idx_toPartyId`(`toOrgId` ASC) USING BTREE,
                                      INDEX `idx_approveStatus`(`approveStatus` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2006288201522839555 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '组织关系转移表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for trainer_relation
-- ----------------------------
DROP TABLE IF EXISTS `trainer_relation`;
CREATE TABLE `trainer_relation`  (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                     `userId` bigint NOT NULL COMMENT '用户ID（被培养人）',
                                     `userName` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '被培养人姓名',
                                     `trainerId` bigint NOT NULL COMMENT '培养人ID',
                                     `trainerName` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '培养人姓名',
                                     `startDate` date NOT NULL COMMENT '开始日期',
                                     `endDate` date NULL DEFAULT NULL COMMENT '结束日期',
                                     `status` int NOT NULL COMMENT '状态：1.进行中/2.已完成/3.已终止',
                                     `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                     `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                     `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                     PRIMARY KEY (`id`) USING BTREE,
                                     UNIQUE INDEX `uk_userId_trainerId`(`userId` ASC, `trainerId` ASC) USING BTREE,
                                     INDEX `idx_userId`(`userId` ASC) USING BTREE,
                                     INDEX `idx_trainerId`(`trainerId` ASC) USING BTREE,
                                     INDEX `idx_status`(`status` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '培养人关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                         `userAccount` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
                         `userPassword` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
                         `userName` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户姓名',
                         `userAvatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '用户头像',
                         `userRole` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'activist_development' COMMENT '用户角色(与系统使用权限有关)：super_admin超级管理员/org_admin组织管理员/org_member党员/activist_development积极分子/发展人员',
                         `orgId` bigint NULL DEFAULT 0 COMMENT '所属党组织ID',
                         `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '手机号',
                         `email` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱',
                         `userType` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '学生' COMMENT '用户类型：教师/学生',
                         `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态：0-正常，1-禁用',
                         `joinDate` date NULL DEFAULT NULL COMMENT '申请入党日期',
                         `positiveDate` date NULL DEFAULT NULL COMMENT '转正日期',
                         `politicalStatus` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '政治面貌（与权限无关）：党员/预备党员/共青团员',
                         `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                         PRIMARY KEY (`id`) USING BTREE,
                         INDEX `idx_orgId`(`orgId` ASC) USING BTREE,
                         INDEX `idx_userRole`(`userRole` ASC) USING BTREE,
                         INDEX `idx_userType`(`userType` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2040756748779782146 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_quantify
-- ----------------------------
DROP TABLE IF EXISTS `user_quantify`;
CREATE TABLE `user_quantify`  (
                                  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
                                  `userId` bigint NOT NULL COMMENT '用户ID',
                                  `statDate` date NOT NULL COMMENT '统计日期',
                                  `totalActivity` int NOT NULL DEFAULT 0 COMMENT '所属组织及其父组织开展总活动数',
                                  `participateActivity` int NOT NULL DEFAULT 0 COMMENT '报名活动数',
                                  `participateRate` double NOT NULL DEFAULT 0 COMMENT '活动参与度(报名且参与活动数/总活动数)',
                                  `signActivity` int NOT NULL DEFAULT 0 COMMENT '签到活动数',
                                  `signRate` double NOT NULL DEFAULT 0 COMMENT '签到率(签到活动数/报名活动数)',
                                  `fileCount` int NOT NULL DEFAULT 0 COMMENT '材料文件数量',
                                  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                  `isDelete` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  UNIQUE INDEX `uk_userId_statDate`(`userId` ASC, `statDate` ASC) USING BTREE,
                                  INDEX `idx_userId`(`userId` ASC) USING BTREE,
                                  INDEX `idx_statDate`(`statDate` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2040836868072902658 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '用户量化统计表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
