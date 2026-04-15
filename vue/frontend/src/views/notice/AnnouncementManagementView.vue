<template>
  <!-- 页面整体框架 -->
  <div class="notice-manage-page">
    <!-- 视图切换栏 -->
    <a-card style="margin-bottom: 16px">
      <a-space size="large">
        <a-button
            :status="activeView === 'manage' ? 'primary' : 'normal'"
            type="primary"
            @click="switchView('manage')"
        >
          <icon-edit/>
          公告管理
        </a-button>
        <a-button
            :status="activeView === 'list' ? 'primary' : 'normal'"
            @click="switchView('list')"
        >
          <icon-list/>
          公告列表
        </a-button>
        <a-button
            :status="activeView === 'stat' ? 'primary' : 'normal'"
            @click="switchView('stat')"
        >
          <icon-chart/>
          公告统计
        </a-button>
        <!-- 权限提示：仅管理员可见公告管理 -->
        <a-tag v-if="userRole !== 'admin'" color="orange" disabled>
          <icon-warning/>
          仅管理员可管理公告
        </a-tag>
      </a-space>
    </a-card>

    <!-- 公告管理核心区域（仅管理员可见） -->
    <a-card v-if="activeView === 'manage' && userRole === 'admin'">
      <!-- 筛选+操作区 -->
      <div
          class="filter-operation-bar"
          style="
          margin-bottom: 16px;
          padding-bottom: 16px;
          border-bottom: 1px solid #eee;
        "
      >
        <a-row :gutter="16" align="middle">
          <!-- 筛选条件 -->
          <a-col :span="18">
            <a-space size="middle">
              <a-form-item label="公告类型" label-col-flex="80px">
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

              <a-form-item label="发布状态" label-col-flex="80px">
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

              <a-form-item label="时间范围" label-col-flex="80px">
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

              <a-form-item label="关键词" label-col-flex="60px">
                <a-input
                    v-model="searchParams.keyword"
                    allow-clear
                    placeholder="请输入公告标题关键词"
                    style="width: 200px"
                    @press.enter="handleSearch"
                >
                  <template #prefix>
                    <icon-search/>
                  </template>
                </a-input>
              </a-form-item>

              <a-button type="outline" @click="handleSearch">
                <icon-search/>
                筛选
              </a-button>
              <a-button type="outline" @click="resetSearch">
                <icon-refresh/>
                重置
              </a-button>
            </a-space>
          </a-col>

          <!-- 操作按钮 -->
          <a-col :span="6" style="text-align: right">
            <a-space size="middle">
              <a-button type="primary" @click="openAddModal">
                <icon-plus/>
                新增公告
              </a-button>
              <a-button
                  :disabled="selectedRowKeys.length === 0"
                  status="warning"
                  type="primary"
                  @click="batchOffline"
              >
                <icon-down-circle/>
                批量下架
              </a-button>
              <a-button
                  :disabled="selectedRowKeys.length === 0"
                  status="danger"
                  type="primary"
                  @click="batchDelete"
              >
                <icon-delete/>
                批量删除
              </a-button>
            </a-space>
          </a-col>
        </a-row>
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
        <!-- 公告类型列自定义渲染 -->
        <template #type="{ record }">
          <a-tag :color="getTypeColor(record.type)">
            {{
              record.type === "notice"
                  ? "通知类"
                  : record.type === "activity"
                      ? "活动类"
                      : record.type === "remind"
                          ? "提醒类"
                          : "政策类"
            }}
          </a-tag>
        </template>

        <!-- 发布状态列自定义渲染 -->
        <template #status="{ record }">
          <a-tag :color="getStatusColor(record.status)">
            {{
              record.status === "draft"
                  ? "草稿"
                  : record.status === "published"
                      ? "已发布"
                      : "已下架"
            }}
          </a-tag>
        </template>

        <!-- 操作列 -->
        <template #operation="{ record }">
          <a-space wrap>
            <a-button type="text" @click="openEditModal(record)">编辑</a-button>
            <a-button type="text" @click="previewNotice(record)">预览</a-button>
            <!-- 按状态显示不同操作 -->
            <a-button
                v-if="record.status === 'draft'"
                status="primary"
                type="text"
                @click="publishNotice(record)"
            >
              发布
            </a-button>
            <a-button
                v-if="record.status === 'published'"
                status="warning"
                type="text"
                @click="offlineNotice(record)"
            >
              下架
            </a-button>
            <a-button
                v-if="record.status === 'offline'"
                status="primary"
                type="text"
                @click="republishNotice(record)"
            >
              重新发布
            </a-button>
            <a-button
                v-if="record.status !== 'published'"
                status="danger"
                type="text"
                @click="deleteNotice(record)"
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
        <icon-warning-circle style="font-size: 48px; color: #faad14"/>
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
        width="900px"
        @cancel="handleModalCancel"
        @ok="handleModalOk"
    >
      <a-form
          ref="formRef"
          :model="formData"
          :rules="formRules"
          label-col-flex="100px"
          wrapper-col-flex="auto"
      >
        <a-row :gutter="16">
          <a-col :span="24">
            <a-form-item :required="true" field="title" label="公告标题">
              <a-input
                  v-model="formData.title"
                  maxlength="100"
                  placeholder="请输入公告标题（如：2025年党员发展工作安排）"
                  show-word-limit
              />
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item :required="true" field="type" label="公告类型">
              <a-select v-model="formData.type" placeholder="请选择公告类型">
                <a-option value="notice">通知类（正式通知/工作安排）</a-option>
                <a-option value="activity"
                >活动类（党日/学习活动通知）
                </a-option>
                <a-option value="remind">提醒类（学习/材料提交提醒）</a-option>
                <a-option value="policy">政策类（党建政策/文件解读）</a-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item field="isTop" label="是否置顶">
              <a-switch v-model="formData.isTop"/>
              <span style="margin-left: 8px; font-size: 12px; color: #86909c">
                置顶公告将在列表优先展示
              </span>
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item field="validStart" label="生效开始时间">
              <a-date-picker
                  v-model="formData.validStart"
                  placeholder="选择生效开始时间"
                  style="width: 100%"
              />
            </a-form-item>
          </a-col>

          <a-col :span="12">
            <a-form-item field="validEnd" label="生效结束时间">
              <a-date-picker
                  v-model="formData.validEnd"
                  placeholder="选择生效结束时间（为空则永久有效）"
                  style="width: 100%"
              />
            </a-form-item>
          </a-col>

          <a-col :span="24">
            <a-form-item :required="true" field="content" label="公告正文">
              <!-- 富文本编辑器 -->
              <div style="border: 1px solid #e5e6eb; border-radius: 4px">
                <Toolbar
                    :defaultConfig="toolbarConfig"
                    :editor="editorRef"
                    :mode="mode"
                    style="border-bottom: 1px solid #e5e6eb"
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

          <a-col :span="24">
            <a-form-item field="attachments" label="公告附件">
              <a-upload
                  v-model="formData.fileList"
                  :file-list-max="5"
                  :multiple="true"
                  :show-file-list="true"
                  accept=".docx,.doc,.pdf,.xlsx,.xls,.jpg,.png"
                  action="/api/upload/notice/attachment"
                  @change="handleFileChange"
              >
                <a-button type="dashed">
                  <icon-upload/>
                  点击上传附件（最多5个）
                </a-button>
              </a-upload>
              <div style="margin-top: 8px; font-size: 12px; color: #86909c">
                支持格式：docx/doc/pdf/xlsx/xls/jpg/png，单文件大小≤20MB
              </div>
            </a-form-item>
          </a-col>

          <a-col v-if="isEdit" :span="24">
            <a-form-item field="status" label="公告状态">
              <a-radio-group v-model="formData.status">
                <a-radio value="draft">草稿（仅自己可见）</a-radio>
                <a-radio value="published">已发布（全角色可见）</a-radio>
                <a-radio value="offline">已下架（仅管理员可见）</a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
        </a-row>
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
            <span>发布人：{{ previewNoticeData.publisher }}</span>
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
              <icon-paper-clip style="margin-right: 8px; color: #86909c"/>
              <a-link @click="downloadAttachment(file)">{{ file.name }}</a-link>
            </div>
          </a-space>
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import {onUnmounted, reactive, ref, watchEffect} from "vue";
import message from "@arco-design/web-vue/es/message";
import type {FormInstance} from "@arco-design/web-vue/es/form";
import dayjs from "dayjs";
// 导入Arco图标
import {
  IconChart,
  IconDelete,
  IconDownCircle,
  IconEdit,
  IconList,
  IconPaperClip,
  IconPlus,
  IconRefresh,
  IconSearch,
  IconUpload,
  IconWarning,
  IconWarningCircle,
} from "@arco-design/web-vue/es/icon";
// 导入富文本编辑器
import {Editor, Toolbar} from "@wangeditor/editor-for-vue";
import type {IDomEditor} from "@wangeditor/editor";
import {useRouter} from "vue-router";
// 导入API
import * as gonggaoguanli from "@/api/gonggaoguanli";
import * as gonggaokuozhangongneng from "@/api/gonggaokuozhangongneng";

