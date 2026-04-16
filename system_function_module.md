# 智慧党建综合管理系统——系统功能模块文档

## 模块一：用户管理模块

### 1.1 模块概述

用户管理模块是系统的基石模块，负责全系统用户的身份认证、账户管理与权限控制。模块支持多角色体系（超级管理员、组织管理员、党员、积极分子/发展对象），通过基于角色的访问控制（RBAC）机制实现不同用户的功能权限隔离。管理员可进行用户的增删改查操作，普通用户可维护个人基本信息。

### 1.2 数据库设计

**核心数据表：user（用户表）**

| 字段名             | 类型            | 说明                                                                |
| --------------- | ------------- | ----------------------------------------------------------------- |
| id              | bigint        | 主键，自增                                                             |
| userAccount     | varchar(256)  | 登录账号                                                              |
| userPassword    | varchar(512)  | 加密密码（MD5）                                                         |
| userName        | varchar(256)  | 用户姓名                                                              |
| userAvatar      | varchar(1024) | 用户头像URL                                                           |
| userRole        | varchar(256)  | 角色编码（super\_admin/org\_admin/party\_member/activist\_development） |
| orgId           | bigint        | 所属党组织ID                                                           |
| phone           | varchar(32)   | 手机号                                                               |
| email           | varchar(256)  | 邮箱                                                                |
| userType        | varchar(64)   | 用户类型（教师/学生）                                                       |
| status          | tinyint       | 状态（0-正常，1-禁用）                                                     |
| joinDate        | date          | 申请入党日期                                                            |
| positiveDate    | date          | 转正日期                                                              |
| politicalStatus | varchar(64)   | 政治面貌（党员/预备党员/共青团员）                                                |

### 1.3 后端代码结构

**Controller层：** `UserController.java`

- 路径前缀：`/user`
- 核心接口：
  - `POST /login` — 用户登录，返回`LoginUserVO`
  - `POST /logout` — 用户注销
  - `GET /get/login` — 获取当前登录用户信息
  - `POST /add` — 创建用户（需super\_admin或org\_admin角色）
  - `POST /delete` — 删除用户（需管理员角色）
  - `POST /update` — 更新用户信息（需管理员角色）
  - `GET /get` — 根据ID获取用户详情
  - `POST /list/page` — 分页获取用户列表
  - `POST /list/page/vo` — 分页获取用户VO封装列表
  - `POST /update/my` — 更新个人信息
  - `GET /list/role` — 获取所有角色列表

**Service层：** `UserService.java` / `UserServiceImpl.java`

- 核心方法：`userLogin()`、`userLogout()`、`getLoginUser()`、`userRegister()`、`getUserVO()`、`existsByUserAccount()`

**Mapper层：** `UserMapper.java` + `UserMapper.xml`

- 继承MyBatis-Plus的`BaseMapper<User>`

**数据模型：**

- Entity：`User.java`
- DTO：`UserRegisterRequest`、`UserLoginRequest`、`UserAddRequest`、`UserUpdateRequest`、`UserUpdateMyRequest`、`UserQueryRequest`
- VO：`LoginUserVO`、`UserVO`
- Enum：`UserRoleEnum`（定义角色映射关系）

**安全机制：**

- 注解：`@AuthCheck(mustRole = {...})` 用于接口级权限控制
- 密码加密：`EncryptUtils.encryptPassword()` 使用MD5加密
- 默认密码：新建用户默认密码为"123456"

### 1.4 前端代码结构

**API定义：** `api/yonghuguanli.ts`

- 封装所有用户相关API调用

**页面视图：**

- `views/user/UserLoginView.vue` — 登录页面，包含账号密码表单及登录逻辑

**状态管理：**

- `store/user.ts` — Vuex用户状态（登录状态、当前用户信息等）

**权限控制：**

- `access/checkAccess.ts` — 前端路由级权限校验
- `access/accessEnum.ts` — 权限枚举定义

***

## 模块二：党组织管理模块

### 2.1 模块概述

党组织管理模块负责维护系统内完整的党组织架构体系。支持树形层级结构（党委→党总支→党支部），提供组织的创建、编辑、删除、查询功能。模块同时包含组织负责人绑定、组织成员管理以及党员组织关系转移审批流程。

### 2.2 数据库设计

**核心数据表：**

**organization（党组织表）**

