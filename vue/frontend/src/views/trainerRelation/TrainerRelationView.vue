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
    <div class="stats-container" v-if="!loading">
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
      <a-form layout="inline" :model="searchParams" class="search-form">
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
            placeholder="请输入姓名/学号/工号..."
            style="width: 200px"
            allow-clear
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
          <icon-download />
          批量导出
        </a-button>
        <a-button type="primary" @click="modifyData(1)">
          <icon-check-circle />
          批量审核
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
        border
        row-key="id"
        pagination-position="bottom"
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
        @selection-change="handleSelectionChange"
      >
        <template #columns>
          <a-table-column title="姓名" data-index="userName" width="120">
            <template #cell="{ record }">
              <a-tag color="blue">{{ record.userName }}</a-tag>
            </template>
          </a-table-column>
          <a-table-column title="用户类型" data-index="userType" width="100">
            <template #cell="{ record }">
              <a-tag
                :color="record.userType === 'teacher' ? 'green' : 'orange'"
              >
                {{ record.userType === "teacher" ? "教师" : "学生" }}
              </a-tag>
            </template>
          </a-table-column>
          <a-table-column title="所属支部" data-index="orgName" width="150">
            <template #cell="{ record }">
              <span>{{ record.orgName || "未知支部" }}</span>
            </template>
          </a-table-column>
          <a-table-column
            title="政治面貌"
            data-index="politicalStatus"
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
              <a-tag :color="record.stageStatus === 0 ? 'blue' : 'green'">
                {{ record.stageStatus === 0 ? "进行中" : "已完成" }}
              </a-tag>
            </template>
          </a-table-column>
          <a-table-column title="培养人" data-index="trainerName" width="120" />
          <a-table-column
            title="阶段开始时间"
            data-index="stageStartTime"
            width="180"
          />
          <a-table-column
            title="阶段结束时间"
            data-index="stageEndTime"
            width="180"
          />
          <a-table-column
            title="申请入党日期"
            data-index="joinDate"
            width="150"
          />
          <a-table-column
            title="转正日期"
            data-index="positiveDate"
            width="150"
          />
          <a-table-column title="操作" width="120">
            <template #cell="{ record }">
              <a-space wrap>
                <a-button
                  type="primary"
                  size="small"
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
      width="1400px"
      v-model:visible="infoVisible"
      :title="`${isEdit ? '编辑' : '查看'}发展记录`"
      @cancel="handleInfoCancel"
      :footer="null"
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
          <a-button type="text" @click="toggleEdit">
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
                  color="green"
                  :dot="h('icon-check', { style: { color: '#00b42a' } })"
                >
                  <div class="timeline-title">积极分子</div>
                  <div class="timeline-status">已完成</div>
                  <div class="timeline-time">完成时间：2025-01-10</div>
                </a-timeline-item>
                <a-timeline-item
                  color="blue"
                  :dot="h('icon-circle-fill', { style: { color: '#165dff' } })"
                >
                  <div class="timeline-title">发展对象</div>
                  <div class="timeline-status">进行中</div>
                  <div class="timeline-time">进入时间：2025-01-10</div>
                  <div class="timeline-time">预计完成：2025-06-30</div>
                </a-timeline-item>
                <a-timeline-item
                  color="gray"
                  :dot="h('icon-circle', { style: { color: '#86909c' } })"
                >
                  <div class="timeline-title">预备党员</div>
                  <div class="timeline-status">未开始</div>
                </a-timeline-item>
                <a-timeline-item
                  color="gray"
                  :dot="h('icon-circle', { style: { color: '#86909c' } })"
                >
                  <div class="timeline-title">正式党员</div>
                  <div class="timeline-status">未开始</div>
                </a-timeline-item>
              </a-timeline>
            </div>
          </a-card>
        </a-col>

        <!-- 右侧：当前阶段详情表单 -->
        <a-col :span="18">
          <a-card>
            <template #title>
              <span style="font-weight: 600">当前阶段详情</span>
            </template>
            <div class="stage-detail">
              <!-- 1. 阶段基础信息 -->
              <a-card class="stage-section">
                <template #title>
                  <span>📌 阶段信息</span>
                </template>
                <a-form :model="stageForm" :disabled="!isEdit">
                  <a-row :gutter="12">
                    <a-col :span="12">
                      <a-form-item label="进入时间">
                        <a-date-picker
                          v-model="stageForm.enterTime"
                          style="width: 100%"
                        />
                      </a-form-item>
                    </a-col>
                    <a-col :span="12">
                      <a-form-item label="预计完成时间">
                        <a-date-picker
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
                  placeholder="请输入思想表现、学习情况、谈话记录等..."
                  :rows="6"
                  style="width: 100%"
                  :disabled="!isEdit"
                />
              </a-card>

              <!-- 3. 阶段材料 -->
              <a-card class="stage-section">
                <template #title>
                  <span>📎 阶段材料</span>
                </template>
                <a-space direction="vertical" style="width: 100%">
                  <a-space>
                    <icon-file-text style="color: #165dff" />
                    <span>政审材料</span>
                    <a-button type="text" size="small" @click="previewFile"
                      >预览
                    </a-button>
                    <a-button type="text" size="small" @click="downloadFile"
                      >下载
                    </a-button>
                  </a-space>
                  <a-space>
                    <icon-file-text style="color: #165dff" />
                    <span>思想汇报</span>
                    <a-upload
                      v-if="isEdit"
                      action="/api/upload"
                      :show-file-list="false"
                      @change="handleUpload"
                    >
                      <a-button type="text" size="small">上传</a-button>
                    </a-upload>
                    <span v-else>未上传</span>
                  </a-space>
                </a-space>
              </a-card>

              <!-- 4. 审核记录 -->
              <a-card class="stage-section">
                <template #title>
                  <span>✅ 审核记录</span>
                </template>
                <div class="audit-record">
                  <div class="audit-item"><b>审核人：</b>王五</div>
                  <div class="audit-item"><b>审核时间：</b>2025-02-01</div>
                  <div class="audit-item"><b>审核意见：</b>同意继续培养</div>
                </div>
              </a-card>
            </div>
          </a-card>
        </a-col>
      </a-row>

      <!-- 底部操作按钮 -->
      <div class="modal-footer">
        <a-button @click="handleInfoCancel">取消</a-button>
        <a-button v-if="isEdit" type="primary" @click="saveForm">保存</a-button>
        <a-button type="primary" @click="submitAudit">提交审核</a-button>
        <a-button type="dashed" @click="viewChangeLog">查看变更日志</a-button>
      </div>
    </a-modal>

    <!-- 新增发展记录模态框 -->
    <a-modal
      v-model:visible="addDevelopmentVisible"
      title="新增发展记录"
      width="600px"
      @ok="handleAddDevelopmentOk"
      @cancel="handleAddDevelopmentCancel"
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
import { h, reactive, ref, watchEffect, onMounted, computed } from "vue";
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
} from "@arco-design/web-vue/es/icon";
// 导入发展阶段相关API
import * as DevelopmentStageApi from "@/api/fazhanjieduanguanli";

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
// 党员基础信息表单
const addFormData = reactive({
  name: "张三",
  identity: "教师党员",
  branch: "教师一支部",
  currentStage: "发展对象",
  trainer: "李四（教师党员）",
});
// 阶段详情表单
const stageForm = reactive({
  enterTime: "2025-01-10",
  expectTime: "2025-06-30",
  inspectRecord:
    "思想表现良好，积极参与支部学习活动，谈话过程中对党的理论认知清晰，符合发展对象标准。",
});

