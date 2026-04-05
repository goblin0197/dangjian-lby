<template>
  <div class="activity-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>党建活动管理</h1>
      <div class="header-actions">
        <a-button type="primary" @click="handleCreateActivity">
          <icon-plus />
          创建活动
        </a-button>
      </div>
    </div>

    <!-- 主要内容区 -->
    <!-- 筛选区域 -->
    <a-card class="filter-card">
      <a-form layout="inline" :model="filterForm" class="filter-form">
        <a-form-item label="活动名称">
          <a-input
            v-model="filterForm.activityName"
            placeholder="请输入活动名称"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="活动类型">
          <a-select
            v-model="filterForm.activityType"
            placeholder="请选择活动类型"
            allow-clear
          >
            <a-option value="1">会议</a-option>
            <a-option value="2">志愿活动</a-option>
            <a-option value="3">学习活动</a-option>
            <a-option value="4">其他</a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="所属党组织">
          <a-select
            v-model="filterForm.orgId"
            placeholder="请选择所属党组织"
            allow-clear
          >
            <a-option
              v-for="option in orgOptions"
              :key="option.value"
              :value="option.value.toString()"
            >
              {{ option.label }}
            </a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="活动状态">
          <a-select
            v-model="filterForm.status"
            placeholder="请选择活动状态"
            allow-clear
          >
            <a-option value="1">待发布</a-option>
            <a-option value="2">已发布</a-option>
            <a-option value="3">进行中</a-option>
            <a-option value="4">已结束</a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="活动时间">
          <a-range-picker
            v-model="filterForm.timeRange"
            format="YYYY-MM-DD"
            placeholder="请选择活动时间范围"
          />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="handleSearch">查询</a-button>
          <a-button @click="handleResetFilter">重置</a-button>
        </a-form-item>
      </a-form>
    </a-card>

    <!-- 活动列表 -->
    <a-card class="list-card" style="margin-top: 16px">
      <a-table
        :data="activityList"
        :loading="loading"
        border
        row-key="id"
        pagination-position="bottom"
        :pagination="{
          current: pagination.current,
          pageSize: pagination.pageSize,
          total: pagination.total,
          showSizeChanger: true,
          showTotal: (total) => `共 ${total} 条记录`,
          onChange: handlePageChange,
          onShowSizeChange: handleSizeChange,
        }"
      >
        <template #columns>
          <a-table-column title="活动ID" data-index="id" width="100" />
          <a-table-column title="活动名称" data-index="activityName" />
          <a-table-column title="所属党组织" data-index="orgId">
            <template #cell="{ record }">
              {{ record.orgName || "未知组织" }}
            </template>
          </a-table-column>
          <a-table-column title="活动类型" data-index="activityType">
            <template #cell="{ record }">
              <a-tag :color="typeColorMap[record.activityType]">
                {{ typeLabelMap[record.activityType] }}
              </a-tag>
            </template>
          </a-table-column>
          <a-table-column title="活动时间" width="220">
            <template #cell="{ record }">
              {{ formatDate(record.startTime) }} 至
              {{ formatDate(record.endTime) }}
            </template>
          </a-table-column>
          <a-table-column title="报名截止时间" data-index="enrollDeadline">
            <template #cell="{ record }">
              {{ formatDate(record.enrollDeadline) }}
            </template>
          </a-table-column>
          <a-table-column title="活动状态" data-index="status">
            <template #cell="{ record }">
              <a-tag :color="statusColorMap[record.status]">
                {{ statusLabelMap[record.status] }}
              </a-tag>
            </template>
          </a-table-column>
          <a-table-column title="参与数据">
            <template #cell="{ record }">
              <div>
                报名人数：{{ record.totalParticipant }}/{{ record.maxNum
                }}<br />
                签到人数：{{ record.actualParticipant }}<br />
                签到率：{{
                  record.signRate
                    ? (record.signRate * 100).toFixed(2) + "%"
                    : "0%"
                }}
              </div>
            </template>
          </a-table-column>
          <a-table-column title="操作" width="420">
            <template #cell="{ record }">
              <a-button size="small" @click="handleViewDetail(record)">
                查看
              </a-button>
              <!-- 新增：报名/取消报名按钮 -->
              <a-button
                size="small"
                type="primary"
                @click="handleEnrollActivity(record)"
                v-if="[2, 3].includes(record.status) && !record.hasEnrolled"
              >
                报名
              </a-button>
              <a-button
                size="small"
                type="warning"
                @click="handleCancelEnroll(record)"
                v-if="[2, 3].includes(record.status) && record.hasEnrolled"
              >
                取消报名
              </a-button>
              <!-- 原有按钮 -->
              <a-button
                size="small"
                type="success"
                @click="handlePublishActivity(record)"
                v-if="record.status === 1"
              >
                发布
              </a-button>
              <a-button
                size="small"
                type="primary"
                @click="handleEditActivity(record)"
                v-if="[1, 2].includes(record.status)"
              >
                编辑
              </a-button>
              <a-button
                size="small"
                type="success"
                @click="handleSignIn(record)"
                v-if="record.status === 3"
              >
                签到管理
              </a-button>
              <a-button
                size="small"
                type="warning"
                @click="handleActivityReview(record)"
                v-if="record.status === 4"
              >
                活动回顾
              </a-button>
              <a-button
                size="small"
                type="danger"
                @click="handleDeleteActivity(record.id)"
                v-if="record.status === 1"
              >
                删除
              </a-button>
            </template>
          </a-table-column>
        </template>
      </a-table>
    </a-card>

    <!-- 创建/编辑活动弹窗 -->
    <a-modal
      v-model:visible="activityModalVisible"
      :title="isEdit ? '编辑活动' : '创建活动'"
      width="800px"
      @ok="handleSaveActivity"
    >
      <a-form
        ref="activityFormRef"
        :model="activityForm"
        :rules="activityFormRules"
        layout="vertical"
      >
        <a-form-item label="活动名称" field="activityName">
          <a-input
            v-model="activityForm.activityName"
            placeholder="请输入活动名称"
          />
        </a-form-item>
        <a-form-item label="所属党组织" field="orgId">
          <a-select v-model="activityForm.orgId" placeholder="请选择所属党组织">
            <a-option
              v-for="option in orgOptions"
              :key="option.value"
              :value="option.value"
            >
              {{ option.label }}
            </a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="活动类型" field="activityType">
          <a-select
            v-model="activityForm.activityType"
            placeholder="请选择活动类型"
          >
            <a-option value="1">会议</a-option>
            <a-option value="2">志愿活动</a-option>
            <a-option value="3">学习活动</a-option>
            <a-option value="4">其他</a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="活动开始时间" field="startTime">
          <a-date-picker
            v-model="activityForm.startTime"
            show-time
            placeholder="请选择活动开始时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </a-form-item>
        <a-form-item label="活动结束时间" field="endTime">
          <a-date-picker
            v-model="activityForm.endTime"
            show-time
            placeholder="请选择活动结束时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </a-form-item>
        <a-form-item label="报名截止时间" field="enrollDeadline">
          <a-date-picker
            v-model="activityForm.enrollDeadline"
            show-time
            placeholder="请选择报名截止时间"
            format="YYYY-MM-DD HH:mm:ss"
            value-format="YYYY-MM-DD HH:mm:ss"
          />
        </a-form-item>
        <a-form-item label="活动地点" field="location">
          <a-input
            v-model="activityForm.location"
            placeholder="请输入活动地点"
          />
        </a-form-item>
        <a-form-item label="最大参与人数" field="maxNum">
          <a-input-number
            v-model="activityForm.maxNum"
            min="1"
            placeholder="请输入最大参与人数"
          />
        </a-form-item>
        <a-form-item label="活动描述" field="activityContent">
          <a-textarea
            v-model="activityForm.activityContent"
            placeholder="请输入活动详细描述"
            :rows="4"
          />
        </a-form-item>
        <a-form-item label="活动状态" field="status" v-if="isEdit">
          <a-select v-model="activityForm.status">
            <a-option value="1">待发布</a-option>
            <a-option value="2">已发布</a-option>
            <a-option value="3">进行中</a-option>
            <a-option value="4">已结束</a-option>
          </a-select>
        </a-form-item>
      </a-form>
    </a-modal>

    <!-- 活动详情弹窗 -->
    <a-modal
      v-model:visible="detailModalVisible"
      title="活动详情"
      width="1000px"
      :footer="null"
    >
      <a-tabs v-model:active-key="detailActiveKey">
        <a-tab-pane key="base" title="基本信息">
          <!-- 新增：报名/取消报名按钮 -->
          <div style="margin-bottom: 16px">
            <a-button
              type="primary"
              @click="handleEnrollActivity(currentActivity)"
              v-if="
                [2, 3].includes(currentActivity?.status) &&
                !currentActivity?.hasEnrolled
              "
            >
              报名活动
            </a-button>
            <a-button
              type="warning"
              @click="handleCancelEnroll(currentActivity)"
              v-if="
                [2, 3].includes(currentActivity?.status) &&
                currentActivity?.hasEnrolled
              "
            >
              取消报名
            </a-button>
          </div>
          <a-descriptions :column="2" bordered>
            <a-descriptions-item label="活动ID"
              >{{ currentActivity?.id }}
            </a-descriptions-item>
            <a-descriptions-item label="活动名称"
              >{{ currentActivity?.activityName }}
            </a-descriptions-item>
            <a-descriptions-item label="所属党组织">
              {{ currentActivity?.orgName || "未知组织" }}
            </a-descriptions-item>
            <a-descriptions-item label="创建人ID"
              >{{ currentActivity?.userId || "-" }}
            </a-descriptions-item>
            <a-descriptions-item label="活动类型"
              >{{ typeLabelMap[currentActivity?.activityType] }}
            </a-descriptions-item>
            <a-descriptions-item label="活动状态"
              >{{ statusLabelMap[currentActivity?.status] }}
            </a-descriptions-item>
            <a-descriptions-item label="活动开始时间"
              >{{ formatDate(currentActivity?.startTime) }}
            </a-descriptions-item>
            <a-descriptions-item label="活动结束时间"
              >{{ formatDate(currentActivity?.endTime) }}
            </a-descriptions-item>
            <a-descriptions-item label="报名截止时间"
              >{{ formatDate(currentActivity?.enrollDeadline) }}
            </a-descriptions-item>
            <a-descriptions-item label="活动地点"
              >{{ currentActivity?.location || "-" }}
            </a-descriptions-item>
            <a-descriptions-item label="最大参与人数"
              >{{ currentActivity?.maxNum || 0 }}
            </a-descriptions-item>
            <a-descriptions-item label="当前参与人数"
              >{{ currentActivity?.currentNum || 0 }}
            </a-descriptions-item>
            <a-descriptions-item label="总报名人数"
              >{{ currentActivity?.totalParticipant || 0 }}
            </a-descriptions-item>
            <a-descriptions-item label="实际签到人数"
              >{{ currentActivity?.actualParticipant || 0 }}
            </a-descriptions-item>
            <a-descriptions-item label="签到率">
              {{
                currentActivity?.signRate
                  ? (currentActivity.signRate * 100).toFixed(2) + "%"
                  : "0%"
              }}
            </a-descriptions-item>
            <a-descriptions-item label="创建时间"
              >{{ formatDate(currentActivity?.createTime) }}
            </a-descriptions-item>
            <a-descriptions-item label="活动描述" :span="2"
              >{{ currentActivity?.activityContent || "-" }}
            </a-descriptions-item>
            <a-descriptions-item label="活动总结" :span="2"
              >{{ currentActivity?.reviewContent || "-" }}
            </a-descriptions-item>
          </a-descriptions>
        </a-tab-pane>
        <a-tab-pane key="participant" title="参与人员">
          <a-table
            :data="currentActivity?.participants || []"
            border
            row-key="id"
          >
            <template #columns>
              <a-table-column title="党员ID" data-index="id" />
              <a-table-column title="姓名" data-index="name" />
              <a-table-column title="党员类型" data-index="type">
                <template #cell="{ value }">
                  {{ value === "student" ? "学生党员" : "教师党员" }}
                </template>
              </a-table-column>
              <a-table-column title="报名时间" data-index="signUpTime">
                <template #cell="{ value }">
                  {{ formatDate(value) }}
                </template>
              </a-table-column>
              <a-table-column title="签到状态" data-index="signStatus">
                <template #cell="{ value }">
                  <a-tag :color="value ? 'green' : 'red'">
                    {{ value ? "已签到" : "未签到" }}
                  </a-tag>
                </template>
              </a-table-column>
            </template>
          </a-table>
        </a-tab-pane>
        <a-tab-pane key="review" title="活动回顾">
          <div class="review-content">
            <a-textarea
              v-model="reviewContent"
              placeholder="请输入活动总结内容"
              :rows="6"
              style="margin-bottom: 16px"
            />
            <a-upload
              v-model="reviewFiles"
              action="/api/file/upload"
              list-type="picture-card"
              :multiple="true"
              placeholder="上传活动回顾图片/文档"
            />
            <a-button
              type="primary"
              style="margin-top: 16px"
              @click="saveReview"
            >
              保存活动回顾
            </a-button>
          </div>
        </a-tab-pane>
      </a-tabs>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from "vue";
