# 智慧党建系统数据库表设计
# 创建库
create database if not exists party_building;

# 切换库
use party_building;

# 用户表（扩展现有结构，增加党建相关字段）
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           not null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    userName     varchar(256)                           not null comment '用户姓名',
    userAvatar   varchar(1024)                          null comment '用户头像',
    userRole     varchar(256) default 'activist_development'            not null comment '用户角色(与系统使用权限有关)：super_admin超级管理员/org_admin组织管理员/org_member党员/activist_development积极分子/发展人员',
    orgId        bigint       default 0                                null comment '所属党组织ID',
    phone        varchar(32)                            null comment '手机号',
    email        varchar(256)                           null comment '邮箱',
    userType     varchar(64)  default '学生'                          not null comment '用户类型：教师/学生',
    status       tinyint      default 0                 not null comment '状态：0-正常，1-禁用',
    joinDate     date                                   null comment '申请入党日期',
    positiveDate date                                   null comment '转正日期',
    politicalStatus varchar(64)                         null comment '政治面貌（与权限无关）：党员/预备党员/共青团员',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    index idx_orgId (orgId),
    index idx_userRole (userRole),
    index idx_userType (userType)
) comment '用户表' collate = utf8mb4_unicode_ci;

# 党组织表
create table if not exists organization
(
    id           bigint auto_increment comment 'id' primary key,
    orgName      varchar(256)                           not null comment '组织名称',
    orgCode      varchar(64)                            not null comment '组织编码',
    parentId     bigint                                 null comment '父组织ID',
    orgType      varchar(64)                            not null comment '组织类型：党委/党总支/党支部',
    orgLevel     int                                    not null comment '组织级别',
    leaderId     bigint                                 null comment '负责人ID',
    address      varchar(512)                           null comment '地址',
    description  varchar(1024)                          null comment '组织描述',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    unique index uk_orgCode (orgCode),
    index idx_parentId (parentId),
    index idx_orgType (orgType)
) comment '党组织表' collate = utf8mb4_unicode_ci;



# 组织关系转移表
create table if not exists org_relation_transfer
(
    id                bigint auto_increment comment 'id' primary key,
    userId            bigint                                 not null comment '用户ID（党员ID）',
    fromOrgId         bigint                                 not null comment '原党组织ID',
    fromOrgName       varchar(256)                           null comment '原党组织名称',
    toOrgId           bigint                                 not null comment '目标党组织ID',
    toOrgName         varchar(256)                           null comment '目标党组织名称',
    transferReason    varchar(1024)                          null comment '转移原因',
    transferTime      datetime                               not null comment '转移时间',
    approveStatus     INT                                    not null comment '审批状态：1-待审批/2-已通过/3-已拒绝',
    approveUserId     bigint                                 null comment '审批人ID',
    approveUserName   varchar(256)                           null comment '审批人姓名',
    approveTime       datetime                               null comment '审批时间',
    approveComment    varchar(1024)                          null comment '审批意见',
    createTime        datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime        datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete          tinyint      default 0                 not null comment '是否删除',
    index idx_userId (userId),
    index idx_fromOrgId (fromOrgId),
    index idx_toOrgId (toOrgId),
    index idx_approveStatus (approveStatus)
) comment '组织关系转移表' collate = utf8mb4_unicode_ci;



# 培养人关联表
create table if not exists trainer_relation
(
    id                bigint auto_increment comment 'id' primary key,
    userId            bigint                                 not null comment '用户ID（被培养人）',
    userName          varchar(256)                           null comment '被培养人姓名',
    trainerId         bigint                                 not null comment '培养人ID',
    trainerName       varchar(256)                           null comment '培养人姓名',
    startDate         date                                   not null comment '开始日期',
    endDate           date                                   null comment '结束日期',
    status            INT                                    not null comment '状态：1.进行中/2.已完成/3.已终止',
    -- step              int                                    not null comment '培养阶段：1.入党积极分子 2.预备党员 3.党员',
    createTime        datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime        datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete          tinyint      default 0                 not null comment '是否删除',
    unique index uk_userId_trainerId (userId, trainerId),
    index idx_userId (userId),
    index idx_trainerId (trainerId),
    index idx_status (status)
) comment '培养人关联表' collate = utf8mb4_unicode_ci;



