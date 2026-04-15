<template>
  <!-- 页面整体框架 -->
  <div class="material-submit-audit-page">
    <!-- 视图切换栏 -->
    <a-card style="margin-bottom: 16px">
      <a-space size="large">
        <a-button
            :disabled="userRole !== 'admin'"
            :status="activeView === 'template' ? 'primary' : 'normal'"
            @click="switchView('template')"
        >
          <icon-file-text/>
          材料模板管理
        </a-button>
        <a-button
            :status="activeView === 'submit' ? 'primary' : 'normal'"
            type="primary"
            @click="switchView('submit')"
        >
          <icon-upload/>
          材料提交审核
        </a-button>
        <a-button
            :status="activeView === 'archive' ? 'primary' : 'normal'"
            @click="switchView('archive')"
        >
          <icon-folder/>
          材料归档查询
        </a-button>
      </a-space>
    </a-card>

    <!-- 材料提交/审核核心区域（全角色可见） -->
    <a-card>
      <!-- 高级筛选区 -->
      <div
          class="filter-bar"
          style="
          margin-bottom: 16px;
          padding-bottom: 16px;
          border-bottom: 1px solid #eee;
        "
      >
        <a-row :gutter="16" align="middle">
          <a-col :span="6">
            <a-form-item label="时间范围" label-col-flex="80px">
              <a-select
                  v-model="filterParams.timeRange"
                  style="width: 100%"
                  @change="refreshData"
              >
                <a-option value="3month">近3个月</a-option>
                <a-option value="6month">近6个月</a-option>
                <a-option value="1year">近1年</a-option>
                <a-option value="custom">自定义</a-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :span="6">
            <a-form-item label="组织层级" label-col-flex="80px">
              <a-select
                  v-model="filterParams.orgLevel"
                  style="width: 100%"
                  @change="refreshData"
              >
                <a-option value="all">全部</a-option>
                <a-option value="branch1">教师一支部</a-option>
                <a-option value="branch2">教师二支部</a-option>
                <a-option value="branch3">学生一支部</a-option>
                <a-option value="branch4">学生二支部</a-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :span="6">
            <a-form-item label="发展阶段" label-col-flex="80px">
              <a-select
                  v-model="filterParams.stage"
                  style="width: 100%"
                  @change="refreshData"
              >
                <a-option value="all">全部</a-option>
                <a-option value="activist">积极分子</a-option>
                <a-option value="developmentObject">发展对象</a-option>
                <a-option value="probationaryPartyMember">预备党员</a-option>
              </a-select>
            </a-form-item>
          </a-col>

          <a-col :span="6">
            <a-form-item label="提交状态" label-col-flex="80px">
              <a-select
                  v-model="filterParams.submitStatus"
                  style="width: 100%"
                  @change="refreshData"
              >
                <a-option value="all">全部</a-option>
                <a-option value="unsubmit">未提交</a-option>
                <a-option value="submitted">已提交</a-option>
                <a-option value="approved">审核通过</a-option>
                <a-option value="rejected">退回</a-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <!-- 自定义时间范围（按需显示） -->
        <a-row
            v-if="filterParams.timeRange === 'custom'"
            style="margin-top: 16px"
        >
          <a-col :span="12">
            <a-range-picker
                v-model="filterParams.customTime"
                style="width: 100%"
            />
          </a-col>
          <a-col :span="12" style="text-align: right">
            <a-button type="outline" @click="refreshData">
              <icon-search/>
              确认筛选
            </a-button>
          </a-col>
        </a-row>

        <!-- 刷新按钮 -->
        <a-row style="margin-top: 8px">
          <a-col :span="24" style="text-align: right">
            <a-button type="outline" @click="refreshData">
              <icon-refresh/>
              刷新数据
            </a-button>
          </a-col>
        </a-row>
      </div>

      <!-- 待办提醒区 -->
      <div class="todo-remind" style="margin-bottom: 20px">
        <a-row :gutter="16">
          <a-col v-if="todoCount.unsubmit > 0" :span="12">
            <a-card
                class="todo-card unsubmit-card"
                hoverable
                @click="filterUnsubmit"
            >
              <div class="todo-icon">🚨</div>
              <div class="todo-text">
                待我提交：<span class="todo-count">{{
                  todoCount.unsubmit
                }}</span
              >份材料
              </div>
            </a-card>
          </a-col>
          <a-col v-if="todoCount.audit > 0" :span="12">
            <a-card class="todo-card audit-card" hoverable @click="filterAudit">
              <div class="todo-icon">🚨</div>
              <div class="todo-text">
                待我审核：<span class="todo-count">{{ todoCount.audit }}</span
              >份材料
              </div>
            </a-card>
          </a-col>
        </a-row>
      </div>

      <!-- 材料提交/审核列表 -->
      <a-table
          :columns="tableColumns"
          :data="materialList"
          :pagination="{
          showTotal: true,
          pageSize: 10,
          current: 1,
          total: materialList.length,
        }"
          :row-class-name="getRowClassName"
          row-key="id"
      >
        <!-- 所属支部列自定义渲染 -->
        <template #orgName="{ record }">
          <span>
            {{
              record.orgLevel === "branch1"
                  ? "教师一支部"
                  : record.orgLevel === "branch2"
                      ? "教师二支部"
                      : record.orgLevel === "branch3"
                          ? "学生一支部"
                          : "学生二支部"
            }}
          </span>
        </template>

        <!-- 发展阶段列自定义渲染 -->
        <template #stage="{ record }">
          <span>
            {{
              record.stage === "activist"
                  ? "积极分子"
                  : record.stage === "developmentObject"
                      ? "发展对象"
                      : "预备党员"
            }}
          </span>
        </template>

        <!-- 提交状态列自定义渲染 -->
        <template #submitStatus="{ record }">
          <a-tag :color="getSubmitStatusColor(record.submitStatus)">
            {{
              record.submitStatus === "unsubmit"
                  ? "未提交"
                  : record.submitStatus === "submitted"
                      ? "已提交"
                      : record.submitStatus === "approved"
                          ? "审核通过"
                          : "退回"
            }}
          </a-tag>
        </template>

        <!-- 审核状态列自定义渲染 -->
        <template #auditStatus="{ record }">
          <a-tag :color="getAuditStatusColor(record.auditStatus)">
            {{
              record.auditStatus === "none"
                  ? "-"
                  : record.auditStatus === "pending"
                      ? "待审核"
                      : record.auditStatus === "approved"
                          ? "审核通过"
                          : record.auditStatus === "finalApproved"
                              ? "终审通过"
                              : "退回"
            }}
          </a-tag>
        </template>

        <!-- 操作列（动态渲染，按角色区分） -->
        <template #operation="{ record }">
          <a-space wrap>
            <!-- 公共操作：查看/预览模板 -->
            <a-button
                v-if="record.submitStatus !== 'unsubmit'"
                type="text"
                @click="viewMaterial(record)"
            >查看
            </a-button>
            <a-button
                v-if="record.submitStatus === 'unsubmit'"
                type="text"
                @click="previewTemplate(record)"
            >预览模板
            </a-button>
            <a-button
                v-if="record.submitStatus !== 'unsubmit'"
                type="text"
                @click="downloadMaterial(record)"
            >下载
            </a-button>

            <!-- 普通党员操作：上传 -->
            <a-button
                v-if="
                userRole === 'student' && record.submitStatus === 'unsubmit'
              "
                status="primary"
                type="text"
                @click="openUploadModal(record)"
            >
              上传
            </a-button>

            <!-- 培养联系人操作：审核/退回 -->
            <a-button
                v-if="userRole === 'teacher' && record.auditStatus === 'pending'"
                status="primary"
                type="text"
                @click="openAuditModal(record, 'approve')"
            >
              审核
            </a-button>
            <a-button
                v-if="userRole === 'teacher' && record.auditStatus === 'pending'"
                status="danger"
                type="text"
                @click="openAuditModal(record, 'reject')"
            >
              退回
            </a-button>

            <!-- 管理员操作：终审/退回/归档 -->
            <a-button
                v-if="userRole === 'admin' && record.auditStatus === 'approved'"
                status="primary"
                type="text"
                @click="finalAudit(record, 'approve')"
            >
              终审
            </a-button>
            <a-button
                v-if="userRole === 'admin' && record.auditStatus === 'approved'"
                status="danger"
                type="text"
                @click="finalAudit(record, 'reject')"
            >
              退回
            </a-button>
            <a-button
                v-if="
                userRole === 'admin' && record.auditStatus === 'finalApproved'
              "
                type="text"
                @click="archiveMaterial(record)"
            >
              归档
            </a-button>
          </a-space>
        </template>
      </a-table>
    </a-card>

    <!-- 材料上传弹窗 -->
    <a-modal
        v-model:visible="uploadModalVisible"
        title="上传材料"
        width="600px"
        @cancel="handleUploadCancel"
        @ok="handleUploadOk"
    >
      <div v-if="currentMaterial" style="margin-bottom: 16px">
        <p><strong>待上传材料：</strong>{{ currentMaterial.materialName }}</p>
        <p>
          <strong>所属阶段：</strong
          >{{
            currentMaterial.stage === "activist"
                ? "积极分子"
                : currentMaterial.stage === "developmentObject"
                    ? "发展对象"
                    : "预备党员"
          }}
        </p>
      </div>
      <a-form :model="uploadForm" label-col-flex="80px" wrapper-col-flex="auto">
        <a-form-item label="材料文件">
          <a-upload
              v-model="uploadForm.fileList"
              :file-list-max="1"
              :multiple="false"
              :show-file-list="true"
              accept=".docx,.doc,.pdf,.jpg,.png"
              action="/api/upload/material"
              @change="handleFileUploadChange"
          >
            <a-button type="dashed">
              <icon-upload/>
              点击上传（支持Word/PDF/图片）
            </a-button>
          </a-upload>
          <div style="margin-top: 8px; font-size: 12px; color: #86909c">
            支持格式：docx/doc/pdf/jpg/png，单文件大小≤20MB
          </div>
        </a-form-item>
        <a-form-item label="备注说明">
          <a-textarea
              v-model="uploadForm.remark"
              :rows="3"
              placeholder="请输入材料填写说明、补充信息等（可选）"
          />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 审核/退回弹窗 -->
    <a-modal
        v-model:visible="auditModalVisible"
        :title="auditType === 'approve' ? '审核材料' : '退回材料'"
        width="600px"
        @cancel="auditModalVisible = false"
        @ok="handleAuditOk"
    >
      <div v-if="currentMaterial" style="margin-bottom: 16px">
        <p><strong>审核材料：</strong>{{ currentMaterial.materialName }}</p>
        <p><strong>提交人：</strong>{{ currentMaterial.userName }}</p>
      </div>
      <a-form :model="auditForm" label-col-flex="80px" wrapper-col-flex="auto">
        <a-form-item :required="auditType === 'reject'" label="审核意见">
          <a-textarea
              v-model="auditForm.opinion"
              :placeholder="
              auditType === 'approve'
                ? '请输入审核意见（可选）'
                : '请输入退回原因（必填）'
            "
              :rows="4"
          />
        </a-form-item>
        <a-form-item v-if="userRole === 'admin'" label="终审备注">
          <a-input
              v-model="auditForm.remark"
              placeholder="请输入终审备注（可选）"
          />
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 模板预览弹窗 -->
    <a-modal
        v-model:visible="previewModalVisible"
        :footer="null"
        title="模板预览"
        width="700px"
        @cancel="previewModalVisible = false"
    >
      <div v-if="currentMaterial" style="padding: 16px">
        <div style="font-size: 16px; font-weight: 600; margin-bottom: 16px">
          {{ currentMaterial.materialName }}模板
        </div>
        <div
            style="
            border: 1px solid #eee;
            padding: 24px;
            min-height: 300px;
            text-align: center;
          "
        >
          <icon-file-text style="font-size: 64px; color: #ccc"/>
          <div style="margin-top: 16px; color: #86909c">
            模板文件在线预览效果（实际项目中对接PDF/Word预览组件）
          </div>
        </div>
        <div style="margin-top: 20px; text-align: right">
          <a-button
              type="primary"
              @click="downloadTemplateFile(currentMaterial)"
          >
            <icon-download/>
            下载模板文件
          </a-button>
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import {reactive, ref, watchEffect} from "vue";
import message from "@arco-design/web-vue/es/message";
// 导入Arco图标
import {
  IconDownload,
  IconFileText,
  IconFolder,
  IconRefresh,
  IconSearch,
  IconUpload,
} from "@arco-design/web-vue/es/icon";
import {useRouter} from "vue-router";
// 导入API
import * as cailiaotijiaoshenhe from "@/api/cailiaotijiaoshenhe";
import * as cailiaoguidangchaxun from "@/api/cailiaoguidangchaxun";

