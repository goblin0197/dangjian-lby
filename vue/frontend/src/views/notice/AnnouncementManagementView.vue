<template>
  <div class="notice-manage-container">
    <!-- 页面标题和面包屑 -->
    <div class="page-header">
      <div class="breadcrumb">
        <a-breadcrumb>
          <a-breadcrumb-item>首页</a-breadcrumb-item>
          <a-breadcrumb-item>公告管理</a-breadcrumb-item>
          <a-breadcrumb-item
            >{{
              activeView === "manage"
                ? "公告管理"
                : activeView === "stat"
                ? "公告统计"
                : "公告列表"
            }}
          </a-breadcrumb-item>
        </a-breadcrumb>
      </div>
      <h1>公告管理</h1>
    </div>

    <!-- 视图切换栏 -->
    <div class="view-switch-bar">
      <a-space size="large">
        <a-button
          :status="activeView === 'manage' ? 'primary' : 'normal'"
          type="primary"
          @click="switchView('manage')"
        >
          <icon-edit />
          公告管理
        </a-button>
        <a-button
          :status="activeView === 'list' ? 'primary' : 'normal'"
          @click="switchView('list')"
        >
          <icon-list />
          公告列表
        </a-button>
        <a-button
          :status="activeView === 'stat' ? 'primary' : 'normal'"
          @click="switchView('stat')"
        >
          <icon-chart />
          公告统计
        </a-button>
        <!-- 权限提示：仅管理员可见公告管理 -->
        <a-tag v-if="userRole !== 'super_admin'" color="orange" disabled>
          <icon-warning />
          仅管理员可管理公告
        </a-tag>
      </a-space>
    </div>

    <!-- 公告管理核心区域（仅管理员可见） -->
    <a-card
      v-if="activeView === 'manage' && userRole === 'super_admin'"
      class="list-card"
    >
      <!-- 筛选+操作区 -->
      <div class="filter-operation-bar">
        <a-form :model="searchParams" class="filter-form" layout="inline">
          <a-row :gutter="16" align="middle">
            <!-- 筛选条件 -->
            <a-col :span="18">
              <a-space size="middle">
                <a-form-item label="公告类型">
                  <a-select
                    v-model="searchParams.type"
                    placeholder="全部"
                    style="width: 150px"
                    @change="handleSearch"
                  >
                    <a-option value="all">全部</a-option>
                    <a-option value="notice">通知类</a-option>
                    <a-option value="activity">活动类</a-option>
                    <a-option value="remind">提醒类</a-option>
                    <a-option value="policy">政策类</a-option>
                  </a-select>
                </a-form-item>

                <a-form-item label="发布状态">
                  <a-select
                    v-model="searchParams.status"
                    placeholder="全部"
                    style="width: 150px"
                    @change="handleSearch"
                  >
                    <a-option value="all">全部</a-option>
                    <a-option value="draft">草稿</a-option>
                    <a-option value="published">已发布</a-option>
                    <a-option value="offline">已下架</a-option>
                  </a-select>
                </a-form-item>

                <a-form-item label="时间范围">
                  <a-select
                    v-model="searchParams.timeRange"
                    placeholder="全部"
                    style="width: 150px"
                    @change="handleSearch"
                  >
                    <a-option value="all">全部</a-option>
                    <a-option value="7day">近7天</a-option>
                    <a-option value="30day">近30天</a-option>
                    <a-option value="3month">近3个月</a-option>
                  </a-select>
                </a-form-item>

                <a-form-item label="关键词">
                  <a-input
                    v-model="searchParams.keyword"
                    allow-clear
                    placeholder="请输入公告标题关键词"
                    style="width: 200px"
                    @press.enter="handleSearch"
                  >
                    <template #prefix>
                      <icon-search />
                    </template>
                  </a-input>
                </a-form-item>

                <a-button type="outline" @click="handleSearch">
                  <icon-search />
                  筛选
                </a-button>
                <a-button type="outline" @click="resetSearch">
                  <icon-refresh />
                  重置
                </a-button>
              </a-space>
            </a-col>

            <!-- 操作按钮 -->
            <a-col :span="6" style="text-align: right">
              <a-space size="middle">
                <a-button type="primary" @click="openAddModal">
                  <icon-plus />
                  新增公告
                </a-button>
                <a-button
                  :disabled="selectedRowKeys.length === 0"
                  status="warning"
                  type="primary"
                  @click="openBatchOfflineModal"
                >
                  <icon-down-circle />
                  批量下架
                </a-button>
                <a-button
                  :disabled="selectedRowKeys.length === 0"
                  status="danger"
                  type="primary"
                  @click="openBatchDeleteModal"
                >
                  <icon-delete />
                  批量删除
                </a-button>
              </a-space>
            </a-col>
          </a-row>
        </a-form>
      </div>

      <!-- 公告管理列表 -->
      <a-table
        :columns="columns"
        :data="noticeList"
        :pagination="{
          showTotal: true,
          pageSize: searchParams.pageSize,
          current: searchParams.current,
          total: total,
        }"
        :row-selection="rowSelection"
        :selectedRowKeys="selectedRowKeys"
        row-key="id"
        @page-change="onPageChange"
        @selection-change="handleSelectionChange"
      >
        <!-- 操作列 -->
        <template #operation="{ record }">
          <a-space wrap>
            <a-button type="text" @click="openEditModal(record)">编辑</a-button>
            <a-button type="text" @click="previewNotice(record)">预览</a-button>
            <!-- 按状态显示不同操作 -->
            <a-button
              v-if="record.status !== 1"
              status="primary"
              type="text"
              @click="openPublishModal(record)"
            >
              发布
            </a-button>
            <a-button
              v-if="record.status === 1"
              status="warning"
              type="text"
              @click="openOfflineModal(record)"
            >
              下架
            </a-button>
            <a-button
              v-if="record.status === 2"
              status="primary"
              type="text"
              @click="openRepublishModal(record)"
            >
              重新发布
            </a-button>
            <a-button
              v-if="record.status !== 1"
              status="danger"
              type="text"
              @click="openDeleteModal(record)"
            >
              删除
            </a-button>
          </a-space>
        </template>
      </a-table>
    </a-card>

    <!-- 非管理员提示 -->
    <a-card v-else-if="activeView === 'manage' && userRole !== 'admin'">
      <div style="text-align: center; padding: 60px 0">
        <icon-warning-circle style="font-size: 48px; color: #faad14" />
        <div style="margin-top: 16px; font-size: 16px">
          暂无权限访问此页面，请切换为管理员账号登录
        </div>
      </div>
    </a-card>

    <!-- 新增/编辑公告模态框 -->
    <a-modal
      v-model:visible="modalVisible"
      :title="isEdit ? '编辑公告' : '新增公告'"
      destroy-on-close
      width="950px"
      @cancel="handleModalCancel"
      @ok="handleModalOk"
      :body-style="{ maxHeight: '750px', overflow: 'auto' }"
      :footer-style="{ borderTop: '1px solid #f0f0f0', padding: '20px' }"
    >
      <a-form
        ref="formRef"
        :model="formData"
        :rules="formRules"
        label-col-flex="120px"
        wrapper-col-flex="auto"
        class="notice-form"
        :style="{ padding: '0 20px' }"
      >
        <!-- 基本信息区域 -->
        <div class="form-section">
          <h3 class="section-title">基本信息</h3>

          <a-row :gutter="24" :style="{ marginBottom: '20px' }">
            <a-col :span="24">
              <a-form-item :required="true" field="title" label="公告标题">
                <a-input
                  v-model="formData.title"
                  maxlength="100"
                  placeholder="请输入公告标题（如：2025年党员发展工作安排）"
                  show-word-limit
                  size="large"
                  style="width: 100%"
                  :style="{
                    borderRadius: '8px',
                    boxShadow: '0 1px 2px rgba(0, 0, 0, 0.05)',
                  }"
                />
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="24">
            <!-- 类型选择器 -->
            <a-col :span="12">
              <a-form-item field="type" label="公告类型">
                <a-select
                  v-model="formData.type"
                  placeholder="请选择公告类型"
                  size="large"
                  style="width: 100%"
                  :style="{
                    borderRadius: '8px',
                    boxShadow: '0 1px 2px rgba(0, 0, 0, 0.05)',
                  }"
                >
                  <a-option value="notice">通知类</a-option>
                  <a-option value="activity">活动类</a-option>
                  <a-option value="remind">提醒类</a-option>
                  <a-option value="policy">政策类</a-option>
                </a-select>
              </a-form-item>
            </a-col>

            <!-- 是否置顶 -->
            <a-col :span="12">
              <a-form-item field="isTop" label="是否置顶">
                <div style="display: flex; align-items: center; height: 48px">
                  <a-switch v-model="formData.isTop" size="large" />
                  <span
                    style="margin-left: 12px; font-size: 13px; color: #86909c"
                  >
                    置顶公告将在列表优先展示
                  </span>
                </div>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="24">
            <!-- 所属党组织 -->
            <a-col :span="12">
              <a-form-item field="orgId" label="所属党组织">
                <a-select
                  v-model="formData.orgId"
                  placeholder="请选择所属党组织"
                  size="large"
                  style="width: 100%"
                  :style="{
                    borderRadius: '8px',
                    boxShadow: '0 1px 2px rgba(0, 0, 0, 0.05)',
                  }"
                  :disabled="userRole === 'org_admin'"
                >
                  <!-- 系统公告选项，只有super_admin可以选择 -->
                  <a-option v-if="userRole === 'super_admin'" value="null"
                    >系统公告</a-option
                  >
                  <!-- 党组织选项，根据用户角色动态生成 -->
                  <a-option
                    v-for="org in orgList"
                    :key="org.id"
                    :value="org.id"
                    >{{ org.name }}</a-option
                  >
                </a-select>
              </a-form-item>
            </a-col>
          </a-row>

          <a-row :gutter="24">
            <!-- 生效开始时间：仅在新增时显示 -->
            <a-col v-if="!isEdit" :span="12">
              <a-form-item field="publishTime" label="生效开始时间">
                <a-date-picker
                  showTime
                  v-model="formData.publishTime"
                  placeholder="选择生效开始时间"
                  style="width: 100%"
                  size="large"
                  :style="{
                    borderRadius: '8px',
                    boxShadow: '0 1px 2px rgba(0, 0, 0, 0.05)',
                  }"
                />
              </a-form-item>
            </a-col>

            <!-- 生效结束时间 -->
            <a-col :span="12">
              <a-form-item field="expireTime" label="生效结束时间">
                <a-date-picker
                  showTime
                  v-model="formData.expireTime"
                  placeholder="选择生效结束时间（为空则永久有效）"
                  style="width: 100%"
                  size="large"
                  :style="{
                    borderRadius: '8px',
                    boxShadow: '0 1px 2px rgba(0, 0, 0, 0.05)',
                  }"
                />
              </a-form-item>
            </a-col>
          </a-row>
        </div>

        <!-- 公告内容区域 -->
        <div class="form-section">
          <h3 class="section-title">公告内容</h3>

          <a-row :gutter="24">
            <a-col :span="24">
              <a-form-item :required="true" field="content" label="公告正文">
                <!-- 富文本编辑器 -->
                <div
                  style="
                    border: 1px solid #e5e6eb;
                    border-radius: 8px;
                    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
                    overflow: hidden;
                  "
                >
                  <Toolbar
                    :defaultConfig="toolbarConfig"
                    :editor="editorRef"
                    :mode="mode"
                    style="
                      border-bottom: 1px solid #e5e6eb;
                      background-color: #fafafa;
                      padding: 8px 16px;
                    "
                  />
                  <Editor
                    v-model="formData.content"
                    :defaultConfig="editorConfig"
                    :mode="mode"
                    style="height: 300px; overflow-y: auto"
                    @onCreated="handleEditorCreated"
                  />
                </div>
              </a-form-item>
            </a-col>
          </a-row>
        </div>

        <!-- 附件区域 -->
        <div class="form-section">
          <h3 class="section-title">附件管理</h3>

          <a-row :gutter="24">
            <a-col :span="24">
              <a-form-item field="attachments" label="公告附件">
                <div
                  style="
                    background-color: #f8f9fa;
                    border: 2px dashed #e1e5e9;
                    border-radius: 8px;
                    padding: 40px 20px;
                    text-align: center;
                    transition: all 0.3s ease;
                  "
                  :style="{
                    borderColor:
                      formData.fileList.length > 0 ? '#165dff' : '#e1e5e9',
                    backgroundColor:
                      formData.fileList.length > 0 ? '#f0f5ff' : '#f8f9fa',
                  }"
                >
                  <a-upload
                    v-model="formData.fileList"
                    :file-list-max="5"
                    :multiple="true"
                    :show-file-list="true"
                    accept=".docx,.doc,.pdf,.xlsx,.xls,.jpg,.png"
                    action="/api/upload/notice/attachment"
                    @change="handleFileChange"
                  >
                    <div
                      style="
                        display: flex;
                        flex-direction: column;
                        align-items: center;
                        cursor: pointer;
                      "
                    >
                      <icon-upload
                        style="
                          font-size: 40px;
                          color: #165dff;
                          margin-bottom: 16px;
                        "
                      />
                      <a-button
                        type="primary"
                        size="large"
                        :style="{ borderRadius: '6px' }"
                      >
                        点击上传附件（最多5个）
                      </a-button>
                    </div>
                  </a-upload>
                  <div
                    style="
                      margin-top: 16px;
                      font-size: 13px;
                      color: #86909c;
                      text-align: center;
                    "
                  >
                    支持格式：docx/doc/pdf/xlsx/xls/jpg/png，单文件大小≤20MB
                  </div>
                </div>
              </a-form-item>
            </a-col>
          </a-row>
        </div>

        <!-- 状态设置区域 -->
        <div v-if="isEdit" class="form-section">
          <h3 class="section-title">状态设置</h3>

          <a-row :gutter="24">
            <a-col :span="24">
              <a-form-item field="status" label="公告状态">
                <a-radio-group v-model="formData.status" size="large">
                  <a-radio
                    value="0"
                    style="
                      margin-right: 40px;
                      font-size: 14px;
                      padding: 8px 16px;
                      border-radius: 6px;
                      transition: all 0.2s ease;
                    "
                    :style="{
                      backgroundColor:
                        formData.status === '0' ? '#f0f5ff' : 'transparent',
                    }"
                  >
                    草稿（仅自己可见）
                  </a-radio>
                  <a-radio
                    value="1"
                    style="
                      margin-right: 40px;
                      font-size: 14px;
                      padding: 8px 16px;
                      border-radius: 6px;
                      transition: all 0.2s ease;
                    "
                    :style="{
                      backgroundColor:
                        formData.status === '1' ? '#f0f5ff' : 'transparent',
                    }"
                  >
                    已发布（全角色可见）
                  </a-radio>
                  <a-radio
                    value="2"
                    style="
                      font-size: 14px;
                      padding: 8px 16px;
                      border-radius: 6px;
                      transition: all 0.2s ease;
                    "
                    :style="{
                      backgroundColor:
                        formData.status === '2' ? '#f0f5ff' : 'transparent',
                    }"
                  >
                    已下架（仅管理员可见）
                  </a-radio>
                </a-radio-group>
              </a-form-item>
            </a-col>
          </a-row>
        </div>
      </a-form>
    </a-modal>

    <!-- 公告预览弹窗 -->
    <a-modal
      v-model:visible="previewVisible"
      :footer="null"
      title="公告预览"
      width="800px"
      @cancel="previewVisible = false"
    >
      <div v-if="previewNoticeData" class="notice-preview">
        <!-- 预览头部 -->
        <div
          class="preview-header"
          style="
            border-bottom: 1px solid #eee;
            padding-bottom: 16px;
            margin-bottom: 16px;
          "
        >
          <h2 style="margin: 0; font-size: 20px; font-weight: 600">
            {{ previewNoticeData.title }}
            <a-tag
              v-if="previewNoticeData.isTop"
              color="red"
              style="margin-left: 8px"
              >置顶
            </a-tag>
          </h2>
          <div style="margin-top: 8px; font-size: 14px; color: #86909c">
            <span
              >公告类型：{{
                previewNoticeData.type === "notice"
                  ? "通知类"
                  : previewNoticeData.type === "activity"
                  ? "活动类"
                  : previewNoticeData.type === "remind"
                  ? "提醒类"
                  : "政策类"
              }}</span
            >
            <span style="margin: 0 8px">|</span>
            <span>发布人：{{ previewNoticeData.publisherName }}</span>
            <span style="margin: 0 8px">|</span>
            <span>发布时间：{{ previewNoticeData.publishTime }}</span>
            <span style="margin: 0 8px">|</span>
            <span
              >状态：{{
                previewNoticeData.status === "draft"
                  ? "草稿"
                  : previewNoticeData.status === "published"
                  ? "已发布"
                  : "已下架"
              }}</span
            >
          </div>
        </div>

        <!-- 预览正文 -->
        <div
          class="preview-content"
          style="line-height: 1.8; font-size: 14px"
          v-html="previewNoticeData.content"
        ></div>

        <!-- 预览附件 -->
        <div
          v-if="
            previewNoticeData.attachments &&
            previewNoticeData.attachments.length
          "
          style="
            margin-top: 24px;
            border-top: 1px solid #eee;
            padding-top: 16px;
          "
        >
          <h4 style="margin: 0 0 8px 0; font-size: 14px; font-weight: 600">
            附件：
          </h4>
          <a-space direction="vertical" style="width: 100%">
            <div
              v-for="(file, index) in previewNoticeData.attachments"
              :key="index"
              style="display: flex; align-items: center"
            >
              <icon-paper-clip style="margin-right: 8px; color: #86909c" />
              <a-link @click="downloadAttachment(file)">{{ file.name }}</a-link>
            </div>
          </a-space>
        </div>
      </div>
    </a-modal>

    <!-- 下架公告模态框 -->
    <a-modal
      v-model:visible="offlineModalVisible"
      title="下架公告"
      @cancel="handleOfflineCancel"
      @ok="handleOfflineConfirm"
    >
      <div style="padding: 20px 0">
        <p style="font-size: 14px; line-height: 1.6">
          确定要下架【{{ currentOfflineRecord?.title }}】吗？下架后仅管理员可见
        </p>
      </div>
    </a-modal>

    <!-- 重新发布公告模态框 -->
    <a-modal
      v-model:visible="republishModalVisible"
      title="重新发布公告"
      @cancel="handleRepublishCancel"
      @ok="handleRepublishConfirm"
    >
      <div style="padding: 20px 0">
        <p style="font-size: 14px; line-height: 1.6">
          确定要重新发布【{{
            currentRepublishRecord?.title
          }}】吗？发布后所有角色均可查看
        </p>
      </div>
    </a-modal>

    <!-- 发布公告模态框 -->
    <a-modal
      v-model:visible="publishModalVisible"
      title="发布公告"
      @cancel="handlePublishCancel"
      @ok="handlePublishConfirm"
    >
      <div style="padding: 20px 0">
        <p style="font-size: 14px; line-height: 1.6">
          确定要发布【{{
            currentPublishRecord?.title
          }}】吗？发布后所有角色均可查看
        </p>
      </div>
    </a-modal>

    <!-- 删除公告模态框 -->
    <a-modal
      v-model:visible="deleteModalVisible"
      title="删除公告"
      @cancel="handleDeleteCancel"
      @ok="handleDeleteConfirm"
      status="danger"
    >
      <div style="padding: 20px 0">
        <p style="font-size: 14px; line-height: 1.6">
          确定要删除【{{ currentDeleteRecord?.title }}】吗？删除后不可恢复！
        </p>
      </div>
    </a-modal>

    <!-- 批量删除公告模态框 -->
    <a-modal
      v-model:visible="batchDeleteModalVisible"
      title="批量删除"
      @cancel="handleBatchDeleteCancel"
      @ok="handleBatchDeleteConfirm"
      status="danger"
    >
      <div style="padding: 20px 0">
        <p style="font-size: 14px; line-height: 1.6">
          确定要删除选中的{{ selectedRowKeys.length }}条公告吗？删除后不可恢复！
        </p>
      </div>
    </a-modal>

    <!-- 批量下架公告模态框 -->
    <a-modal
      v-model:visible="batchOfflineModalVisible"
      title="批量下架"
      @cancel="handleBatchOfflineCancel"
      @ok="handleBatchOfflineConfirm"
      status="warning"
    >
      <div style="padding: 20px 0">
        <p style="font-size: 14px; line-height: 1.6">
          确定要下架选中的{{
            selectedRowKeys.length
          }}条公告吗？下架后仅管理员可见
        </p>
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { onUnmounted, reactive, ref, watchEffect } from "vue";
import type { FormInstance } from "@arco-design/web-vue/es/form";
import dayjs from "dayjs";
// 导入Arco图标
import {
  IconDelete,
  IconDownCircle,
  IconEdit,
  IconList,
  IconPlus,
  IconRefresh,
  IconSearch,
  IconUpload,
} from "@arco-design/web-vue/es/icon";
// 导入富文本编辑器
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";
import type { IDomEditor } from "@wangeditor/editor";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
// 导入API
import * as gonggaoguanli from "@/api/gonggaoguanli";
import * as gonggaokuozhangongneng from "@/api/gonggaokuozhangongneng";
import * as yonghuguanli from "@/api/yonghuguanli";
import * as dangzuzhiguanli from "@/api/dangzuzhiguanli";
import { message } from "ant-design-vue";

