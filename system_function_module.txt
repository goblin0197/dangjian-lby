# 智慧党建管理系统——系统功能模块文档

---

## 模块一：用户管理模块

### 1.1 模块概述

用户管理模块是系统的基础权限与身份管理核心，负责用户的注册（管理员添加）、登录认证、注销、信息维护及角色权限控制。系统定义了五类用户角色：超级管理员（super_admin）、组织管理员（org_admin）、党员（org_member/activist_development）、积极分子等，不同角色拥有差异化的功能操作权限。

### 1.2 数据库设计

**表名：user**

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | bigint | 主键自增 |
| userAccount | varchar(256) | 登录账号 |
| userPassword | varchar(512) | 加密密码（MD5+盐） |
| userName | varchar(256) | 用户姓名 |
| userAvatar | varchar(1024) | 头像地址 |
| userRole | varchar(256) | 角色标识 |
| orgId | bigint | 所属党组织ID |
| phone | varchar(32) | 手机号 |
| email | varchar(256) | 邮箱 |
| userType | varchar(64) | 用户类型（教师/学生） |
| status | tinyint | 状态（0正常/1禁用） |
| joinDate | date | 申请入党日期 |
| positiveDate | date | 转正日期 |
| politicalStatus | varchar(64) | 政治面貌 |

### 1.3 后端代码结构

**Controller层**：UserController.java
- `POST /user/login` — 用户登录
- `POST /user/logout` — 用户注销
- `GET /user/get/login` — 获取当前登录用户
- `POST /user/add` — 创建用户（管理员）
- `POST /user/delete` — 删除用户（管理员）
- `POST /user/update` — 更新用户（管理员）
- `POST /user/update/my` — 更新个人信息
- `GET /user/list/role` — 获取所有角色列表
- `POST /user/list/page` — 分页查询用户
- `POST /user/list/page/vo` — 分页查询用户VO

**Service层**：UserService.java / UserServiceImpl.java
- 核心方法：userLogin()、userLogout()、getLoginUser()、getUserVO()、existsByUserAccount()
- 密码加密：EncryptUtils.encryptPassword()

**安全机制**：
- @AuthCheck注解实现接口级角色鉴权
- AuthInterceptor切面拦截请求进行权限校验
- Spring Session + Redis实现分布式会话管理

### 1.4 前端代码结构

**视图文件**：views/user/UserLoginView.vue
**API接口**：api/yonghuguanli.ts
**状态管理**：store/user.ts（维护登录态）

---

## 模块二：党组织管理模块

### 2.1 模块概述

党组织管理模块负责维护系统中党组织的层级体系结构，支持党委→党总支→党支部的三级树形组织架构。提供组织的增删改查、负责人绑定、树形层级展示等功能，为其他模块（活动、公告、量化统计等）提供组织维度的数据基础。

### 2.2 数据库设计

**表名：organization**

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | bigint | 主键自增 |
| orgName | varchar(256) | 组织名称 |
| orgCode | varchar(64) | 组织编码（唯一） |
| parentId | bigint | 父组织ID（0表示顶级） |
| orgType | varchar(64) | 组织类型（党委/党总支/党支部） |
| orgLevel | int | 组织级别 |
| leaderId | bigint | 负责人ID（关联user.id） |
| address | varchar(512) | 地址 |
| description | varchar(1024) | 描述 |

### 2.3 后端代码结构

**Controller层**：OrganizationController.java
- `POST /organization/add` — 创建党组织
- `POST /organization/update` — 更新党组织信息
- `POST /organization/delete` — 删除党组织
- `GET /organization/get` — 根据ID查询
- `GET /organization/list` — 查询所有党组织
- `POST /organization/list/page` — 分页条件查询
- `POST /organization/bind` — 绑定负责人
- `GET /organization/get/graded` — 获取组织及其子组织树形结构

**Service层**：OrganizationService.java
- 核心方法：createOrganization()、updateOrganization()、deleteOrganization()
- fillOrganizationLeader()：填充负责人信息
- getSubGradedOrgs()：递归获取子级组织

**Mapper层**：OrganizationMapper.java / OrganizationMapper.xml

### 2.4 前端代码结构

**视图文件**：views/organization/OrganizationView.vue
**API接口**：api/dangzuzhiguanli.ts

---

## 模块三：活动管理模块

### 3.1 模块概述