const router = useRouter();

// 1. 基础配置
// 模拟用户角色（实际从登录态获取：admin/teacher/student）
const userRole = ref("student"); // 可切换测试：admin/teacher/student

// 视图切换
const activeView = ref("submit"); // template/submit/archive
const switchView = (view: string) => {
  activeView.value = view;
  if (view === "template") {
    message.info("已切换至材料模板管理视图（仅管理员可见）");
    toShowMaterialTemplateManagementView();
  } else if (view === "archive") {
    message.info("已切换至材料归档查询视图");
    toShowMaterialArchivingQueryView();
  }
};

// 2. 筛选参数
const filterParams = reactive({
  timeRange: "3month", // 时间范围
  orgLevel: "all", // 组织层级
  stage: "all", // 发展阶段
  submitStatus: "all", // 提交状态
  customTime: [] as any[], // 自定义时间
});

// 3. 待办数量
const todoCount = reactive({
  unsubmit: 0, // 待我提交
  audit: 0, // 待我审核
});

// 4. 原始材料列表（模拟全量数据）
const rawMaterialList = ref([
  // 普通党员（学生）自己的材料
  {
    id: "1",
    userName: "王五（学生党员）",
    orgLevel: "branch3",
    stage: "activist",
    materialName: "思想汇报",
    submitStatus: "unsubmit",
    auditStatus: "none",
    uploadTime: "",
    auditor: "",
    auditTime: "",
    fileUrl: "",
  },
  {
    id: "2",
    userName: "王五（学生党员）",
    orgLevel: "branch3",
    stage: "developmentObject",
    materialName: "政审表",
    submitStatus: "submitted",
    auditStatus: "pending",
    uploadTime: "2025-02-10",
    auditor: "",
    auditTime: "",
    fileUrl: "/materials/王五-政审表.pdf",
  },
  // 培养联系人（教师）对接的党员材料
  {
    id: "3",
    userName: "张三（教师党员）",
    orgLevel: "branch1",
    stage: "probationaryPartyMember",
    materialName: "转正申请书",
    submitStatus: "submitted",
    auditStatus: "pending",
    uploadTime: "2025-02-15",
    auditor: "",
    auditTime: "",
    fileUrl: "/materials/张三-转正申请书.pdf",
  },
  {
    id: "4",
    userName: "李四（教师党员）",
    orgLevel: "branch1",
    stage: "activist",
    materialName: "思想汇报",
    submitStatus: "approved",
    auditStatus: "approved",
    uploadTime: "2025-01-20",
    auditor: "赵六（培养联系人）",
    auditTime: "2025-01-25",
    fileUrl: "/materials/李四-思想汇报.pdf",
  },
  // 管理员终审材料
  {
    id: "5",
    userName: "赵六（教师党员）",
    orgLevel: "branch2",
    stage: "developmentObject",
    materialName: "政审表",
    submitStatus: "approved",
    auditStatus: "approved",
    uploadTime: "2025-01-18",
    auditor: "钱七（培养联系人）",
    auditTime: "2025-01-22",
    fileUrl: "/materials/赵六-政审表.pdf",
  },
]);