const router = useRouter();
const store = useStore();

// 1. 基础配置
// 从登录态获取用户角色
const userRole = ref("student"); // 初始值，后续会从store更新
// 用户组织信息
const userOrgId = ref<string | null>(null);
const userOrgName = ref<string | null>(null);
// 党组织列表
const orgList = ref<Array<{ id: string; name: string }>>([]);

// 获取用户信息
const getUserInfo = async () => {
  try {
    const res = await yonghuguanli.getLoginUserUsingGet();
    if (res.data.code === 0 && res.data.data) {
      const loginUser = res.data.data;
      if (loginUser.userRole) {
        userRole.value = loginUser.userRole;
      }
      if (loginUser.orgId) {
        userOrgId.value = loginUser.orgId;
      }
      if (loginUser.orgName) {
        userOrgName.value = loginUser.orgName;
      }

      // 根据用户角色获取党组织列表
      if (userRole.value === "super_admin") {
        // 从API获取所有党组织列表
        try {
          const orgRes = await dangzuzhiguanli.listOrganizationsUsingGet();
          if (orgRes.data.code === 0 && orgRes.data.data) {
            orgList.value = orgRes.data.data.map((org: any) => ({
              id: org.id.toString(),
              name: org.orgName,
            }));
          }
        } catch (error) {
          console.error("获取党组织列表失败:", error);
          // 如果获取失败，使用默认数据
          orgList.value = [
            { id: "1", name: "广州城市理工学院" },
            { id: "2", name: "计算机工程学院/大数据学院" },
          ];
        }
      } else if (
        userRole.value === "org_admin" &&
        userOrgId.value &&
        userOrgName.value
      ) {
        // 对于org_admin，只显示当前组织
        orgList.value = [{ id: userOrgId.value, name: userOrgName.value }];
        // 自动设置为当前组织
        formData.orgId = userOrgId.value;
      }
    }
  } catch (error) {
    console.error("获取用户信息失败:", error);
  }
};