| 字段名         | 类型            | 说明               |
| ----------- | ------------- | ---------------- |
| id          | bigint        | 主键，自增            |
| orgName     | varchar(256)  | 组织名称             |
| orgCode     | varchar(64)   | 组织编码（唯一）         |
| parentId    | bigint        | 父组织ID（0表示顶级）     |
| orgType     | varchar(64)   | 组织类型（党委/党总支/党支部） |
| orgLevel    | int           | 组织级别             |
| leaderId    | bigint        | 负责人ID            |
| address     | varchar(512)  | 地址               |
| description | varchar(1024) | 组织描述             |

**org\_relation\_transfer（组织关系转移表）**

| 字段名            | 类型            | 说明                      |
| -------------- | ------------- | ----------------------- |
| id             | bigint        | 主键                      |
| userId         | bigint        | 用户ID（党员ID）              |
| fromOrgId      | bigint        | 原党组织ID                  |
| toOrgId        | bigint        | 目标党组织ID                 |
| transferReason | varchar(1024) | 转移原因                    |
| transferTime   | datetime      | 转移时间                    |
| approveStatus  | int           | 审批状态（1-待审批/2-已通过/3-已拒绝） |
| approveUserId  | bigint        | 审批人ID                   |
| approveComment | varchar(1024) | 审批意见                    |

### 2.3 后端代码结构

**Controller层：**

- `OrganizationController.java` — 路径前缀 `/organization`
  - `POST /add` — 创建党组织
  - `POST /update` — 更新党组织信息
  - `POST /delete` — 删除党组织
  - `GET /get` — 根据ID查询党组织（返回`OrganizationVO`含负责人信息）
  - `GET /list` — 查询所有党组织
  - `POST /list/page` — 分页查询党组织
  - `POST /bind` — 绑定组织负责人
  - `GET /get/graded` — 获取党组织及其子党组织树形结构
- `OrgRelationTransferController.java` — 路径前缀 `/orgRelationTransfer`
  - 组织关系转移的申请与审批接口
- `OrgMemberController.java` — 路径前缀 `/orgMember`
  - 组织成员管理接口

**Service层：**

- `OrganizationService.java` — 核心方法：`createOrganization()`、`updateOrganization()`、`deleteOrganization()`、`fillOrganizationLeader()`、`getSubGradedOrgs()`、`bindOrganizationLeader()`
- `OrgRelationTransferService.java` — 组织关系转移业务逻辑
- `OrgMemberService.java` — 组织成员管理业务逻辑

**Mapper层：** `OrganizationMapper.java` + `OrganizationMapper.xml`、`OrgRelationTransferMapper.java` + `OrgRelationTransferMapper.xml`

**数据模型：**

- Entity：`Organization.java`、`OrgRelationTransfer.java`
- DTO：`OrganizationAddRequest`、`OrganizationUpdateRequest`、`OrganizationQueryRequest`、`OrganizationBindRequest`
- VO：`OrganizationVO`（含负责人姓名填充）、`OrganizationGradedVO`（树形结构）

### 2.4 前端代码结构

**API定义：**

- `api/dangzuzhiguanli.ts` — 党组织管理API
- `api/zuzhiguanxizhuanyiguanli.ts` — 组织关系转移API
- `api/zuzhichengyuanguanli.ts` — 组织成员API

**页面视图：**

- `views/organization/OrganizationView.vue` — 党组织管理主页面（树形展示+CRUD操作）

***

## 模块三：发展阶段模块

### 3.1 模块概述

发展阶段模块是本系统的核心特色模块，用于跟踪和管理党员从入党积极分子到正式党员的全流程发展轨迹。模块记录每个发展阶段的时间节点、考察内容与结果，支持培养人关联绑定，并提供完整的审核流程与操作日志追溯能力。

### 3.2 数据库设计

**核心数据表：**

**development\_stage（发展阶段表）**

| 字段名               | 类型           | 说明                        |
| ----------------- | ------------ | ------------------------- |
| id                | bigint       | 主键                        |
| userId            | bigint       | 用户ID                      |
| trainerId         | bigint       | 培养人ID                     |
| stageName         | varchar(256) | 阶段名称（积极分子/发展对象/预备党员/正式党员） |
| stageStartTime    | date         | 阶段开始时间                    |
| stageEndTime      | date         | 阶段结束时间                    |
| stageStatus       | tinyint      | 阶段状态（0-进行中/1-已完成/2-已终止）   |
| assessmentContent | text         | 考察内容                      |
| assessmentResult  | tinyint      | 考察结果（1-合格/0-不合格/2-未审核）    |
| auditUserId       | bigint       | 审核人员ID                    |
| auditTime         | datetime     | 审核时间                      |
| auditRemark       | text         | 审核意见                      |

**development\_stage\_log（发展阶段变更日志表）**