import { Message } from "@arco-design/web-vue";
import { IconPlus } from "@arco-design/web-vue/es/icon";
import { Service } from "../../../generated"; // 请根据实际路径调整
import { useStore } from "vuex";
import type {
  ActivityAddRequest,
  ActivityUpdateRequest,
  ActivityQueryRequest,
  DeleteRequest,
  BaseResponse_OrganizationVO_,
  OrganizationVO,
  // 新增：导入报名相关请求模型
  ActivityEnrollAddRequest,
  ActivityEnrollCancelRequest,
} from "../../../generated/models";

// ========== 类型定义 ==========
interface Activity {
  id: number; // 活动ID
  activityName: string; // 活动名称
  orgId: number; // 所属党组织ID
  userId: number; // 创建人ID
  activityType: 1 | 2 | 3 | 4; // 活动类型:1.会议/2.志愿活动/3.学习/4.其他
  activityContent: string; // 活动描述
  enrollDeadline: string | null; // 报名截止时间
  startTime: string | null; // 开始时间
  endTime: string | null; // 结束时间
  location: string; // 活动地点
  maxNum: number; // 最大参与人数
  currentNum: number; // 当前参与人数
  totalParticipant: number; // 总参与人数（已报名人数）
  actualParticipant: number; // 实际参与人数（已签到人数）
  signRate: number | null; // 签到率（实际参与人数/总参与人数）
  status: 1 | 2 | 3 | 4; // 活动状态:1.待发布/2.已发布/3.进行中/4.已结束
  reviewContent: string; // 活动总结
  createTime: string | null; // 创建时间
  updateTime: string | null; // 更新时间
  participants?: {
    // 参与人员（扩展字段）
    id: string;
    name: string;
    type: "student" | "teacher";
    signUpTime: string;
    signStatus: boolean;
  }[];
  orgName?: string; // 扩展字段：党组织名称
  hasEnrolled?: boolean; // 扩展字段：当前用户是否已报名
}