// 页面加载时获取用户信息
getUserInfo();

// 视图切换
const activeView = ref("manage"); // manage/list/stat
const switchView = (view: string) => {
  activeView.value = view;
  if (view === "list") {
    message.info("已切换至公告列表视图");
    toShowAnnouncementListView();
  } else if (view === "stat") {
    message.info("已切换至公告统计视图");
    toShowAnnouncementDetailsView();
  }
};

// 2. 搜索参数
const searchParams = reactive({
  current: 1,
  pageSize: 10,
  type: "all", // 公告类型
  status: "all", // 发布状态
  timeRange: "all", // 时间范围
  keyword: "", // 关键词
});
const total = ref(0);
const selectedRowKeys = ref<string[]>([]); // 表格选中行

// 表格选择配置
const rowSelection = reactive({
  type: "checkbox",
  showCheckedAll: true,
  onlyCurrent: false,
});
const handleSelectionChange = (keys: string[]) => {
  selectedRowKeys.value = keys;
};

// 3. 公告列表
const noticeList = ref<any[]>([]);

// 4. 表格列配置
const columns = [
  {
    title: "公告标题",
    dataIndex: "title",
    ellipsis: true,
    tooltip: true,
    width: 300,
  },
  {
    title: "发布人",
    dataIndex: "publisherName",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "所属组织",
    dataIndex: "orgName",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "发布时间",
    dataIndex: "publishTime",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "过期时间",
    dataIndex: "expireTime",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "是否置顶",
    dataIndex: "isTop",
    ellipsis: true,
    tooltip: true,
    render: (text: any) => {
      return text == 1 ? "是" : "否";
    },
  },
  {
    title: "发布状态",
    dataIndex: "statusName",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "操作",
    slotName: "operation",
    width: 350,
  },
];