| 字段名           | 类型           | 说明                     |
| ------------- | ------------ | ---------------------- |
| id            | bigint       | 主键                     |
| stageId       | bigint       | 发展阶段记录ID               |
| userId        | bigint       | 用户ID                   |
| operationType | varchar(64)  | 操作类型（创建/更新/提交审核/审核/删除） |
| operatorId    | bigint       | 操作人ID                  |
| operatorName  | varchar(128) | 操作人姓名                  |
| beforeData    | text         | 变更前数据（JSON格式）          |
| afterData     | text         | 变更后数据（JSON格式）          |
| remark        | text         | 备注说明                   |

**trainer\_relation（培养人关联表）**

| 字段名         | 类型           | 说明                    |
| ----------- | ------------ | --------------------- |
| id          | bigint       | 主键                    |
| userId      | bigint       | 被培养人ID                |
| userName    | varchar(256) | 被培养人姓名                |
| trainerId   | bigint       | 培养人ID                 |
| trainerName | varchar(256) | 培养人姓名                 |
| startDate   | date         | 开始日期                  |
| endDate     | date         | 结束日期                  |
| status      | int          | 状态（1-进行中/2-已完成/3-已终止） |

### 3.3 后端代码结构

**Controller层：** `DevelopmentStageController.java`

- 路径前缀：`/developmentStage`
- 核心接口：
  - `POST /add` — 添加发展阶段记录（带`@DevelopmentStageLogAnnotation`日志注解）
  - `POST /update` — 更新发展阶段记录（带日志注解）
  - `POST /delete` — 删除发展阶段记录（带日志注解）
  - `GET /get` — 根据ID查询发展阶段记录
  - `GET /list/byUserId` — 根据用户ID获取发展阶段列表
  - `GET /list/byOrgId` — 根据组织ID获取发展阶段列表
  - `POST /submitForAudit` — 提交审核（带日志注解）
  - `POST /audit` — 审核发展阶段记录（带日志注解）
  - `POST /list` — 条件查询发展阶段列表

**TrainerRelationController.java** — 路径前缀 `/trainerRelation`

- 培养人关联的CRUD操作接口

**Service层：**

- `DevelopmentStageService.java` — 核心方法：`addDevelopmentStage()`、`updateDevelopmentStage()`、`deleteDevelopmentStage()`、`getDevelopmentStagesByUserId()`、`getDevelopmentStagesByOrgId()`、`submitForAudit()`、`auditDevelopmentStage()`
- `DevelopmentStageLogService.java` — 日志记录服务
- `TrainerRelationService.java` — 培养人关联服务

**AOP切面机制：**

- 注解：`@DevelopmentStageLogAnnotation(operationType, description)`
- 切面：`DevelopmentStageLogAspect.java` — 自动拦截标注了该注解的方法，将操作记录写入`development_stage_log`表
- 枚举：`DevelopmentStageOperationEnum`（CREATE/UPDATE/SUBMIT\_AUDIT/AUDIT/DELETE）

**Mapper层：** `DevelopmentStageMapper.java` + `DevelopmentStageMapper.xml`、`DevelopmentStageLogMapper.java` + `DevelopmentStageLogMapper.xml`、`TrainerRelationMapper.java` + `TrainerRelationMapper.xml`

**数据模型：**

- Entity：`DevelopmentStage.java`、`DevelopmentStageLog.java`、`TrainerRelation.java`
- DTO：`DevelopmentStageAddRequest`、`DevelopmentStageUpdateRequest`、`DevelopmentStageQueryRequest`
- VO：`DevelopmentStageVO`

### 3.4 前端代码结构

**API定义：**

- `api/fazhanjieduanguanli.ts` — 发展阶段管理API
- `api/fazhanjieduanbiangengrizhi.ts` — 发展阶段变更日志API
- `api/peiyangrenguanlianguanli.ts` — 培养人关联API

**页面视图：**

- `views/trainerRelation/TrainerRelationView.vue` — 发展阶段管理主页面（包含发展流程展示、培养人绑定、阶段审核等功能）

***

## 模块四：活动管理模块

### 4.1 模块概述

活动管理模块支撑党建活动的完整生命周期管理，包括会议、志愿活动、学习培训等多种类型活动的创建、发布、报名、签到和总结回顾。模块支持两种签到方式（扫码签到和手动签到），并能自动统计活动的参与率和签到率数据，为量化统计提供基础数据支撑。

### 4.2 数据库设计

**核心数据表：**

**activity（活动表）**

