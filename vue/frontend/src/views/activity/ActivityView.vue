<template>
  <div class="activity-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h1>党建活动管理</h1>
      <div class="header-actions">
        <a-button type="primary" @click="handleCreateActivity">
          <icon-plus/>
          创建活动
        </a-button>
      </div>
    </div>

    <!-- 统计卡片区域 -->
    <div v-if="!loading" class="stats-container">
      <div class="stat-card">
        <div class="stat-number" style="color: #1890ff">
          {{ activityStats.total }}
        </div>
        <div class="stat-label">总活动数</div>
      </div>
      <div class="stat-card">
        <div class="stat-number" style="color: #52c41a">
          {{ activityStats.published }}
        </div>
        <div class="stat-label">已发布</div>
      </div>
      <div class="stat-card">
        <div class="stat-number" style="color: #faad14">
          {{ activityStats.ongoing }}
        </div>
        <div class="stat-label">进行中</div>
      </div>
      <div class="stat-card">
        <div class="stat-number" style="color: #f5222d">
          {{ activityStats.ended }}
        </div>
        <div class="stat-label">已结束</div>
      </div>
    </div>

    <!-- 主要内容区 -->
    <!-- 筛选区域 -->
    <a-card class="filter-card">
      <a-form :model="filterForm" class="filter-form" layout="inline">
        <a-form-item label="活动名称">
          <a-input
              v-model="filterForm.activityName"
              allow-clear
              placeholder="请输入活动名称"
              style="width: 200px"
          />
        </a-form-item>
        <a-form-item label="活动类型">
          <a-select
              v-model="filterForm.activityType"
              allow-clear
              placeholder="请选择活动类型"
              style="width: 150px"
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
              allow-clear
              placeholder="请选择所属党组织"
              style="width: 200px"
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
              allow-clear
              placeholder="请选择活动状态"
              style="width: 120px"
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
              style="width: 300px"
          />
        </a-form-item>
        <a-form-item label="排序方式">
          <a-select
              v-model="filterForm.sortBy"
              placeholder="请选择排序方式"
              style="width: 150px"
          >
            <a-option value="createTime">创建时间</a-option>
            <a-option value="startTime">开始时间</a-option>
            <a-option value="status">活动状态</a-option>
          </a-select>
        </a-form-item>
        <a-form-item label="排序顺序">
          <a-select v-model="filterForm.sortOrder" style="width: 100px">
            <a-option value="desc">降序</a-option>
            <a-option value="asc">升序</a-option>
          </a-select>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="handleSearch">查询</a-button>
          <a-button @click="handleResetFilter">重置</a-button>
        </a-form-item>
      </a-form>
    </a-card>

    <!-- 活动列表 -->
    <a-card class="list-card" style="margin-top: 16px">
      <template #loading>
        <div style="display: flex; justify-content: center; padding: 40px">
          <a-spin size="large" tip="加载中..."/>
        </div>
      </template>
      <a-table
          :data="activityList"
          :loading="loading"
          :pagination="{
          current: pagination.current,
          pageSize: pagination.pageSize,
          total: pagination.total,
          showSizeChanger: true,
          showTotal: (total) => `共 ${total} 条记录`,
          onChange: handlePageChange,
          onShowSizeChange: handleSizeChange,
        }"
          border
          pagination-position="bottom"
          row-key="id"
      >
        <template #columns>
          <a-table-column data-index="id" title="活动ID" width="100"/>
          <a-table-column data-index="activityName" title="活动名称"/>
          <a-table-column data-index="orgId" title="所属党组织">
            <template #cell="{ record }">
              {{ record.orgName || "未知组织" }}
            </template>
          </a-table-column>
          <a-table-column data-index="activityType" title="活动类型">
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
          <a-table-column data-index="enrollDeadline" title="报名截止时间">
            <template #cell="{ record }">
              {{ formatDate(record.enrollDeadline) }}
            </template>
          </a-table-column>
          <a-table-column data-index="status" title="活动状态">
            <template #cell="{ record }">
              <a-tag :color="statusColorMap[record.status]">
                {{ statusLabelMap[record.status] }}
              </a-tag>
            </template>
          </a-table-column>
          <a-table-column title="参与数据">
            <template #cell="{ record }">
              <div>
                报名人数：{{ record.totalParticipant }}/{{
                  record.maxNum
                }}<br/>
                签到人数：{{ record.actualParticipant }}<br/>
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
                  v-if="[2, 3].includes(record.status) && !record.hasEnrolled"
                  size="small"
                  type="primary"
                  @click="handleEnrollActivity(record)"
              >
                报名
              </a-button>
              <a-button
                  v-if="[2, 3].includes(record.status) && record.hasEnrolled"
                  size="small"
                  type="warning"
                  @click="handleCancelEnroll(record)"
              >
                取消报名
              </a-button>
              <!-- 原有按钮 -->
              <a-button
                  v-if="record.status === 1"
                  size="small"
                  type="success"
                  @click="handlePublishActivity(record)"
              >
                发布
              </a-button>
              <a-button
                  v-if="[1, 2].includes(record.status)"
                  size="small"
                  type="primary"
                  @click="handleEditActivity(record)"
              >
                编辑
              </a-button>
              <a-button
                  v-if="record.status === 3"
                  size="small"
                  type="success"
                  @click="handleSignIn(record)"
              >
                签到管理
              </a-button>
              <a-button
                  v-if="record.status === 4"
                  size="small"
                  type="warning"
                  @click="handleActivityReview(record)"
              >
                活动回顾
              </a-button>
              <a-button
                  v-if="record.status === 1"
                  size="small"
                  type="danger"
                  @click="handleDeleteActivity(record.id)"
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
        :ok-loading="buttonLoading.save"
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
        <a-form-item field="activityName" label="活动名称">
          <a-input
              v-model="activityForm.activityName"
              placeholder="请输入活动名称"
          />
        </a-form-item>
        <a-form-item field="orgId" label="所属党组织">
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
        <a-form-item field="activityType" label="活动类型">
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
        <a-form-item field="startTime" label="活动开始时间">
          <a-date-picker
              v-model="activityForm.startTime"
              format="YYYY-MM-DD HH:mm:ss"
              placeholder="请选择活动开始时间"
              show-time
              value-format="YYYY-MM-DD HH:mm:ss"
          />
        </a-form-item>
        <a-form-item field="endTime" label="活动结束时间">
          <a-date-picker
              v-model="activityForm.endTime"
              format="YYYY-MM-DD HH:mm:ss"
              placeholder="请选择活动结束时间"
              show-time
              value-format="YYYY-MM-DD HH:mm:ss"
          />
        </a-form-item>
        <a-form-item field="enrollDeadline" label="报名截止时间">
          <a-date-picker
              v-model="activityForm.enrollDeadline"
              format="YYYY-MM-DD HH:mm:ss"
              placeholder="请选择报名截止时间"
              show-time
              value-format="YYYY-MM-DD HH:mm:ss"
          />
        </a-form-item>
        <a-form-item field="location" label="活动地点">
          <a-input
              v-model="activityForm.location"
              placeholder="请输入活动地点"
          />
        </a-form-item>
        <a-form-item field="maxNum" label="最大参与人数">
          <a-input-number
              v-model="activityForm.maxNum"
              min="1"
              placeholder="请输入最大参与人数"
          />
        </a-form-item>
        <a-form-item field="activityContent" label="活动描述">
          <a-textarea
              v-model="activityForm.activityContent"
              :rows="4"
              placeholder="请输入活动详细描述"
          />
        </a-form-item>
        <a-form-item v-if="isEdit" field="status" label="活动状态">
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
        :footer="null"
        title="活动详情"
        width="1000px"
    >
      <a-tabs v-model:active-key="detailActiveKey">
        <a-tab-pane key="base" title="基本信息">
          <!-- 操作按钮区域 -->
          <div
              style="
              margin-bottom: 20px;
              display: flex;
              gap: 12px;
              flex-wrap: wrap;
            "
          >
            <a-button
                v-if="
                [2, 3].includes(currentActivity?.status) &&
                !currentActivity?.hasEnrolled
              "
                type="primary"
                @click="handleEnrollActivity(currentActivity)"
            >
              报名活动
            </a-button>
            <a-button
                v-if="
                [2, 3].includes(currentActivity?.status) &&
                currentActivity?.hasEnrolled
              "
                type="warning"
                @click="handleCancelEnroll(currentActivity)"
            >
              取消报名
            </a-button>
            <a-button
                v-if="currentActivity?.status === 1"
                type="success"
                @click="handlePublishActivity(currentActivity)"
            >
              发布活动
            </a-button>
            <a-button
                v-if="[1, 2].includes(currentActivity?.status)"
                type="primary"
                @click="handleEditActivity(currentActivity)"
            >
              编辑活动
            </a-button>
            <a-button
                v-if="currentActivity?.status === 3"
                type="success"
                @click="handleSignIn(currentActivity)"
            >
              签到管理
            </a-button>
            <a-button
                v-if="currentActivity?.status === 1"
                type="danger"
                @click="handleDeleteActivity(currentActivity?.id)"
            >
              删除活动
            </a-button>
          </div>

          <!-- 活动状态和统计信息卡片 -->
          <div
              style="
              margin-bottom: 20px;
              display: flex;
              gap: 16px;
              flex-wrap: wrap;
            "
          >
            <div
                class="stat-card"
                style="flex: 1; min-width: 150px; padding: 16px"
            >
              <div class="stat-number" style="font-size: 24px; color: #1890ff">
                {{ currentActivity?.totalParticipant || 0 }}/{{
                  currentActivity?.maxNum || 0
                }}
              </div>
              <div class="stat-label">报名情况</div>
            </div>
            <div
                class="stat-card"
                style="flex: 1; min-width: 150px; padding: 16px"
            >
              <div class="stat-number" style="font-size: 24px; color: #52c41a">
                {{ currentActivity?.actualParticipant || 0 }}
              </div>
              <div class="stat-label">签到人数</div>
            </div>
            <div
                class="stat-card"
                style="flex: 1; min-width: 150px; padding: 16px"
            >
              <div class="stat-number" style="font-size: 24px; color: #faad14">
                {{
                  currentActivity?.signRate
                      ? (currentActivity.signRate * 100).toFixed(0) + "%"
                      : "0%"
                }}
              </div>
              <div class="stat-label">签到率</div>
            </div>
          </div>

          <!-- 活动基本信息 -->
          <a-descriptions :column="2" bordered style="margin-bottom: 20px">
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
            <a-descriptions-item label="活动状态">
              <a-tag :color="statusColorMap[currentActivity?.status]">
                {{ statusLabelMap[currentActivity?.status] }}
              </a-tag>
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
            <a-descriptions-item label="创建时间"
            >{{ formatDate(currentActivity?.createTime) }}
            </a-descriptions-item>
            <a-descriptions-item label="更新时间"
            >{{ formatDate(currentActivity?.updateTime) }}
            </a-descriptions-item>
          </a-descriptions>

          <!-- 活动描述 -->
          <a-card style="margin-bottom: 20px">
            <template #title>
              <span style="font-weight: 600">活动描述</span>
            </template>
            <div style="line-height: 1.6">
              {{ currentActivity?.activityContent || "-" }}
            </div>
          </a-card>

          <!-- 活动总结 -->
          <a-card v-if="currentActivity?.reviewContent">
            <template #title>
              <span style="font-weight: 600">活动总结</span>
            </template>
            <div style="line-height: 1.6">
              {{ currentActivity?.reviewContent }}
            </div>
          </a-card>
        </a-tab-pane>
        <a-tab-pane key="participant" title="参与人员">
          <div
              style="
              margin-bottom: 16px;
              display: flex;
              justify-content: space-between;
              align-items: center;
            "
          >
            <div>
              <span style="margin-right: 16px"
              >总参与人数：{{ currentActivity?.totalParticipant || 0 }}</span
              >
              <span
              >已签到人数：{{ currentActivity?.actualParticipant || 0 }}</span
              >
            </div>
            <a-button
                v-if="currentActivity?.status === 3"
                type="primary"
                @click="handleSignIn(currentActivity)"
            >
              进入签到管理
            </a-button>
          </div>
          <a-table
              :data="currentActivity?.participants || []"
              :pagination="{
              current: 1,
              pageSize: 10,
              showSizeChanger: true,
              showTotal: (total) => `共 ${total} 条记录`,
            }"
              border
              row-key="id"
          >
            <template #columns>
              <a-table-column title="序号" type="index" width="80"/>
              <a-table-column data-index="id" title="党员ID"/>
              <a-table-column data-index="name" title="姓名"/>
              <a-table-column data-index="type" title="党员类型">
                <template #cell="{ value }">
                  <a-tag :color="value === 'student' ? 'blue' : 'green'">
                    {{ value === "student" ? "学生党员" : "教师党员" }}
                  </a-tag>
                </template>
              </a-table-column>
              <a-table-column data-index="signUpTime" title="报名时间">
                <template #cell="{ value }">
                  {{ formatDate(value) }}
                </template>
              </a-table-column>
              <a-table-column data-index="signStatus" title="签到状态">
                <template #cell="{ value }">
                  <a-tag :color="value ? 'green' : 'red'">
                    {{ value ? "已签到" : "未签到" }}
                  </a-tag>
                </template>
              </a-table-column>
            </template>
          </a-table>
        </a-tab-pane>
      </a-tabs>
    </a-modal>

    <!-- 签到管理弹窗 -->
    <a-modal
        v-model:visible="signInModalVisible"
        :ok-loading="buttonLoading.signIn"
        title="签到管理"
        width="800px"
        @ok="handleSignInConfirm"
    >
      <div v-if="currentActivity">
        <div style="margin-bottom: 20px;">
          <h3>{{ currentActivity.activityName }}</h3>
          <p style="color: #666;">
            活动时间：{{ formatDate(currentActivity.startTime) }} 至 {{ formatDate(currentActivity.endTime) }}
          </p>
          <p style="color: #666;">
            活动地点：{{ currentActivity.location }}
          </p>
        </div>
        
        <div style="margin-bottom: 16px;">
          <a-input
              v-model:value="signInSearchKey"
              placeholder="搜索参与人员姓名"
            >
              <template #prefix>
                <a-icon name="search" />
              </template>
            </a-input>
        </div>
        
        <a-table
            :data="filteredParticipants"
            :pagination="{
            current: 1,
            pageSize: 10,
            showSizeChanger: true,
            showTotal: (total) => `共 ${total} 条记录`,
          }"
            border
            row-key="id"
        >
          <template #columns>
            <a-table-column title="序号" type="index" width="80"/>
            <a-table-column data-index="id" title="党员ID"/>
            <a-table-column data-index="name" title="姓名"/>
            <a-table-column data-index="type" title="党员类型">
              <template #cell="{ value }">
                <a-tag :color="value === 'student' ? 'blue' : 'green'">
                  {{ value === "student" ? "学生党员" : "教师党员" }}
                </a-tag>
              </template>
            </a-table-column>
            <a-table-column data-index="signUpTime" title="报名时间">
              <template #cell="{ value }">
                {{ formatDate(value) }}
              </template>
            </a-table-column>
            <a-table-column data-index="signStatus" title="签到状态">
              <template #cell="{ value }">
                <a-tag :color="value ? 'green' : 'red'">
                  {{ value ? "已签到" : "未签到" }}
                </a-tag>
              </template>
            </a-table-column>
            <a-table-column title="操作" width="120">
              <template #cell="{ record }">
                <a-button
                    v-if="!record.signStatus"
                    size="small"
                    type="primary"
                    @click="handleSingleSignIn(record)"
                >
                  签到
                </a-button>
                <span v-else style="color: #999;">已签到</span>
              </template>
            </a-table-column>
          </template>
        </a-table>
      </div>
    </a-modal>
  </div>