interface ActivityForm
  extends Omit<
    Activity,
    | "id"
    | "currentNum"
    | "totalParticipant"
    | "actualParticipant"
    | "signRate"
    | "createTime"
    | "updateTime"
    | "participants"
    | "orgName"
    | "hasEnrolled"
  > {
  id?: number;
}

// ========== 常量映射 ==========
const typeLabelMap = {
  1: "会议",
  2: "志愿活动",
  3: "学习活动",
  4: "其他",
};

const typeColorMap = {
  1: "blue",
  2: "green",
  3: "purple",
  4: "gray",
};

const statusLabelMap = {
  1: "待发布",
  2: "已发布",
  3: "进行中",
  4: "已结束",
};

const statusColorMap = {
  1: "gray",
  2: "orange",
  3: "blue",
  4: "green",
};

// ========== 响应式数据 ==========
const loading = ref(false);
const activityModalVisible = ref(false);
const detailModalVisible = ref(false);
const isEdit = ref(false);
const detailActiveKey = ref("base");
const reviewFiles = ref([]);
const reviewContent = ref("");

const orgData = ref<Record<number, string>>({});
const orgOptions = ref<{ label: string; value: number }[]>([]);

const pagination = reactive({
  current: 1,
  pageSize: 10,
  total: 0,
});