// 模态框事件
const handleInfoOk = async () => {
  infoVisible.value = false;
};
const handleInfoCancel = () => {
  infoVisible.value = false;
  isEdit.value = false; // 关闭时重置编辑状态
};
const toShowInfo = (record: any) => {
  // 模拟赋值（实际项目中从接口获取）
  addFormData.name = record.userName || "张三";
  addFormData.identity =
    record.userType === "teacher" ? "教师党员" : "学生党员";
  addFormData.branch = record.orgId || "教师一支部";
  addFormData.currentStage = record.politicalStatus || "发展对象";
  infoVisible.value = true;
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
    // 获取发展阶段列表
    const stageRes =
      await DevelopmentStageApi.getDevelopmentStagesByOrgIdUsingGet({
        orgId: 4,
      });

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
      message.error("加载失败：" + (stageRes?.message || "未知错误"));
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
  isEdit.value = !isEdit.value;
  message.info(isEdit.value ? "进入编辑模式" : "退出编辑模式");
};
const saveForm = () => {
  if (!isEdit.value) {
    message.warning("请先进入编辑模式");
    return;
  }
  // 实际项目中调用保存接口
  message.success("保存成功");
  isEdit.value = false;
};
const printInfo = () => {
  message.info("打印功能开发中...");
};
const previewFile = () => {
  message.info("预览政审材料");
};
const downloadFile = () => {
  message.info("下载政审材料成功");
};
const handleUpload = () => {
  message.success("思想汇报上传成功");
};
const submitAudit = () => {
  message.success("提交审核成功，等待管理员审批");
  infoVisible.value = false;
};
const viewChangeLog = () => {
  message.info("查看变更日志功能开发中...");
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
    if (res.code === 0) {
      userList.value = res.data.records;
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

    if (res.code === 0) {
      message.success("新增发展记录成功");
      addDevelopmentVisible.value = false;
      // 重新加载数据
      loadData();
    } else {
      message.error("新增失败：" + (res.message || "未知错误"));
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
      user.politicalStatus !== "正式党员" &&
      user.politicalStatus !== "formalPartyMember" &&
      !inProgressUserIds.has(user.id),
  );
});

// 计算属性：正式党员列表
const formalPartyMembers = computed(() => {
  return userList.value.filter(
    (user) =>
      user.politicalStatus === "正式党员" ||
      user.politicalStatus === "formalPartyMember",
  );
});
const delData = () => {
  message.info("批量导出功能开发中...");
};
const modifyData = (type: number) => {
  message.info("批量审核功能开发中...");
};

// 初始加载数据
watchEffect(() => {
  loadData();
});
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

.timeline-title {
  font-weight: 600;
  margin-bottom: 4px;
}

.timeline-status {
  font-size: 12px;
  color: #666;
  margin-bottom: 4px;
}

.timeline-time {
  font-size: 12px;
  color: #999;
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