| 字段名               | 类型            | 说明                            |
| ----------------- | ------------- | ----------------------------- |
| id                | bigint        | 主键                            |
| activityName      | varchar(512)  | 活动名称                          |
| orgId             | bigint        | 所属党组织ID                       |
| userId            | bigint        | 创建人ID                         |
| activityType      | int           | 活动类型（1-会议/2-志愿活动/3-学习/4-其他）   |
| activityContent   | text          | 活动描述                          |
| enrollDeadline    | datetime      | 报名截止时间                        |
| startTime         | datetime      | 开始时间                          |
| endTime           | datetime      | 结束时间                          |
| location          | varchar(512)  | 活动地点                          |
| maxNum            | int           | 最大参与人数                        |
| currentNum        | int           | 当前参与人数                        |
| totalParticipant  | int           | 总报名人数                         |
| actualParticipant | int           | 实际参与人数（已报名且签到）                |
| signRate          | double        | 签到率（实际参与人数/总参与人数）             |
| status            | int           | 活动状态（1-待发布/2-已发布/3-进行中/4-已结束） |
| signInType        | int           | 签到方式（1-扫码签到/2-手动签到）           |
| QRCodeUrl         | varchar(1024) | 签到二维码URL                      |
| reviewContent     | text          | 活动总结                          |

**activity\_enroll（活动报名记录表）**

| 字段名               | 类型       | 说明                |
| ----------------- | -------- | ----------------- |
| id                | bigint   | 主键                |
| activityId        | bigint   | 活动ID              |
| userId            | bigint   | 用户ID              |
| enrollTime        | datetime | 报名时间              |
| participantStatus | int      | 参与状态（1-已报名/2-已取消） |
| isSign            | tinyint  | 是否签到（0-未签到/1-已签到） |

### 4.3 后端代码结构

**Controller层：**

- `ActivityController.java` — 路径前缀 `/activity`
  - `POST /add` — 添加活动（需管理员角色）
  - `POST /update` — 更新活动（需管理员角色）
  - `POST /delete` — 删除活动（需管理员角色）
  - `GET /get` — 获取活动详情（返回`ActivityVO`）
  - `POST /list/page` — 分页获取活动列表
  - `POST /list/page/vo` — 分页获取活动VO列表
  - `POST /publish` — 发布活动（需管理员角色）
  - `POST /end` — 结束活动（需管理员角色）
  - `POST /statistics/update` — 更新指定活动的统计数据
  - `POST /statistics/update/all` — 更新所有活动的统计数据
- `ActivityEnrollController.java` — 路径前缀 `/activityEnroll`
  - 活动报名、取消报名、签到等操作接口

**Service层：**

- `ActivityService.java` — 核心方法：`addActivity()`、`updateActivity()`、`deleteActivity()`、`getActivityVO()`、`listActivityVO()`、`publishActivity()`、`endActivity()`、`updateActivityStatisticsById()`、`updateAllActivityStatistics()`
- `ActivityEnrollService.java` — 报名与签到业务逻辑

**Mapper层：** `ActivityMapper.java` + `ActivityMapper.xml`、`ActivityEnrollMapper.java` + `ActivityEnrollMapper.xml`

**数据模型：**

- Entity：`Activity.java`、`ActivityEnroll.java`
- DTO：`ActivityAddRequest`、`ActivityUpdateRequest`、`ActivityQueryRequest`
- VO：`ActivityVO`

**常量定义：** `ActivityConstant.java` — 定义活动类型、状态等常量

### 4.4 前端代码结构

**API定义：**

- `api/huodongguanli.ts` — 活动管理API
- `api/huodongbaomingguanli.ts` — 活动报名API

**页面视图：**

- `views/activity/ActivityView.vue` — 活动管理主页面（活动列表、创建/编辑表单、活动发布、数据统计）

***

## 模块五：材料管理模块

### 5.1 模块概述

材料管理模块实现党员发展过程中各类材料的全生命周期管理。超级管理员可上传和维护各发展阶段的材料模板；入党积极分子/发展对象可按模板要求提交个人材料；支部管理员和超级管理员分别执行初审和终审两级审核流程；审核通过的材料自动归档，支持后续的归档查询。

### 5.2 数据库设计

**核心数据表：**

**material\_template（材料模板表）**

| 字段名                | 类型           | 说明                         |
| ------------------ | ------------ | -------------------------- |
| id                 | bigint       | 主键                         |
| name               | varchar(255) | 模板名称                       |
| stage              | varchar(50)  | 所属发展阶段                     |
| type               | varchar(20)  | 材料类型                       |
| status             | varchar(20)  | 模板状态（enable-启用/disable-停用） |
| file\_url          | varchar(500) | 模板文件路径                     |
| file\_size         | bigint       | 文件大小（字节）                   |
| upload\_user\_id   | bigint       | 上传用户ID                     |
| upload\_user\_name | varchar(100) | 上传用户姓名                     |
| remark             | varchar(500) | 模板说明                       |