# 文件表
create table if not exists file
(
    id              bigint auto_increment comment 'id' primary key,
    fileName       varchar(512)                           not null comment '文件名称',
    originFileName varchar(512)                          not null comment '原始文件名',
    orgId          bigint                                 null comment '所属党组织ID',
    userId         bigint                                 null comment '上传用户ID',
    fileUrl        varchar(1024)                          not null comment '文件URL',
    fileSize       int                                 not null comment '文件大小（字节）',
    isTemplate     tinyint      default 0                 not null comment '是否为模板：0-否，1-是',
    createTime     datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime     datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete       tinyint      default 0                 not null comment '是否删除',
    index idx_org_id (orgId),
    index idx_user_id (userId),
    index idx_is_template (isTemplate)
) comment '文件表' collate = utf8mb4_unicode_ci;

# 活动表
create table if not exists activity
(
    id                bigint auto_increment comment 'id' primary key,
    activityName      varchar(512)                           not null comment '活动名称',
    orgId             bigint                                 not null comment '所属党组织ID',
    userId            bigint                                 not null comment '创建人ID',
    activityType      int                                    not null comment '活动类型:1.会议/2.志愿活动/3.学习/4.其他',
    activityContent   text                                   not null comment '活动描述',
    enrollDeadline     datetime                              not null comment '报名截止时间',    
    startTime      datetime                                  not null comment '开始时间',
    endTime        datetime                                  not null comment '结束时间',
    location          varchar(512)                           not null comment '活动地点',
    maxNum   int                                        DEFAULT 1 not null  comment '最大参与人数',
    currentNum int                                      default 0 not null comment '当前参与人数',
    status            int                                    not null comment '活动状态:1.待发布/2.已发布/3.进行中/4.已结束',
    signInType        int                                    null comment '签到方式:1.扫码签到/2.手动签到',
    QRCodeUrl         varchar(1024)                          null comment '签到二维码URL(系统生成)',
    reviewContent     text                                   null comment '活动总结',
    createTime        datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime        datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete          tinyint      default 0                 not null comment '是否删除',
    index idx_orgId (orgId),
    index idx_creatorId (userId),
    index idx_activityType (activityType),
    index idx_status (status)
) comment '活动表' collate = utf8mb4_unicode_ci;

# 活动报名记录表
create table if not exists activity_enroll
(
    id                bigint auto_increment comment 'id' primary key,
    activityId        bigint                                 not null comment '活动ID',
    userId            bigint                                 not null comment '用户ID',
    enrollTime        datetime                               not null comment '报名时间',
    participantStatus int                                    not null comment '参与状态:1.已报名/2.已取消',
    isSign            tinyint      default 0                 not null comment '是否签到:0.未签到/1.已签到',
    createTime        datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    opUserId          bigint                                 not null comment '操作人ID',
    updateTime        datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete          tinyint      default 0                 not null comment '是否删除',
    unique index uk_activityId_userId (activityId, userId),
    index idx_activityId (activityId),
    index idx_userId (userId),
    index idx_participantStatus (participantStatus)
) comment '活动报名记录表' collate = utf8mb4_unicode_ci;

# 系统公告表
create table if not exists notice
(
    id                bigint auto_increment comment 'id' primary key,
    title             varchar(512)                           not null comment '公告标题',
    content           text                                   not null comment '公告内容',
    publisherId       bigint                                 not null comment '发布人ID',
    orgId           bigint                                 null comment '所属党组织ID(null表示系统公告)',
    publishTime       datetime                               null comment '首次发布时间',
    expireTime        datetime                               null comment '过期时间',
    isTop             tinyint      default 0                 not null comment '是否置顶:0-否,1-是',
    status            tinyint      default 0                 not null comment '状态:0-草稿/1-已发布',
    createTime        datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime        datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete          tinyint      default 0                 not null comment '是否删除',
    index idx_orgId (orgId),
    index idx_publisherId (publisherId)
) comment '公告表' collate = utf8mb4_unicode_ci;