// 权限过滤后的材料列表
const materialList = ref<any[]>([]);

// 5. 表格列配置
const tableColumns = ref([
  {
    title: "姓名",
    dataIndex: "userName",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "所属支部",
    slotName: "orgName",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "发展阶段",
    slotName: "stage",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "材料名称",
    dataIndex: "materialName",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "提交状态",
    slotName: "submitStatus",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "审核状态",
    slotName: "auditStatus",
    ellipsis: true,
    tooltip: true,
  },
  {
    title: "操作",
    slotName: "operation",
  },
]);

// 6. 弹窗相关
const uploadModalVisible = ref(false); // 上传弹窗
const auditModalVisible = ref(false); // 审核/退回弹窗
const previewModalVisible = ref(false); // 模板预览弹窗
const currentMaterial = ref<any>(null); // 当前操作的材料
const auditType = ref("approve"); // 审核类型：approve/reject

// 上传表单
const uploadForm = reactive({
  fileList: [] as any[],
  remark: "",
});

// 审核表单
const auditForm = reactive({
  opinion: "",
  remark: "",
});

// 7. 核心方法
// 刷新数据（按角色过滤）
const refreshData = async () => {
  try {
    const res = await cailiaotijiaoshenhe.listMaterialSubmissionUsingGet({
      timeRange: filterParams.timeRange,
      orgLevel: filterParams.orgLevel === "all" ? undefined : filterParams.orgLevel,
      stage: filterParams.stage === "all" ? undefined : filterParams.stage,
      submitStatus: filterParams.submitStatus === "all" ? undefined : filterParams.submitStatus,
      page: 1,
      pageSize: 100,
    });
    if (res.code === 0) {
      materialList.value = res.data?.records || [];
      // 计算待办数量
      await calculateTodoCount();
    } else {
      message.error(res.message || "获取材料列表失败");
    }
  } catch (error) {
    console.error("获取材料列表失败:", error);
    message.error("网络请求异常");
  }
};