const router = useRouter();

// 1. 基础配置
// 模拟用户角色（实际项目中从登录信息获取）
const userRole = ref("admin"); // admin/teacher/student

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

// 3. 公告列表（模拟数据）
const noticeList = ref([
  {
    id: "1",
    title: "关于2025年党员发展工作安排",
    type: "notice",
    publisher: "管理员-王五",
    status: "published",
    publishTime: "2025-03-01",
    isTop: true,
    validStart: "2025-03-01",
    validEnd: "2025-12-31",
    content:
        "<p>各支部：</p><p>为做好2025年党员发展工作，现将有关事项通知如下：</p><p>一、发展计划：2025年计划发展党员50名，各支部需于3月10日前提报推荐名单。</p><p>二、提报要求：推荐名单需包含基本信息、政治表现、培养情况等。</p><p>三、时间安排：3月中旬开展初审，4月完成考察，5月进行公示。</p>",
    attachments: [
      {
        name: "2025年党员发展工作细则.pdf",
        url: "/attachments/2025党员发展细则.pdf",
      },
      {
        name: "党员发展申请表模板.docx",
        url: "/attachments/党员发展申请表.docx",
      },
    ],
  },
  {
    id: "2",
    title: "3月主题党日活动通知",
    type: "activity",
    publisher: "管理员-王五",
    status: "published",
    publishTime: "2025-02-28",
    isTop: false,
    validStart: "2025-02-28",
    validEnd: "2025-03-31",
    content:
        "<p>各支部：</p><p>3月5日将开展“学习二十大 奋进新征程”主题党日活动，具体安排如下：</p><ul><li>活动时间：3月5日下午2点</li><li>活动地点：党员活动室</li><li>参与人员：全体党员</li><li>活动要求：携带学习笔记，提前做好学习准备</li></ul>",
    attachments: [],
  },
  {
    id: "3",
    title: "党建知识学习提醒",
    type: "remind",
    publisher: "管理员-王五",
    status: "draft",
    publishTime: "2025-02-27",
    isTop: false,
    validStart: "",
    validEnd: "",
    content:
        "<p>各位党员：</p><p>本月党建知识线上学习需在2月28日前完成，未完成将影响量化考核，请及时完成学习。</p><p>学习地址：<a href='#' style='color: #f53f3f'>http://dangjian.com/study</a></p>",
    attachments: [],
  },
  {
    id: "4",
    title: "2025年党建政策解读",
    type: "policy",
    publisher: "管理员-王五",
    status: "offline",
    publishTime: "2025-01-10",
    isTop: false,
    validStart: "2025-01-10",
    validEnd: "2025-01-31",
    content:
        "<p>2025年党建工作重点围绕“强基固本、提质增效”展开，主要包含以下政策调整：</p><p>1. 党员教育数字化转型，全面推广线上学习平台</p><p>2. 支部考核体系优化，增加群众满意度指标</p><p>3. 发展党员流程简化，缩短考察周期</p>",
    attachments: [
      {name: "2025党建政策全文.pdf", url: "/attachments/2025党建政策.pdf"},
    ],
  },
]);
total.value = noticeList.value.length;

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
    title: "公告类型",
    slotName: "type",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "发布人",
    dataIndex: "publisher",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "发布状态",
    slotName: "status",
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

