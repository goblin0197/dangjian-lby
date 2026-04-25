# 智慧党建管理系统 - Code Wiki

## 1. 项目概述

智慧党建管理系统是一套面向高校党建工作的综合性信息管理平台，采用前后端分离架构，旨在实现党组织管理、党员发展全流程跟踪、活动组织与签到、材料提交审核、量化数据统计等核心业务的数字化管理，提升党建工作效率和信息化水平。

系统服务于高校党建场景，主要用户角色包括超级管理员、组织管理员（党支部书记/委员）、党员、入党积极分子等不同层级人员，各角色拥有差异化的功能权限。

## 2. 技术栈

### 2.1 后端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 11 | 开发语言 |
| Spring Boot | 2.7.2 | 基础框架 |
| MyBatis-Plus | 3.5.2 | ORM框架 |
| MySQL | 8.x | 关系型数据库 |
| Redis | — | 缓存与会话存储（Spring Session） |
| Knife4j | 4.4.0 | 接口文档（Swagger增强） |
| Tencent COS | 5.6.89 | 对象存储（预留） |
| EasyExcel | 3.1.1 | Excel文件处理 |
| Hutool | 5.8.8 | 工具类库 |
| Lombok | 1.18.30 | 代码简化 |
| Spring AOP | — | 面向切面编程（鉴权、日志） |
| wx-java | 4.4.0 | 微信公众号集成（预留） |

### 2.2 前端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.x | 前端框架 |
| TypeScript | ~4.5.5 | 类型安全 |
| Vue Router | 4.0.3 | 路由管理 |
| Vuex | 4.0.0 | 状态管理 |
| Ant Design Vue | 4.2.6 | UI组件库 |
| Arco Design Vue | 2.56.3 | 辅助UI组件库 |
| Axios | 1.14.0 | HTTP请求库 |
| ECharts | 6.0.0 | 数据可视化图表 |
| WangEditor | 5.1.23 | 富文本编辑器 |
| Day.js | 1.11.19 | 日期处理 |
| Moment | 2.30.1 | 日期处理 |
| openapi-typescript-codegen | 0.29.0 | API类型自动生成 |

## 3. 项目结构

### 3.1 后端结构

```
com.coder.springbootinit/
├── annotation/          # 自定义注解（AuthCheck、DevelopmentStageLogAnnotation）
├── aop/                 # 切面实现（AuthInterceptor、LogInterceptor、DevelopmentStageLogAspect）
├── common/              # 公共类（BaseResponse、ErrorCode、ResultUtils等）
├── config/              # 配置类（CORS、COS、JSON、MyBatis-Plus、WxOpen）
├── constant/            # 常量定义
├── controller/          # 控制器层（19个Controller）
├── exception/           # 异常处理（BusinessException、GlobalExceptionHandler）
├── generate/            # 代码生成器
├── manager/             # 第三方服务管理（CosManager）
├── mapper/              # 数据访问层接口（23个Mapper）
├── model/               # 数据模型（entity/dto/enums/vo）
├── service/             # 业务逻辑层（26个Service接口及实现）
├── task/                # 定时任务（QuantifyScheduledTask）
├── utils/               # 工具类
├── wxmp/                # 微信公众号相关
└── MainApplication.java # 主应用类
```

### 3.2 前端结构

```
src/
├── access/              # 权限校验（accessEnum、checkAccess）
├── api/                 # API接口定义（按模块划分，18个api文件）
├── assets/              # 静态资源（图片等）
├── components/          # 公共组件（GlobalHeader、HelloWorld）
├── generated/           # OpenAPI自动生成的类型和Service
├── layouts/             # 布局组件（BasicLayout、UserLayout）
├── plugins/             # 插件（axios封装）
├── router/              # 路由配置（routes.ts、index.ts）
├── store/               # Vuex状态管理（user、index、headerMenu）
├── views/               # 页面视图（按功能模块组织）
│   ├── activity/        # 活动管理
│   ├── material/        # 材料管理（模板/提交审核/归档查询）
│   ├── notice/          # 公告通知（列表/详情/管理/内容查看）
│   ├── organization/    # 党组织管理
│   ├── quantify/        # 量化统计（个人/管理员）
│   ├── trainerRelation/ # 培养人关系
│   └── user/            # 用户登录
├── App.vue              # 根组件
├── main.ts              # 入口文件
└── request.ts           # 请求封装
```

## 4. 后端实现

### 4.1 核心模块

#### 4.1.1 用户管理模块

