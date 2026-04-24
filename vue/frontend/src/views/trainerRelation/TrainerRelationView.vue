<template>
  <div class="trainer-relation-container">
    <!-- 页面标题和面包屑 -->
    <div class="page-header">
      <div class="breadcrumb">
        <a-breadcrumb>
          <a-breadcrumb-item>首页</a-breadcrumb-item>
          <a-breadcrumb-item>发展阶段管理</a-breadcrumb-item>
        </a-breadcrumb>
      </div>
      <h1>发展阶段管理</h1>
    </div>

    <!-- 统计卡片区域 -->
    <div v-if="!loading" class="stats-container">
      <div class="stat-card">
        <div class="stat-number">{{ stats.total }}</div>
        <div class="stat-label">总记录数</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ stats.activist }}</div>
        <div class="stat-label">积极分子</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ stats.developmentObject }}</div>
        <div class="stat-label">发展对象</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ stats.probationaryPartyMember }}</div>
        <div class="stat-label">预备党员</div>
      </div>
      <div class="stat-card">
        <div class="stat-number">{{ stats.formalPartyMember }}</div>
        <div class="stat-label">正式党员</div>
      </div>
    </div>

    <!-- 搜索区域 -->
    <a-card class="search-card">
      <a-form :model="searchParams" class="search-form" layout="inline">
        <a-form-item label="组织层级">
          <a-dropdown :popup-max-height="false" @select="cropTypeSelect">
            <a-button>
              {{ cropTypeDefaultValue }}
              <icon-down />
            </a-button>
            <template #content>
              <a-doption value="partyCommittees">党委</a-doption>
              <a-doption value="branch">支部</a-doption>
            </template>
          </a-dropdown>
        </a-form-item>
        <a-form-item label="发展阶段">
          <a-dropdown :popup-max-height="false" @select="developStageSelect">
            <a-button>
              {{ developStageDefaultValue }}
              <icon-down />
            </a-button>
            <template #content>
              <a-doption value="all">全部</a-doption>
              <a-doption value="activist">积极分子</a-doption>
              <a-doption value="developmentObject">发展对象</a-doption>
              <a-doption value="probationaryPartyMember">预备党员</a-doption>
              <a-doption value="formalPartyMember">正式党员</a-doption>
            </template>
          </a-dropdown>
        </a-form-item>
        <a-form-item label="党员类型">
          <a-dropdown :popup-max-height="false" @select="partyMemberTypeSelect">
            <a-button>
              {{ partyMemberTypeDefaultValue }}
              <icon-down />
            </a-button>
            <template #content>
              <a-doption value="teacher">教师</a-doption>
              <a-doption value="student">学生</a-doption>
            </template>
          </a-dropdown>
        </a-form-item>
        <a-form-item label="搜索">
          <a-input
            v-model="searchParams.cropName"
            allow-clear
            placeholder="请输入姓名/学号/工号..."
            style="width: 200px"
          />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="handlesumbit">查询</a-button>
          <a-button @click="handleReset">重置</a-button>
        </a-form-item>
      </a-form>
    </a-card>

    <!-- 操作按钮区域 -->
    <div class="action-bar">
      <a-space size="large">
        <a-button type="primary" @click="AddData">
          <icon-plus />
          新增发展记录
        </a-button>
        <a-button type="primary" @click="delData">
          <icon-delete />
          批量删除
        </a-button>
        <a-button type="primary" @click="modifyData(1)">
          <icon-check-circle />
          审核
        </a-button>
      </a-space>
    </div>

    <!-- 数据列表区域 -->
    <a-card class="list-card">
      <template #loading>
        <div style="display: flex; justify-content: center; padding: 40px">
          <a-spin size="large" tip="加载中..." />
        </div>
      </template>
      <a-table
        :data="dataList"
        :loading="loading"
        :pagination="{
          showTotal: true,
          pageSize: searchParams.pageSize,
          current: searchParams.current,
          total,
          showSizeChanger: true,
          onChange: onPageChange,
          onShowSizeChange: onSizeChange,
        }"
        :row-selection="rowSelection"
        :selectedRowKeys="selectedRowKeys"
        border
        pagination-position="bottom"
        row-key="id"
        @selection-change="handleSelectionChange"
      >
        <template #columns>
          <a-table-column data-index="userName" title="姓名" width="120">
            <template #cell="{ record }">
              <a-tag color="blue">{{ record.userName }}</a-tag>
            </template>
          </a-table-column>
          <a-table-column data-index="userType" title="用户类型" width="100">
            <template #cell="{ record }">
              <a-tag
                :color="record.userType === 'teacher' ? 'green' : 'orange'"
              >
                {{ record.userType === "teacher" ? "教师" : "学生" }}
              </a-tag>
            </template>
          </a-table-column>
          <a-table-column data-index="orgName" title="所属支部" width="150">
            <template #cell="{ record }">
              <span>{{ record.orgName || "未知支部" }}</span>
            </template>
          </a-table-column>
          <a-table-column
            data-index="politicalStatus"
            title="政治面貌"
            width="120"
          >
            <template #cell="{ record }">
              <a-tag :color="getStatusColor(record.politicalStatus)">
                {{ record.politicalStatus || "未知" }}
              </a-tag>
            </template>
          </a-table-column>
          <a-table-column title="发展阶段" width="150">
            <template #cell="{ record }">
              <a-tag
                :color="getStatusColor(record.stageName || record.stageType)"
              >
                {{ record.stageName || record.stageType || "未知" }}
              </a-tag>
            </template>
          </a-table-column>
          <a-table-column title="阶段状态" width="120">
            <template #cell="{ record }">
              <a-tag :color="getStageStatusColor(record.stageStatus)">
                {{ getStageStatusText(record.stageStatus) }}
              </a-tag>
            </template>
          </a-table-column>
          <a-table-column title="考察结果" width="120">
            <template #cell="{ record }">
              <a-tag :color="getAssessmentResultColor(record.assessmentResult)">
                {{ getAssessmentResultText(record.assessmentResult) }}
              </a-tag>
            </template>
          </a-table-column>
          <a-table-column data-index="trainerName" title="培养人" width="120" />
          <a-table-column
            data-index="stageStartTime"
            title="阶段开始时间"
            width="180"
          >
            <template #cell="{ record }">
              {{ formatDate(record.stageStartTime) }}
            </template>
          </a-table-column>
          <a-table-column
            data-index="stageEndTime"
            title="阶段结束时间"
            width="180"
          >
            <template #cell="{ record }">
              {{ formatDate(record.stageEndTime) }}
            </template>
          </a-table-column>
          <a-table-column
            data-index="joinDate"
            title="申请入党日期"
            width="150"
          >
            <template #cell="{ record }">
              {{ formatDate(record.joinDate) }}
            </template>
          </a-table-column>
          <a-table-column
            data-index="positiveDate"
            title="转正日期"
            width="150"
          >
            <template #cell="{ record }">
              {{ formatDate(record.positiveDate) }}
            </template>
          </a-table-column>
          <a-table-column title="操作" width="120" fixed="right">
            <template #cell="{ record }">
              <a-space wrap>
                <a-button
                  size="small"
                  type="primary"
                  @click="toShowInfo(record)"
                >
                  详情
                </a-button>
              </a-space>
            </template>
          </a-table-column>
        </template>
      </a-table>
    </a-card>

    <!-- 详情/编辑模态框 - 完整布局 -->
    <a-modal
      v-model:visible="infoVisible"
      :footer="null"
      :title="`${isEdit ? '编辑' : '查看'}发展记录`"
      width="1400px"
      @cancel="handleInfoCancel"
    >
      <!-- 党员基础信息卡片 -->
      <a-card style="margin-bottom: 20px">
        <template #title>
          <span style="font-weight: 600; font-size: 16px">党员基础信息</span>
        </template>
        <div class="basic-info">
          <div class="info-item">
            <span class="info-label">姓名：</span>
            <span class="info-value">{{ addFormData.name }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">身份：</span>
            <span class="info-value">{{ addFormData.identity }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">支部：</span>
            <span class="info-value">{{ addFormData.branch }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">当前阶段：</span>
            <span class="info-value">{{ addFormData.currentStage }}</span>
          </div>
          <div class="info-item">
            <span class="info-label">培养联系人：</span>
            <span class="info-value">{{ addFormData.trainer }}</span>
          </div>
        </div>
        <div class="info-actions">
          <a-button type="text" @click="goBackList">
            <icon-left />
            返回列表
          </a-button>
          <a-button type="text" @click="toggleEdit" :disabled="isAudited">
            <icon-edit />
            {{ isEdit ? "退出编辑" : "编辑" }}
          </a-button>
          <a-button type="text" @click="printInfo">
            <icon-printer />
            打印
          </a-button>
        </div>
      </a-card>

      <!-- 核心内容：左侧时间轴 + 右侧详情表单 -->
      <a-row :gutter="24">
        <!-- 左侧：发展阶段时间轴 -->
        <a-col :span="6">
          <a-card>
            <template #title>
              <span style="font-weight: 600">发展阶段时间轴</span>
            </template>
            <div class="timeline-container">
              <a-timeline>
                <a-timeline-item
                  v-for="stage in stageHistory"
                  :key="stage.id"
                  :dot="getStageDot(stage.status)"
                  :color="getStageColor(stage.status)"
                  class="timeline-item"
                >
                  <div class="timeline-content">
                    <div class="timeline-title">
                      <span class="stage-emoji">{{
                        getStageEmoji(stage.status)
                      }}</span>
                      {{ stage.stageName }}
                    </div>
                    <div
                      class="timeline-status"
                      :class="`status-${stage.status}`"
                    >
                      {{ getStageStatusText(stage.status) }}
                    </div>
                    <div class="timeline-time">
                      <span class="time-label">开始时间：</span>
                      <span class="time-value">{{ stage.startTime }}</span>
                    </div>
                    <div
                      v-if="stage.status === 'completed'"
                      class="timeline-time"
                    >
                      <span class="time-label">完成时间：</span>
                      <span class="time-value">{{ stage.endTime }}</span>
                    </div>
                    <div
                      v-if="stage.status === 'in_progress'"
                      class="timeline-time"
                    >
                      <span class="time-label">预计完成：</span>
                      <span class="time-value">{{ stage.endTime }}</span>
                    </div>
                  </div>
                </a-timeline-item>
              </a-timeline>
            </div>
          </a-card>
        </a-col>

        <!-- 右侧：当前阶段详情表单 -->
        <a-col :span="18">
          <a-card>
            <template #title>
              <div class="stage-detail-title">
                <span style="font-weight: 600">当前阶段详情</span>
                <img
                  v-if="isAudited"
                  src="@/assets/audited.png"
                  alt="已审核"
                  class="audited-logo"
                />
              </div>
            </template>
            <div class="stage-detail">
              <!-- 1. 阶段基础信息 -->
              <a-card class="stage-section">
                <template #title>
                  <span>📌 阶段信息</span>
                </template>
                <a-form :disabled="!isEdit" :model="stageForm">
                  <a-row :gutter="12">
                    <a-col :span="12">
                      <a-form-item label="阶段开始时间">
                        <a-date-picker
                          showTime
                          v-model="stageForm.enterTime"
                          style="width: 100%"
                        />
                      </a-form-item>
                    </a-col>
                    <a-col :span="12">
                      <a-form-item label="预计完成时间">
                        <a-date-picker
                          showTime
                          v-model="stageForm.expectTime"
                          style="width: 100%"
                        />
                      </a-form-item>
                    </a-col>
                  </a-row>
                </a-form>
              </a-card>

              <!-- 2. 考察记录（富文本） -->
              <a-card class="stage-section">
                <template #title>
                  <span>📝 考察记录</span>
                </template>
                <a-textarea
                  v-model="stageForm.inspectRecord"
                  :disabled="!isEdit"
                  :rows="6"
                  placeholder="请输入思想表现、学习情况、谈话记录等..."
                  style="width: 100%"
                />
              </a-card>

              <!-- 3. 阶段材料 -->
              <a-card class="stage-section">
                <template #title>
                  <span>📎 阶段材料</span>
                </template>
                <a-space direction="vertical" style="width: 100%">
                  <a-space
                    v-for="material in materialList"
                    :key="material.id"
                    style="width: 100%"
                  >
                    <icon-file-text style="color: #165dff" />
                    <span>{{ material.name }}</span>
                    <span
                      v-if="material.status === '已上传'"
                      style="color: #52c41a; margin-left: 8px"
                    >
                      {{ material.fileName }}
                    </span>
                    <span
                      v-else-if="material.status === '未上传'"
                      style="color: #ff4d4f; margin-left: 8px"
                    >
                      未上传
                    </span>
                    <a-space style="margin-left: auto">
                      <a-button
                        v-if="material.status === '已上传'"
                        size="small"
                        type="text"
                        @click="previewFile(material.fileId, material.fileName)"
                      >
                        预览
                      </a-button>
                      <a-button
                        v-if="material.status === '已上传'"
                        size="small"
                        type="text"
                        @click="
                          downloadFile(material.fileId, material.fileName)
                        "
                      >
                        下载
                      </a-button>
                      <a-upload
                        v-if="isEdit && material.status === '未上传'"
                        :show-file-list="false"
                        :action="''"
                        :before-upload="
                          (file) => handleUploadBefore(file, material.id)
                        "
                        :custom-request="
                          (options) => handleCustomRequest(options, material.id)
                        "
                      >
                        <a-button size="small" type="text">上传</a-button>
                      </a-upload>
                    </a-space>
                  </a-space>
                </a-space>
              </a-card>

              <!-- 4. 审核记录 -->
              <a-card class="stage-section">
                <template #title>
                  <span>✅ 审核记录</span>
                </template>
                <div v-if="auditRecords.length > 0" class="audit-record">
                  <div
                    v-for="record in auditRecords"
                    :key="record.id"
                    class="audit-item-container"
                  >
                    <div class="audit-item">
                      <b>审核人：</b>{{ record.auditor }}
                    </div>
                    <div class="audit-item">
                      <b>审核时间：</b>{{ record.auditTime }}
                    </div>
                    <div class="audit-item">
                      <b>审核结果：</b
                      ><a-tag
                        :color="record.auditResult === 1 ? 'green' : 'red'"
                        >{{
                          record.auditResult === 1 ? "通过" : "不通过"
                        }}</a-tag
                      >
                    </div>
                    <div class="audit-item">
                      <b>审核意见：</b>{{ record.auditRemark }}
                    </div>
                    <div class="audit-divider"></div>
                  </div>
                </div>
                <div v-else class="audit-record">
                  <div class="audit-item">暂无审核记录</div>
                </div>
              </a-card>
            </div>
          </a-card>
        </a-col>
      </a-row>

      <!-- 底部操作按钮 -->
      <div class="modal-footer">
        <a-button @click="handleInfoCancel">取消</a-button>
        <a-button
          v-if="isEdit"
          type="primary"
          @click="saveForm"
          :disabled="isAudited"
          >保存</a-button
        >
        <a-button type="primary" @click="submitAudit" :disabled="isAudited"
          >提交审核</a-button
        >
        <a-button type="dashed" @click="viewChangeLog">查看变更日志</a-button>
      </div>
    </a-modal>

    <!-- 变更日志模态框 -->
    <a-modal
      v-model:visible="changeLogModalVisible"
      title="变更日志"
      width="800px"
    >
      <a-table
        :data="changeLogs"
        :columns="changeLogColumns"
        :pagination="false"
      />
    </a-modal>

    <!-- 新增发展记录模态框 -->
    <a-modal
      v-model:visible="addDevelopmentVisible"
      title="新增发展记录"
      width="600px"
      @cancel="handleAddDevelopmentCancel"
      @ok="handleAddDevelopmentOk"
    >
      <a-form :model="developmentForm" layout="vertical">
        <a-form-item label="发展人姓名" required>
          <a-select v-model="developmentForm.userId" placeholder="请选择">
            <a-option
              v-for="user in nonFormalPartyMembers"
              :key="user.id"
              :value="user.id"
            >
              {{ user.userName }} ({{ user.userAccount }})
            </a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="阶段名称" required>
          <a-select
            v-model="developmentForm.stageName"
            placeholder="请选择阶段名称"
          >
            <a-option value="积极分子">积极分子</a-option>
            <a-option value="发展对象">发展对象</a-option>
            <a-option value="预备党员">预备党员</a-option>
            <a-option value="正式党员">正式党员</a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="阶段开始时间" required>
          <a-date-picker
            v-model="developmentForm.stageStartTime"
            type="datetime"
            style="width: 100%"
          />
        </a-form-item>
        <a-form-item label="培养人" required>
          <a-select
            v-model="developmentForm.trainerId"
            placeholder="请选择培养人"
          >
            <a-option
              v-for="user in formalPartyMembers"
              :key="user.id"
              :value="user.id"
            >
              {{ user.userName }} ({{ user.userAccount }})
            </a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="考察内容">
          <a-textarea
            v-model="developmentForm.assessmentContent"
            :rows="4"
            placeholder="请输入考察内容"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import { computed, h, onMounted, reactive, ref, watchEffect } from "vue";
import message from "@arco-design/web-vue/es/message";
// 导入组织管理API
import * as OrganizationApi from "@/api/dangzuzhiguanli";
// 导入用户管理API
import * as UserApi from "@/api/yonghuguanli";
// 正确导入Arco图标
import {
  IconCheckCircle,
  IconDown,
  IconDownload,
  IconEdit,
  IconFileText,
  IconLeft,
  IconPlus,
  IconPrinter,
  IconDelete,
} from "@arco-design/web-vue/es/icon";
// 导入发展阶段相关API
import * as DevelopmentStageApi from "@/api/fazhanjieduanguanli";
// 导入文件管理相关API
import * as FileApi from "@/api/wenjianguanli";
// 导入培养人关系相关API
import * as TrainerRelationApi from "@/api/peiyangrenguanlianguanli";
// 导入发展阶段变更日志相关API
import * as DevelopmentStageLogApi from "@/api/fazhanjieduanbiangengrizhi";
// 导入权限枚举
import ACCESS_ENUM from "@/access/accessEnum";

// 下拉框默认值
const cropTypeDefaultValue = ref("全部");
const developStageDefaultValue = ref("全部");
const partyMemberTypeDefaultValue = ref("全部");

// 搜索参数
const searchParams = ref({
  current: 1,
  pageSize: 10,
  cropName: "", // 姓名/学号/工号
  orgLevel: "", // 组织层级
  politicalStatus: "", // 发展阶段
  userType: "", // 党员类型
});
const total = ref(0);
const dataList = ref([]);
// 所有过滤后的数据（未分页）
const allFilteredData = ref([]);
const alignLeft = ref(false);
const loading = ref(false);

// 统计数据
const stats = reactive({
  total: 0,
  activist: 0,
  developmentObject: 0,
  probationaryPartyMember: 0,
  formalPartyMember: 0,
});

// 表格选择相关
const rowSelection = reactive({
  type: "checkbox",
  showCheckedAll: true,
  onlyCurrent: false,
});
const selectedRowKeys = ref([]);
const handleSelectionChange = (keys: any) => {
  selectedRowKeys.value = keys;
};

// 详情模态框相关
const infoVisible = ref(false);
const isEdit = ref(false); // 编辑状态标识
// 当前发展记录ID
const currentStageId = ref(0);
// 当前记录是否已审核完成
const isAudited = ref(false);
// 党员基础信息表单
const addFormData = reactive({
  name: "张三",
  identity: "教师党员",
  branch: "教师一支部",
  currentStage: "发展对象",
  trainer: "李四（教师党员）",
});
// 材料列表
const materialList = ref([
  {
    id: 1,
    name: "政审材料",
    type: "political",
    status: "未上传",
    fileId: null,
    fileName: null,
    uploadTime: null,
    filePath: null, // 存储上传返回的路径
  },
  {
    id: 2,
    name: "思想汇报",
    type: "report",
    status: "未上传",
    fileId: null,
    fileName: null,
    uploadTime: null,
    filePath: null, // 存储上传返回的路径
  },
]);

// 存储材料路径的对象
const materialPaths = reactive({
  political: null,
  report: null,
});
// 阶段详情表单
const stageForm = reactive({
  enterTime: "2025-01-10",
  expectTime: "2025-06-30",
  inspectRecord:
    "思想表现良好，积极参与支部学习活动，谈话过程中对党的理论认知清晰，符合发展对象标准。",
});

// 变更日志
const changeLogs = ref<any[]>([]);
const changeLogModalVisible = ref(false);

// 变更日志表格列定义
const changeLogColumns = [
  {
    title: "变更时间",
    dataIndex: "createTime",
    key: "createTime",
    width: "180px",
  },
  {
    title: "变更类型",
    dataIndex: "operationType",
    key: "operationType",
    width: "120px",
  },
  {
    title: "变更内容",
    dataIndex: "remark",
    key: "remark",
  },
  {
    title: "操作人",
    dataIndex: "operatorName",
    key: "operatorName",
    width: "100px",
  },
];

// 发展阶段历史记录
const stageHistory = ref([
  {
    id: 1,
    stageName: "积极分子",
    status: "completed", // completed, in_progress, not_started
    startTime: "2025-01-10",
    endTime: "2025-06-30",
  },
  {
    id: 2,
    stageName: "发展对象",
    status: "in_progress",
    startTime: "2025-07-01",
    endTime: "2025-12-31",
  },
  {
    id: 3,
    stageName: "预备党员",
    status: "not_started",
    startTime: null,
    endTime: null,
  },
  {
    id: 4,
    stageName: "正式党员",
    status: "not_started",
    startTime: null,
    endTime: null,
  },
]);

// 审核记录
const auditRecords = ref([
  {
    id: 1,
    auditor: "王五",
    auditTime: "2025-02-01",
    auditRemark: "同意继续培养",
    auditResult: 1,
  },
]);

// 模态框事件
const handleInfoOk = async () => {
  infoVisible.value = false;
};
const handleInfoCancel = () => {
  infoVisible.value = false;
  isEdit.value = false; // 关闭时重置编辑状态
};
const toShowInfo = async (record: any) => {
  try {
    // 先设置基本信息
    addFormData.name = record.userName || "张三";
    addFormData.identity =
      record.userType === "teacher" ? "教师党员" : "学生党员";
    addFormData.branch = record.orgName || record.orgId || "教师一支部";
    addFormData.currentStage =
      record.stageName || record.stageType || "发展对象";
    addFormData.trainer = record.trainerName || "未知";

    // 从接口获取详细信息
    // 设置当前发展记录ID
    currentStageId.value = record.id;

    const res = await DevelopmentStageApi.getDevelopmentStageByIdUsingGet({
      id: record.id,
    });
    if (res.data.code === 0) {
      const stageData = res.data.data;
      // 更新阶段详情表单
      stageForm.enterTime = stageData.stageStartTime
        ? new Date(stageData.stageStartTime)
        : "";
      stageForm.expectTime = stageData.stageEndTime
        ? new Date(stageData.stageEndTime)
        : "";
      stageForm.inspectRecord = stageData.assessmentContent || "";

      // 判断是否已审核完成（只有assessmentResult为1-合格或0-不合格时才显示已审核logo）
      isAudited.value =
        stageData.assessmentResult === 0 || stageData.assessmentResult === 1;

      // 从接口返回的数据中提取审核记录
      auditRecords.value = [];
      if (stageData.auditTime) {
        // 尝试获取审核人姓名，实际项目中应调用用户接口获取
        let auditorName = "未知审核人";
        if (stageData.auditUserId) {
          try {
            const auditUserRes = await UserApi.getUserByIdUsingGet({
              id: Number(stageData.auditUserId),
            });
            if (auditUserRes.data.code === 0 && auditUserRes.data.data) {
              auditorName = auditUserRes.data.data.userName || "未知审核人";
            }
          } catch (err) {
            console.error("获取审核人信息失败:", err);
          }
        }

        auditRecords.value = [
          {
            id: 1,
            auditor: auditorName,
            auditTime: stageData.auditTime,
            auditRemark: stageData.auditRemark || "无",
            auditResult: stageData.assessmentResult || 0,
          },
        ];
      }
    }

    // 获取用户的所有发展阶段记录
    try {
      const userId = record.userId || record.user?.id;
      if (userId) {
        const stageListRes =
          await DevelopmentStageApi.getDevelopmentStagesByUserIdUsingGet({
            userId: Number(userId),
          });
        if (stageListRes.data.code === 0 && stageListRes.data.data) {
          const stages = stageListRes.data.data;
          // 排序发展阶段记录，按阶段开始时间排序
          stages.sort((a: any, b: any) => {
            return (
              new Date(a.stageStartTime).getTime() -
              new Date(b.stageStartTime).getTime()
            );
          });

          // 更新发展阶段历史记录
          stageHistory.value = stages.map((stage: any) => ({
            id: stage.id,
            stageName: stage.stageName,
            status:
              stage.assessmentResult === 2
                ? "in_progress"
                : stage.stageStatus === 0
                ? "in_progress"
                : "completed",
            startTime: stage.stageStartTime,
            endTime: stage.stageEndTime,
          }));
        }
      }
    } catch (err) {
      console.error("获取发展阶段历史记录失败:", err);
    }

    infoVisible.value = true;
  } catch (error) {
    console.error("获取发展记录详情失败:", error);
    message.error("获取详情失败");
    // 即使获取失败，也显示基本信息
    addFormData.name = record.userName || "张三";
    addFormData.identity =
      record.userType === "teacher" ? "教师党员" : "学生党员";
    addFormData.branch = record.orgName || record.orgId || "教师一支部";
    addFormData.currentStage =
      record.stageName || record.stageType || "发展对象";
    addFormData.trainer = record.trainerName || "未知";
    infoVisible.value = true;
  }
};

// 下拉框选择事件
const cropTypeSelect = (value: string) => {
  cropTypeDefaultValue.value = value === "partyCommittees" ? "党委" : "支部";
  searchParams.value.orgLevel = value;
};
const developStageSelect = (value: string) => {
  const stageMap = {
    all: "全部",
    activist: "积极分子",
    developmentObject: "发展对象",
    probationaryPartyMember: "预备党员",
    formalPartyMember: "正式党员",
  };
  developStageDefaultValue.value = stageMap[value] || "全部";
  searchParams.value.politicalStatus = value === "all" ? "" : value;
};
const partyMemberTypeSelect = (value: string) => {
  partyMemberTypeDefaultValue.value = value === "teacher" ? "教师" : "学生";
  searchParams.value.userType = value;
};

// 获取状态颜色
const getStatusColor = (status: string) => {
  const colorMap = {
    积极分子: "blue",
    发展对象: "green",
    预备党员: "orange",
    正式党员: "red",
    activist: "blue",
    developmentObject: "green",
    probationaryPartyMember: "orange",
    formalPartyMember: "red",
  };
  return colorMap[status] || "gray";
};

// 重置搜索参数
const handleReset = () => {
  searchParams.value = {
    current: 1,
    pageSize: 10,
    cropName: "", // 姓名/学号/工号
    orgLevel: "", // 组织层级
    politicalStatus: "", // 发展阶段
    userType: "", // 党员类型
  };
  cropTypeDefaultValue.value = "全部";
  developStageDefaultValue.value = "全部";
  partyMemberTypeDefaultValue.value = "全部";
  loadData();
};

// 分页大小变化
const onSizeChange = (pageSize: number) => {
  searchParams.value.pageSize = pageSize;
  loadData();
};

// 加载数据
const loadData = async () => {
  try {
    loading.value = true;
    console.log("开始加载数据");

    // 获取当前用户信息，获取用户角色
    const loginUserRes = await UserApi.getLoginUserUsingGet();
    console.log("loginUserRes:", loginUserRes);
    const userRole = loginUserRes.data.data?.userRole || "";
    console.log("当前用户角色:", userRole);

    // 根据用户角色选择调用的API
    let stageRes;
    if (userRole === ACCESS_ENUM.SUPER_ADMIN_ROLE) {
      // 超级管理员调用获取所有发展阶段列表
      stageRes = await DevelopmentStageApi.getDevelopmentStagesListUsingPost({
        current: searchParams.value.current,
        pageSize: searchParams.value.pageSize,
      });
    } else {
      // 组织管理员调用根据组织ID获取发展阶段列表
      stageRes = await DevelopmentStageApi.getDevelopmentStagesByOrgIdUsingGet({
        orgId: 1,
      });
    }

    console.log("接口返回结果:", stageRes);

    if (stageRes && stageRes.data.code === 0) {
      const stages = Array.isArray(stageRes.data.data)
        ? stageRes.data.data
        : [];
      total.value = stages.length;
      console.log("000");
      console.log("发展阶段数据:", stages);

      // 为每个发展阶段获取用户信息和培养人信息
      const stagesWithDetails = await Promise.all(
        stages.map(async (stage: any) => {
          try {
            // 获取用户信息
            const userRes = await UserApi.getUserByIdUsingGet({
              id: stage.userId,
            });
            console.log("用户信息:", userRes);
            const user = userRes.data.code === 0 ? userRes.data.data : null;
            console.log("222 - 用户:", user);

            // 获取组织信息
            let orgName = "未知支部";
            if (user && user.orgId) {
              try {
                const orgRes =
                  await OrganizationApi.getOrganizationByIdUsingGet({
                    id: Number(user.orgId),
                  });
                console.log("orgRes:", orgRes);
                if (orgRes.data.code === 0 && orgRes.data.data) {
                  orgName = orgRes.data.data.orgName || "未知支部";
                }
              } catch (err) {
                console.error("获取组织信息出错:", err);
              }
            }

            // 获取培养人信息
            let trainerName = "";
            if (stage.trainerId) {
              const trainerUserRes = await UserApi.getUserByIdUsingGet({
                id: stage.trainerId,
              });
              trainerName =
                trainerUserRes.data.code === 0 && trainerUserRes.data.data
                  ? trainerUserRes.data.data.userName
                  : "";
              console.log("培养人信息:", trainerUserRes);
            }

            return {
              ...stage,
              user,
              userName: user?.userName || "未知",
              userType: user?.userType || "未知",
              orgId: user?.orgId || "未知",
              orgName: orgName,
              politicalStatus: user?.politicalStatus || "未知",
              joinDate: user?.joinDate || "",
              positiveDate: user?.positiveDate || "",
              trainerName,
              stageType: stage.stageName, // 映射stageName到stageType以保持兼容
              status: stage.stageStatus === 0 ? "进行中" : "已完成", // 映射stageStatus到status
            };
          } catch (err) {
            console.error("处理阶段出错:", err);
            return {
              ...stage,
              user: null,
              userName: "未知",
              userType: "未知",
              orgId: "未知",
              politicalStatus: "未知",
              joinDate: "",
              positiveDate: "",
              trainerName: "",
              stageType: stage.stageName || "未知",
              status: stage.stageStatus === 0 ? "进行中" : "已完成",
            };
          }
        }),
      );

      console.log("处理后的数据:", stagesWithDetails);

      // 根据搜索条件过滤数据
      let filteredData = stagesWithDetails;

      // 按发展阶段过滤
      if (searchParams.value.politicalStatus) {
        filteredData = filteredData.filter(
          (stage: any) =>
            stage.stageType === searchParams.value.politicalStatus,
        );
        console.log("按发展阶段过滤后:", filteredData);
      }

      // 按用户类型过滤
      if (searchParams.value.userType) {
        filteredData = filteredData.filter(
          (stage: any) => stage.userType === searchParams.value.userType,
        );
        console.log("按用户类型过滤后:", filteredData);
      }

      // 按姓名/学号/工号搜索
      if (searchParams.value.cropName) {
        const searchTerm = searchParams.value.cropName.toLowerCase();
        filteredData = filteredData.filter(
          (stage: any) =>
            stage.userName.toLowerCase().includes(searchTerm) ||
            (stage.user &&
              (stage.user.userAccount || "")
                .toLowerCase()
                .includes(searchTerm)),
        );
        console.log("按姓名搜索后:", filteredData);
      }

      // 保存所有过滤后的数据
      allFilteredData.value = filteredData;

      // 分页处理
      const start =
        (searchParams.value.current - 1) * searchParams.value.pageSize;
      const end = start + searchParams.value.pageSize;
      dataList.value = filteredData.slice(start, end);
      console.log("分页后的数据:", dataList.value);

      // 更新统计数据
      updateStats(filteredData);
      console.log("统计数据:", stats);
    } else {
      console.error("接口返回错误:", stageRes);
      message.error("加载失败：" + (stageRes?.data?.message || "未知错误"));
    }
  } catch (error) {
    console.error("加载数据出错:", error);
    message.error("网络异常，加载失败");
  } finally {
    loading.value = false;
    console.log("加载完成");
  }
};

// 更新统计数据
const updateStats = (stages: any[]) => {
  const newStats = {
    total: stages.length,
    activist: 0,
    developmentObject: 0,
    probationaryPartyMember: 0,
    formalPartyMember: 0,
  };

  stages.forEach((stage) => {
    switch (stage.stageType) {
      case "积极分子":
      case "activist":
        newStats.activist++;
        break;
      case "发展对象":
      case "developmentObject":
        newStats.developmentObject++;
        break;
      case "预备党员":
      case "probationaryPartyMember":
        newStats.probationaryPartyMember++;
        break;
      case "正式党员":
      case "formalPartyMember":
        newStats.formalPartyMember++;
        break;
    }
  });

  Object.assign(stats, newStats);
};

// 分页事件
const onPageChange = (page: number) => {
  searchParams.value.current = page;
  loadData(); // 分页切换时重新加载数据
};

// 搜索事件
const handlesumbit = () => {
  searchParams.value.current = 1; // 搜索时重置页码
  loadData();
};

// 详情页操作方法
const goBackList = () => {
  infoVisible.value = false;
};
const toggleEdit = () => {
  if (isAudited.value) {
    message.warning("该记录已审核完成，不能编辑");
    return;
  }
  isEdit.value = !isEdit.value;
  message.info(isEdit.value ? "进入编辑模式" : "退出编辑模式");
};
const saveForm = async () => {
  if (!isEdit.value) {
    message.warning("请先进入编辑模式");
    return;
  }
  try {
    const updateRequest = {
      id: currentStageId.value,
      assessmentContent: stageForm.inspectRecord,
      stageEndTime: stageForm.expectTime,
      stageStartTime: stageForm.enterTime,
    };
    const res = await DevelopmentStageApi.updateDevelopmentStageUsingPost(
      updateRequest,
    );
    if (res.data.code === 0) {
      message.success("保存成功");
      isEdit.value = false;
      loadData();
    } else {
      message.error("保存失败：" + (res.data.message || "未知错误"));
    }
  } catch (error) {
    console.error("保存失败:", error);
    message.error("网络异常，保存失败");
  }
};
const printInfo = () => {
  message.info("打印功能开发中...");
};
const previewFile = async (fileId: number, fileName: string) => {
  try {
    // 调用文件下载接口
    const res = await FileApi.downloadFileUsingGet({ fileId });
    if (res.data.code === 0) {
      // 预览文件，实际项目中可能需要处理文件流
      message.success(`预览${fileName}成功`);
    } else {
      message.error("预览失败: " + res.data.message);
    }
  } catch (error) {
    console.error("预览文件失败:", error);
    message.error("预览失败");
  }
};
const downloadFile = async (fileId: number, fileName: string) => {
  try {
    // 调用文件下载接口
    const res = await FileApi.downloadFileUsingGet({ fileId });
    if (res.data.code === 0) {
      // 下载文件，实际项目中可能需要处理文件流
      message.success(`下载${fileName}成功`);
    } else {
      message.error("下载失败: " + res.data.message);
    }
  } catch (error) {
    console.error("下载文件失败:", error);
    message.error("下载失败");
  }
};
// 处理文件上传前的逻辑
const handleUploadBefore = async (file: any, materialId: number) => {
  console.log("上传的文件:", file);
  if (!file) {
    message.error("请选择要上传的文件");
    return false;
  }
  // 阻止默认上传行为
  return false;
};

// 处理自定义上传逻辑
const handleCustomRequest = async (options: any, materialId: number) => {
  const { fileItem, onSuccess, onError } = options;
  const file = fileItem.file as File;
  console.log("自定义上传文件:", file);
  try {
    // 调用文件上传接口
    const res = await FileApi.uploadMaterialFileUsingPost({}, file);
    if (res.data.code === 0) {
      // 更新材料列表状态
      const material = materialList.value.find(
        (item) => item.id === materialId,
      );
      if (material) {
        material.status = "已上传";
        material.fileId = 1; // 假设返回的文件ID为1，实际应从接口返回获取
        material.fileName = file.name;
        material.uploadTime = new Date()
          .toISOString()
          .slice(0, 19)
          .replace("T", " ");
        material.filePath = res.data.data; // 存储上传返回的路径

        // 更新materialPaths对象
        if (material.type === "political") {
          materialPaths.political = res.data.data;
        } else if (material.type === "report") {
          materialPaths.report = res.data.data;
        }

        // 分装成JSON字符串
        const materialJson = JSON.stringify(materialPaths);
        console.log("材料路径JSON:", materialJson);

        // 调用updateAttachmentByIdUsingPost接口更新到后端
        try {
          const updateRes =
            await DevelopmentStageApi.updateAttachmentByIdUsingPost({
              id: currentStageId.value,
              attachment: materialJson,
            });
          if (updateRes.data.code === 0) {
            console.log("材料路径更新成功");
          } else {
            console.error("材料路径更新失败:", updateRes.data.message);
          }
        } catch (updateError) {
          console.error("调用更新接口失败:", updateError);
        }
      }
      message.success("上传成功");
      onSuccess(res.data);
    } else {
      const error = new Error(res.data.message || "上传失败");
      message.error("上传失败: " + res.data.message);
      onError(error);
    }
  } catch (error) {
    console.error("上传文件失败:", error);
    message.error("上传失败");
    onError(error);
  }
};

// 时间轴相关方法
const getStageDot = (status: string) => {
  switch (status) {
    case "completed":
      return h("icon-check", { style: { color: "#00b42a" } });
    case "in_progress":
      return h("icon-circle-fill", { style: { color: "#165dff" } });
    case "not_started":
    default:
      return h("icon-circle", { style: { color: "#86909c" } });
  }
};

const getStageColor = (status: string) => {
  switch (status) {
    case "completed":
      return "green";
    case "in_progress":
      return "blue";
    case "not_started":
    default:
      return "gray";
  }
};

const getStageStatusText = (status: string | number) => {
  if (typeof status === "number") {
    switch (status) {
      case 0:
        return "进行中";
      case 1:
        return "已完成";
      case 2:
        return "已终止";
      default:
        return "未知";
    }
  }
  switch (status) {
    case "completed":
      return "已完成";
    case "in_progress":
      return "审核中";
    case "not_started":
    default:
      return "未开始";
  }
};

const getStageStatusColor = (status: number) => {
  switch (status) {
    case 0:
      return "blue";
    case 1:
      return "green";
    case 2:
      return "red";
    default:
      return "gray";
  }
};

const getAssessmentResultText = (result: number) => {
  switch (result) {
    case 1:
      return "合格";
    case 0:
      return "不合格";
    case 2:
      return "未审核";
    default:
      return "未提交审核";
  }
};

const getAssessmentResultColor = (result: number) => {
  switch (result) {
    case 1:
      return "green";
    case 0:
      return "red";
    case 2:
      return "orange";
    default:
      return "gray";
  }
};

// 获取阶段状态对应的emoji
const getStageEmoji = (status: string) => {
  switch (status) {
    case "completed":
      return "✅";
    case "in_progress":
      return "🔄";
    case "not_started":
    default:
      return "⏳";
  }
};

// 日期格式化函数，只提取日期部分
const formatDate = (dateString: string) => {
  if (!dateString) return "";
  try {
    // 提取日期部分，去掉时间
    return dateString.split(" ")[0];
  } catch (error) {
    return dateString;
  }
};

// 查看变更日志
const viewChangeLog = async () => {
  if (!currentStageId.value) {
    message.error("请先选择发展阶段");
    return;
  }

  try {
    const res = await DevelopmentStageLogApi.getLogsByStageIdUsingGet({
      stageId: currentStageId.value,
    });
    console.log("changeLogs:", res);
    if (res.data.code === 0) {
      changeLogs.value = res.data.data || [];
      changeLogModalVisible.value = true;
    } else {
      message.error(res.data.message || "获取变更日志失败");
    }
  } catch (error) {
    console.error("获取变更日志失败:", error);
    message.error("获取变更日志失败");
  }
};

const submitAudit = async () => {
  try {
    const res = await DevelopmentStageApi.submitForAuditUsingPost({
      id: currentStageId.value,
      assessmentContent: stageForm.inspectRecord,
    });
    if (res.data.code === 0) {
      message.success("提交审核成功，等待管理员审批");
      infoVisible.value = false;
      loadData();
    } else {
      message.error("提交审核失败：" + (res.data.message || "未知错误"));
    }
  } catch (error) {
    console.error("提交审核失败:", error);
    message.error("网络异常，提交审核失败");
  }
};

// 审核发展阶段记录
const auditDevelopmentStage = async (
  id: number,
  result: number,
  remark: string,
) => {
  try {
    const res = await DevelopmentStageApi.auditDevelopmentStageUsingPost({
      id,
      assessmentResult: result,
      auditRemark: remark,
    });
    if (res.data.code === 0) {
      message.success("审核成功");
      loadData();
    } else {
      message.error("审核失败：" + (res.data.message || "未知错误"));
    }
  } catch (error) {
    console.error("审核失败:", error);
    message.error("网络异常，审核失败");
  }
};

// 新增发展记录
const AddData = () => {
  // 打开新增发展记录模态框
  currentUser.value = null;
  developmentForm.userId = "";
  developmentForm.stageName = "";
  developmentForm.stageStartTime = "";
  developmentForm.trainerId = "";
  developmentForm.assessmentContent = "";
  addDevelopmentVisible.value = true;
};

// 用户列表
const userList = ref<any[]>([]);

// 加载用户列表
const loadUserList = async () => {
  try {
    const res = await UserApi.listUserByPageUsingPost({
      current: 1,
      pageSize: 100, // 加载足够多的用户
    });
    // 注意：res 是整个响应对象，res.data 才是响应体
    if (res.data.code === 0) {
      userList.value = res.data.data.records;
    }
  } catch (error) {
    console.error("加载用户列表失败:", error);
  }
};

// 新增发展记录模态框
const addDevelopmentVisible = ref(false);
const currentUser = ref<any>({});
const developmentForm = reactive({
  userId: "",
  stageName: "",
  stageStartTime: "",
  trainerId: "",
  assessmentContent: "",
});

const toAddDevelopmentRecord = (record: any) => {
  currentUser.value = record.user || record;
  developmentForm.userId = record.userId || record.id;
  developmentForm.stageType = "";
  developmentForm.enterTime = "";
  developmentForm.expectTime = "";
  developmentForm.assessmentContent = "";
  addDevelopmentVisible.value = true;
};

const handleAddDevelopmentOk = async () => {
  try {
    // 准备请求数据
    const addRequest = {
      userId: Number(developmentForm.userId),
      stageName: developmentForm.stageName,
      stageStartTime: developmentForm.stageStartTime,
      trainerId: Number(developmentForm.trainerId),
      assessmentContent: developmentForm.assessmentContent,
    };

    console.log("新增发展记录请求:", addRequest);

    // 调用API
    const res = await DevelopmentStageApi.addDevelopmentStageUsingPost(
      addRequest,
    );

    // 注意：res 是整个响应对象，res.data 才是响应体
    if (res.data.code === 0) {
      message.success("新增发展记录成功");
      addDevelopmentVisible.value = false;
      // 重新加载数据
      loadData();
    } else {
      message.error("新增失败：" + (res.data.message || "未知错误"));
    }
  } catch (error) {
    console.error("新增发展记录失败:", error);
    message.error("网络异常，新增失败");
  }
};

const handleAddDevelopmentCancel = () => {
  addDevelopmentVisible.value = false;
};

// 初始化
onMounted(() => {
  loadData();
  loadUserList();
});

// 计算属性：非正式党员列表（排除已有进行中阶段的用户）
const nonFormalPartyMembers = computed(() => {
  // 获取所有进行中阶段的用户ID
  const inProgressUserIds = new Set(
    allFilteredData.value
      .filter((item) => item.stageStatus === 0)
      .map((item) => item.userId),
  );

  return userList.value.filter(
    (user) =>
      user.politicalStatus !== "党员" &&
      user.politicalStatus !== "formalPartyMember" &&
      !inProgressUserIds.has(user.id),
  );
});

// 计算属性：正式党员列表
const formalPartyMembers = computed(() => {
  return userList.value.filter(
    (user) =>
      user.politicalStatus === "党员" ||
      user.politicalStatus === "formalPartyMember",
  );
});
const delData = () => {
  message.info("批量导出功能开发中...");
};
const modifyData = (type: number) => {
  if (selectedRowKeys.value.length === 0) {
    message.warning("请选择要审核的记录");
    return;
  }

  if (selectedRowKeys.value.length > 1) {
    message.warning("只能选择一条记录进行审核");
    return;
  }

  // 审核操作，实际项目中应弹出审核对话框
  const id = selectedRowKeys.value[0];
  auditDevelopmentStage(id, type, "审核通过");
};

// 初始加载数据不需要watchEffect，因为onMounted已经调用了loadData
</script>

<style scoped>
.trainer-relation-container {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: 100vh;
}

/* 页面标题和面包屑 */
.page-header {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e8e8e8;
}

.page-header h1 {
  font-size: 24px;
  font-weight: 600;
  color: #1890ff;
  margin: 0;
  margin-top: 8px;
}

.breadcrumb {
  margin-bottom: 8px;
}

/* 统计卡片 */
.stats-container {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.stat-card {
  flex: 1;
  min-width: 150px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.12);
}

.stat-number {
  font-size: 32px;
  font-weight: 600;
  margin-bottom: 8px;
  color: #1890ff;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

/* 搜索卡片 */
.search-card {
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.search-form {
  padding: 20px;
}

/* 操作按钮区域 */
.action-bar {
  margin-bottom: 20px;
}

/* 列表卡片 */
.list-card {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

/* 详情模态框样式 */
.basic-info {
  display: flex;
  flex-wrap: wrap;
  gap: 24px;
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e8e8e8;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.info-label {
  font-weight: 600;
  color: #333;
  min-width: 80px;
}

.info-value {
  color: #666;
}

.info-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

.timeline-container {
  max-height: 500px;
  overflow-y: auto;
}

.timeline-item {
  position: relative;
  transition: all 0.3s ease;
}

.timeline-item:hover {
  transform: translateX(5px);
}

.timeline-content {
  background: #f9f9f9;
  border-radius: 8px;
  padding: 12px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.timeline-item:hover .timeline-content {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background: #f0f7ff;
}

.timeline-title {
  font-weight: 600;
  margin-bottom: 8px;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.stage-emoji {
  font-size: 16px;
  animation: pulse 2s infinite;
}

.timeline-status {
  font-size: 12px;
  font-weight: 500;
  margin-bottom: 8px;
  padding: 2px 8px;
  border-radius: 12px;
  display: inline-block;
  transition: all 0.3s ease;
}

.status-completed {
  background: #e6f7ee;
  color: #52c41a;
  border: 1px solid #b7eb8f;
}

.status-in_progress {
  background: #e6f7ff;
  color: #1890ff;
  border: 1px solid #91d5ff;
  animation: blink 2s infinite;
}

.status-not_started {
  background: #fff7e6;
  color: #fa8c16;
  border: 1px solid #ffd591;
}

.timeline-time {
  font-size: 12px;
  margin-bottom: 4px;
  display: flex;
  align-items: center;
  gap: 4px;
}

.time-label {
  color: #999;
  flex-shrink: 0;
}

.time-value {
  color: #666;
  font-weight: 500;
}

/* 动画效果 */
@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
  100% {
    transform: scale(1);
  }
}

@keyframes blink {
  0% {
    opacity: 1;
  }
  50% {
    opacity: 0.7;
  }
  100% {
    opacity: 1;
  }
}

/* 滚动条优化 */
.timeline-container::-webkit-scrollbar {
  width: 6px;
}

.timeline-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.timeline-container::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 3px;
}

.timeline-container::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 时间单元格样式 */
.time-cell {
  padding: 4px 8px;
  border-radius: 6px;
  background: #f9f9f9;
  transition: all 0.3s ease;
  display: inline-block;
}

.time-cell:hover {
  background: #e6f7ff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transform: translateY(-1px);
}

.time-text {
  font-size: 12px;
  color: #666;
  font-weight: 500;
  white-space: nowrap;
}

/* 模态框标题样式 */
.stage-detail-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.audited-logo {
  width: 80px;
  height: 60px;
}

@keyframes rotate {
  0% {
    transform: rotate(0deg) scale(0.5);
    opacity: 0;
  }
  50% {
    transform: rotate(180deg) scale(1.1);
  }
  100% {
    transform: rotate(360deg) scale(1);
    opacity: 1;
  }
}

.stage-detail {
  max-height: 600px;
  overflow-y: auto;
}

.stage-section {
  margin-bottom: 16px;
}

.audit-record {
  border: 1px solid #e8e8e8;
  padding: 16px;
  border-radius: 4px;
}

.audit-item {
  margin-bottom: 8px;
}

.audit-item-container {
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px dashed #e8e8e8;
}

.audit-item-container:last-child {
  margin-bottom: 0;
  padding-bottom: 0;
  border-bottom: none;
}

.audit-divider {
  margin-top: 12px;
  border-top: 1px dashed #e8e8e8;
}

.modal-footer {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

/* 下拉框箭头旋转 */
.arco-dropdown-open .arco-icon-down {
  transform: rotate(180deg);
  transition: transform 0.2s;
}

/* 时间轴样式优化 */
:deep(.arco-timeline-item-content) {
  padding-bottom: 24px !important;
}

/* 滚动条优化 */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-thumb {
  border-radius: 3px;
  background-color: #e5e6eb;
}

::-webkit-scrollbar-track {
  background-color: transparent;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .trainer-relation-container {
    padding: 10px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .stats-container {
    flex-direction: column;
  }

  .stat-card {
    min-width: 100%;
  }

  .search-form {
    padding: 12px;
  }

  .basic-info {
    flex-direction: column;
    align-items: flex-start;
  }

  .info-actions {
    flex-wrap: wrap;
  }
}
</style>