**material\_submission（材料提交表）**

| 字段名                   | 类型           | 说明                                                                     |
| --------------------- | ------------ | ---------------------------------------------------------------------- |
| id                    | bigint       | 主键                                                                     |
| user\_id              | bigint       | 用户ID                                                                   |
| user\_name            | varchar(100) | 用户姓名                                                                   |
| org\_level            | varchar(50)  | 组织层级                                                                   |
| stage                 | varchar(50)  | 发展阶段                                                                   |
| material\_name        | varchar(255) | 材料名称                                                                   |
| submit\_status        | varchar(20)  | 提交状态（unsubmitted/submitted/approved/final\_approved/rejected/archived） |
| audit\_status         | varchar(20)  | 审核状态（pending/auditing/approved/rejected）                               |
| file\_url             | varchar(500) | 材料文件路径                                                                 |
| auditor               | varchar(100) | 审核人                                                                    |
| audit\_opinion        | varchar(500) | 审核意见                                                                   |
| final\_auditor        | varchar(100) | 终审人                                                                    |
| final\_audit\_opinion | varchar(500) | 终审意见                                                                   |

**material\_archive（材料归档表）**

| 字段名             | 类型           | 说明     |
| --------------- | ------------ | ------ |
| id              | bigint       | 主键     |
| submission\_id  | bigint       | 提交ID   |
| user\_id        | bigint       | 用户ID   |
| material\_name  | varchar(255) | 材料名称   |
| file\_url       | varchar(500) | 归档文件路径 |
| archive\_user   | varchar(100) | 归档人    |
| archive\_time   | datetime     | 归档时间   |
| archive\_remark | varchar(500) | 归档备注   |

**material\_audit\_log（材料审核日志表）**

| 字段名             | 类型           | 说明                             |
| --------------- | ------------ | ------------------------------ |
| id              | bigint       | 主键                             |
| submission\_id  | bigint       | 提交ID                           |
| audit\_user\_id | bigint       | 审核用户ID                         |
| audit\_type     | varchar(20)  | 审核类型（audit-初审/final\_audit-终审） |
| audit\_result   | varchar(20)  | 审核结果（approved/rejected）        |
| audit\_opinion  | varchar(500) | 审核意见                           |
| audit\_time     | datetime     | 审核时间                           |

### 5.3 后端代码结构

**Controller层：**

- `MaterialTemplateController.java` — 路径前缀 `/material/template`
  - 材料模板的上传、启停用、列表查询等接口
- `MaterialSubmissionController.java` — 路径前缀 `/api/material/submission`
  - `GET /list` — 获取材料提交列表
  - `POST /submit` — 提交材料
  - `PUT /audit` — 审核材料（初审，需管理员角色）
  - `PUT /reject` — 退回材料（需管理员角色）
  - `PUT /finalAudit` — 终审材料（仅超级管理员）
  - `GET /view` — 查看材料详情
  - `GET /download` — 下载材料文件
  - `GET /todoCount` — 获取待提交和待审核数量
- `MaterialArchiveController.java` — 路径前缀 `/material/archive`
  - 材料归档查询相关接口

**Service层：**

- `MaterialTemplateService.java` — 模板管理服务
- `MaterialSubmissionService.java` — 核心方法：`submitMaterial()`、`auditMaterial()`、`rejectMaterial()`、`finalAuditMaterial()`、`getMaterialSubmissionVO()`、`getMaterialTodoCount()`
- `MaterialArchiveService.java` — 归档查询服务
- `MaterialAuditLogService.java` — 审核日志服务

**Mapper层：** `MaterialTemplateMapper.java`、`MaterialSubmissionMapper.java`、`MaterialArchiveMapper.java`、`MaterialAuditLogMapper.java`

**数据模型：**

- DTO（material目录下）：`MaterialSubmissionSubmitRequest`、`MaterialSubmissionAuditRequest`、`MaterialSubmissionFinalAuditRequest`、`MaterialSubmissionQueryRequest`
- VO：`MaterialSubmissionVO`、`MaterialTodoCountVO`

### 5.4 前端代码结构

**API定义：** `api/wenjianguanli.ts` — 文件/材料管理API

**页面视图：**