// 5. 公告类型/状态颜色映射
const getTypeColor = (type: string) => {
  switch (type) {
    case "notice":
      return "blue";
    case "activity":
      return "green";
    case "remind":
      return "orange";
    case "policy":
      return "purple";
    default:
      return "gray";
  }
};

const getStatusColor = (status: string) => {
  switch (status) {
    case "draft":
      return "gray";
    case "published":
      return "green";
    case "offline":
      return "red";
    default:
      return "gray";
  }
};

// 6. 分页事件
const onPageChange = (page: number) => {
  searchParams.current = page;
  loadNoticeList();
};

// 7. 加载公告列表
const loadNoticeList = async () => {
  try {
    const res = await gonggaoguanli.listNoticeVoByPageUsingGet({
      type: searchParams.type === "all" ? undefined : searchParams.type,
      status: searchParams.status === "all" ? undefined : searchParams.status,
      keyword: searchParams.keyword || undefined,
      current: searchParams.current,
      pageSize: searchParams.pageSize,
    });
    console.log("res:", res);
    if (res.data.code === 0) {
      // 直接使用接口返回的数据，确保所有字段都被包含
      noticeList.value = res.data.data;
      total.value = res.data.length;
    } else {
      message.error(res.data.message || "获取公告列表失败");
    }
  } catch (error) {
    console.error("获取公告列表失败:", error);
    message.error("网络请求异常");
  }
};