活动管理模块覆盖党建活动的完整生命周期，包括活动创建、编辑、发布、报名、签到、结束和总结。支持多种活动类型（会议、志愿活动、学习培训、其他），提供扫码签到和手动签到两种方式，并自动统计参与人数和签到率数据。

### 3.2 数据库设计

**表名：activity（活动主表）**

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | bigint | 主键自增 |
| activityName | varchar(512) | 活动名称 |
| orgId | bigint | 所属党组织ID |
| userId | bigint | 创建人ID |
| activityType | int | 活动类型（1会议/2志愿/3学习/4其他） |
| activityContent | text | 活动描述 |
| enrollDeadline | datetime | 报名截止时间 |
| startTime | datetime | 开始时间 |
| endTime | datetime | 结束时间 |
| location | varchar(512) | 活动地点 |
| maxNum | int | 最大参与人数 |
| currentNum | int | 当前参与人数 |
| totalParticipant | int | 总报名人数 |
| actualParticipant | int | 实际参与人数（已签到） |
| signRate | double | 签到率 |
| status | int | 活动状态（1待发布/2已发布/3进行中/4已结束） |
| signInType | int | 签到方式（1扫码/2手动） |
| QRCodeUrl | varchar(1024) | 签到二维码URL |
| reviewContent | text | 活动总结 |

**表名：activity_enroll（活动报名记录表）**

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | bigint | 主键自增 |
| activityId | bigint | 活动ID |
| userId | bigint | 用户ID |
| enrollTime | datetime | 报名时间 |
| participantStatus | int | 参与状态（1已报名/2已取消） |
| isSign | tinyint | 是否签到（0否/1是） |

### 3.3 后端代码结构

**Controller层**：
- ActivityController.java — 活动主管理
  - `POST /activity/add` — 添加活动
  - `POST /activity/update` — 更新活动
  - `POST /activity/delete` — 删除活动
  - `GET /activity/get` — 获取活动详情
  - `POST /activity/list/page` — 分页查询
  - `POST /activity/publish` — 发布活动
  - `POST /activity/end` — 结束活动
  - `POST /activity/statistics/update` — 更新单活动统计
  - `POST /activity/statistics/update/all` — 更新全部统计

- ActivityEnrollController.java — 报名与签到管理
  - `POST /activity/enroll/enroll` — 报名活动
  - `POST /activity/enroll/cancel` — 取消报名
  - `POST /activity/enroll/signIn` — 签到（扫码/手动）
  - `GET /activity/enroll/my` — 我的报名记录
  - `GET /activity/enroll/activity/participants` — 活动参与者列表

**Service层**：ActivityService.java、ActivityEnrollService.java
- 核心方法：addActivity()、publishActivity()、endActivity()
- updateActivityStatisticsById()：更新活动统计指标
- updateAllActivityStatistics()：批量更新所有活动统计

### 3.4 前端代码结构

**视图文件**：views/activity/ActivityView.vue
**API接口**：api/huodongguanli.ts、api/huodongbaomingguanli.ts

---

## 模块四：公告通知模块

### 4.1 模块概述

公告通知模块实现党建信息的发布与传播管理，具备完整的公告生命周期管理能力。功能涵盖富文本内容编辑、附件上传下载、发布/撤回/下架/重发、置顶排序、已读未读追踪、分享链接生成、时间线展示以及搜索功能，满足党组织内部信息高效传达的需求。

### 4.2 数据库设计

**表名：notice（公告主表）**

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | bigint | 主键自增 |
| title | varchar(512) | 公告标题 |
| content | text | 公告内容（富文本HTML） |
| publisherId | bigint | 发布人ID |
| orgId | bigint | 所属党组织ID（null为系统公告） |
| publishTime | datetime | 首次发布时间 |
| expireTime | datetime | 过期时间 |
| isTop | tinyint | 是否置顶（0否/1是） |
| status | tinyint | 状态（0草稿/1已发布） |

**扩展表**：
- **announcement_attachment** — 公告附件表（id、announcementId、name、url、size、type）
- **announcement_read_record** — 阅读记录表（id、announcementId、userId、readTime）
- **announcement_share** — 分享记录表（id、announcementId、shareCode、expireTime、accessCount）

### 4.3 后端代码结构