# 签到表
-- create table if not exists sign_in
-- (
--     id                bigint auto_increment comment 'id' primary key,
--     activityId        bigint                                 not null comment '活动ID',
--     userId            bigint                                 not null comment '用户ID',
--     signInTime        datetime                               not null comment '签到时间',
--     signInType        varchar(64)                            not null comment '签到方式：扫码签到/手动签到',
--     signInStatus      varchar(64)                            not null comment '签到状态：已签到/迟到/未签到',
--     createTime        datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
--     updateTime        datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
--     isDelete          tinyint      default 0                 not null comment '是否删除',
--     unique index uk_activityId_userId (activityId, userId),
--     index idx_activityId (activityId),
--     index idx_userId (userId),
--     index idx_signInStatus (signInStatus)
-- ) comment '签到表' collate = utf8mb4_unicode_ci;


# 发展阶段表
-- create table if not exists development_stage
-- (
--     id                bigint auto_increment comment 'id' primary key,
--     userId            bigint                                 not null comment '用户ID',
--     trainerId         bigint                                 not null comment '培养人ID',
--     stageName         varchar(256)                           not null comment '阶段名称：入党申请/积极分子/发展对象/预备党员/正式党员',
--     stageStartTime    date                                   not null comment '阶段开始时间',
--     stageEndTime      date                                   null comment '阶段结束时间',
--     stageStatus       varchar(64)                            not null comment '阶段状态：进行中/已完成/已终止',
--     assessmentContent text                                   null comment '考察内容',
--     assessmentResult  varchar(64)                            null comment '考察结果：合格/不合格',
--     createTime        datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
--     updateTime        datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
--     isDelete          tinyint      default 0                 not null comment '是否删除',
--     index idx_userId (userId),
--     index idx_trainerId (trainerId),
--     index idx_stageName (stageName),
--     index idx_stageStatus (stageStatus)
-- ) comment '发展阶段表' collate = utf8mb4_unicode_ci;


-- # 量化统计表
-- create table if not exists quantitative_statistics
-- (
--     id                bigint auto_increment comment 'id' primary key,
--     orgId           bigint                                 null comment '党组织ID（null表示个人统计）',
--     userId            bigint                                 null comment '用户ID（null表示组织统计）',
--     statisticType     varchar(64)                            not null comment '统计类型：活动参与率/签到率/材料完成率',
--     statisticValue    decimal(5,2)                           not null comment '统计值（百分比）',
--     statisticDate     date                                   not null comment '统计日期',
--     statisticPeriod   varchar(64)                            not null comment '统计周期：日/周/月/年',
--     activityCount     int                                    default 0 not null comment '活动总数',
--     participateCount  int                                    default 0 not null comment '参与活动数',
--     signInCount       int                                    default 0 not null comment '签到次数',
--     materialCount     int                                    default 0 not null comment '应提交材料数',
--     completeCount     int                                    default 0 not null comment '已完成材料数',
--     createTime        datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
--     updateTime        datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
--     isDelete          tinyint      default 0                 not null comment '是否删除',
--     index idx_orgId (orgId),
--     index idx_userId (userId),
--     index idx_statisticType (statisticType),
--     index idx_statisticDate (statisticDate),
--     index idx_statisticPeriod (statisticPeriod)
-- ) comment '量化统计表' collate = utf8mb4_unicode_ci;

-- # 发展材料关联表
-- create table if not exists development_material_relation
-- (
--     id                bigint auto_increment comment 'id' primary key,
--     stageId           bigint                                 not null comment '发展阶段ID',
--     materialId        bigint                                 not null comment '材料ID',
--     createTime        datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
--     updateTime        datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
--     isDelete          tinyint      default 0                 not null comment '是否删除',
--     unique index uk_stageId_materialId (stageId, materialId),
--     index idx_stageId (stageId),
--     index idx_materialId (materialId)
-- ) comment '发展材料关联表' collate = utf8mb4_unicode_ci;

-- # 角色权限表
-- create table if not exists role_permission
-- (
--     id                bigint auto_increment comment 'id' primary key,
--     roleName          varchar(256)                           not null comment '角色名称',
--     permissionCode    varchar(256)                           not null comment '权限编码',
--     createTime        datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
--     updateTime        datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
--     isDelete          tinyint      default 0                 not null comment '是否删除',
--     unique index uk_roleName_permissionCode (roleName, permissionCode),
--     index idx_roleName (roleName),
--     index idx_permissionCode (permissionCode)
-- ) comment '角色权限表' collate = utf8mb4_unicode_ci;