const filterForm = reactive({
  activityName: "",
  activityType: "",
  orgId: "",
  status: "",
  timeRange: [],
});

const activityFormRef = ref();
const activityForm = reactive<ActivityForm>({
  activityName: "",
  orgId: 0,
  userId: 1, // 实际从登录态获取
  activityType: 1,
  activityContent: "",
  enrollDeadline: null,
  startTime: null,
  endTime: null,
  location: "",
  maxNum: 0,
  status: 1,
  reviewContent: "",
});

const activityFormRules = {
  activityName: [{ required: true, message: "请输入活动名称" }],
  orgId: [{ required: true, message: "请选择所属党组织" }],
  activityType: [{ required: true, message: "请选择活动类型" }],
  startTime: [{ required: true, message: "请选择活动开始时间" }],
  endTime: [{ required: true, message: "请选择活动结束时间" }],
  maxNum: [
    { required: true, message: "请输入最大参与人数", type: "number", min: 1 },
  ],
};

const currentActivity = ref<Activity | null>(null);
const activityList = ref<Activity[]>([]);

const store = useStore();

// ========== 工具方法 ==========
const formatDate = (date: string | null) => {
  if (!date) return "-";
  return new Date(date).toLocaleString("zh-CN", {
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
  });
};

const showMessage = (msg: string, type: "success" | "error" = "success") => {
  if (type === "success") {
    Message.success(msg);
  } else {
    Message.error(msg);
  }
};