**Controller层**：
- NoticeController.java — 公告基础管理
  - `POST /notice/add` — 添加公告
  - `POST /notice/update` — 更新公告
  - `POST /notice/delete` — 删除公告
  - `GET /notice/get` — 获取详情
  - `GET /notice/list/page` — 分页列表
  - `POST /notice/publish` — 发布公告
  - `POST /notice/withdraw` — 撤回公告
  - `PUT /notice/offline` — 下架公告
  - `PUT /notice/republish` — 重发公告
  - `PUT /notice/top` — 设置置顶
  - `POST /notice/markRead` — 标记已读
  - `GET /notice/unreadCount` — 未读数量

- AnnouncementExtensionController.java — 公告扩展功能
  - `GET /announcement/attachment/list` — 附件列表
  - `POST /announcement/upload/notice/attachment` — 上传附件
  - `POST /announcement/upload/notice/image` — 上传富文本图片
  - `GET /announcement/attachment/download` — 下载附件
  - `GET /announcement/attachment/preview` — 预览附件
  - `POST /announcement/share/generate` — 生成分享链接
  - `GET /announcement/share/access` — 访问分享链接
  - `GET /announcement/timeline` — 时间线列表
  - `GET /announcement/timeline/search` — 时间线搜索

**Service层**：NoticeService.java、AnnouncementAttachmentService.java、AnnouncementReadRecordService.java、AnnouncementShareService.java

### 4.4 前端代码结构

**视图文件**：
- views/notice/AnnouncementListView.vue — 公告列表页
- views/notice/AnnouncementDetailsView.vue — 公告详情页
- views/notice/AnnouncementManagementView.vue — 公告管理页
- views/notice/NoticeContentView.vue — 内容查看页

**API接口**：api/gonggaoguanli.ts、api/gonggaokuozhangongneng.ts

---

## 模块五：材料与文件管理模块

### 5.1 模块概述

材料与文件管理模块整合了党建材料的模板管理、提交审核、归档查询及统一文件存储服务。材料审核采用多级审批流程（提交→初审→退回/终审），支持Word、PDF、图片等多种格式。文件管理提供统一的上传下载接口，按业务类型区分存储路径（模板/材料/附件），并实施严格的权限控制。

### 5.2 数据库设计

**表名：file（统一文件表）**

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | bigint | 主键自增 |
| fileName | varchar(512) | 存储文件名（随机生成） |
| originFileName | varchar(512) | 原始文件名 |
| orgId | bigint | 所属党组织ID |
| userId | bigint | 上传用户ID |
| stageId | bigint | 所属发展阶段ID |
| fileUrl | varchar(1024) | 文件相对路径URL |
| fileSize | int | 文件大小（字节） |
| isTemplate | tinyint | 是否模板（0否/1是） |

**材料相关表**：
- **material_template** — 材料模板表（id、templateName、description、orgId、fileId、stageId）
- **material_submission** — 材料提交表（id、userId、templateId、fileUrl、submitTime、auditStatus、auditOpinion、finalAuditStatus、finalAuditOpinion）
- **material_audit_log** — 审核日志表（id、submissionId、auditType、auditorId、opinion、createTime）
- **material_archive** — 材料归档表（id、userId、stageId、fileId、archiveTime）

### 5.3 后端代码结构

**Controller层**：
- FileController.java — 统一文件管理
  - `POST /file/upload` — 文件上传（通用）
  - `POST /file/api/upload/template` — 上传模板文件
  - `POST /file/api/upload/material` — 上传材料文件
  - `GET /file/download` — 文件下载
  - `POST /file/delete` — 文件删除
  - `GET /file/template/list` — 模板文件列表
  - `GET /file/material/list` — 材料文件列表

- MaterialSubmissionController.java — 材料提交审核
  - `GET /api/material/submission/list` — 提交列表
  - `POST /api/material/submission/submit` — 提交材料
  - `PUT /api/material/submission/audit` — 初审
  - `PUT /api/material/submission/reject` — 退回
  - `PUT /api/material/submission/finalAudit` — 终审
  - `GET /api/material/submission/view` — 查看详情
  - `GET /api/material/submission/download` — 下载材料
  - `GET /api/material/submission/todoCount` — 待办数量

- MaterialTemplateController.java — 模板管理
- MaterialArchiveController.java — 归档查询