- `views/material/MaterialTemplateManagementView.vue` — 材料模板管理页面（模板上传、启停用管理）
- `views/material/MaterialSubmissionReviewView.vue` — 材料提交与审核页面（材料上传、初审/终审操作）
- `views/material/MaterialArchivingQueryView.vue` — 材料归档查询页面（已归档材料的检索与查看）

***

## 模块六：公告管理模块

### 6.1 模块概述

公告管理模块为系统提供信息发布与通知推送能力。支持超级管理员发布系统级公告、党支部管理员发布支部公告。提供公告的发布、撤回、置顶、下架、重新发布等完整生命周期管理，以及已读标记和未读数量统计功能，确保重要信息能及时触达目标用户。

### 6.2 数据库设计

**核心数据表：notice（公告表）**

| 字段名         | 类型           | 说明                  |
| ----------- | ------------ | ------------------- |
| id          | bigint       | 主键                  |
| title       | varchar(512) | 公告标题                |
| content     | text         | 公告内容                |
| publisherId | bigint       | 发布人ID               |
| orgId       | bigint       | 所属党组织ID（null表示系统公告） |
| publishTime | datetime     | 首次发布时间              |
| expireTime  | datetime     | 过期时间                |
| isTop       | tinyint      | 是否置顶（0-否/1-是）       |
| status      | tinyint      | 状态（0-草稿/1-已发布）      |

**扩展表（notice\_module\_extend.sql）：**

- `announcement_attachment` — 公告附件表
- `announcement_read_record` — 公告已读记录表
- `announcement_share` — 公告分享记录表

### 6.3 后端代码结构

**Controller层：** `NoticeController.java`

- 路径前缀：`/notice`
- 核心接口：
  - `POST /add` — 添加公告（需管理员角色）
  - `POST /update` — 更新公告（需管理员角色）
  - `POST /delete` — 删除公告（需管理员角色）
  - `GET /get` — 获取公告详情（返回`NoticeVO`）
  - `GET /list/page` — 分页获取公告列表
  - `POST /publish` — 发布公告（需管理员角色）
  - `POST /withdraw` — 撤回公告（需管理员角色）
  - `POST /batchDelete` — 批量删除公告（需管理员角色）
  - `PUT /offline` — 下架公告（需管理员角色）
  - `PUT /republish` — 重新发布公告（需管理员角色）
  - `PUT /top` — 设置公告置顶状态（需管理员角色）
  - `POST /markRead` — 标记公告为已读
  - `GET /unreadCount` — 获取未读公告数量
- `AnnouncementExtensionController.java` — 扩展功能控制器
  - 公告附件、已读记录、分享等扩展接口

**Service层：**

- `NoticeService.java` — 核心方法：`addNotice()`、`updateNotice()`、`deleteNotice()`、`getNoticeVO()`、`listNoticeVO()`、`publishNotice()`、`withdrawNotice()`、`batchDeleteNotices()`、`offlineNotice()`、`republishNotice()`、`topNotice()`、`markAsRead()`、`getUnreadCount()`
- `AnnouncementAttachmentService.java` — 附件管理服务
- `AnnouncementReadRecordService.java` — 已读记录服务
- `AnnouncementShareService.java` — 分享记录服务

**Mapper层：** `NoticeMapper.java` + `NoticeMapper.xml`、`AnnouncementAttachmentMapper.java`、`AnnouncementReadRecordMapper.java`、`AnnouncementShareMapper.java`

**数据模型：**

- Entity：`Notice.java`
- DTO：`NoticeAddRequest`、`NoticeUpdateRequest`、`NoticeQueryRequest`
- VO：`NoticeVO`

### 6.4 前端代码结构

**API定义：** `api/gonggaoguanli.ts` — 公告管理API

**页面视图：**

- `views/notice/AnnouncementManagementView.vue` — 公告管理页面（发布公告、编辑、状态管理）
- `views/notice/AnnouncementListView.vue` — 公告列表页面（用户视角的公告浏览）
- `views/notice/AnnouncementDetailsView.vue` — 公告详情页面（富文本内容展示、附件下载）

***

## 模块七：量化统计模块

### 7.1 模块概述

量化统计模块是系统的数据分析中心，自动从活动管理和材料管理等模块汇聚基础数据，按照预定义的统计指标生成组织和个人的党建工作量化报表。模块支持从活动参与率、签到率、材料完成率等多维度对党组织和个人进行量化评估，并以图表形式直观呈现，为党建工作的科学决策提供数据支撑。

### 7.2 数据库设计

**核心数据表：**

**user\_quantify（用户量化统计表）**