// 8. 筛选/重置
const handleSearch = async () => {
  searchParams.current = 1;
  await loadNoticeList();
  message.success("筛选成功");
};
const resetSearch = async () => {
  searchParams.type = "all";
  searchParams.status = "all";
  searchParams.timeRange = "all";
  searchParams.keyword = "";
  searchParams.current = 1;
  await loadNoticeList();
};

// 9. 富文本编辑器配置
const editorRef = ref<IDomEditor | null>(null);
const mode = ref("default");
const toolbarConfig = reactive({
  excludeKeys: ["uploadVideo", "insertTable", "codeBlock"], // 排除不需要的功能
});
const editorConfig = reactive({
  placeholder: "请输入公告正文（支持图文混排）",
  MENU_CONF: {
    uploadImage: {
      server: "/api/upload/notice/image", // 图片上传接口
      maxFileSize: 20 * 1024 * 1024, // 20MB
    },
  },
});

// 编辑器创建回调
const handleEditorCreated = (editor: IDomEditor) => {
  editorRef.value = editor;
};

// 销毁编辑器（防止内存泄漏）
onUnmounted(() => {
  const editor = editorRef.value;
  if (editor == null) return;
  editor.destroy();
});

// 10. 新增/编辑模态框
const modalVisible = ref(false);
const isEdit = ref(false);
const formRef = ref<FormInstance>();
const formData = reactive({
  id: "",
  title: "",
  type: "",
  isTop: false,
  publishTime: "",
  expireTime: "",
  content: "",
  fileList: [] as any[],
  attachments: [] as any[],
  status: 0,
  publisherName: "超级管理员",
  orgId: null,
});