</template>

<script lang="ts" setup>
import {computed, onMounted, reactive, ref} from "vue";
import {Message} from "@arco-design/web-vue";
import {IconPlus} from "@arco-design/web-vue/es/icon";
import {Service} from "../../../generated"; // 请根据实际路径调整
import {useStore} from "vuex";
import type {
  ActivityAddRequest,
  ActivityEnrollAddRequest,
  ActivityEnrollCancelRequest,
  ActivityEnrollSignInRequest,
  ActivityQueryRequest,
  ActivityUpdateRequest,
  BaseResponse_OrganizationVO_,
  DeleteRequest,
  OrganizationVO,
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
const signInModalVisible = ref(false);
const isEdit = ref(false);
const detailActiveKey = ref("base");
const reviewFiles = ref([]);
const reviewContent = ref("");
const signInSearchKey = ref("");

// 按钮加载状态
const buttonLoading = reactive({
  create: false,
  save: false,
  publish: false,
  enroll: false,
  cancelEnroll: false,
  delete: false,
  saveReview: false,
  signIn: false,
});

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
  sortBy: "createTime",
  sortOrder: "desc",
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
  activityName: [{required: true, message: "请输入活动名称"}],
  orgId: [{required: true, message: "请选择所属党组织"}],
  activityType: [{required: true, message: "请选择活动类型"}],
  startTime: [{required: true, message: "请选择活动开始时间"}],
  endTime: [{required: true, message: "请选择活动结束时间"}],
  maxNum: [
    {required: true, message: "请输入最大参与人数", type: "number", min: 1},
  ],
};