| 字段名                 | 类型     | 说明     |
| ------------------- | ------ | ------ |
| id                  | bigint | 主键     |
| userId              | bigint | 用户ID   |
| statDate            | date   | 统计日期   |
| totalActivity       | int    | 总活动数   |
| participateActivity | int    | 报名活动数  |
| participateRate     | double | 活动参与度  |
| signActivity        | int    | 签到活动数  |
| signRate            | double | 签到率    |
| fileCount           | int    | 材料文件数量 |

**org\_quantify（组织量化统计表）**

| 字段名              | 类型     | 说明         |
| ---------------- | ------ | ---------- |
| id               | bigint | 主键         |
| orgId            | bigint | 组织ID       |
| statDate         | date   | 统计日期       |
| activityCount    | int    | 活动次数       |
| totalParticipant | int    | 总报名人数      |
| totalSign        | int    | 总签到人数      |
| avgParticipant   | double | 平均每次活动报名人数 |
| avgSign          | double | 平均每次活动签到人数 |

**quantify\_indicator（量化指标表）**

| 字段名        | 类型           | 说明                               |
| ---------- | ------------ | -------------------------------- |
| id         | bigint       | 主键（指标ID）                         |
| name       | varchar(256) | 指标名称                             |
| rule       | varchar(512) | 统计规则                             |
| dimension  | varchar(64)  | 统计维度（organization/personal/both） |
| org\_level | varchar(512) | 适用组织层级（JSON格式）                   |
| status     | varchar(32)  | 状态（enable/disable）               |

**quantify\_data（量化数据记录表）**

| 字段名            | 类型            | 说明                      |
| -------------- | ------------- | ----------------------- |
| id             | bigint        | 主键（数据ID）                |
| indicator\_id  | bigint        | 指标ID                    |
| target\_id     | bigint        | 统计对象ID                  |
| target\_type   | varchar(32)   | 对象类型（organization/user） |
| period         | varchar(32)   | 统计周期                    |
| value          | decimal(10,2) | 统计值                     |
| activity\_rate | decimal(5,2)  | 活动参与率                   |
| sign\_rate     | decimal(5,2)  | 签到率                     |
| material\_rate | decimal(5,2)  | 材料完成率                   |

### 7.3 后端代码结构

**Controller层：**

- `QuantifyController.java` — 路径前缀 `/quantify`（基础量化统计）
  - `POST /user/generate/{userId}` — 生成指定用户量化数据
  - `POST /user/generate/all` — 生成所有用户量化数据
  - `GET /user/{userId}` — 获取指定用户量化数据
  - `POST /org/generate/{orgId}` — 生成指定组织量化数据
  - `POST /org/generate/all` — 生成所有组织量化数据
  - `GET /org/{orgId}` — 获取指定组织量化数据
  - `POST /generate/all` — 生成全部量化数据
- `QuantifyDataController.java` — 路径前缀 `/quantify/data`（扩展量化数据）
  - 量化数据的CRUD与查询接口
- `QuantifyIndicatorController.java` — 路径前缀 `/quantify/indicator`
  - 量化指标的配置管理接口

**Service层：**

- `UserQuantifyService.java` — 核心方法：`generateUserQuantifyData()`、`generateAllUserQuantifyData()`
- `OrgQuantifyService.java` — 核心方法：`generateOrgQuantifyData()`、`generateAllOrgQuantifyData()`
- `QuantifyDataService.java` — 量化数据记录服务
- `QuantifyIndicatorService.java` — 量化指标配置服务

**定时任务：** `QuantifyScheduledTask.java`

- 定时触发量化数据的自动计算与更新

**Mapper层：** `UserQuantifyMapper.java` + `UserQuantifyMapper.xml`、`OrgQuantifyMapper.java` + `OrgQuantifyMapper.xml`、`QuantifyDataMapper.java`、`QuantifyIndicatorMapper.java`

**数据模型：**

- Entity：`UserQuantify.java`、`OrgQuantify.java`、`QuantifyData.java`、`QuantifyIndicator.java`

### 7.4 前端代码结构

**API定义：** `api/lianghuatongji.ts` — 量化统计API

**页面视图：**

- `views/quantify/QuantifyManagerView.vue` — 量化统计管理页面（管理员视角，组织/个人数据报表、ECharts图表展示）
- `views/quantify/QuantifyView.vue` — 个人量化统计页面（普通用户视角，查看个人参与率、签到率等数据）

**图表技术：** 使用 ECharts 6.0.0 进行数据可视化展示（柱状图、折线图、饼图等）

***

## 八、系统技术架构总结

### 8.1 整体架构

本系统采用经典的前后端分离架构模式：