// 表单校验规则
const formRules = reactive({
  title: [{ required: true, message: "请输入公告标题" }],
  content: [{ required: true, message: "请输入公告正文" }],
});

// 文件上传回调
const handleFileChange = (fileList: any[]) => {
  formData.fileList = fileList;
  // 模拟上传成功后整理附件列表
  formData.attachments = fileList
    .filter((file) => file.status === "done")
    .map((file) => ({
      name: file.name,
      url: file.response?.url || `/attachments/${file.name}`,
    }));
};

// 打开新增模态框
const openAddModal = () => {
  isEdit.value = false;
  // 重置表单
  formData.id = "";
  formData.title = "";
  formData.type = "";
  formData.isTop = false;
  formData.publishTime = dayjs().format("YYYY-MM-DD");
  formData.expireTime = "";
  formData.content = "";
  formData.fileList = [];
  formData.attachments = [];
  formData.status = 0;
  formData.publishTime = "";
  modalVisible.value = true;
};

// 打开编辑模态框
const openEditModal = (record: any) => {
  try {
    isEdit.value = true;
    // 填充表单数据
    formData.id = record.id;
    formData.title = record.title;
    formData.type = record.type || "notice"; // 默认类型为notice
    formData.isTop = record.isTop === 1;
    formData.publishTime = record.publishTime;
    formData.expireTime = record.expireTime;
    formData.content = record.content;
    formData.fileList = record.attachments
      ? record.attachments.map((file: any) => ({
          name: file.name,
          status: "done",
          url: file.url,
        }))
      : [];
    formData.attachments = record.attachments || [];
    formData.status = record.status;
    formData.publishTime = record.publishTime;
    formData.orgId = record.orgId;
    modalVisible.value = true;
  } catch (error) {
    console.error("打开编辑模态框失败:", error);
    message.error("打开编辑模态框失败");
  }
};

// 模态框确认
const handleModalOk = async () => {
  if (!formRef.value) return;
  try {
    if (isEdit.value) {
      // 编辑公告
      const res = await gonggaoguanli.updateNoticeUsingPost({
        id: Number(formData.id),
        title: formData.title,
        content: formData.content,
        expireTime: formData.expireTime,
        isTop: formData.isTop ? 1 : 0,
        status: formData.status,
      });
      if (res.data.code === 0) {
        message.success("编辑公告成功");
        modalVisible.value = false;
        await loadNoticeList();
      } else {
        message.error(res.data.message || "编辑公告失败");
      }
    } else {
      // 新增公告
      const res = await gonggaoguanli.addNoticeUsingPost({
        title: formData.title,
        content: formData.content,
        publishTime: formData.publishTime,
        expireTime: formData.expireTime,
        isTop: formData.isTop ? 1 : 0,
        status: formData.status ? 0 : 1,
        orgId: Number(formData.orgId),
      });
      console.log("res:", res);
      if (res.data.code === 0) {
        message.success("新增公告成功");
        modalVisible.value = false;
        await loadNoticeList();
      } else {
        message.error(res.data.message || "新增公告失败");
      }
    }
  } catch (error) {
    console.error("操作公告失败:", error);
    message.error("网络请求异常");
  }
};

// 模态框取消
const handleModalCancel = () => {
  modalVisible.value = false;
  formRef.value?.resetFields();
};

// 11. 公告预览
const previewVisible = ref(false);
const previewNoticeData = ref<any>(null);

const previewNotice = async (record: any) => {
  try {
    const res = await gonggaoguanli.getNoticeVoUsingGet({ id: record.id });
    if (res.data.code === 0) {
      previewNoticeData.value = res.data.data;
      previewVisible.value = true;
    } else {
      message.error(res.data.message || "获取公告详情失败");
    }
  } catch (error) {
    console.error("获取公告详情失败:", error);
    message.error("网络请求异常");
  }
};