// ========== 党组织相关方法 ==========
const getOrgName = async (orgId: number | undefined) => {
  if (!orgId) return "未知组织";
  if (orgData.value[orgId]) {
    return orgData.value[orgId];
  }
  try {
    const res: BaseResponse_OrganizationVO_ =
      await Service.getOrganizationByIdUsingGet(orgId);
    if (res.code === 0 && res.data) {
      const orgName = res.data.orgName || "未知组织";
      orgData.value[orgId] = orgName;
      return orgName;
    }
    return "未知组织";
  } catch (error) {
    console.error(`获取党组织[${orgId}]名称失败:`, error);
    return "未知组织";
  }
};

const loadAllOrganizations = async () => {
  try {
    const res = await Service.listOrganizationsUsingGet();
    if (res.code === 0) {
      const orgList = res.data || [];
      const orgMapTemp: Record<number, string> = {};
      const optionsTemp: { label: string; value: number }[] = [];

      orgList.forEach((org: OrganizationVO) => {
        if (org.id && org.name) {
          orgMapTemp[org.id] = org.name;
          optionsTemp.push({ label: org.name, value: org.id });
        }
      });

      orgData.value = orgMapTemp;
      orgOptions.value = optionsTemp;

      if (optionsTemp.length > 0 && activityForm.orgId === 0) {
        activityForm.orgId = optionsTemp[0].value;
      }
    } else {
      showMessage(res.message || "加载党组织列表失败", "error");
    }
  } catch (error) {
    console.error("加载党组织列表失败:", error);
    showMessage("网络请求异常，加载党组织失败", "error");
  }
};

// ========== 分页方法 ==========
const handlePageChange = (page: number) => {
  pagination.current = page;
  handleSearch();
};

const handleSizeChange = (pageSize: number) => {
  pagination.pageSize = pageSize;
  handleSearch();
};

