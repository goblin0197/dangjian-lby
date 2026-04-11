-- MySQL dump 10.13  Distrib 8.0.21, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: party_building
-- ------------------------------------------------------
-- Server version	8.0.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `activity`
--

DROP TABLE IF EXISTS `activity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `activityName` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '活动名称',
  `orgId` bigint NOT NULL COMMENT '所属党组织ID',
  `userId` bigint NOT NULL COMMENT '创建人ID',
  `activityType` int NOT NULL COMMENT '活动类型:1.会议/2.志愿活动/3.学习/4.其他',
  `activityContent` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '活动描述',
  `enrollDeadline` datetime NOT NULL COMMENT '报名截止时间',
  `startTime` datetime NOT NULL COMMENT '开始时间',
  `endTime` datetime NOT NULL COMMENT '结束时间',
  `location` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '活动地点',
  `maxNum` int NOT NULL DEFAULT '1' COMMENT '最大参与人数',
  `currentNum` int NOT NULL DEFAULT '0' COMMENT '当前参与人数',
  `totalParticipant` int NOT NULL DEFAULT '0' COMMENT '总报名人数（已报名人数）',
  `actualParticipant` int NOT NULL DEFAULT '0' COMMENT '实际参与人数（已报名且签到人数）',
  `signRate` double NOT NULL DEFAULT '0' COMMENT '签到率（实际参与人数/总参与人数）',
  `status` int NOT NULL COMMENT '活动状态:1.待发布/2.已发布/3.进行中/4.已结束',
  `signInType` int DEFAULT NULL COMMENT '签到方式:1.扫码签到/2.手动签到',
  `QRCodeUrl` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '签到二维码URL(系统生成)',
  `reviewContent` text COLLATE utf8mb4_unicode_ci COMMENT '活动总结',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_orgId` (`orgId`),
  KEY `idx_creatorId` (`userId`),
  KEY `idx_activityType` (`activityType`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=2041935759308615683 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity`
--

LOCK TABLES `activity` WRITE;
/*!40000 ALTER TABLE `activity` DISABLE KEYS */;
INSERT INTO `activity` VALUES (2041933320333422593,'计算工程学院/大数据学院迎新生志愿活动',2,2040756748779782145,2,'计算工程学院/大数据学院迎新生志愿活动','2026-06-30 01:36:26','2026-06-16 01:36:08','2026-06-30 01:36:12','计算工程学院/大数据学院',150,0,0,0,0,3,NULL,NULL,NULL,'2026-04-09 01:36:55','2026-04-09 01:44:53',0),(2041935759308615682,'汽车与交通工程学院迎新生志愿活动',3,2040756748779782145,2,'汽车与交通工程学院迎新生志愿活动','2026-04-30 01:46:23','2026-06-10 01:46:00','2026-06-30 01:46:05','汽车与交通工程学院',110,0,0,0,0,1,NULL,NULL,NULL,'2026-04-09 01:46:37','2026-04-09 01:46:37',0);
/*!40000 ALTER TABLE `activity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `activity_enroll`
--

DROP TABLE IF EXISTS `activity_enroll`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `activity_enroll` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `activityId` bigint NOT NULL COMMENT '活动ID',
  `userId` bigint NOT NULL COMMENT '用户ID',
  `enrollTime` datetime NOT NULL COMMENT '报名时间',
  `participantStatus` int NOT NULL COMMENT '参与状态:1.已报名/2.已取消',
  `isSign` tinyint NOT NULL DEFAULT '0' COMMENT '是否签到:0.未签到/1.已签到',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `opUserId` bigint NOT NULL COMMENT '操作人ID',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_activityId_userId` (`activityId`,`userId`),
  KEY `idx_activityId` (`activityId`),
  KEY `idx_userId` (`userId`),
  KEY `idx_participantStatus` (`participantStatus`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='活动报名记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `activity_enroll`
--

LOCK TABLES `activity_enroll` WRITE;
/*!40000 ALTER TABLE `activity_enroll` DISABLE KEYS */;
/*!40000 ALTER TABLE `activity_enroll` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `development_stage`
--

DROP TABLE IF EXISTS `development_stage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `development_stage` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userId` bigint NOT NULL COMMENT '用户ID',
  `trainerId` bigint NOT NULL COMMENT '培养人ID',
  `stageName` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '阶段名称：积极分子/发展对象/预备党员/正式党员',
  `stageStartTime` date NOT NULL COMMENT '阶段开始时间',
  `stageEndTime` date DEFAULT NULL COMMENT '阶段结束时间',
  `stageStatus` tinyint NOT NULL DEFAULT '0' COMMENT '阶段状态：0进行中/1已完成/2已终止',
  `assessmentContent` text COLLATE utf8mb4_unicode_ci COMMENT '考察内容',
  `assessmentResult` tinyint DEFAULT NULL COMMENT '考察结果：1合格/0不合格/2未审核',
  `auditUserId` bigint DEFAULT NULL COMMENT '审核人员ID',
  `auditTime` datetime DEFAULT NULL COMMENT '审核时间',
  `auditRemark` text COLLATE utf8mb4_unicode_ci COMMENT '审核意见',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`userId`),
  KEY `idx_trainerId` (`trainerId`),
  KEY `idx_stageName` (`stageName`),
  KEY `idx_stageStatus` (`stageStatus`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='发展阶段表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `development_stage`
--

LOCK TABLES `development_stage` WRITE;
/*!40000 ALTER TABLE `development_stage` DISABLE KEYS */;
INSERT INTO `development_stage` VALUES (1,2004442313254998018,2040756748779782145,'积极分子','2026-01-09',NULL,0,'第一季度思想汇报',NULL,NULL,NULL,NULL,'2026-04-09 02:46:11','2026-04-09 02:46:11',0);
/*!40000 ALTER TABLE `development_stage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `development_stage_log`
--

DROP TABLE IF EXISTS `development_stage_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `development_stage_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `stageId` bigint NOT NULL COMMENT '发展阶段记录ID',
  `userId` bigint NOT NULL COMMENT '用户ID',
  `operationType` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作类型：创建/更新/提交审核/审核/删除',
  `operatorId` bigint NOT NULL COMMENT '操作人ID',
  `operatorName` varchar(128) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '操作人姓名',
  `beforeData` text COLLATE utf8mb4_unicode_ci COMMENT '变更前数据（JSON格式）',
  `afterData` text COLLATE utf8mb4_unicode_ci COMMENT '变更后数据（JSON格式）',
  `remark` text COLLATE utf8mb4_unicode_ci COMMENT '备注说明',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_stageId` (`stageId`),
  KEY `idx_userId` (`userId`),
  KEY `idx_operationType` (`operationType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='发展阶段变更日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `development_stage_log`
--

LOCK TABLES `development_stage_log` WRITE;
/*!40000 ALTER TABLE `development_stage_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `development_stage_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file`
--

DROP TABLE IF EXISTS `file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `fileName` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件名称',
  `originFileName` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '原始文件名',
  `orgId` bigint DEFAULT NULL COMMENT '所属党组织ID',
  `userId` bigint DEFAULT NULL COMMENT '上传用户ID',
  `stageId` bigint DEFAULT NULL COMMENT '所属发展阶段ID',
  `fileUrl` varchar(1024) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件URL',
  `fileSize` int NOT NULL COMMENT '文件大小（字节）',
  `isTemplate` tinyint NOT NULL DEFAULT '0' COMMENT '是否为模板：0-否，1-是',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_org_id` (`orgId`),
  KEY `idx_user_id` (`userId`),
  KEY `idx_is_template` (`isTemplate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file`
--

LOCK TABLES `file` WRITE;
/*!40000 ALTER TABLE `file` DISABLE KEYS */;
/*!40000 ALTER TABLE `file` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notice`
--

DROP TABLE IF EXISTS `notice`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notice` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `title` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告标题',
  `content` text COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '公告内容',
  `publisherId` bigint NOT NULL COMMENT '发布人ID',
  `orgId` bigint DEFAULT NULL COMMENT '所属党组织ID(null表示系统公告)',
  `publishTime` datetime DEFAULT NULL COMMENT '首次发布时间',
  `expireTime` datetime DEFAULT NULL COMMENT '过期时间',
  `isTop` tinyint NOT NULL DEFAULT '0' COMMENT '是否置顶:0-否,1-是',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态:0-草稿/1-已发布',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_orgId` (`orgId`),
  KEY `idx_publisherId` (`publisherId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notice`
--

LOCK TABLES `notice` WRITE;
/*!40000 ALTER TABLE `notice` DISABLE KEYS */;
/*!40000 ALTER TABLE `notice` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `org_quantify`
--

DROP TABLE IF EXISTS `org_quantify`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `org_quantify` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `orgId` bigint NOT NULL COMMENT '组织ID',
  `statDate` date NOT NULL COMMENT '统计日期',
  `activityCount` int NOT NULL DEFAULT '0' COMMENT '活动次数',
  `totalParticipant` int NOT NULL DEFAULT '0' COMMENT '总报名人数',
  `totalSign` int NOT NULL DEFAULT '0' COMMENT '总签到人数',
  `avgParticipant` double NOT NULL DEFAULT '0' COMMENT '平均每次活动报名人数',
  `avgSign` double NOT NULL DEFAULT '0' COMMENT '平均每次活动签到人数',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_orgId_statDate` (`orgId`,`statDate`),
  KEY `idx_orgId` (`orgId`),
  KEY `idx_statDate` (`statDate`)
) ENGINE=InnoDB AUTO_INCREMENT=2041924029463523331 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='组织量化统计表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_quantify`
--

LOCK TABLES `org_quantify` WRITE;
/*!40000 ALTER TABLE `org_quantify` DISABLE KEYS */;
INSERT INTO `org_quantify` VALUES (2040836868458778626,1,'2026-04-09',2,0,0,0,0,'2026-04-06 01:00:01','2026-04-09 04:53:11',0),(2040836868655910913,2,'2026-04-09',1,0,0,0,0,'2026-04-06 01:00:01','2026-04-09 04:53:11',0),(2040836868781740033,3,'2026-04-09',1,0,0,0,0,'2026-04-06 01:00:01','2026-04-09 04:53:11',0),(2040836868848848897,4,'2026-04-09',0,0,0,0,0,'2026-04-06 01:00:01','2026-04-09 01:00:00',0),(2040836868974678017,5,'2026-04-09',0,0,0,0,0,'2026-04-06 01:00:01','2026-04-09 01:00:00',0),(2040836869360553985,9,'2026-04-06',0,0,0,0,0,'2026-04-06 01:00:01','2026-04-06 01:00:01',0),(2040836869624795138,14,'2026-04-06',0,0,0,0,0,'2026-04-06 01:00:01','2026-04-06 01:00:01',0),(2040836869821927426,16,'2026-04-09',0,0,0,0,0,'2026-04-06 01:00:01','2026-04-09 01:00:00',0),(2041924029463523330,17,'2026-04-09',0,0,0,0,0,'2026-04-09 01:00:00','2026-04-09 01:00:00',0);
/*!40000 ALTER TABLE `org_quantify` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `org_relation_transfer`
--

DROP TABLE IF EXISTS `org_relation_transfer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `org_relation_transfer` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userId` bigint NOT NULL COMMENT '用户ID（党员ID）',
  `fromOrgId` bigint NOT NULL COMMENT '原党组织ID',
  `fromOrgName` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '原党组织名称',
  `toOrgId` bigint NOT NULL COMMENT '目标党组织ID',
  `toOrgName` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '目标党组织名称',
  `transferReason` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '转移原因',
  `transferTime` datetime NOT NULL COMMENT '转移时间',
  `approveStatus` int NOT NULL COMMENT '审批状态：1-待审批/2-已通过/3-已拒绝',
  `approveUserId` bigint DEFAULT NULL COMMENT '审批人ID',
  `approveUserName` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '审批人姓名',
  `approveTime` datetime DEFAULT NULL COMMENT '审批时间',
  `approveComment` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '审批意见',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_userId` (`userId`),
  KEY `idx_fromOrgId` (`fromOrgId`),
  KEY `idx_toOrgId` (`toOrgId`),
  KEY `idx_approveStatus` (`approveStatus`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='组织关系转移表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `org_relation_transfer`
--

LOCK TABLES `org_relation_transfer` WRITE;
/*!40000 ALTER TABLE `org_relation_transfer` DISABLE KEYS */;
/*!40000 ALTER TABLE `org_relation_transfer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organization`
--

DROP TABLE IF EXISTS `organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `organization` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `orgName` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '组织名称',
  `orgCode` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '组织编码',
  `parentId` bigint DEFAULT NULL COMMENT '父组织ID',
  `orgType` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '组织类型：党委/党总支/党支部',
  `orgLevel` int NOT NULL COMMENT '组织级别',
  `leaderId` bigint DEFAULT NULL COMMENT '负责人ID',
  `address` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '地址',
  `description` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '组织描述',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_orgCode` (`orgCode`),
  KEY `idx_parentId` (`parentId`),
  KEY `idx_orgType` (`orgType`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='党组织表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization`
--

LOCK TABLES `organization` WRITE;
/*!40000 ALTER TABLE `organization` DISABLE KEYS */;
INSERT INTO `organization` VALUES (1,'广州城市理工学院','GCU',0,'党委',1,2040756748779782145,'广州城市理工学院党委','广州城市理工学院党委','2025-12-27 16:20:27','2026-04-06 01:13:57',0),(2,'计算机工程学院/大数据学院','CST',1,'党总支',2,2040756748779782145,'广州城市理工学院计算机工程学院/大数据学院党总支','广州城市理工学院计算机工程学院/大数据学院党总支','2025-12-27 16:21:19','2026-04-06 01:14:14',0),(3,'汽车与交通工程学院','QC',1,'党总支',2,2040756748779782145,'广州城市理工学院汽车与交通工程学院党总支','广州城市理工学院汽车与交通工程学院党总支','2026-04-05 23:32:34','2026-04-06 01:14:36',0),(4,'计算机工程学院/大数据学院学生第一党支部','CSTX1',2,'党支部',3,2040756748779782145,'广州城市理工学院计算机工程学院/大数据学院学生第一党支部','广州城市理工学院计算机工程学院/大数据学院学生第一党支部','2026-04-05 23:35:57','2026-04-06 01:14:52',0),(5,'计算机工程学院/大数据学院学生第二党支部','CSTX2',2,'党支部',3,2040756748779782145,'广州城市理工学院计算机工程学院/大数据学院学生第二党支部','广州城市理工学院计算机工程学院/大数据学院学生第二党支部','2026-04-05 23:42:42','2026-04-06 01:15:02',0),(16,'汽车与交通工程学院学生第一党支部','QCX1',3,'党支部',3,2040756748779782145,'广州城市理工学院汽车与交通工程学院学生第一党支部','广州城市理工学院汽车与交通工程学院学生第一党支部','2026-04-06 00:55:07','2026-04-06 01:15:21',0),(17,'汽车与交通工程学院学生第二党支部','QCX2',3,'党支部',3,2040756748779782145,'广州城市理工学院汽车与交通工程学院学生第二党支部','广州城市理工学院汽车与交通工程学院学生第二党支部','2026-04-06 01:12:25','2026-04-08 22:38:01',0);
/*!40000 ALTER TABLE `organization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trainer_relation`
--

DROP TABLE IF EXISTS `trainer_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `trainer_relation` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userId` bigint NOT NULL COMMENT '用户ID（被培养人）',
  `userName` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '被培养人姓名',
  `trainerId` bigint NOT NULL COMMENT '培养人ID',
  `trainerName` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '培养人姓名',
  `startDate` date NOT NULL COMMENT '开始日期',
  `endDate` date DEFAULT NULL COMMENT '结束日期',
  `status` int NOT NULL COMMENT '状态：1.进行中/2.已完成/3.已终止',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_userId_trainerId` (`userId`,`trainerId`),
  KEY `idx_userId` (`userId`),
  KEY `idx_trainerId` (`trainerId`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='培养人关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trainer_relation`
--

LOCK TABLES `trainer_relation` WRITE;
/*!40000 ALTER TABLE `trainer_relation` DISABLE KEYS */;
/*!40000 ALTER TABLE `trainer_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userAccount` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
  `userPassword` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
  `userName` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户姓名',
  `userAvatar` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户头像',
  `userRole` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'activist_development' COMMENT '用户角色(与系统使用权限有关)：super_admin超级管理员/org_admin组织管理员/org_member党员/activist_development积极分子/发展人员',
  `orgId` bigint DEFAULT '0' COMMENT '所属党组织ID',
  `phone` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `email` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `userType` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '学生' COMMENT '用户类型：教师/学生',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态：0-正常，1-禁用',
  `joinDate` date DEFAULT NULL COMMENT '申请入党日期',
  `positiveDate` date DEFAULT NULL COMMENT '转正日期',
  `politicalStatus` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '政治面貌（与权限无关）：党员/预备党员/共青团员',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  KEY `idx_orgId` (`orgId`),
  KEY `idx_userRole` (`userRole`),
  KEY `idx_userType` (`userType`)
) ENGINE=InnoDB AUTO_INCREMENT=2040756748779782146 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (2004440568437137410,'stest','160d06e9d66b05f2ca1124e12d6a317e','党员1','','super_admin',4,NULL,NULL,'教师',0,NULL,NULL,'正式党员','2025-12-26 14:34:06','2026-04-09 04:31:38',0),(2004442313254998018,'otest','160d06e9d66b05f2ca1124e12d6a317e','积极分子1','','org_admin',4,NULL,NULL,'教师',0,NULL,NULL,'积极分子','2025-12-26 14:41:02','2026-04-09 04:22:26',0),(2004442386789535746,'ptest','160d06e9d66b05f2ca1124e12d6a317e','积极分子2','','party_member',4,NULL,NULL,'教师',0,NULL,NULL,'积极分子','2025-12-26 14:41:20','2026-04-09 04:31:38',0),(2004442425519738881,'atest','160d06e9d66b05f2ca1124e12d6a317e','发展对象1','','activist_development',4,NULL,NULL,'学生',0,NULL,NULL,'发展对象','2025-12-26 14:41:29','2026-04-09 04:31:38',0),(2004816881140359169,'test2','160d06e9d66b05f2ca1124e12d6a317e','预备党员1','11111','activist_development',4,'111111','qqqq','学生',1,'2025-12-12','2025-12-12','预备党员','2025-12-27 15:29:26','2026-04-09 04:31:38',0),(2040756748779782145,'superadmin','160d06e9d66b05f2ca1124e12d6a317e','超级管理员','','super_admin',4,'12345678910','123@gcu.com','教师',0,NULL,NULL,'正式党员','2026-04-05 19:41:38','2026-04-09 04:31:38',0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_quantify`
--

DROP TABLE IF EXISTS `user_quantify`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_quantify` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `userId` bigint NOT NULL COMMENT '用户ID',
  `statDate` date NOT NULL COMMENT '统计日期',
  `totalActivity` int NOT NULL DEFAULT '0' COMMENT '所属组织及其父组织开展总活动数',
  `participateActivity` int NOT NULL DEFAULT '0' COMMENT '报名活动数',
  `participateRate` double NOT NULL DEFAULT '0' COMMENT '活动参与度(报名且参与活动数/总活动数)',
  `signActivity` int NOT NULL DEFAULT '0' COMMENT '签到活动数',
  `signRate` double NOT NULL DEFAULT '0' COMMENT '签到率(签到活动数/报名活动数)',
  `fileCount` int NOT NULL DEFAULT '0' COMMENT '材料文件数量',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `isDelete` tinyint NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_userId_statDate` (`userId`,`statDate`),
  KEY `idx_userId` (`userId`),
  KEY `idx_statDate` (`statDate`)
) ENGINE=InnoDB AUTO_INCREMENT=2040836868072902658 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户量化统计表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_quantify`
--

LOCK TABLES `user_quantify` WRITE;
/*!40000 ALTER TABLE `user_quantify` DISABLE KEYS */;
INSERT INTO `user_quantify` VALUES (2040836866726531074,2004440568437137410,'2026-04-09',1,0,0,0,0,0,'2026-04-06 01:00:00','2026-04-09 04:53:11',0),(2040836867431174145,2004442313254998018,'2026-04-09',1,0,0,0,0,0,'2026-04-06 01:00:00','2026-04-09 04:53:11',0),(2040836867619917826,2004442386789535746,'2026-04-09',1,0,0,0,0,0,'2026-04-06 01:00:00','2026-04-09 04:53:11',0),(2040836867754135553,2004442425519738881,'2026-04-09',1,0,0,0,0,0,'2026-04-06 01:00:00','2026-04-09 04:53:11',0),(2040836867942879233,2004816881140359169,'2026-04-09',1,0,0,0,0,0,'2026-04-06 01:00:00','2026-04-09 04:53:11',0),(2040836868072902657,2040756748779782145,'2026-04-09',1,0,0,0,0,0,'2026-04-06 01:00:00','2026-04-09 04:53:11',0);
/*!40000 ALTER TABLE `user_quantify` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-11  0:04:09