- **功能**：用户登录、注销、信息管理、权限控制
- **关键文件**：
  - [UserController.java](file:///workspace/java/springboot2.7-init-master/src/main/java/com/coder/springbootinit/controller/UserController.java)
  - [UserService.java](file:///workspace/java/springboot2.7-init-master/src/main/java/com/coder/springbootinit/service/UserService.java)
  - [User.java](file:///workspace/java/springboot2.7-init-master/src/main/java/com/coder/springbootinit/model/entity/User.java)

#### 4.1.2 党组织管理模块

- **功能**：党组织信息管理、组织关系转移
- **关键文件**：
  - [OrganizationController.java](file:///workspace/java/springboot2.7-init-master/src/main/java/com/coder/springbootinit/controller/OrganizationController.java)
  - [OrgRelationTransferController.java](file:///workspace/java/springboot2.7-init-master/src/main/java/com/coder/springbootinit/controller/OrgRelationTransferController.java)

#### 4.1.3 活动管理模块

- **功能**：活动创建、报名、签到管理
- **关键文件**：
  - [ActivityController.java](file:///workspace/java/springboot2.7-init-master/src/main/java/com/coder/springbootinit/controller/ActivityController.java)
  - [ActivityEnrollController.java](file:///workspace/java/springboot2.7-init-master/src/main/java/com/coder/springbootinit/controller/ActivityEnrollController.java)

#### 4.1.4 材料管理模块

- **功能**：材料模板管理、材料提交审核、材料归档查询
- **关键文件**：
  - [MaterialTemplateController.java](file:///workspace/java/springboot2.7-init-master/src/main/java/com/coder/springbootinit/controller/MaterialTemplateController.java)
  - [MaterialSubmissionController.java](file:///workspace/java/springboot2.7-init-master/src/main/java/com/coder/springbootinit/controller/MaterialSubmissionController.java)
  - [MaterialArchiveController.java](file:///workspace/java/springboot2.7-init-master/src/main/java/com/coder/springbootinit/controller/MaterialArchiveController.java)

#### 4.1.5 量化统计模块

- **功能**：量化指标管理、数据统计、定时任务
- **关键文件**：
  - [QuantifyController.java](file:///workspace/java/springboot2.7-init-master/src/main/java/com/coder/springbootinit/controller/QuantifyController.java)
  - [QuantifyIndicatorController.java](file:///workspace/java/springboot2.7-init-master/src/main/java/com/coder/springbootinit/controller/QuantifyIndicatorController.java)
  - [QuantifyScheduledTask.java](file:///workspace/java/springboot2.7-init-master/src/main/java/com/coder/springbootinit/task/QuantifyScheduledTask.java)

### 4.2 关键类与函数

#### 4.2.1 MainApplication

- **功能**：项目启动入口
- **关键注解**：
  - `@SpringBootApplication`：Spring Boot应用注解
  - `@MapperScan`：MyBatis扫描路径
  - `@EnableScheduling`：启用定时任务
  - `@EnableAspectJAutoProxy`：启用AOP代理

#### 4.2.2 UserController

- **功能**：用户相关接口
- **关键方法**：
  - `userLogin`：用户登录
  - `userLogout`：用户注销
  - `addUser`：创建用户（管理员权限）
  - `updateUser`：更新用户（管理员权限）
  - `deleteUser`：删除用户（管理员权限）
  - `listUserByPage`：分页获取用户列表（管理员权限）

#### 4.2.3 UserService

- **功能**：用户业务逻辑
- **关键方法**：
  - `userRegister`：用户注册
  - `userLogin`：用户登录
  - `userLogout`：用户注销
  - `getLoginUser`：获取当前登录用户
  - `isAdmin`：判断是否为管理员
  - `isSuperAdmin`：判断是否为超级管理员

#### 4.2.4 AuthCheck 注解

- **功能**：权限校验注解
- **使用方式**：`@AuthCheck(mustRole = {UserConstant.SUPER_ADMIN_ROLE, UserConstant.ORG_ADMIN_ROLE})`

#### 4.2.5 GlobalExceptionHandler

- **功能**：全局异常处理
- **处理异常**：BusinessException、RuntimeException等

### 4.3 配置说明

- **服务端口**：8101
- **上下文路径**：/api
- **数据库**：MySQL（party_building）
- **Redis**：localhost:6379，database=2
- **Session**：Redis存储，30天过期
- **MyBatis-Plus**：全局ID自增策略、逻辑删除字段isDelete
- **文件上传限制**：单文件最大100MB
- **接口文档**：Knife4j启用，扫描controller包

## 5. 前端实现

### 5.1 核心模块

#### 5.1.1 路由管理

- **功能**：前端页面路由配置
- **关键文件**：[routes.ts](file:///workspace/vue/frontend/src/router/routes.ts)
- **主要路由**：
  - `/user/login`：用户登录
  - `/organization`：组织管理
  - `/userManagement`：用户管理
  - `/activity`：活动管理
  - `/materialTemplateManagement`：材料管理
  - `/announcementManagement`：公告管理
  - `/quantifyManager`：量化统计

#### 5.1.2 状态管理

- **功能**：前端状态管理
- **关键文件**：[user.ts](file:///workspace/vue/frontend/src/store/user.ts)
- **主要状态**：
  - `loginUser`：登录用户信息
  - `actions`：异步操作，如获取登录用户信息
  - `mutations`：同步操作，如更新用户信息

#### 5.1.3 API 调用

- **功能**：前端API调用封装
- **关键文件**：
  - [request.ts](file:///workspace/vue/frontend/src/request.ts)
  - [generated/services/Service.ts](file:///workspace/vue/frontend/generated/services/Service.ts)

### 5.2 关键组件

#### 5.2.1 UserLoginView

- **功能**：用户登录页面
- **路径**：[UserLoginView.vue](file:///workspace/vue/frontend/src/views/user/UserLoginView.vue)

#### 5.2.2 OrganizationView

- **功能**：组织管理页面
- **路径**：[OrganizationView.vue](file:///workspace/vue/frontend/src/views/organization/OrganizationView.vue)

#### 5.2.3 ActivityView

- **功能**：活动管理页面
- **路径**：[ActivityView.vue](file:///workspace/vue/frontend/src/views/activity/ActivityView.vue)

#### 5.2.4 MaterialTemplateManagementView

- **功能**：材料模板管理页面
- **路径**：[MaterialTemplateManagementView.vue](file:///workspace/vue/frontend/src/views/material/MaterialTemplateManagementView.vue)

#### 5.2.5 QuantifyManagerView

- **功能**：量化统计管理页面
- **路径**：[QuantifyManagerView.vue](file:///workspace/vue/frontend/src/views/quantify/QuantifyManagerView.vue)

## 6. 依赖关系

### 6.1 后端依赖

| 依赖 | 版本 | 用途 |
|------|------|------|
| spring-boot-starter-web | 2.7.2 | Web应用支持 |
| spring-boot-starter-aop | 2.7.2 | AOP支持 |
| mybatis-plus-boot-starter | 3.5.2 | ORM框架增强 |
| spring-boot-starter-data-redis | 2.7.2 | Redis支持 |
| spring-session-data-redis | 2.7.2 | Redis会话存储 |
| knife4j-openapi2-spring-boot-starter | 4.4.0 | API文档 |
| cos_api | 5.6.89 | 对象存储 |
| easyexcel | 3.1.1 | Excel处理 |
| hutool-all | 5.8.8 | 工具类库 |
| lombok | 1.18.30 | 代码简化 |

### 6.2 前端依赖

| 依赖 | 版本 | 用途 |
|------|------|------|
| vue | ^3.2.13 | 前端框架 |
| vue-router | ^4.0.3 | 路由管理 |
| vuex | ^4.0.0 | 状态管理 |
| ant-design-vue | ^4.2.6 | UI组件库 |
| arco-design/web-vue | ^2.56.3 | 辅助UI组件库 |
| axios | ^1.14.0 | HTTP请求库 |
| echarts | ^6.0.0 | 数据可视化 |
| wangeditor/editor | ^5.1.23 | 富文本编辑器 |
| dayjs | ^1.11.19 | 日期处理 |
| moment | ^2.30.1 | 日期处理 |
| openapi-typescript-codegen | ^0.29.0 | API类型生成 |

## 7. 项目运行方式

### 7.1 后端运行

1. **配置数据库**：
   - 修改 `application.yml` 中的数据库配置
   - 执行 `sql/party_building_create_table.sql` 创建数据库表

2. **配置Redis**（可选）：
   - 修改 `application.yml` 中的Redis配置
   - 取消注释 `MainApplication` 中的Redis配置

3. **启动项目**：
   - 运行 `MainApplication.java` 中的 `main` 方法
   - 访问 `http://localhost:8101/api/doc.html` 查看API文档

### 7.2 前端运行

1. **安装依赖**：
   ```bash
   npm install
   ```

2. **生成API类型**：
   ```bash
   npm run openapi2ts
   ```

3. **启动开发服务器**：
   ```bash
   npm run serve
   ```

4. **访问前端**：
   - 登录页面：`http://localhost:8080/user/login`

## 8. 总结

智慧党建管理系统是一套功能完整的高校党建工作管理平台，采用前后端分离架构，具有以下特点：

1. **完整的功能模块**：涵盖党组织管理、党员发展、活动组织、材料管理、量化统计等核心业务
2. **清晰的权限控制**：基于角色的权限管理，支持超级管理员、组织管理员、普通党员等不同角色
3. **现代化技术栈**：后端使用Spring Boot 2.7.x + MyBatis-Plus，前端使用Vue 3 + TypeScript
4. **良好的可扩展性**：预留了微信公众号集成、对象存储等功能接口
5. **完善的文档支持**：使用Knife4j生成API文档，方便接口调试和开发

系统通过数字化手段提升党建工作效率，为高校党建工作提供了有力的技术支撑。