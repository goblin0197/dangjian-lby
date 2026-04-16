# 智慧党建综合管理系统——项目规则文档

## 一、项目概述

本项目为**智慧党建综合管理系统**，是一套面向高校党组织管理的全栈Web应用系统。系统旨在通过信息化手段提升党建工作效率，实现党员发展流程规范化、组织管理数字化、活动开展便捷化、材料归档自动化。系统支持多角色权限控制（超级管理员、党支部管理员、培养人/党员、入党积极分子/发展对象），覆盖从入党积极分子到正式党员的全生命周期管理。

**项目名称：** 智慧党建综合管理系统（Party Building Management System）

**项目路径：**
- 后端：`java/springboot2.7-init-master/`
- 前端：`vue/frontend/`

## 二、技术栈

### 2.1 后端技术栈

| 技术项 | 版本/说明 |
|--------|-----------|
| 开发语言 | Java 11 |
| 核心框架 | Spring Boot 2.7.2 |
| ORM框架 | MyBatis-Plus 3.5.2 |
| 数据库 | MySQL 8.x（数据库名：party_building） |
| 缓存中间件 | Redis（Spring Session + Spring Data Redis） |
| 接口文档 | Knife4j 4.4.0（Swagger增强） |
| 文件存储 | 腾讯云COS SDK 5.6.89 |
| Excel处理 | Alibaba EasyExcel 3.1.1 |
| 工具库 | Hutool 5.8.8、Apache Commons Lang3 |
| 微信集成 | WxJava 4.4.0（微信公众平台SDK） |
| AOP切面 | Spring AOP（用于权限校验与日志记录） |
| API设计风格 | RESTful API |

### 2.2 前端技术栈

| 技术项 | 版本/说明 |
|--------|-----------|
| 核心框架 | Vue 3.2.13 |
| 开发语言 | TypeScript 4.5.5 |
| 构建工具 | Vue CLI 5.0.0 |
| UI组件库 | Ant Design Vue 4.2.6 / Arco Design Vue 2.56.3 |
| 状态管理 | Vuex 4.0.0 |
| 路由管理 | Vue Router 4.0.3 |
| HTTP客户端 | Axios 1.14.0 |
| 图表库 | ECharts 6.0.0 |
| 富文本编辑器 | WangEditor 5.1.23（@wangeditor/editor-for-vue） |
| 日期处理 | Day.js 1.11.19 / Moment.js 2.30.1 |
| API代码生成 | openapi-typescript-codegen 0.29.0 |

## 三、后端配置

### 3.1 项目基础配置

- **服务端口：** 8101
- **上下文路径：** `/api`
- **会话管理：** Redis Session（30天过期）
- **数据库连接：** MySQL（localhost:3306/party_building）
- **Redis连接：** localhost:6379（database: 2）
- **文件上传限制：** 单文件最大100MB
- **全局ID策略：** 自增（AUTO）
- **逻辑删除字段：** isDelete（0未删除/1已删除）

### 3.2 包结构

```
com.coder.springbootinit/
├── annotation/          # 自定义注解（AuthCheck、DevelopmentStageLogAnnotation）
├── aop/                 # 切面实现（AuthInterceptor、LogInterceptor、DevelopmentStageLogAspect）
├── common/              # 公共类（BaseResponse、DeleteRequest、ErrorCode、PageRequest、ResultUtils）
├── config/              # 配置类（CorsConfig、CosClientConfig、JsonConfig、MyBatisPlusConfig、WxOpenConfig）
├── constant/            # 常量定义（ActivityConstant、CommonConstant、FileConstant、UserConstant等）
├── controller/          # 控制器层（18个Controller）
├── exception/           # 全局异常处理（BusinessException、GlobalExceptionHandler、ThrowUtils）
├── generate/            # 代码生成器（CodeGenerator）
├── manager/             # 第三方服务封装（CosManager）
├── mapper/              # MyBatis Mapper接口层（23个Mapper）
├── model/               # 数据模型
│   ├── dto/             # 数据传输对象（按模块分目录）
│   ├── entity/          # 数据库实体类
│   ├── enums/           # 枚举定义（UserRoleEnum、DevelopmentStageOperationEnum等）
│   └── vo/              # 视图对象
├── service/             # 业务逻辑层（24个Service接口及实现）
├── task/                # 定时任务（QuantifyScheduledTask）
├── utils/               # 工具类（EncryptUtils、NetUtils、SqlUtils、SpringContextUtils）
└── wxmp/                # 微信公众号相关（WxMpConstant、WxMpMsgRouter）
```

### 3.3 角色权限体系