```
┌─────────────────────────────────────────────┐
│              前端表示层 (Vue 3)               │
│    Ant Design Vue + ECharts + TypeScript     │
└──────────────────┬──────────────────────────┘
                   │ HTTP/RESTful API (JSON)
                   ▼
┌─────────────────────────────────────────────┐
│           后端业务层 (Spring Boot)            │
│   Controller → Service → Mapper → MySQL      │
└──────────────────┬──────────────────────────┘
                   │
          ┌────────┼────────┐
          ▼        ▼        ▼
    ┌──────────┐ ┌──────┐ ┌──────────┐
    │  MySQL   │ │ Redis │ │ 腾讯云COS │
    │ (数据存储)│ │(缓存/会话)│(文件存储)│
    └──────────┘ └──────┘ └──────────┘
```

### 8.2 分层设计

- **表现层（前端）：** Vue 3组件化开发，使用Vuex进行状态管理，Vue Router管理路由，Axios处理HTTP请求，ECharts负责数据可视化
- **接口层（Controller）：** RESTful风格API设计，使用Swagger/Knife4j生成接口文档，`@AuthCheck`注解实现接口级权限控制
- **业务层（Service）：** 封装核心业务逻辑，事务管理在此层控制
- **持久层（Mapper）：** 基于MyBatis-Plus实现ORM映射，支持条件构造器与分页插件
- **交叉关注点：** 通过Spring AOP实现权限拦截（`AuthInterceptor`）、操作日志记录（`DevelopmentStageLogAspect`）

### 8.3 设计模式应用

- **MVC模式：** Controller-Service-Mapper三层分离
- **DTO模式：** 使用独立的请求/响应对象进行数据传输，与Entity解耦
- **VO模式：** 视图对象封装前端展示所需的数据格式
- **策略模式：** `UserRoleEnum`角色枚举映射不同权限策略
- **模板方法模式：** MyBatis-Plus的`BaseMapper`提供通用CRUD模板
- **观察者模式：** Spring事件机制可用于跨模块通知（如量化统计定时任务）

## 九、开发规范与最佳实践

### 9.1 编码规范

- 所有Controller类添加`@RestController`和`@RequestMapping`注解
- 接口方法使用`@ApiOperation`标注Swagger文档说明
- 管理员接口使用`@AuthCheck(mustRole = {...})`进行权限控制
- Service层接口与实现类分开命名（XxxService / XxxServiceImpl）
- DTO按模块分子目录组织（如`dto/user/`、`dto/activity/`）
- 统一使用`ResultUtils.success()`包装成功响应，`throw new BusinessException()`抛出业务异常

### 9.2 API设计规范

- 使用RESTful风格的URL命名（名词复数形式）
- POST用于创建/修改操作，GET用于查询，DELETE用于删除
- 分页查询统一使用`PageRequest`基类的current/pageSize参数
- 删除操作统一使用`DeleteRequest{id}`请求体
- 所有返回值使用`BaseResponse<T>`泛型包装

### 9.3 安全规范

- 用户密码使用`EncryptUtils.encryptPassword()`加密后存储
- 敏感操作需校验用户登录状态（`userService.getLoginUser(request)`）
- 文件上传通过腾讯云COS管理，不直接存储在本地服务器
- 接口权限通过AOP切面统一校验，避免硬编码判断
- SQL注入防护依赖MyBatis-Plus的参数化查询机制

## 十、测试建议

### 10.1 单元测试

- 重点测试Service层的业务逻辑（如量化统计计算、审核流程状态流转）
- 测试工具类（`EncryptUtils`密码加密、`SqlUtils`工具方法）
- 已有测试文件参考：`UserServiceTest.java`、`EasyExcelTest.java`、`CosManagerTest.java`

### 10.2 接口测试

- 使用Knife4j（Swagger UI）进行接口联调测试
- 测试重点场景：用户登录鉴权、权限边界验证、审核流程完整性
- 关注点：不同角色访问同一接口的权限差异

### 10.3 集成测试场景

1. **用户管理流程：** 管理员创建用户 → 用户登录 → 修改个人信息
2. **党员发展流程：** 创建发展阶段记录 → 提交审核 → 审核通过 → 进入下一阶段
3. **活动管理流程：** 创建活动 → 发布 → 用户报名 → 签到 → 结束活动 → 统计更新
4. **材料管理流程：** 上传模板 → 用户提交材料 → 初审 → 终审 → 归档
5. **组织关系转移：** 发起转移申请 → 管理员审批 → 组织关系变更
6. **量化统计联动：** 活动/材料数据变化后触发量化数据重算