// 计算待办数量
const calculateTodoCount = async () => {
  try {
    const res = await cailiaotijiaoshenhe.getMaterialTodoCountUsingGet();
    if (res.code === 0) {
      todoCount.unsubmit = res.data?.unsubmit || 0;
      todoCount.audit = res.data?.audit || 0;
    } else {
      message.error(res.message || "获取待办数量失败");
    }
  } catch (error) {
    console.error("获取待办数量失败:", error);
    // 失败时使用本地计算作为 fallback
    if (userRole.value === "student") {
      todoCount.unsubmit = materialList.value.filter(item => item.submitStatus === "unsubmit").length;
      todoCount.audit = 0;
    } else if (userRole.value === "teacher") {
      todoCount.unsubmit = 0;
      todoCount.audit = materialList.value.filter(item => item.auditStatus === "pending").length;
    } else if (userRole.value === "admin") {
      todoCount.unsubmit = 0;
      todoCount.audit = materialList.value.filter(item => item.auditStatus === "approved").length;
    }
  }
};

// 获取提交状态颜色
const getSubmitStatusColor = (status: string) => {
  switch (status) {
    case "unsubmit":
      return "red";
    case "submitted":
      return "orange";
    case "approved":
      return "green";
    case "rejected":
      return "purple";
    default:
      return "gray";
  }
};