**Service层**：FileService.java、MaterialSubmissionService.java、MaterialTemplateService.java、MaterialArchiveService.java
- FileUploadBizEnum枚举定义业务类型：USER_AVATAR、MATERIAL、TEMPLATE
- validFile()方法校验文件类型和大小限制
- getRelativePath()方法按业务类型构建存储路径

### 5.4 前端代码结构

**视图文件**：
- views/material/MaterialTemplateManagementView.vue — 模板管理
- views/material/MaterialSubmissionReviewView.vue — 提交与审核
- views/material/MaterialArchivingQueryView.vue — 归档查询

**API接口**：api/cailiaomobanguanli.ts、api/cailiaotijiaoshenhe.ts、api/cailiaoguidangchaxun.ts、api/wenjianguanli.ts

---

## 模块六：量化统计模块

### 6.1 模块概述

量化统计模块从用户和组织两个维度对党建活动数据进行统计分析。用户维度统计个人参与度、签到率、材料提交数量；组织维度统计活动次数、平均参与人数、平均签到人数等指标。系统通过定时任务每日凌晨自动计算并更新统计数据，前端通过ECharts图表直观展示分析结果。

### 6.2 数据库设计

**表名：user_quantify（用户量化统计表）**

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | bigint | 主键自增 |
| userId | bigint | 用户ID |
| statDate | date | 统计日期 |
| totalActivity | int | 总活动数 |
| participateActivity | int | 报名活动数 |
| participateRate | double | 活动参与度 |
| signActivity | int | 签到活动数 |
| signRate | double | 签到率 |
| fileCount | int | 材料文件数量 |

**表名：org_quantify（组织量化统计表）**

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | bigint | 主键自增 |
| orgId | bigint | 组织ID |
| statDate | date | 统计日期 |
| activityCount | int | 活动次数 |
| totalParticipant | int | 总报名人数 |
| totalSign | int | 总签到人数 |
| avgParticipant | double | 平均每次活动报名人数 |
| avgSign | double | 平均每次活动签到人数 |

**辅助表**：
- **quantify_indicator** — 量化指标配置表（id、indicatorName、indicatorType、weight、description）
- **quantify_data** — 量化明细数据表（id、userId/orgId、indicatorId、value、statDate）

### 6.3 后端代码结构

**Controller层**：
- QuantifyController.java — 量化统计控制
  - `POST /quantify/user/generate/{userId}` — 生成单用户数据
  - `POST /quantify/user/generate/all` — 生成所有用户数据
  - `GET /quantify/user/{userId}` — 查询用户统计数据
  - `POST /quantify/org/generate/{orgId}` — 生成单组织数据
  - `POST /quantify/org/generate/all` — 生成所有组织数据
  - `GET /quantify/org/{orgId}` — 查询组织统计数据
  - `POST /quantify/generate/all` — 生成全部数据

- QuantifyDataController.java — 明细数据管理
- QuantifyIndicatorController.java — 指标配置管理

**定时任务**：QuantifyScheduledTask.java
- `@Scheduled(cron = "0 0 1 * * ?")` — 每日凌晨1点执行
- generateQuantifyDataDaily()：依次执行活动统计更新→用户量化计算→组织量化计算

**Service层**：UserQuantifyService.java、OrgQuantifyService.java、QuantifyDataService.java、QuantifyIndicatorService.java
- 核心逻辑：遍历活动报名记录聚合计算各维度指标值

### 6.4 前端代码结构

**视图文件**：
- views/quantify/QuantifyView.vue — 个人量化统计（ECharts图表展示）
- views/quantify/QuantifyManagerView.vue — 管理员量化统计面板

**API接口**：api/lianghuatongji.ts、api/lianghuashuju.ts、api/lianghuazhibiao.ts

---

## 模块七：发展阶段管理模块

### 7.1 模块概述

发展阶段管理模块对党员从入党积极分子到正式党员的全流程进行跟踪管理。每个发展阶段包含阶段起止时间、考察内容、考察结果等信息，支持阶段创建、更新、删除、提交审核、审核操作等全流程管理，并通过AOP注解自动记录关键操作的审计日志。

### 7.2 数据库设计

