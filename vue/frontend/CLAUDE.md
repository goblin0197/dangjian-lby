# 党建发展对象后端管理系统 - Frontend

## 项目概述

基于 Vue 3 + TypeScript + Arco Design 的党员发展工作管理系统前端。

## 技术栈

- **框架**: Vue 3.2+ (Composition API)
- **语言**: TypeScript 4.5+
- **UI 库**: Arco Design Web Vue 2.56+
- **路由**: Vue Router 4.0+
- **状态管理**: Vuex 4.0+
- **HTTP 客户端**: Axios 1.14+
- **富文本编辑器**: @wangeditor/editor 5.1+
- **图表**: ECharts 6.0+
- **日期处理**: Dayjs 1.11+
- **构建工具**: Vue CLI 5.0+

## 开发命令

```bash
npm run serve      # 启动开发服务器
npm run build      # 生产环境构建
npm run lint       # 代码检查
npm run openapi2ts # 从 OpenAPI 生成 TypeScript 类型
```

## 目录结构

```
src/
├── api/                    # API 接口定义（OpenAPI 生成）
├── access/                 # 权限控制相关
│   ├── accessEnum.ts       # 权限枚举定义
│   └── checkAccess.ts      # 权限检查逻辑
├── components/             # 公共组件
├── layouts/                # 布局组件
│   ├── BasicLayout.vue     # 基础布局
│   └── UserLayout.vue      # 用户布局
├── router/                 # 路由配置
│   ├── index.ts            # 路由实例导出
│   └── routes.ts           # 路由表定义
├── store/                  # Vuex 状态管理
│   ├── index.ts            # Store 实例
│   ├── user.ts             # 用户状态
│   └── headerMenu.ts       # 菜单状态
├── views/                  # 页面视图
│   ├── notice/             # 公告管理相关页面
│   ├── material/           # 材料管理相关页面
│   ├── trainerRelation/    # 培养关系/发展阶段
│   ├── organization/       # 组织管理
│   ├── activity/           # 活动管理
│   └── ...
├── plugins/                # 插件配置
│   └── axios.ts            # Axios 拦截器配置
├── main.ts                 # 应用入口
├── App.vue                 # 根组件
└── request.ts              # 请求封装
```

## 权限体系

权限角色定义在 `src/access/accessEnum.ts`:

- `NOT_LOGIN` - 未登录
- `SUPER_ADMIN_ROLE` - 超级管理员
- `ORG_ADMIN_ROLE` - 组织管理员
- `ORG_MEMBER_ROLE` - 组织成员
- `ACTIVIST_DEVELOPMENT_ROLE` - 积极分子发展

## 代码规范

### 组件风格

- 使用 `<script setup lang="ts">` 语法糖
- 优先使用 Composition API
- 类型定义使用 TypeScript interface/type

### 命名约定

- 文件：PascalCase (如 `AnnouncementDetailsView.vue`)
- 组件：PascalCase
- 变量/函数：camelCase
- 常量：UPPER_SNAKE_CASE

### 导入顺序

1. Vue 核心及内置模块
2. 第三方库
3. Arco Design 组件/图标
4. @/ 别名导入（按目录层级）
5. 相对路径导入

## API 调用

- API 定义位于 `src/api/` 目录
- 使用 OpenAPI 自动生成 TypeScript 类型
- Axios 拦截器配置在 `src/plugins/axios.ts`

## 路由配置

- 路由表定义在 `src/router/routes.ts`
- 首页重定向到 `/user/login`
- `meta.hideInMenu: true` 隐藏菜单项

## 注意事项

- 项目使用 Arco Design 作为主要 UI 组件库
- 所有 API 响应通过 Axios 拦截器统一处理
- 消息提示使用 `@arco-design/web-vue/es/message`