| 角色编码 | 角色名称 | 说明 |
|----------|----------|------|
| super_admin | 超级管理员 | 系统最高权限，可管理所有组织和用户 |
| org_admin | 组织管理员（党支部管理员） | 管理所属支部的组织、成员、活动等 |
| party_member | 党员 | 参与活动、提交材料、查看统计 |
| activist_development | 积极分子/发展对象 | 参与活动、上传发展材料、查看个人发展阶段 |

### 3.4 核心机制

- **权限校验：** 通过`@AuthCheck`注解 + `AuthInterceptor`AOP切面实现基于角色的访问控制
- **日志记录：** 通过`@DevelopmentStageLogAnnotation`注解 + `DevelopmentStageLogAspect`切面实现操作日志自动记录
- **统一响应格式：** `BaseResponse<T>`包装所有API返回值
- **全局异常处理：** `GlobalExceptionHandler`统一捕获并处理业务异常
- **密码加密：** `EncryptUtils.encryptPassword()`对用户密码进行加密存储

## 四、前端配置

### 4.1 项目结构

```
vue/frontend/src/
├── api/                  # API接口定义（按模块分文件）
│   ├── index.ts         # API入口
│   ├── typings.d.ts     # 类型声明
│   ├── yonghuguanli.ts          # 用户管理
│   ├── dangzuzhiguanli.ts       # 党组织管理
│   ├── zuzhiguanxizhuanyiguanli.ts  # 组织关系转移
│   ├── zuzhichengyuanguanli.ts  # 组织成员
│   ├── peiyangrenguanlianguanli.ts # 培养人关联
│   ├── huodongguanli.ts         # 活动管理
│   ├── huodongbaomingguanli.ts  # 活动报名
│   ├── fazhanjieduanguanli.ts   # 发展阶段
│   ├── fazhanjieduanbiangengrizhi.ts # 发展阶段变更日志
│   ├── wenjianguanli.ts         # 文件管理
│   ├── gonggaoguanli.ts         # 公告管理
│   └── lianghuatongji.ts        # 量化统计
├── assets/               # 静态资源（图片等）
├── components/           # 公共组件（GlobalHeader、HelloWorld）
├── layouts/              # 布局组件（BasicLayout、UserLayout）
├── plugins/              # 插件（axios配置）
├── router/               # 路由配置
│   ├── index.ts          # 路由入口
│   └── routes.ts         # 路由定义
├── store/                # Vuex状态管理
│   ├── index.ts
│   ├── user.ts           # 用户状态
│   └── headerMenu.ts     # 菜单状态
├── views/                # 页面视图（按模块分目录）
│   ├── user/UserLoginView.vue           # 登录页
│   ├── organization/OrganizationView.vue    # 组织管理
│   ├── activity/ActivityView.vue           # 活动管理
│   ├── trainerRelation/TrainerRelationView.vue # 发展阶段
│   ├── quantify/QuantifyView.vue           # 量化统计(用户)
│   ├── quantify/QuantifyManagerView.vue    # 量化统计(管理员)
│   ├── material/MaterialTemplateManagementView.vue  # 材料模板管理
│   ├── material/MaterialSubmissionReviewView.vue    # 材料提交审核
│   ├── material/MaterialArchivingQueryView.vue      # 材料归档查询
│   ├── notice/AnnouncementManagementView.vue        # 公告管理
│   ├── notice/AnnouncementListView.vue              # 公告列表
│   ├── notice/AnnouncementDetailsView.vue           # 公告详情
│   ├── HomeView.vue                        # 首页
│   ├── NoAuthView.vue                      # 无权限页
│   └── AboutView.vue                       # 关于页
├── access/               # 权限控制
│   ├── index.ts         # 权限入口
│   ├── checkAccess.ts   # 权限校验逻辑
│   └── accessEnum.ts    # 权限枚举
├── App.vue               # 根组件
├── main.ts               # 入口文件
├── request.ts            # 请求封装
└── shims-vue.d.ts        # Vue类型声明
```

### 4.2 路由配置要点

- 默认路由重定向到登录页（`/` → `/user/login`）
- 使用`meta.hideInMenu`控制菜单隐藏项
- 权限控制通过`access`字段结合`ACCESS_ENUM`实现
- 主要功能路由：组织管理、活动管理、发展阶段、量化统计、材料管理、公告管理

### 4.3 前端核心依赖版本

- vue: ^3.2.13
- ant-design-vue: ^4.2.6
- @arco-design/web-vue: ^2.56.3
- axios: ^1.14.0
- echarts: ^6.0.0
- vuex: ^4.0.0
- vue-router: ^4.0.3
- @wangeditor/editor-for-vue: ^5.1.12

## 五、系统功能模块文档

详细功能模块说明请参阅：#system_function_module.md