// ========== 新增：报名状态检查 ==========
const checkEnrolledStatus = async (activityId: number) => {
  try {
    // 获取当前登录用户ID
    const loginUser = store.state.user.loginUser;
    // console.log("store", store.state.user.loginUser);
    if (!loginUser?.id) {
      showMessage("请先登录", "error");
      return false;
    }
    // 调用检查报名状态接口
    const res = await Service.checkEnrolledUsingGet(activityId, loginUser.id);
    return res.code === 0 && res.data === true;
  } catch (error) {
    console.error("检查报名状态失败:", error);
    return false;
  }
};

// ========== 业务方法 ==========
const handleSearch = async () => {
  try {
    loading.value = true;
    const queryParams: ActivityQueryRequest = {
      activityName: filterForm.activityName,
      activityType: filterForm.activityType
        ? Number(filterForm.activityType)
        : undefined,
      orgId: filterForm.orgId ? Number(filterForm.orgId) : undefined,
      status: filterForm.status ? Number(filterForm.status) : undefined,
      startTime: filterForm.timeRange?.[0]
        ? new Date(filterForm.timeRange[0]).toISOString()
        : undefined,
      endTime: filterForm.timeRange?.[1]
        ? new Date(filterForm.timeRange[1]).toISOString()
        : undefined,
      current: pagination.current,
      pageSize: pagination.pageSize,
    };

    const res = await Service.listActivityByPageUsingPost(queryParams);
    if (res.code === 0) {
      const activityRecords = res.data.records || [];
      // 补充党组织名称 + 报名状态
      const activityListWithExtra = await Promise.all(
        activityRecords.map(async (activity) => {
          const orgName = await getOrgName(activity.orgId);
          const hasEnrolled = await checkEnrolledStatus(activity.id);
          return { ...activity, orgName, hasEnrolled };
        }),
      );
      activityList.value = activityListWithExtra;
      pagination.total = res.data.total || 0;
    } else {
      showMessage(res.message || "查询失败", "error");
    }
  } catch (error) {
    console.error("查询活动失败:", error);
    showMessage("网络请求异常，查询失败", "error");
  } finally {
    loading.value = false;
  }
};

const handleResetFilter = () => {
  Object.keys(filterForm).forEach((key) => {
    if (key === "timeRange") {
      filterForm[key] = [];
    } else {
      filterForm[key] = "";
    }
  });
  pagination.current = 1;
  pagination.pageSize = 10;
  handleSearch();
};

const handleCreateActivity = () => {
  isEdit.value = false;
  Object.keys(activityForm).forEach((key) => {
    if (["startTime", "endTime", "enrollDeadline"].includes(key)) {
      activityForm[key] = null;
    } else if (key === "maxNum") {
      activityForm[key] = 0;
    } else if (key === "activityType" || key === "status") {
      activityForm[key] = 1;
    } else if (key === "orgId" && orgOptions.value.length > 0) {
      activityForm[key] = orgOptions.value[0].value;
    } else {
      activityForm[key] = "";
    }
  });
  const loginUser = store.state.user.currentUser;
  if (loginUser) {
    activityForm.userId = loginUser.id;
  }
  activityModalVisible.value = true;
};

const handleEditActivity = (record: Activity) => {
  isEdit.value = true;
  Object.assign(activityForm, {
    id: record.id,
    activityName: record.activityName,
    orgId: record.orgId,
    userId: record.userId,
    activityType: record.activityType,
    activityContent: record.activityContent,
    enrollDeadline: record.enrollDeadline,
    startTime: record.startTime,
    endTime: record.endTime,
    location: record.location,
    maxNum: record.maxNum,
    status: record.status,
    reviewContent: record.reviewContent,
  });
  activityModalVisible.value = true;
};

// ========== 新增：发布活动 ==========
const handlePublishActivity = async (record: Activity) => {
  try {
    loading.value = true;
    const res = await Service.publishActivityUsingPost(record.id);
    if (res.code === 0) {
      showMessage(`【${record.activityName}】发布成功`);
      handleSearch();
    } else {
      showMessage(res.message || `【${record.activityName}】发布失败`, "error");
    }
  } catch (error) {
    console.error("发布活动失败:", error);
    showMessage("网络请求异常，发布失败", "error");
  } finally {
    loading.value = false;
  }
};