// 下载附件
const downloadAttachment = async (file: any) => {
  try {
    const res =
      await gonggaokuozhangongneng.downloadAnnouncementAttachmentUsingGet({
        attachmentId: file.id,
      });
    if (res.data.code === 0) {
      message.success(`已开始下载【${file.name}】`);
      // 实际项目中处理下载
    } else {
      message.error(res.data.message || "下载附件失败");
    }
  } catch (error) {
    console.error("下载附件失败:", error);
    message.error("网络请求异常");
  }
};

// 发布公告模态框
const publishModalVisible = ref(false);
const currentPublishRecord = ref<any>(null);

const openPublishModal = (record: any) => {
  currentPublishRecord.value = record;
  publishModalVisible.value = true;
};

const handlePublishConfirm = async () => {
  if (!currentPublishRecord.value) return;
  try {
    const res = await gonggaoguanli.publishNoticeUsingPost({
      id: currentPublishRecord.value.id,
    });
    if (res.data.code === 0) {
      message.success("公告发布成功");
      publishModalVisible.value = false;
      currentPublishRecord.value = null;
      await loadNoticeList();
    } else {
      message.error(res.data.message || "发布公告失败");
    }
  } catch (error) {
    console.error("发布公告失败:", error);
    message.error("网络请求异常");
  }
};

const handlePublishCancel = () => {
  publishModalVisible.value = false;
  currentPublishRecord.value = null;
};

// 下架公告模态框
const offlineModalVisible = ref(false);
const currentOfflineRecord = ref<any>(null);

const openOfflineModal = (record: any) => {
  console.log("点击了下架");
  currentOfflineRecord.value = record;
  offlineModalVisible.value = true;
};

const handleOfflineConfirm = async () => {
  if (!currentOfflineRecord.value) return;
  try {
    const res = await gonggaoguanli.offlineNoticeUsingPut({
      id: currentOfflineRecord.value.id,
    });
    console.log("res：", res);
    if (res.data.code === 0) {
      message.success("公告下架成功");
      offlineModalVisible.value = false;
      currentOfflineRecord.value = null;
      await loadNoticeList();
    } else {
      message.error(res.data.message || "下架公告失败");
    }
  } catch (error) {
    message.error("网络请求异常");
  }
};

const handleOfflineCancel = () => {
  offlineModalVisible.value = false;
  currentOfflineRecord.value = null;
};

// 重新发布公告模态框
const republishModalVisible = ref(false);
const currentRepublishRecord = ref<any>(null);

const openRepublishModal = (record: any) => {
  currentRepublishRecord.value = record;
  republishModalVisible.value = true;
};

const handleRepublishConfirm = async () => {
  if (!currentRepublishRecord.value) return;
  try {
    const res = await gonggaoguanli.republishNoticeUsingPut({
      id: currentRepublishRecord.value.id,
    });
    console.log("res：", res);
    if (res.data.code === 0) {
      message.success("公告重新发布成功");
      republishModalVisible.value = false;
      currentRepublishRecord.value = null;
      await loadNoticeList();
    } else {
      message.error(res.data.message || "重新发布公告失败");
    }
  } catch (error) {
    message.error("网络请求异常");
  }
};

const handleRepublishCancel = () => {
  republishModalVisible.value = false;
  currentRepublishRecord.value = null;
};

// 删除公告模态框
const deleteModalVisible = ref(false);
const currentDeleteRecord = ref<any>(null);

const openDeleteModal = (record: any) => {
  currentDeleteRecord.value = record;
  deleteModalVisible.value = true;
};

const handleDeleteConfirm = async () => {
  if (!currentDeleteRecord.value) return;
  try {
    const res = await gonggaoguanli.deleteNoticeUsingPost({
      id: currentDeleteRecord.value.id,
    });
    if (res.data.code === 0) {
      message.success("公告删除成功");
      deleteModalVisible.value = false;
      currentDeleteRecord.value = null;
      await loadNoticeList();
    } else {
      message.error(res.data.message || "删除公告失败");
    }
  } catch (error) {
    console.error("删除公告失败:", error);
    message.error("网络请求异常");
  }
};

const handleDeleteCancel = () => {
  deleteModalVisible.value = false;
  currentDeleteRecord.value = null;
};

// 批量下架模态框
const batchOfflineModalVisible = ref(false);

const openBatchOfflineModal = () => {
  if (selectedRowKeys.value.length === 0) {
    message.warning("请先选择要下架的公告");
    return;
  }
  batchOfflineModalVisible.value = true;
};

const handleBatchOfflineConfirm = async () => {
  try {
    // 这里需要循环调用下架接口，因为API没有批量下架接口
    let successCount = 0;
    for (const id of selectedRowKeys.value) {
      const res = await gonggaoguanli.offlineNoticeUsingPut({ id });
      if (res.code === 0) {
        successCount++;
      }
    }
    message.success(`成功下架${successCount}条公告`);
    batchOfflineModalVisible.value = false;
    selectedRowKeys.value = [];
    await loadNoticeList();
  } catch (error) {
    console.error("批量下架失败:", error);
    message.error("网络请求异常");
  }
};

const handleBatchOfflineCancel = () => {
  batchOfflineModalVisible.value = false;
};