// 获取审核状态颜色
const getAuditStatusColor = (status: string) => {
  switch (status) {
    case "pending":
      return "orange";
    case "approved":
      return "blue";
    case "finalApproved":
      return "green";
    case "rejected":
      return "purple";
    default:
      return "gray";
  }
};

// 获取行样式（按状态高亮）
const getRowClassName = (record: any) => {
  if (record.submitStatus === "unsubmit") return "row-unsubmit";
  if (record.auditStatus === "pending") return "row-pending";
  if (record.submitStatus === "rejected") return "row-rejected";
  return "";
};

// 筛选待提交
const filterUnsubmit = () => {
  filterParams.submitStatus = "unsubmit";
  refreshData();
};

// 筛选待审核
const filterAudit = () => {
  if (userRole.value === "teacher") {
    filterParams.submitStatus = "submitted";
  } else if (userRole.value === "admin") {
    filterParams.submitStatus = "approved";
  }
  refreshData();
};

// 打开上传弹窗
const openUploadModal = (record: any) => {
  currentMaterial.value = record;
  uploadForm.fileList = [];
  uploadForm.remark = "";
  uploadModalVisible.value = true;
};

// 处理上传确认
const handleUploadOk = async () => {
  if (
      uploadForm.fileList.length === 0 ||
      uploadForm.fileList[0].status !== "done"
  ) {
    message.error("请先上传材料文件");
    return;
  }

  try {
    const res = await cailiaotijiaoshenhe.submitMaterialUsingPost({
      userId: currentMaterial.value.userId || currentMaterial.value.id,
      stage: currentMaterial.value.stage,
      materialName: currentMaterial.value.materialName,
      fileUrl: uploadForm.fileList[0].response?.data || uploadForm.fileList[0].url,
      remark: uploadForm.remark,
    });
    if (res.code === 0) {
      uploadModalVisible.value = false;
      message.success("材料上传成功，等待审核");
      await refreshData();
    } else {
      message.error(res.message || "材料上传失败");
    }
  } catch (error) {
    console.error("材料上传失败:", error);
    message.error("网络请求异常");
  }
};