// ========== 新增：活动报名 ==========
const handleEnrollActivity = async (activity: Activity) => {
  try {
    // 1. 前置校验
    const loginUser = store.state.user.loginUser;
    if (!loginUser?.id) {
      showMessage("请先登录后再报名", "error");
      return;
    }
    if (activity.totalParticipant >= activity.maxNum) {
      showMessage("活动报名人数已达上限，无法报名", "error");
      return;
    }
    if (new Date() > new Date(activity.enrollDeadline || "")) {
      showMessage("报名已截止，无法报名", "error");
      return;
    }
    if (activity.hasEnrolled) {
      showMessage("您已报名该活动，无需重复报名", "error");
      return;
    }

    loading.value = true;
    // 2. 构造报名请求参数
    const enrollParams: ActivityEnrollAddRequest = {
      activityId: activity.id,
      userId: loginUser.id, // 当前登录用户ID
    };
    // 3. 调用报名接口
    const res = await Service.enrollActivityUsingPost(enrollParams);
    if (res.code === 0) {
      showMessage(`【${activity.activityName}】报名成功`);
      // 4. 更新报名状态
      activity.hasEnrolled = true;
      // 5. 刷新活动参与人数
      activity.totalParticipant += 1;
    } else {
      showMessage(
        res.message || `【${activity.activityName}】报名失败`,
        "error",
      );
    }
  } catch (error) {
    console.error("报名活动失败:", error);
    showMessage("网络请求异常，报名失败", "error");
  } finally {
    loading.value = false;
  }
};

// ========== 新增：取消报名 ==========
const handleCancelEnroll = async (activity: Activity) => {
  try {
    // 1. 前置校验
    const loginUser = store.state.user.currentUser;
    if (!loginUser?.id) {
      showMessage("请先登录", "error");
      return;
    }
    if (!activity.hasEnrolled) {
      showMessage("您未报名该活动，无需取消", "error");
      return;
    }

    loading.value = true;
    // 2. 构造取消报名请求参数
    const cancelParams: ActivityEnrollCancelRequest = {
      activityId: activity.id,
      userId: loginUser.id,
    };
    // 3. 调用取消报名接口
    const res = await Service.cancelEnrollUsingPost(cancelParams);
    if (res.code === 0) {
      showMessage(`【${activity.activityName}】取消报名成功`);
      // 4. 更新报名状态
      activity.hasEnrolled = false;
      // 5. 刷新活动参与人数
      activity.totalParticipant -= 1;
    } else {
      showMessage(
        res.message || `【${activity.activityName}】取消报名失败`,
        "error",
      );
    }
  } catch (error) {
    console.error("取消报名失败:", error);
    showMessage("网络请求异常，取消报名失败", "error");
  } finally {
    loading.value = false;
  }
};

const handleSaveActivity = async () => {
  try {
    await activityFormRef.value?.validate();
    loading.value = true;

    if (!isEdit.value) {
      const addParams: ActivityAddRequest = {
        activityName: activityForm.activityName,
        orgId: activityForm.orgId,
        userId: activityForm.userId,
        activityType: activityForm.activityType,
        activityContent: activityForm.activityContent,
        enrollDeadline: activityForm.enrollDeadline,
        startTime: activityForm.startTime,
        endTime: activityForm.endTime,
        location: activityForm.location,
        maxNum: activityForm.maxNum,
        status: activityForm.status,
        reviewContent: activityForm.reviewContent,
      };
      const res = await Service.addActivityUsingPost(addParams);
      if (res.code === 0) {
        showMessage("创建活动成功");
        activityModalVisible.value = false;
        handleSearch();
      } else {
        showMessage(res.message || "创建活动失败", "error");
      }
    } else {
      const updateParams: ActivityUpdateRequest = {
        id: activityForm.id,
        activityName: activityForm.activityName,
        orgId: activityForm.orgId,
        activityType: activityForm.activityType,
        activityContent: activityForm.activityContent,
        enrollDeadline: activityForm.enrollDeadline,
        startTime: activityForm.startTime,
        endTime: activityForm.endTime,
        location: activityForm.location,
        maxNum: activityForm.maxNum,
        status: activityForm.status,
        reviewContent: activityForm.reviewContent,
      };
      const res = await Service.updateActivityUsingPost(updateParams);
      if (res.code === 0) {
        showMessage("编辑活动成功");
        activityModalVisible.value = false;
        handleSearch();
      } else {
        showMessage(res.message || "编辑活动失败", "error");
      }
    }
  } catch (error) {
    if (error.name !== "ValidationError") {
      console.error("保存活动失败:", error);
      showMessage("网络请求异常，保存失败", "error");
    }
  } finally {
    loading.value = false;
  }
};