const currentActivity = ref<Activity | null>(null);
const activityList = ref<Activity[]>([]);

// 活动统计数据
const activityStats = reactive({
  total: 0,
  published: 0,
  ongoing: 0,
  ended: 0,
});

// 过滤后的参与人员
const filteredParticipants = computed(() => {
  if (!currentActivity.value?.participants) return [];
  if (!signInSearchKey.value) return currentActivity.value.participants;
  return currentActivity.value.participants.filter(participant => 
    participant.name.toLowerCase().includes(signInSearchKey.value.toLowerCase())
  );
});

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

// 更新活动统计数据
const updateActivityStats = (activities: Activity[]) => {
  const stats = {
    total: activities.length,
    published: 0,
    ongoing: 0,
    ended: 0,
  };

  activities.forEach((activity) => {
    if (activity.status === 2) {
      stats.published++;
    } else if (activity.status === 3) {
      stats.ongoing++;
    } else if (activity.status === 4) {
      stats.ended++;
    }
  });

  Object.assign(activityStats, stats);
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
        if (org.id && (org.orgName || org.name)) {
          const orgId = Number(org.id);
          const orgName = org.orgName || org.name;
          orgMapTemp[orgId] = orgName;
          optionsTemp.push({label: orgName, value: orgId});
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
// 查询活动列表
const handleSearch = async () => {
  try {
    loading.value = true;
    const queryParams = buildQueryParams();

    const res = await Service.listActivityByPageUsingPost(queryParams);
    if (res.code === 0) {
      const activityRecords = res.data.records || [];
      // 补充党组织名称 + 报名状态
      const activityListWithExtra = await enrichActivityData(activityRecords);
      activityList.value = activityListWithExtra;
      pagination.total = res.data.total || 0;

      // 更新活动统计数据
      updateActivityStats(activityListWithExtra);
    } else {
      showMessage(res.message || "查询失败", "error");
    }
  } catch (error) {
    showMessage("网络请求异常，查询失败", "error");
  } finally {
    loading.value = false;
  }
};

// 构建查询参数
const buildQueryParams = (): ActivityQueryRequest => {
  return {
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
};

// 丰富活动数据（添加党组织名称和报名状态）
const enrichActivityData = async (activities: any[]): Promise<Activity[]> => {
  return await Promise.all(
      activities.map(async (activity) => {
        const orgName = await getOrgName(activity.orgId);
        const hasEnrolled = await checkEnrolledStatus(activity.id);
        return {...activity, orgName, hasEnrolled};
      }),
  );
};

const handleResetFilter = () => {
  Object.keys(filterForm).forEach((key) => {
    if (key === "timeRange") {
      filterForm[key] = [];
    } else if (key === "sortBy") {
      filterForm[key] = "createTime";
    } else if (key === "sortOrder") {
      filterForm[key] = "desc";
    } else {
      filterForm[key] = "";
    }
  });
  pagination.current = 1;
  pagination.pageSize = 10;
  handleSearch();
};

const handleCreateActivity = () => {
  buttonLoading.create = true;
  setTimeout(() => {
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
    buttonLoading.create = false;
  }, 300);
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
    showMessage("网络请求异常，取消报名失败", "error");
  } finally {
    loading.value = false;
  }
};

const handleSaveActivity = async () => {
  try {
    await activityFormRef.value?.validate();
    buttonLoading.save = true;

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
      showMessage("网络请求异常，保存失败", "error");
    }
  } finally {
    buttonLoading.save = false;
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
      currentActivity.value = {...res.data, orgName, hasEnrolled};
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
  currentActivity.value = record;
  signInSearchKey.value = '';
  signInModalVisible.value = true;
};

// 处理单个人员签到
const handleSingleSignIn = async (participant: any) => {
  if (!currentActivity.value) return;
  
  try {
    buttonLoading.signIn = true;
    
    // 构造签到请求参数
    const signInParams: ActivityEnrollSignInRequest = {
      activityId: currentActivity.value.id,
      userId: Number(participant.id),
    };
    
    // 调用签到接口
    const res = await Service.signInActivityUsingPost(signInParams);
    if (res.code === 0) {
      showMessage(`${participant.name} 签到成功`);
      
      // 更新本地数据
      if (currentActivity.value.participants) {
        const index = currentActivity.value.participants.findIndex(p => p.id === participant.id);
        if (index !== -1) {
          currentActivity.value.participants[index].signStatus = true;
          // 更新实际签到人数
          currentActivity.value.actualParticipant = (currentActivity.value.actualParticipant || 0) + 1;
          // 重新计算签到率
          if (currentActivity.value.totalParticipant) {
            currentActivity.value.signRate = currentActivity.value.actualParticipant / currentActivity.value.totalParticipant;
          }
        }
      }
    } else {
      showMessage(res.message || `${participant.name} 签到失败`, "error");
    }
  } catch (error) {
    showMessage("网络请求异常，签到失败", "error");
  } finally {
    buttonLoading.signIn = false;
  }
};

// 签到管理弹窗确认按钮
const handleSignInConfirm = () => {
  signInModalVisible.value = false;
  // 刷新活动列表，更新签到状态
  handleSearch();
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
    const deleteParams: DeleteRequest = {id};
    const res = await Service.deleteActivityUsingPost(deleteParams);
    if (res.code === 0) {
      showMessage("删除活动成功");
      handleSearch();
    } else {
      showMessage(res.message || "删除活动失败", "error");
    }
  } catch (error) {
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
  background-color: #f0f2f5;
  min-height: 100vh;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e8e8e8;
}

.page-header h1 {
  font-size: 24px;
  font-weight: 600;
  color: #1890ff;
  margin: 0;
}

.filter-card {
  background: #fff;
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.filter-form {
  padding: 20px;
}

.list-card {
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.review-content {
  padding: 16px 0;
}

/* 统计卡片样式 */
.stats-container {
  display: flex;
  gap: 16px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.stat-card {
  flex: 1;
  min-width: 200px;
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
}

.stat-label {
  font-size: 14px;
  color: #666;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .activity-container {
    padding: 10px;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }

  .filter-form {
    padding: 12px;
  }

  .stats-container {
    flex-direction: column;
  }

  .stat-card {
    min-width: 100%;
  }

  .a-table {
    font-size: 12px;
  }

  .a-table-column {
    min-width: 100px;
  }
}
</style>