**表名：development_stage（发展阶段表）**

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | bigint | 主键自增 |
| userId | bigint | 用户ID（被培养人） |
| trainerId | bigint | 培养人ID |
| stageName | varchar(256) | 阶段名称（积极分子/发展对象/预备党员/正式党员） |
| stageStartTime | date | 阶段开始时间 |
| stageEndTime | date | 阶段结束时间 |
| stageStatus | tinyint | 阶段状态（0进行中/1已完成/2已终止） |
| assessmentContent | text | 考察内容 |
| assessmentResult | tinyint | 考察结果（1合格/0不合格/2未审核） |
| auditUserId | bigint | 审核人员ID |
| auditTime | datetime | 审核时间 |
| auditRemark | text | 审核意见 |

**关联表**：development_stage_log（发展阶段变更日志表）

### 7.3 后端代码结构

**Controller层**：DevelopmentStageController.java
- `POST /developmentStage/add` — 添加阶段记录
- `POST /developmentStage/update` — 更新阶段记录
- `POST /developmentStage/delete` — 删除阶段记录
- `GET /developmentStage/get` — 根据ID查询
- `GET /developmentStage/list/byUserId` — 按用户查询阶段列表
- `GET /developmentStage/list/byOrgId` — 按组织查询阶段列表
- `POST /developmentStage/submitForAudit` — 提交审核
- `POST /developmentStage/audit` — 审核阶段记录
- `POST /developmentStage/list` — 条件查询列表

**AOP日志机制**：
- @DevelopmentStageLogAnnotation注解标注在关键操作上
- DevelopmentStageLogAspect切面拦截并记录操作日志
- DevelopmentStageOperationEnum枚举定义操作类型（CREATE/UPDATE/DELETE/SUBMIT_AUDIT/AUDIT）

**Service层**：DevelopmentStageService.java、DevelopmentStageLogService.java

### 7.4 前端代码结构

**API接口**：api/fazhanjieduanguanli.ts、api/fazhanjieduanbiangengrizhi.ts

---

## 模块八：培养人关系管理模块

### 8.1 模块概述

培养人关系管理模块用于建立和维护入党培养人与被培养人之间的对应关系。系统支持一名培养人同时培养多名被培养人的场景，提供关系的建立、查询（按被培养人/培养人/组织维度）和解绑功能，为发展阶段管理和量化考核提供关系数据支撑。

### 8.2 数据库设计

**表名：trainer_relation（培养人关联表）**

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | bigint | 主键自增 |
| userId | bigint | 被培养人ID |
| userName | varchar(256) | 被培养人姓名 |
| trainerId | bigint | 培养人ID |
| trainerName | varchar(256) | 培养人姓名 |
| startDate | date | 开始日期 |
| endDate | date | 结束日期 |
| status | int | 状态（1进行中/2已完成/3已终止） |

**约束**：uk_userId_trainerId（同一被培养人与培养人组合唯一）

### 8.3 后端代码结构

**Controller层**：TrainerRelationController.java
- `POST /trainerRelation/add` — 添加培养人关联
- `GET /trainerRelation/trainer/byUserId` — 按被培养人查询
- `GET /trainerRelation/trainRelation/byTrainerId` — 按培养人查询其被培养人列表
- `GET /trainerRelation/list/byOrgId` — 按组织查询培养关系
- `POST /trainerRelation/delete` — 删除培养关系
- `GET /trainerRelation/listAvailableTrainers` — 获取可选培养人列表

**Service层**：TrainerRelationService.java
- addTrainerRelation()：创建时校验重复性
- getAvailableTrainers()：筛选符合条件的培养人候选人

### 8.4 前端代码结构

**视图文件**：views/trainerRelation/TrainerRelationView.vue
**API接口**：api/peiyangrenguanlianguanli.ts

---

## 模块九：组织关系转移模块

### 9.1 模块概述

组织关系转移模块处理党员因工作调动、毕业等原因需要在党组织之间转移组织关系的业务场景。完整的转移流程包括：党员发起申请→管理员审批（通过/拒绝）→转移记录归档。系统支持按党员和组织两个维度查询历史转移记录。

### 9.2 数据库设计

**表名：org_relation_transfer（组织关系转移表）**

| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | bigint | 主键自增 |
| userId | bigint | 党员ID |
| fromOrgId | bigint | 原党组织ID |
| fromOrgName | varchar(256) | 原党组织名称 |
| toOrgId | bigint | 目标党组织ID |
| toOrgName | varchar(256) | 目标党组织名称 |
| transferReason | varchar(1024) | 转移原因 |
| transferTime | datetime | 转移时间 |
| approveStatus | int | 审批状态（1待审批/2已通过/3已拒绝） |
| approveUserId | bigint | 审批人ID |
| approveUserName | varchar(256) | 审批人姓名 |
| approveTime | datetime | 审批时间 |
| approveComment | varchar(1024) | 审批意见 |