// 处理上传取消
const handleUploadCancel = () => {
  uploadModalVisible.value = false;
  uploadForm.fileList = [];
};

// 文件上传回调
const handleFileUploadChange = (fileList: any[]) => {
  uploadForm.fileList = fileList;
};

// 打开审核/退回弹窗
const openAuditModal = (record: any, type: string) => {
  currentMaterial.value = record;
  auditType.value = type;
  auditForm.opinion = "";
  auditForm.remark = "";
  auditModalVisible.value = true;
};

// 处理审核确认
const handleAuditOk = async () => {
  if (auditType.value === "reject" && !auditForm.opinion) {
    message.error("请填写退回原因");
    return;
  }

  try {
    if (auditType.value === "approve") {
      // 审核通过
      const res = await cailiaotijiaoshenhe.auditMaterialUsingPut({
        id: currentMaterial.value.id,
        auditResult: "approved",
        auditOpinion: auditForm.opinion,
      });
      if (res.code === 0) {
        message.success("审核通过");
        auditModalVisible.value = false;
        await refreshData();
      } else {
        message.error(res.message || "审核失败");
      }
    } else {
      // 退回
      const res = await cailiaotijiaoshenhe.rejectMaterialUsingPut({
        id: currentMaterial.value.id,
        auditOpinion: auditForm.opinion,
      });
      if (res.code === 0) {
        message.success("材料已退回");
        auditModalVisible.value = false;
        await refreshData();
      } else {
        message.error(res.message || "退回失败");
      }
    }
  } catch (error) {
    console.error("审核操作失败:", error);
    message.error("网络请求异常");
  }
};