const handleViewDetail = async (record: Activity) => {
  try {
    loading.value = true;
    const res = await Service.getActivityByIdUsingGet(record.id);
    if (res.code === 0) {
      const orgName = await getOrgName(res.data.orgId);
      // 补充报名状态
      const hasEnrolled = await checkEnrolledStatus(res.data.id);
      currentActivity.value = { ...res.data, orgName, hasEnrolled };
      reviewContent.value = res.data.reviewContent || "";
      detailModalVisible.value = true;
    } else {
      showMessage(res.message || "获取活动详情失败", "error");
    }
  } catch (error) {
    console.error("获取活动详情失败:", error);
    showMessage("网络请求异常，获取详情失败", "error");
  } finally {
    loading.value = false;
  }
};

const handleSignIn = (record: Activity) => {
  Message.info(`进入【${record.activityName}】的签到管理页面`);
};

const handleActivityReview = (record: Activity) => {
  currentActivity.value = record;
  reviewContent.value = record.reviewContent || "";
  detailModalVisible.value = true;
  detailActiveKey.value = "review";
};

const handleDeleteActivity = async (id: number) => {
  try {
    loading.value = true;
    const deleteParams: DeleteRequest = { id };
    const res = await Service.deleteActivityUsingPost(deleteParams);
    if (res.code === 0) {
      showMessage("删除活动成功");
      handleSearch();
    } else {
      showMessage(res.message || "删除活动失败", "error");
    }
  } catch (error) {
    console.error("删除活动失败:", error);
    showMessage("网络请求异常，删除失败", "error");
  } finally {
    loading.value = false;
  }
};

const saveReview = async () => {
  if (!currentActivity.value) return;
  try {
    loading.value = true;
    const updateParams: ActivityUpdateRequest = {
      id: currentActivity.value.id,
      reviewContent: reviewContent.value,
      activityName: currentActivity.value.activityName,
      orgId: currentActivity.value.orgId,
      activityType: currentActivity.value.activityType,
      activityContent: currentActivity.value.activityContent,
      enrollDeadline: currentActivity.value.enrollDeadline,
      startTime: currentActivity.value.startTime,
      endTime: currentActivity.value.endTime,
      location: currentActivity.value.location,
      maxNum: currentActivity.value.maxNum,
      status: currentActivity.value.status,
    };
    const res = await Service.updateActivityUsingPost(updateParams);
    if (res.code === 0) {
      showMessage("活动回顾保存成功");
      currentActivity.value.reviewContent = reviewContent.value;
      detailModalVisible.value = false;
      handleSearch();
    } else {
      showMessage(res.message || "保存活动回顾失败", "error");
    }
  } catch (error) {
    console.error("保存活动回顾失败:", error);
    showMessage("网络请求异常，保存失败", "error");
  } finally {
    loading.value = false;
  }
};

// 页面初始化
onMounted(async () => {
  await loadAllOrganizations();
  handleSearch();
});
</script>

<style scoped>
.activity-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.filter-card {
  background: #fff;
  margin-bottom: 16px;
}

.filter-form {
  padding: 16px 0;
}

.list-card {
  background: #fff;
}

.review-content {
  padding: 8px 0;
}
</style>