// 批量删除模态框
const batchDeleteModalVisible = ref(false);

const openBatchDeleteModal = () => {
  if (selectedRowKeys.value.length === 0) {
    message.warning("请先选择要删除的公告");
    return;
  }
  batchDeleteModalVisible.value = true;
};

const handleBatchDeleteConfirm = async () => {
  try {
    const res = await gonggaoguanli.batchDeleteNoticesUsingPost(
      selectedRowKeys.value.map(Number),
    );
    if (res.data.code === 0) {
      message.success("批量删除成功");
      batchDeleteModalVisible.value = false;
      selectedRowKeys.value = [];
      await loadNoticeList();
    } else {
      message.error(res.data.message || "批量删除失败");
    }
  } catch (error) {
    console.error("批量删除失败:", error);
    message.error("网络请求异常");
  }
};

const handleBatchDeleteCancel = () => {
  batchDeleteModalVisible.value = false;
};

// 初始加载数据
watchEffect(async () => {
  await loadNoticeList();
});

const toShowAnnouncementListView = () => {
  router.push("/announcementList");
};

const toShowAnnouncementDetailsView = () => {
  router.push("/announcementDetails");
};
</script>

<style scoped>
/* 页面样式 */
.notice-manage-container {
  padding: 24px;
  background: #f5f5f5;
  min-height: 100vh;
}

/* 页面头部样式 */
.page-header {
  margin-bottom: 24px;
}

.breadcrumb {
  margin-bottom: 8px;
}

.page-header h1 {
  font-size: 24px;
  font-weight: 600;
  margin: 0;
  color: #333;
}

/* 视图切换栏样式 */
.view-switch-bar {
  margin-bottom: 24px;
  padding: 16px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

/* 卡片样式 */
.list-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

/* 筛选+操作区样式 */
.filter-operation-bar {
  margin-bottom: 16px;
  padding: 16px;
  background: #fafafa;
  border-radius: 4px;
}

.filter-form {
  width: 100%;
}

/* 富文本编辑器样式适配 */
:deep(.w-e-toolbar) {
  padding: 8px 16px !important;
  border-bottom: 1px solid #e5e6eb !important;
  background-color: #fafafa !important;
}

:deep(.w-e-text-container) {
  padding: 16px !important;
  min-height: 300px !important;
}

/* 预览弹窗样式 */
.notice-preview {
  padding: 8px 0;
}

/* 预览内容中的图片样式 */
:deep(.preview-content img) {
  max-width: 100%;
  height: auto;
  display: block;
  margin: 10px 0;
  border-radius: 4px;
}

/* 确保预览内容容器不会溢出 */
.preview-content {
  overflow: hidden;
  word-wrap: break-word;
}

/* 表格样式适配 */
:deep(.arco-table) {
  --arco-table-header-text-color: #1d2129;
  --arco-table-body-text-color: #4e5969;
}

/* 模态框表单样式 */
:deep(.arco-form-item) {
  margin-bottom: 20px;
}

:deep(.arco-form-item-label) {
  font-weight: 500;
  color: #333;
}

:deep(.arco-input-large) {
  height: 48px;
  font-size: 14px;
}

:deep(.arco-select-large .arco-select-view) {
  height: 48px;
  font-size: 14px;
}

:deep(.arco-date-picker-large .arco-input) {
  height: 48px;
  font-size: 14px;
}

/* 分页样式 */
.pagination {
  margin-top: 16px;
  text-align: right;
  padding: 16px;
  background: #fafafa;
  border-top: 1px solid #e8e8e8;
}

/* 表单区域样式 */
.form-section {
  margin-bottom: 32px;
  padding-bottom: 24px;
  border-bottom: 1px solid #f0f0f0;
}

.form-section:last-child {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}

.section-title {
  margin: 0 0 20px 0;
  font-size: 16px;
  font-weight: 600;
  color: #333;
  padding-bottom: 8px;
  border-bottom: 2px solid #165dff;
  display: inline-block;
}

/* 上传区域样式 */
:deep(.arco-upload-list-item) {
  border-radius: 6px;
  margin-top: 12px;
}

:deep(.arco-upload-list-item-name) {
  font-size: 13px;
  color: #4e5969;
}

:deep(.arco-upload-list-item-status-success) {
  border-color: #52c41a;
  background-color: #f6ffed;
}

:deep(.arco-upload-list-item-status-error) {
  border-color: #ff4d4f;
  background-color: #fff1f0;
}

/* 按钮样式 */
:deep(.arco-button-primary) {
  border-radius: 6px;
  font-weight: 500;
}

:deep(.arco-button-outline) {
  border-radius: 6px;
}

/* 开关样式 */
:deep(.arco-switch-large .arco-switch-core) {
  width: 52px;
  height: 28px;
}

:deep(.arco-switch-large .arco-switch-core .arco-switch-button) {
  width: 24px;
  height: 24px;
}

/* 单选框样式 */
:deep(.arco-radio) {
  font-size: 14px;
}

:deep(.arco-radio .arco-radio-inner) {
  width: 18px;
  height: 18px;
}

:deep(.arco-radio-checked .arco-radio-inner) {
  border-color: #165dff;
  background-color: #165dff;
}

:deep(.arco-radio-checked .arco-radio-inner::after) {
  width: 10px;
  height: 10px;
}
</style>

<!-- 富文本编辑器样式引入 -->
<style src="@wangeditor/editor/dist/css/style.css"></style>