// 7. 加载公告列表（调用真实API）
const loadNoticeList = async () => {
  try {
    const res = await gonggaoguanli.listNoticeVoByPageUsingGet({
      type: searchParams.type === "all" ? undefined : searchParams.type,
      status: searchParams.status === "all" ? undefined : searchParams.status,
      keyword: searchParams.keyword || undefined,
      current: searchParams.current,
      pageSize: searchParams.pageSize,
    });
    if (res.code === 0) {
      noticeList.value = res.data?.records || [];
      total.value = res.data?.total || 0;
    } else {
      message.error(res.message || "获取公告列表失败");
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
  validStart: "",
  validEnd: "",
  content: "",
  fileList: [] as any[],
  attachments: [] as any[],
  status: "draft", // 默认为草稿
  publisher: "管理员-王五", // 固定为当前登录管理员
  publishTime: "",
});

// 表单校验规则
const formRules = reactive({
  title: [{required: true, message: "请输入公告标题"}],
  type: [{required: true, message: "请选择公告类型"}],
  content: [{required: true, message: "请输入公告正文"}],
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
  formData.validStart = dayjs().format("YYYY-MM-DD");
  formData.validEnd = "";
  formData.content = "";
  formData.fileList = [];
  formData.attachments = [];
  formData.status = "draft";
  formData.publishTime = "";
  modalVisible.value = true;
};

// 打开编辑模态框
const openEditModal = (record: any) => {
  isEdit.value = true;
  // 填充表单数据
  formData.id = record.id;
  formData.title = record.title;
  formData.type = record.type;
  formData.isTop = record.isTop;
  formData.validStart = record.validStart;
  formData.validEnd = record.validEnd;
  formData.content = record.content;
  formData.fileList = record.attachments.map((file: any) => ({
    name: file.name,
    status: "done",
    url: file.url,
  }));
  formData.attachments = record.attachments;
  formData.status = record.status;
  formData.publishTime = record.publishTime;
  modalVisible.value = true;
};

// 模态框确认
const handleModalOk = async () => {
  if (!formRef.value) return;
  try {
    await formRef.value.validate();
    
    if (isEdit.value) {
      // 编辑公告
      const res = await gonggaoguanli.updateNoticeUsingPost({
        id: formData.id,
        title: formData.title,
        type: formData.type,
        content: formData.content,
        isTop: formData.isTop,
        validStart: formData.validStart,
        validEnd: formData.validEnd,
        status: formData.status,
        attachments: formData.attachments,
      });
      if (res.code === 0) {
        message.success("编辑公告成功");
        modalVisible.value = false;
        await loadNoticeList();
      } else {
        message.error(res.message || "编辑公告失败");
      }
    } else {
      // 新增公告
      const res = await gonggaoguanli.addNoticeUsingPost({
        title: formData.title,
        type: formData.type,
        content: formData.content,
        isTop: formData.isTop,
        validStart: formData.validStart,
        validEnd: formData.validEnd,
        status: formData.status,
        attachments: formData.attachments,
      });
      if (res.code === 0) {
        message.success("新增公告成功");
        modalVisible.value = false;
        await loadNoticeList();
      } else {
        message.error(res.message || "新增公告失败");
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
    if (res.code === 0) {
      previewNoticeData.value = res.data;
      previewVisible.value = true;
    } else {
      message.error(res.message || "获取公告详情失败");
    }
  } catch (error) {
    console.error("获取公告详情失败:", error);
    message.error("网络请求异常");
  }
};

// 下载附件
const downloadAttachment = async (file: any) => {
  try {
    const res = await gonggaokuozhangongneng.downloadAnnouncementAttachmentUsingGet({ attachmentId: file.id });
    if (res.code === 0) {
      message.success(`已开始下载【${file.name}】`);
      // 实际项目中处理下载
    } else {
      message.error(res.message || "下载附件失败");
    }
  } catch (error) {
    console.error("下载附件失败:", error);
    message.error("网络请求异常");
  }
};

// 12. 发布/下架/重新发布公告
const publishNotice = (record: any) => {
  message.confirm({
    title: "发布公告",
    content: `确定要发布【${record.title}】吗？发布后所有角色均可查看`,
    onOk: async () => {
      try {
        const res = await gonggaoguanli.publishNoticeUsingPost({ id: record.id });
        if (res.code === 0) {
          message.success("公告发布成功");
          await loadNoticeList();
        } else {
          message.error(res.message || "发布公告失败");
        }
      } catch (error) {
        console.error("发布公告失败:", error);
        message.error("网络请求异常");
      }
    },
  });
};

const offlineNotice = (record: any) => {
  message.confirm({
    title: "下架公告",
    content: `确定要下架【${record.title}】吗？下架后仅管理员可见`,
    onOk: async () => {
      try {
        const res = await gonggaoguanli.offlineNoticeUsingPut({ id: record.id });
        if (res.code === 0) {
          message.success("公告下架成功");
          await loadNoticeList();
        } else {
          message.error(res.message || "下架公告失败");
        }
      } catch (error) {
        console.error("下架公告失败:", error);
        message.error("网络请求异常");
      }
    },
  });
};

const republishNotice = (record: any) => {
  message.confirm({
    title: "重新发布公告",
    content: `确定要重新发布【${record.title}】吗？发布后所有角色均可查看`,
    onOk: async () => {
      try {
        const res = await gonggaoguanli.republishNoticeUsingPut({ id: record.id });
        if (res.code === 0) {
          message.success("公告重新发布成功");
          await loadNoticeList();
        } else {
          message.error(res.message || "重新发布公告失败");
        }
      } catch (error) {
        console.error("重新发布公告失败:", error);
        message.error("网络请求异常");
      }
    },
  });
};

// 13. 删除公告
const deleteNotice = (record: any) => {
  message.confirm({
    title: "删除公告",
    content: `确定要删除【${record.title}】吗？删除后不可恢复！`,
    onOk: async () => {
      try {
        const res = await gonggaoguanli.deleteNoticeUsingPost({ id: record.id });
        if (res.code === 0) {
          message.success("公告删除成功");
          await loadNoticeList();
        } else {
          message.error(res.message || "删除公告失败");
        }
      } catch (error) {
        console.error("删除公告失败:", error);
        message.error("网络请求异常");
      }
    },
  });
};

// 14. 批量操作
const batchOffline = () => {
  if (selectedRowKeys.value.length === 0) {
    message.warning("请先选择要下架的公告");
    return;
  }
  message.confirm({
    title: "批量下架",
    content: `确定要下架选中的${selectedRowKeys.value.length}条公告吗？下架后仅管理员可见`,
    onOk: async () => {
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
        selectedRowKeys.value = [];
        await loadNoticeList();
      } catch (error) {
        console.error("批量下架失败:", error);
        message.error("网络请求异常");
      }
    },
  });
};

const batchDelete = () => {
  if (selectedRowKeys.value.length === 0) {
    message.warning("请先选择要删除的公告");
    return;
  }
  message.confirm({
    title: "批量删除",
    content: `确定要删除选中的${selectedRowKeys.value.length}条公告吗？删除后不可恢复！`,
    onOk: async () => {
      try {
        const res = await gonggaoguanli.batchDeleteNoticesUsingPost(selectedRowKeys.value.map(Number));
        if (res.code === 0) {
          message.success("批量删除成功");
          selectedRowKeys.value = [];
          await loadNoticeList();
        } else {
          message.error(res.message || "批量删除失败");
        }
      } catch (error) {
        console.error("批量删除失败:", error);
        message.error("网络请求异常");
      }
    },
  });
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
/* 页面样式优化 */
.notice-manage-page {
  padding: 16px;
}

.filter-operation-bar {
  padding-bottom: 16px;
  border-bottom: 1px solid #eee;
}

/* 富文本编辑器样式适配 */
:deep(.w-e-toolbar) {
  padding: 8px 10px !important;
}

:deep(.w-e-text-container) {
  padding: 10px !important;
}

/* 预览弹窗样式 */
.notice-preview {
  padding: 8px 0;
}

/* 表格样式适配 */
:deep(.arco-table) {
  --arco-table-header-text-color: #1d2129;
  --arco-table-body-text-color: #4e5969;
}

/* 模态框表单样式 */
:deep(.arco-form-item) {
  margin-bottom: 16px;
}
</style>

<!-- 富文本编辑器样式引入 -->
<style src="@wangeditor/editor/dist/css/style.css"></style>