### 9.3 后端代码结构

**Controller层**：OrgRelationTransferController.java
- `POST /orgRelationTransfer/add` — 创建转移申请
- `POST /orgRelationTransfer/approve` — 审批转移申请
- `POST /orgRelationTransfer/update` — 更新转移信息
- `POST /orgRelationTransfer/delete` — 删除转移记录
- `GET /orgRelationTransfer/get` — 查询详情
- `GET /orgRelationTransfer/list` — 查询全部记录
- `GET /orgRelationTransfer/page` — 分页查询
- `GET /orgRelationTransfer/byUserId` — 按党员查询
- `GET /orgRelationTransfer/byOrgId` — 按组织查询

**Service层**：OrgRelationTransferService.java
- createOrgRelationTransfer()：创建时默认设置审批状态为PENDING
- approveOrgRelationTransfer()：执行审批操作并记录审批人和时间
- ApproveStatusEnum枚举：PENDING(1)、APPROVED(2)、REJECTED(3)

### 9.4 前端代码结构

**API接口**：api/zuzhiguanxizhuanyiguanli.ts

---

## 十、系统技术架构总结

### 10.1 整体架构
本系统采用经典的前后端分离B/S架构模式：

```
┌─────────────┐     HTTP/RESTful API      ┌─────────────┐
│   Vue3前端   │ ◄──────────────────────► │ Spring Boot │
│  (端口:8080) │                           │  (端口:8101) │
└─────────────┘                           └──────┬──────┘
                                                  │
                                           ┌──────▼──────┐
                                           │    MySQL     │
                                           │ Redis缓存    │
                                           └─────────────┘
```

### 10.2 后端分层架构
- **表现层（Controller）**：接收HTTP请求、参数校验、调用Service、返回响应
- **业务逻辑层（Service）**：实现核心业务规则和数据处理逻辑
- **数据访问层（Mapper）**：通过MyBatis-Plus操作数据库
- **横切关注点（AOP）**：鉴权拦截器、操作日志审计

### 10.3 前端组件架构
- **路由层**：Vue Router实现页面导航和权限守卫
- **状态层**：Vuex集中管理用户态和全局状态
- **视图层**：按功能模块组织的单文件组件
- **服务层**：Axios封装的API调用模块

---

## 十一、开发规范与最佳实践

### 11.1 接口规范
- 所有接口返回统一格式BaseResponse<T>，包含code、data、message字段
- 使用Swagger/Knife4j注解标注接口文档（@Api、@ApiOperation）
- RESTful风格命名：POST新增/PUT修改/DELETE删除/GET查询

### 11.2 安全规范
- 敏感接口使用@AuthCheck注解进行角色鉴权
- 用户密码使用EncryptUtils进行MD5+盐加密存储
- 文件上传校验文件类型和大小，防止恶意上传
- SQL注入防护：使用MyBatis-Plus参数化查询

### 11.3 数据规范
- 全局逻辑删除：isDelete字段（0未删除/1已删除）
- 主键策略：bigint自增ID（雪花算法备用）
- 时间字段统一使用datetime类型，自动维护createTime/updateTime

### 11.4 代码规范
- 使用Lombok简化实体类（@Data、@Slf4j）
- DTO/VO模式隔离请求对象和返回对象
- 统一异常处理：BusinessException + GlobalExceptionHandler

---

## 十二、测试建议

### 12.1 单元测试
- 对Service层的核心业务逻辑编写单元测试
- 使用Spring Boot Test框架进行集成测试
- 重点测试量化统计算法的正确性

### 12.2 接口测试
- 通过Knife4j界面进行接口调试
- 测试各类角色的权限边界情况
- 验证文件上传下载功能的完整性

### 12.3 业务流程测试
- 完整走通"活动创建→发布→报名→签到→结束→统计"流程
- 完整走通"材料提交→初审→退回→重新提交→终审"流程
- 验证定时任务（QuantifyScheduledTask）的数据准确性

### 12.4 性能测试
- 并发场景下的活动报名压力测试
- 大量数据下的量化统计性能验证
- 文件大文件上传的稳定性测试