// 管理员终审
const finalAudit = async (record: any, type: string) => {
  currentMaterial.value = record;
  auditType.value = type;
  auditForm.opinion = type === "approve" ? "终审通过" : "";
  auditForm.remark = "";

  if (type === "approve") {
    // 直接终审通过
    try {
      const res = await cailiaotijiaoshenhe.finalAuditMaterialUsingPut({
        id: record.id,
        auditResult: "finalApproved",
        auditOpinion: auditForm.opinion,
      });
      if (res.code === 0) {
        message.success("终审通过，材料可归档");
        await refreshData();
      } else {
        message.error(res.message || "终审失败");
      }
    } catch (error) {
      console.error("终审失败:", error);
      message.error("网络请求异常");
    }
  } else {
    // 退回需填写原因
    auditModalVisible.value = true;
  }
};

// 归档材料
const archiveMaterial = (record: any) => {
  message.confirm({
    title: "材料归档",
    content: `确定要归档【${record.userName}-${record.materialName}】吗？归档后将进入归档库`,
    onOk: async () => {
      try {
        const res = await cailiaoguidangchaxun.archiveMaterialUsingPost({
          submissionId: record.id,
          archiveRemark: "",
        });
        if (res.code === 0) {
          message.success("材料归档成功");
          await refreshData();
        } else {
          message.error(res.message || "归档失败");
        }
      } catch (error) {
        console.error("归档失败:", error);
        message.error("网络请求异常");
      }
    },
  });
};

// 预览模板
const previewTemplate = (record: any) => {
  currentMaterial.value = record;
  previewModalVisible.value = true;
};

// 下载模板文件
const downloadTemplateFile = (record: any) => {
  message.success(`已开始下载【${record.materialName}】模板文件`);
};

// 查看材料
const viewMaterial = async (record: any) => {
  try {
    const res = await cailiaotijiaoshenhe.viewMaterialUsingGet({ id: record.id });
    if (res.code === 0) {
      // 实际项目中对接在线预览
      message.info(`查看【${record.userName}-${record.materialName}】材料`);
    } else {
      message.error(res.message || "查看材料失败");
    }
  } catch (error) {
    console.error("查看材料失败:", error);
    message.error("网络请求异常");
  }
};

// 下载材料
const downloadMaterial = async (record: any) => {
  try {
    const res = await cailiaotijiaoshenhe.downloadMaterialUsingGet({ id: record.id });
    if (res.code === 0) {
      // 实际项目中处理下载
      message.success(`已开始下载【${record.userName}-${record.materialName}】材料文件`);
    } else {
      message.error(res.message || "下载材料失败");
    }
  } catch (error) {
    console.error("下载材料失败:", error);
    message.error("网络请求异常");
  }
};

// 初始加载
watchEffect(() => {
  (async () => {
    await refreshData();
  })();
});

const toShowMaterialArchivingQueryView = () => {
  router.push("/materialArchivingQuery");
};

const toShowMaterialTemplateManagementView = () => {
  router.push("/materialTemplateManagement");
};
</script>

<style scoped>
/* 页面基础样式 */
.material-submit-audit-page {
  padding: 16px;
}

/* 待办提醒样式 */
.todo-remind {
  margin-bottom: 20px;
}

.todo-card {
  padding: 16px;
  display: flex;
  align-items: center;
  cursor: pointer;
}

.todo-icon {
  font-size: 24px;
  margin-right: 12px;
}

.todo-text {
  font-size: 14px;
  color: #1d2129;
}

.todo-count {
  font-size: 18px;
  font-weight: bold;
  color: #f53f3f;
}

.unsubmit-card {
  border: 1px solid #ffccc7;
  background-color: #fff2f0;
}

.audit-card {
  border: 1px solid #ffe8cc;
  background-color: #fff7e6;
}

/* 表格行样式 */
:deep(.row-unsubmit) {
  background-color: #fff2f0 !important;
}

:deep(.row-pending) {
  background-color: #fff7e6 !important;
}

:deep(.row-rejected) {
  background-color: #f9e5e8 !important;
}

/* 表单样式 */
:deep(.arco-form-item) {
  margin-bottom: 16px;
}
</style>
